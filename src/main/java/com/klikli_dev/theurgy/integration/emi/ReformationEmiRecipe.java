// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.emi;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.integration.jei.JeiDrawables;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;

public class ReformationEmiRecipe implements EmiRecipe {

    private final RecipeHolder<ReformationRecipe> recipe;

    public ReformationEmiRecipe(RecipeHolder<ReformationRecipe> recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiPlugin.REFORMATION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var inputs = new ArrayList<EmiIngredient>();
        this.recipe.value().getSources().forEach(source -> inputs.add(EmiIngredient.of(source.ingredient(), source.count())));
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)));
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        var catalysts = new ArrayList<EmiIngredient>();
        catalysts.add(EmiStack.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()));
        catalysts.add(EmiStack.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()));
        catalysts.add(EmiStack.of(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()));
        catalysts.add(EmiStack.of(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()));
        catalysts.add(EmiIngredient.of(this.recipe.value().getTarget()));
        return catalysts;
    }

    @Override
    public int getDisplayWidth() {
        return 180;
    }

    @Override
    public int getDisplayHeight() {
        return 100;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 130, 19);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 19, 19);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 65, 19);

        widgets.addSlot(EmiStack.of(ItemRegistry.SULFURIC_FLUX_EMITTER.get()), 1, 19).drawBack(false);

        widgets.addSlot(EmiIngredient.of(this.recipe.value().getTarget()), 45, 19).catalyst(true)
                .appendTooltip(Component.literal(" "))
                .appendTooltip(Component.translatable(TheurgyConstants.I18n.JEI.TARGET_SULFUR_TOOLTIP).withStyle(s -> s.withItalic(true).withColor(ChatFormatting.YELLOW)));


        widgets.addSlot(EmiStack.of(ItemRegistry.REFORMATION_TARGET_PEDESTAL.get()), 45, 42).drawBack(false);

        int sourceSlotX = 90;
        int startY = 55;
        int sourceSlotY = startY; // Start from the bottom

        for (int i = 0; i < 8; i++) {
            if (i < recipe.value().getSources().size()){
                var ingredient = recipe.value().getSources().get(i);
                widgets.addSlot(EmiIngredient.of(ingredient.ingredient(), ingredient.count()), sourceSlotX, sourceSlotY);
            } else {
                widgets.addSlot(sourceSlotX, sourceSlotY);
            }

            sourceSlotY -= 18; // Move upwards
            if (i % 4 == 3) {
                sourceSlotY = startY; // Reset to the bottom
                sourceSlotX += 18; // Move to the right
            }
        }

        //draw mercury flux
        int flux = recipe.value().getMercuryFlux();
        Component mercuryFluxComponent = Component.translatable(TheurgyConstants.I18n.JEI.MERCURY_FLUX, flux);
        widgets.addText(mercuryFluxComponent, 1, 90, 0xFF808080, false);

        //draw source pedestal count
        int count = recipe.value().getSources().size();
        Component sourcePedestalComponent = Component.translatable(TheurgyConstants.I18n.JEI.SOURCE_PEDESTAL_COUNT, count);
        widgets.addText(sourcePedestalComponent, 95 - font.width(sourcePedestalComponent), 78, 0xFF808080, false);

        widgets.addSlot(EmiStack.of(ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get()), 90 + 9, startY + 18).drawBack(false);

        widgets.addSlot(EmiStack.of(this.recipe.value().getResultItem(RegistryAccess.EMPTY)), 160, 19).recipeContext(this);

        widgets.addSlot(EmiStack.of(ItemRegistry.REFORMATION_RESULT_PEDESTAL.get()), 160, 42).drawBack(false);
    }
}