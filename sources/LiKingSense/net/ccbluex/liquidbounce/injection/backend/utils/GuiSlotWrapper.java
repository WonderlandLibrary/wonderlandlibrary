/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiSlot
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.GuiSlotImpl;
import net.ccbluex.liquidbounce.injection.backend.MinecraftImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0007\u0012\u0006\u0010\n\u001a\u00020\u0007\u0012\u0006\u0010\u000b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\fJ\b\u0010\u000f\u001a\u00020\u0010H\u0014J@\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u0019H\u0014J(\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u0007H\u0016J\b\u0010\u001f\u001a\u00020\u0007H\u0014J\u0010\u0010 \u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u0007H\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006!"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/utils/GuiSlotWrapper;", "Lnet/minecraft/client/gui/GuiSlot;", "wrapped", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;", "mc", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "width", "", "height", "topIn", "bottomIn", "slotHeightIn", "(Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;IIIII)V", "getWrapped", "()Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;", "drawBackground", "", "drawSlot", "slotIndex", "xPos", "yPos", "heightIn", "mouseXIn", "mouseYIn", "partialTicks", "", "elementClicked", "isDoubleClick", "", "mouseX", "mouseY", "getSize", "isSelected", "LiKingSense"})
public final class GuiSlotWrapper
extends GuiSlot {
    @NotNull
    private final WrappedGuiSlot wrapped;

    protected int func_148127_b() {
        return this.wrapped.getSize();
    }

    protected void func_192637_a(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
        this.wrapped.drawSlot(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn);
    }

    protected boolean func_148131_a(int slotIndex) {
        return this.wrapped.isSelected(slotIndex);
    }

    protected void func_148123_a() {
        this.wrapped.drawBackground();
    }

    public void func_148144_a(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.wrapped.elementClicked(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    @NotNull
    public final WrappedGuiSlot getWrapped() {
        return this.wrapped;
    }

    /*
     * WARNING - void declaration
     */
    public GuiSlotWrapper(@NotNull WrappedGuiSlot wrapped, @NotNull IMinecraft mc, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        Intrinsics.checkParameterIsNotNull((Object)mc, (String)"mc");
        IMinecraft iMinecraft = mc;
        GuiSlotWrapper guiSlotWrapper = this;
        boolean $i$f$unwrap = false;
        Minecraft minecraft = ((MinecraftImpl)$this$unwrap$iv).getWrapped();
        super(minecraft, width, height, topIn, bottomIn, slotHeightIn);
        this.wrapped = wrapped;
        this.wrapped.setRepresented(new GuiSlotImpl(this));
    }
}

