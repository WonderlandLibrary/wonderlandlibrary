/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0016J \u0010\u0019\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0016J(\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/RenderItemImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "wrapped", "Lnet/minecraft/client/renderer/RenderItem;", "(Lnet/minecraft/client/renderer/RenderItem;)V", "getWrapped", "()Lnet/minecraft/client/renderer/RenderItem;", "value", "", "zLevel", "getZLevel", "()F", "setZLevel", "(F)V", "equals", "", "other", "", "renderItemAndEffectIntoGUI", "", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "x", "", "y", "renderItemIntoGUI", "renderItemOverlays", "fontRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "LiKingSense"})
public final class RenderItemImpl
implements IRenderItem {
    @NotNull
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
    public void renderItemAndEffectIntoGUI(@NotNull IItemStack stack, int x, int y) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)stack, (String)"stack");
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
    public void renderItemIntoGUI(@NotNull IItemStack stack, int x, int y) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)stack, (String)"stack");
        IItemStack iItemStack = stack;
        RenderItem renderItem = this.wrapped;
        boolean $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        renderItem.func_175042_a(itemStack, x, y);
    }

    @Override
    public void renderItemOverlays(@NotNull IFontRenderer fontRenderer, @NotNull IItemStack stack, int x, int y) {
        IItemStack $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)fontRenderer, (String)"fontRenderer");
        Intrinsics.checkParameterIsNotNull((Object)stack, (String)"stack");
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
        return other instanceof RenderItemImpl && Intrinsics.areEqual((Object)((RenderItemImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final RenderItem getWrapped() {
        return this.wrapped;
    }

    public RenderItemImpl(@NotNull RenderItem wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

