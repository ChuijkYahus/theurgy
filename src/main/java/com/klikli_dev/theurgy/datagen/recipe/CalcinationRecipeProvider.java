// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class CalcinationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = CalcinationRecipe.DEFAULT_TIME;

    public CalcinationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "calcination");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeRecipe("_from_stone", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.STONES));
        this.makeRecipe("_from_sandstone", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.SANDSTONE_BLOCKS));
        this.makeRecipe("_from_cobblestone", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.COBBLESTONES));
        this.makeRecipe("_from_dirt", new Builder(SaltRegistry.STRATA).sizedIngredient(ItemTags.DIRT));
        this.makeRecipe("_from_sand", new Builder(SaltRegistry.STRATA).sizedIngredient(ItemTags.SAND));
        this.makeRecipe("_from_gravel", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.GRAVEL));
        this.makeRecipe("_from_clay", new Builder(SaltRegistry.STRATA, 4).sizedIngredient(Items.CLAY));
        this.makeRecipe("_from_clay_ball", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.CLAY_BALL));
        this.makeRecipe("_from_netherrack", new Builder(SaltRegistry.STRATA).sizedIngredient(Tags.Items.NETHERRACKS));
        this.makeRecipe("_from_soul_sand", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.SOUL_SAND));
        this.makeRecipe("_from_soul_soil", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.SOUL_SOIL));
        this.makeRecipe("_from_blackstone", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.BLACKSTONE));
        this.makeRecipe("_from_terracotta", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.TERRACOTTA));
        this.makeRecipe("_from_concrete", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Tags.Items.CONCRETES));
        this.makeRecipe("_from_crimson_nylium", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.CRIMSON_NYLIUM));
        this.makeRecipe("_from_warped_nylium", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.WARPED_NYLIUM));
        this.makeRecipe("_from_end_stone", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.END_STONE));
        this.makeRecipe("_from_purpur_block", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.PURPUR_BLOCK));
        this.makeRecipe("_from_mycelium", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.MYCELIUM));
        this.makeRecipe("_from_obsidian", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.OBSIDIAN));
        this.makeRecipe("_from_crying_obsidian", new Builder(SaltRegistry.STRATA, 3).sizedIngredient(Items.CRYING_OBSIDIAN));
        this.makeRecipe("_from_snowball", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.SNOWBALL));
        this.makeRecipe("_from_ice", new Builder(SaltRegistry.STRATA).sizedIngredient(Items.ICE));
        this.makeRecipe("_from_packed_ice", new Builder(SaltRegistry.STRATA, 9).sizedIngredient(Items.PACKED_ICE));
        this.makeRecipe("_from_blue_ice", new Builder(SaltRegistry.STRATA, 64).sizedIngredient(Items.BLUE_ICE));
        this.makeRecipe("_from_magma_block", new Builder(SaltRegistry.STRATA, 2).sizedIngredient(Items.MAGMA_BLOCK));
        this.makeRecipe("_from_crystallized_water", new Builder(SaltRegistry.STRATA, 4).sizedIngredient(ItemRegistry.CRYSTALLIZED_WATER));
        this.makeRecipe("_from_crystallized_lava", new Builder(SaltRegistry.STRATA, 8).sizedIngredient(ItemRegistry.CRYSTALLIZED_LAVA));

        this.makeRecipe("_from_ores", new Builder(SaltRegistry.MINERAL, 3).sizedIngredient(Tags.Items.ORES));
        this.makeRecipe("_from_raw_materials", new Builder(SaltRegistry.MINERAL).sizedIngredient(Tags.Items.RAW_MATERIALS));
        this.makeRecipe("_from_ingots", new Builder(SaltRegistry.MINERAL, 2).sizedIngredient(Tags.Items.INGOTS));
        this.makeRecipe("_from_gems", new Builder(SaltRegistry.MINERAL, 2).sizedIngredient(Tags.Items.GEMS));
        this.makeRecipe("_from_other_minerals", new Builder(SaltRegistry.MINERAL, 2).sizedIngredient(ItemTagRegistry.OTHER_MINERALS));
        this.makeRecipe("_from_strata_salt", new Builder(SaltRegistry.MINERAL).sizedIngredient(SaltRegistry.STRATA.get(), 5));
        this.makeRecipe("_from_crops", new Builder(SaltRegistry.PLANT).sizedIngredient(Tags.Items.CROPS));
        this.makeRecipe("_from_logs", new Builder(SaltRegistry.PLANT).sizedIngredient(ItemTags.LOGS));
        this.makeRecipe("_from_leaves", new Builder(SaltRegistry.PLANT).sizedIngredient(ItemTags.LEAVES));
        this.makeRecipe("_from_saplings", new Builder(SaltRegistry.PLANT).sizedIngredient(ItemTags.SAPLINGS));
        this.makeRecipe("_from_plant_salt", new Builder(SaltRegistry.CREATURE).sizedIngredient(SaltRegistry.PLANT.get(), 2));
    }


    protected void makeRecipe(String suffix, Builder recipe) {
        this.recipeConsumer.accept(this.modLoc(this.name(recipe.result()) + suffix), recipe.build());
    }

    @Override
    public String getName() {
        return "Calcination Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {

        private final ItemStack result;

        protected Builder(ItemLike result) {
            this(result, 1);
        }

        protected Builder(ItemLike result, int count) {
            this(new ItemStack(result, count));
        }

        protected Builder(ItemStack result) {
            super(RecipeTypeRegistry.CALCINATION);
            this.result(result);
            this.time(TIME);
            this.result = result;
        }

        public ItemStack result() {
            return this.result;
        }

        public Builder sizedIngredient(TagKey<Item> tag) {
            return super.sizedIngredient(tag, 1);
        }

        public Builder sizedIngredient(ItemLike item) {
            return super.sizedIngredient(item, 1);
        }
    }
}
