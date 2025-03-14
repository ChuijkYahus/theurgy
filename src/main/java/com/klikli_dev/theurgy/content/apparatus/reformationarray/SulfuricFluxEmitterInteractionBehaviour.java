// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.interaction.InteractionBehaviour;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageShowSulfuricFluxEmitterStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class SulfuricFluxEmitterInteractionBehaviour implements InteractionBehaviour {

    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pHand != InteractionHand.MAIN_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (!(blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (pLevel.isClientSide)
            return ItemInteractionResult.SUCCESS;

        Networking.sendTo((ServerPlayer) pPlayer, new MessageShowSulfuricFluxEmitterStatus(
                pPos,
                sulfuricFluxEmitter.sourcePedestals,
                sulfuricFluxEmitter.targetPedestal,
                sulfuricFluxEmitter.resultPedestal
        ));

        //allows players to tell the system to re-check
        //this is mainly useful if they removed a pedestal and want it to be recognized it again after rebuilding it
        sulfuricFluxEmitter.checkValidMultiblockOnNextQuery = true;

        return ItemInteractionResult.SUCCESS;
    }

    public void showStatus(Level level, BlockPos pos, Player player) {
        var blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter))
            return;

        this.showOutlines(level, sulfuricFluxEmitter);
        this.showStatusMessage(level, player, sulfuricFluxEmitter);
    }

    private void showOutlines(Level level, SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
        if (sulfuricFluxEmitter.targetPedestal != null) {
            BlockPos pos = sulfuricFluxEmitter.targetPedestal.getBlockPos();
            VoxelShape shape = Shapes.block();

            var isValid = level.getBlockEntity(sulfuricFluxEmitter.targetPedestal.getBlockPos()) instanceof ReformationTargetPedestalBlockEntity;

            Outliner.get().showAABB(sulfuricFluxEmitter.targetPedestal, shape.bounds()
                            .move(pos), 20 * 5)
                    .colored(isValid ? sulfuricFluxEmitter.targetPedestal.getColor().getRGB() : 0xFF0000)
                    .lineWidth(1 / 16f);
        }
        if (sulfuricFluxEmitter.resultPedestal != null) {
            BlockPos pos = sulfuricFluxEmitter.resultPedestal.getBlockPos();
            VoxelShape shape = Shapes.block();

            var isValid = level.getBlockEntity(sulfuricFluxEmitter.resultPedestal.getBlockPos()) instanceof ReformationResultPedestalBlockEntity;

            Outliner.get().showAABB(sulfuricFluxEmitter.resultPedestal, shape.bounds()
                            .move(pos), 20 * 5)
                    .colored(isValid ? sulfuricFluxEmitter.resultPedestal.getColor().getRGB() : 0xFF0000)
                    .lineWidth(1 / 16f);
        }

        for (var sourcePedestal : sulfuricFluxEmitter.sourcePedestals) {
            BlockPos pos = sourcePedestal.getBlockPos();
            VoxelShape shape = Shapes.block();

            var isValid = level.getBlockEntity(sourcePedestal.getBlockPos()) instanceof ReformationSourcePedestalBlockEntity;

            Outliner.get().showAABB(sourcePedestal, shape.bounds()
                            .move(pos), 20 * 5)
                    .colored(isValid ? sourcePedestal.getColor().getRGB() : 0xFF0000)
                    .lineWidth(1 / 16f);
        }
    }

    private void showStatusMessage(Level level, Player player, SulfuricFluxEmitterBlockEntity sulfuricFluxEmitter) {
        if (sulfuricFluxEmitter.sourcePedestalsWithContents.isEmpty() && sulfuricFluxEmitter.targetPedestal == null && sulfuricFluxEmitter.resultPedestal == null) {
            player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SELECTION).withStyle(ChatFormatting.RED), true);
        } else {
            //here we actually check if the pedestals are valid
            var hasTarget = sulfuricFluxEmitter.targetPedestal != null && level.getBlockEntity(sulfuricFluxEmitter.targetPedestal.getBlockPos()) instanceof ReformationTargetPedestalBlockEntity;
            var hasResult = sulfuricFluxEmitter.resultPedestal != null && level.getBlockEntity(sulfuricFluxEmitter.resultPedestal.getBlockPos()) instanceof ReformationResultPedestalBlockEntity;

            var sources = sulfuricFluxEmitter.sourcePedestals.stream()
                    .map(p -> level.getBlockEntity(p.getBlockPos()))
                    .filter(e -> e instanceof ReformationSourcePedestalBlockEntity)
                    .count();

            if (!hasTarget) {
                player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_TARGET).withStyle(ChatFormatting.RED), true);
            }
            if (sources <= 0) {
                player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_SOURCES).withStyle(ChatFormatting.RED), true);
            }
            if (!hasResult) {
                player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER_NO_RESULT).withStyle(ChatFormatting.RED), true);
            }

            if (hasTarget && sources > 0 && hasResult) {
                player.displayClientMessage(Component.translatable(
                        TheurgyConstants.I18n.Behaviour.SELECTION_SUMMARY_SULFURIC_FLUX_EMITTER,
                        Component.literal(String.valueOf(sources)).withStyle(ChatFormatting.DARK_PURPLE),
                        Component.literal(String.valueOf(1)).withStyle(ChatFormatting.BLUE),
                        Component.literal(String.valueOf(1)).withStyle(ChatFormatting.GREEN)
                ).withStyle(ChatFormatting.WHITE), true);
            }
        }
    }
}
