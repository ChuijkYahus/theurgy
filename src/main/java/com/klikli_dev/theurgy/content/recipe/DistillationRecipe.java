// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

public class DistillationRecipe implements Recipe<ItemHandlerRecipeInput> {

    public static final int DEFAULT_TIME = 100;

    public static final MapCodec<DistillationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter((r) -> r.ingredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, DistillationRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DistillationRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            r -> r.ingredient,
            ItemStack.OPTIONAL_STREAM_CODEC,
            r -> r.result,
            ByteBufCodecs.INT,
            r -> r.time,
            DistillationRecipe::new
    );

    protected final SizedIngredient ingredient;
    protected final ItemStack result;
    protected final int time;

    public DistillationRecipe(SizedIngredient pIngredient, ItemStack pResult, int time) {
        this.ingredient = pIngredient;
        this.result = pResult;
        this.time = time;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.DISTILLATION.get();
    }

    @Override
    public boolean matches(@NotNull ItemHandlerRecipeInput pContainer, @NotNull Level pLevel) {
        var stack = pContainer.getItem(0);
        return this.ingredient.test(stack);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ItemHandlerRecipeInput pInv, HolderLookup.@NotNull Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider pRegistries) {
        return this.result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient.ingredient());
        return nonnulllist;
    }

    public SizedIngredient getIngredient() {
        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredient.count();
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.DISTILLER.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.DISTILLATION.get();
    }

    public int getTime() {
        return this.time;
    }

    public static class Serializer implements RecipeSerializer<DistillationRecipe> {

        @Override
        public @NotNull MapCodec<DistillationRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DistillationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}