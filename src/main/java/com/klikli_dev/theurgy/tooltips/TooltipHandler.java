// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.tooltips;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TooltipHandler {
    private static final Map<Item, TooltipDataProvider> tooltipDataProviders = new HashMap<>();
    private static final List<String> namespacesToListenFor = new ArrayList<>();

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        //Note to handle fluid stacks in JEI we'd need RenderTooltipEvent (probably .Pre)
        ItemStack stack = event.getItemStack();

        var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        //only run for enabled namespaces to easily improve performance
        if (namespacesToListenFor.contains(itemId.getNamespace()) || ServerConfig.get().tooltipHandler.additionalTooltipHandlerNamespaces.get().contains(itemId.getNamespace())) {
            String tooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.SUFFIX;
            String extendedTooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.EXTENDED_SUFFIX;
            String usageTooltipKey = stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.USAGE_SUFFIX;

            boolean tooltipExists = I18n.exists(tooltipKey);
            boolean extendedTooltipExists = I18n.exists(extendedTooltipKey);
            boolean usageTooltipExists = I18n.exists(usageTooltipKey);

            var additionalTooltipData = getAdditionalTooltipData(stack);

            if (tooltipExists) {
                event.getToolTip().add(Component.translatable(tooltipKey, additionalTooltipData.toArray()).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            }

            //The (conditional) literal " "'s are a hack to add newlines between the additional tooltips headings/"show ..." hints and the text.
            //These newlines are conditional because e.g. they should not appear if there are just two "show ..." hints after each other
            if (extendedTooltipExists) {
                if (Screen.hasShiftDown()) {
                    event.getToolTip().add(Component.literal(" "));
                    event.getToolTip().add(Component.translatable(TheurgyConstants.I18n.Tooltip.EXTENDED_HEADING, additionalTooltipData.toArray()));
                    event.getToolTip().add(Component.translatable(extendedTooltipKey, additionalTooltipData.toArray()).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                } else {
                    event.getToolTip().add(Component.literal(" "));
                    event.getToolTip().add(Component.translatable(TheurgyConstants.I18n.Tooltip.SHOW_EXTENDED, additionalTooltipData.toArray()));
                }
            }

            if (usageTooltipExists) {
                if (Screen.hasControlDown()) {
                    if (!extendedTooltipExists || Screen.hasShiftDown())
                        event.getToolTip().add(Component.literal(" "));
                    event.getToolTip().add(Component.translatable(TheurgyConstants.I18n.Tooltip.USAGE_HEADING, additionalTooltipData.toArray()));
                    event.getToolTip().add(Component.translatable(usageTooltipKey, additionalTooltipData.toArray()).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                } else {
                    if (!extendedTooltipExists || Screen.hasShiftDown())
                        event.getToolTip().add(Component.literal(" "));
                    event.getToolTip().add(Component.translatable(TheurgyConstants.I18n.Tooltip.SHOW_USAGE, additionalTooltipData.toArray()));
                }
            }
        }
    }


    /**
     * Allows to provide additional @{@link MutableComponent}s as parameter
     * to the main tooltip @{@link MutableComponent}
     * <p>
     * Should be called in @{@link net.neoforged.fml.event.lifecycle.FMLClientSetupEvent}
     */
    public static void registerTooltipDataProvider(Item item, TooltipDataProvider provider) {
        tooltipDataProviders.put(item, provider);
    }

    /**
     * Register a namespace (= mod id) of items to listen for during tooltip handling.
     * Should be called in @{@link net.neoforged.fml.event.lifecycle.FMLClientSetupEvent}
     */
    public static void registerNamespaceToListenTo(String namespace) {
        namespacesToListenFor.add(namespace);
    }

    public static List<MutableComponent> getAdditionalTooltipData(ItemStack stack) {
        var provider = tooltipDataProviders.get(stack.getItem());
        if (provider != null) {
            return provider.getTooltipData(stack);
        }
        return ImmutableList.of();
    }

    public static MutableComponent holdShift() {
        return Component.translatable(TheurgyConstants.I18n.Tooltip.SHOW_EXTENDED);
    }
}
