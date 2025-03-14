// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;


import java.util.function.Supplier;

public class DistillationCraftingBehaviour extends CraftingBehaviour<ItemHandlerRecipeInput, DistillationRecipe, DistillationCachedCheck> {
    public DistillationCraftingBehaviour(BlockEntity blockEntity, Supplier<IItemHandlerModifiable> inputInventorySupplier, Supplier<IItemHandlerModifiable> outputInventorySupplier) {
        super(blockEntity,
                Lazy.of(() -> new ItemHandlerRecipeInput(inputInventorySupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new DistillationCachedCheck(RecipeTypeRegistry.DISTILLATION.get()));
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return this.recipeCachedCheck.getRecipeFor(stack, this.blockEntity.getLevel()).isPresent();
    }

    @Override
    protected int getIngredientCount(RecipeHolder<DistillationRecipe> recipe) {
        return recipe.value().getIngredientCount();
    }

    @Override
    protected int getCraftingTime(RecipeHolder<DistillationRecipe> recipe) {
        return recipe.value().getTime();
    }

    @Override
    protected int getDefaultCraftingTime() {
        return DistillationRecipe.DEFAULT_TIME;
    }
}
