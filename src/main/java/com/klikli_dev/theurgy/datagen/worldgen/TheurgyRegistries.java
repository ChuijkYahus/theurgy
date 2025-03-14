// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.worldgen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class TheurgyRegistries {
    public static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap)
            .add(Registries.BIOME, TheurgyRegistries::bootstrapBiomes)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap);

    public static void bootstrapBiomes(BootstrapContext<Biome> context) {
        //doesn't need to do anything, just gives us access to a biome registry with empty tag lookup in our other boopstrap contexts
    }
}
