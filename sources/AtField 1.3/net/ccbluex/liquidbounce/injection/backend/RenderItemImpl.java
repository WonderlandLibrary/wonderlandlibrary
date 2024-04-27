/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class RenderItemImpl
implements IRenderItem {
    private final RenderItem wrapped;

    public RenderItemImpl(RenderItem renderItem) {
        this.wrapped = renderItem;
    }

    @Override
    public void renderItemIntoGUI(IItemStack iItemStack, int n, int n2) {
        IItemStack iItemStack2 = iItemStack;
        RenderItem renderItem = this.wrapped;
        boolean bl = false;
        ItemStack itemStack = ((ItemStackImpl)iItemStack2).getWrapped();
        renderItem.func_175042_a(itemStack, n, n2);
    }

    @Override
    public void setZLevel(float f) {
        this.wrapped.field_77023_b = f;
    }

    public final RenderItem getWrapped() {
        return this.wrapped;
    }

    @Override
    public float getZLevel() {
        return this.wrapped.field_77023_b;
    }

    @Override
    public void renderItemAndEffectIntoGUI(IItemStack iItemStack, int n, int n2) {
        IItemStack iItemStack2 = iItemStack;
        RenderItem renderItem = this.wrapped;
        boolean bl = false;
        ItemStack itemStack = ((ItemStackImpl)iItemStack2).getWrapped();
        renderItem.func_180450_b(itemStack, n, n2);
    }

    @Override
    public void renderItemOverlays(IFontRenderer iFontRenderer, IItemStack iItemStack, int n, int n2) {
        Object object = iFontRenderer;
        RenderItem renderItem = this.wrapped;
        boolean bl = false;
        FontRenderer fontRenderer = ((FontRendererImpl)object).getWrapped();
        object = iItemStack;
        bl = false;
        ItemStack itemStack = ((ItemStackImpl)object).getWrapped();
        renderItem.func_175030_a(fontRenderer, itemStack, n, n2);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof RenderItemImpl && ((RenderItemImpl)object).wrapped.equals(this.wrapped);
    }
}

