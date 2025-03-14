// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.gui.GuiTextures;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.integration.jei.JeiRecipeTypes;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class DistillationCategory implements IRecipeCategory<RecipeHolder<DistillationRecipe>> {
    private final IDrawableAnimated animatedFire;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public DistillationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(82, 43);

        this.animatedFire = JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_FIRE_FULL, 300, IDrawableAnimated.StartDirection.TOP, true);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.DISTILLER.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.DISTILLATION_CATEGORY);

        //We need different animations for different cook times, hence the cache
        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull IDrawableAnimated load(@NotNull Integer cookTime) {
                        return JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_ARROW_RIGHT_FULL, cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    protected IDrawableAnimated getAnimatedArrow(RecipeHolder<DistillationRecipe> recipe) {
        int cookTime = recipe.value().getTime();
        if (cookTime <= 0) {
            cookTime = DistillationRecipe.DEFAULT_TIME;
        }
        return this.cachedAnimatedArrow.getUnchecked(cookTime);
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(@NotNull RecipeHolder<DistillationRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GuiTextures.JEI_FIRE_EMPTY.render(guiGraphics, 1, 20);
        this.animatedFire.draw(guiGraphics, 1, 20);

        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(guiGraphics, 24, 8);
        this.getAnimatedArrow(recipe).draw(guiGraphics, 24, 8);

        this.drawCookTime(recipe, guiGraphics, 34);
    }

    protected void drawCookTime(RecipeHolder<DistillationRecipe> recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.value().getTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable(TheurgyConstants.I18n.Gui.SMELTING_TIME_SECONDS, cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public @NotNull Component getTitle() {
        return this.localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DistillationRecipe> recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.value().getIngredient().getItems()).map(i -> i.copyWithCount(recipe.value().getIngredientCount())).toList());

        builder.addSlot(OUTPUT, 61, 9)
                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
                .addItemStack(recipe.value().getResultItem(RegistryAccess.EMPTY));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<DistillationRecipe>> getRecipeType() {
        return JeiRecipeTypes.DISTILLATION;
    }
}
