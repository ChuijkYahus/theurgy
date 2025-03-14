// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectedPoint;
import com.klikli_dev.theurgy.content.render.Color;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.IntFunction;

public class SulfuricFluxEmitterSelectedPoint extends SelectedPoint<SulfuricFluxEmitterSelectedPoint> {

    public static final Codec<SulfuricFluxEmitterSelectedPoint> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            BlockPos.CODEC.fieldOf("blockPos").forGetter(SulfuricFluxEmitterSelectedPoint::getBlockPos),
                            StringRepresentable.fromEnum(Type::values).fieldOf("type").forGetter(SulfuricFluxEmitterSelectedPoint::getType)
                    )
                    .apply(instance, SulfuricFluxEmitterSelectedPoint::new));

    public static final Codec<List<SulfuricFluxEmitterSelectedPoint>> LIST_CODEC = Codec.list(CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, SulfuricFluxEmitterSelectedPoint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SelectedPoint::getBlockPos,
            Type.STREAM_CODEC,
            SulfuricFluxEmitterSelectedPoint::getType,
            SulfuricFluxEmitterSelectedPoint::new
    );

    protected Type type;

    public SulfuricFluxEmitterSelectedPoint(BlockPos blockPos, Type type) {
        super(blockPos);
        this.type = type;
    }

    protected SulfuricFluxEmitterSelectedPoint(Level level, BlockPos blockPos, BlockState blockState, Type type) {
        super(level, blockPos, blockState);
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public Color getColor() {
        return this.type.getColor();
    }

    @Override
    public Component getModeMessage() {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_SULFURIC_FLUX_EMITTER);
    }

    @Override
    public boolean cycleMode() {
        //has no mode
        //but double-selecting removes the point
        return false;
    }

    @Override
    public Codec<SulfuricFluxEmitterSelectedPoint> codec() {
        return CODEC;
    }

    public enum Type implements StringRepresentable {
        SOURCE("SOURCE", new Color(0xFF00FF, false)),
        TARGET("TARGET", new Color(0x0000FF, false)),
        RESULT("RESULT", new Color(0x008000, false));

        public static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::ordinal);
        private final String name;
        private final Color color;

        Type(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return this.name;
        }

        public Color getColor() {
            return this.color;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
