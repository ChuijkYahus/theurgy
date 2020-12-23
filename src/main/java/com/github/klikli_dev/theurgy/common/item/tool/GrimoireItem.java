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

package com.github.klikli_dev.theurgy.common.item.tool;

import com.github.klikli_dev.theurgy.Theurgy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.List;

public class GrimoireItem extends Item {
    //region Fields
    public static final ResourceLocation GRIMOIRE = new ResourceLocation(Theurgy.MODID, "grimoire");
    //endregion Fields

    //region Initialization
    public GrimoireItem(Properties properties) {
        super(properties);
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) playerIn, GRIMOIRE);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Book book = BookRegistry.INSTANCE.books.get(GRIMOIRE);
        if (book != null && book.contents != null) {
            tooltip.add(book.contents.book.getSubtitle().mergeStyle(TextFormatting.GRAY));
        }

    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        Book book = BookRegistry.INSTANCE.books.get(GRIMOIRE);
        return book != null ? new TranslationTextComponent(book.name, new Object[0]) : super.getDisplayName(stack);
    }
    //endregion Overrides

}
