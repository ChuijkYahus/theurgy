// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A custom cached check
 */
public class FermentationCachedCheck implements RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, FermentationRecipe> {

    private final RecipeType<FermentationRecipe> type;
    private final RecipeManager.CachedCheck<ItemHandlerWithFluidRecipeInput, FermentationRecipe> internal;
    @Nullable
    private ResourceLocation lastRecipeForFluidStack;
    @Nullable
    private ResourceLocation lastRecipeForItemStack;
    @Nullable
    private ResourceLocation lastRecipeForItemStackCollection;

    public FermentationCachedCheck(RecipeType<FermentationRecipe> type) {
        this.type = type;
        this.internal = RecipeManager.createCheck(type);
    }

    private boolean matchesRecipe(RecipeHolder<FermentationRecipe> recipe, Collection<ItemStack> input) {
        var ingredients = recipe.value().getIngredients();

        // Check if every input ItemStack matches at least one ingredient in the recipe
        return input.stream().allMatch(stack ->
                ingredients.stream().anyMatch(ingredient -> ingredient.test(stack))
        );
    }

    private Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(Collection<ItemStack> input, Level level, @Nullable ResourceLocation lastRecipe) {
        var recipeManager = level.getRecipeManager();

        if (lastRecipe != null) {
            var recipe = recipeManager.byKeyTyped(this.type, lastRecipe);
            //test only the ingredient without the (separate) fluid ingredient check that the recipe.matches() would.
            if (recipe != null && this.matchesRecipe(recipe, input)) {
                return Optional.of(recipe);
            }
        }

        return recipeManager.byType(this.type).stream().filter((entry) -> this.matchesRecipe(entry, input)).findFirst();
    }

    private Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {
        var recipeManager = level.getRecipeManager();
        if (lastRecipe != null) {

            var recipe = recipeManager.byKeyTyped(this.type, lastRecipe);
            //test only the ingredient without the (separate) fluid ingredient check that the recipe.matches() would.
            if (recipe != null && recipe.value().getIngredients().stream().anyMatch(i -> i.test(stack))) {
                return Optional.of(recipe);
            }
        }

        return recipeManager.byType(this.type).stream().filter((entry) -> entry.value().getIngredients().stream().anyMatch(i -> i.test(stack))).findFirst();
    }

    private Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(FluidStack stack, Level level, @Nullable ResourceLocation lastRecipe) {
        var recipeManager = level.getRecipeManager();
        if (lastRecipe != null) {

            var recipe = recipeManager.byKeyTyped(this.type, lastRecipe);
            //test only the fluid without the (separate) item ingredients check that the recipe.matches() would.
            if (recipe != null && recipe.value().getFluid().ingredient().test(stack)) {
                return Optional.of(recipe);
            }
        }

        return recipeManager.byType(this.type).stream().filter((entry) -> entry.value().getFluid().ingredient().test(stack)).findFirst();
    }

    /**
     * This only checks ingredients, including ingredients already present, not fluids
     */
    public Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(Collection<ItemStack> input, Level level) {
        var optional = this.getRecipeFor(input, level, this.lastRecipeForItemStackCollection);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipeForItemStackCollection = recipeHolder.id();
            return optional;
        } else {
            return Optional.empty();
        }
    }

    /**
     * This only checks ingredients, not fluids
     */
    public Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(ItemStack stack, Level level) {
        var optional = this.getRecipeFor(stack, level, this.lastRecipeForItemStack);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipeForItemStack = recipeHolder.id();
            return optional;
        } else {
            return Optional.empty();
        }
    }

    /**
     * This only checks fluids, not ingredients
     */
    public Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(FluidStack stack, Level level) {
        var optional = this.getRecipeFor(stack, level, this.lastRecipeForFluidStack);
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            this.lastRecipeForFluidStack = recipeHolder.id();
            return optional;
        } else {
            return Optional.empty();
        }
    }

    /**
     * This checks full recipe validity: ingredients + fluids
     */
    @Override
    public @NotNull Optional<RecipeHolder<FermentationRecipe>> getRecipeFor(@NotNull ItemHandlerWithFluidRecipeInput container, @NotNull Level level) {
        return this.internal.getRecipeFor(container, level);
    }
}
