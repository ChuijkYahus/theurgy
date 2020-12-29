/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, Theurgy.MODID);

    public static final NonNullLazy<IRecipeType<EssentiaRecipe>> ESSENTIA_TYPE =
            NonNullLazy.of(() -> IRecipeType.register("theurgy:essentia"));

    public static final NonNullLazy<IRecipeType<CrucibleRecipe>> CRUCIBLE_TYPE =
            NonNullLazy.of(() -> IRecipeType.register("theurgy:crucible"));

    public static final RegistryObject<IRecipeSerializer<EssentiaRecipe>> ESSENTIA = RECIPES.register("essentia",
            () -> EssentiaRecipe.SERIALIZER);

    public static final RegistryObject<IRecipeSerializer<CrucibleRecipe>> CRUCIBLE = RECIPES.register("crucible",
            () -> CrucibleRecipe.SERIALIZER);
}
