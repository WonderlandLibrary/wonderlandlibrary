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

    @Override
    public float getZLevel() {
        return this.wrapped.field_77023_b;
    }

    @Override
    public void setZLevel(float value) {
        this.wrapped.field_77023_b = value;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void renderItemAndEffectIntoGUI(IItemStack stack, int x, int y) {
        void $this$unwrap$iv;
        IItemStack iItemStack = stack;
        RenderItem renderItem = this.wrapped;
        boolean $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        renderItem.func_180450_b(itemStack, x, y);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void renderItemIntoGUI(IItemStack stack, int x, int y) {
        void $this$unwrap$iv;
        IItemStack iItemStack = stack;
        RenderItem renderItem = this.wrapped;
        boolean $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        renderItem.func_175042_a(itemStack, x, y);
    }

    @Override
    public void renderItemOverlays(IFontRenderer fontRenderer, IItemStack stack, int x, int y) {
        IItemStack $this$unwrap$iv;
        IFontRenderer iFontRenderer = fontRenderer;
        RenderItem renderItem = this.wrapped;
        boolean $i$f$unwrap = false;
        FontRenderer fontRenderer2 = ((FontRendererImpl)((Object)$this$unwrap$iv)).getWrapped();
        $this$unwrap$iv = stack;
        $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        renderItem.func_175030_a(fontRenderer2, itemStack, x, y);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof RenderItemImpl && ((RenderItemImpl)other).wrapped.equals(this.wrapped);
    }

    public final RenderItem getWrapped() {
        return this.wrapped;
    }

    public RenderItemImpl(RenderItem wrapped) {
        this.wrapped = wrapped;
    }
}

