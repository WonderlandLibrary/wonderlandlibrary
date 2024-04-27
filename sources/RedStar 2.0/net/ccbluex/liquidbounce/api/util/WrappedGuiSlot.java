package net.ccbluex.liquidbounce.api.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\n\u0000\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\n\b\b&\u000020B50000\b0\t0¢\nJ\b0H&J80202020202020H&J(020202020H&J\b0H&J 020H&R0\fX.¢\n\u0000\b\r\"\b¨!"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;", "", "mc", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "width", "", "height", "top", "bottom", "slotHeight", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;IIIII)V", "represented", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiSlot;", "getRepresented", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiSlot;", "setRepresented", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiSlot;)V", "drawBackground", "", "drawSlot", "id", "x", "y", "var4", "var5", "var6", "elementClicked", "var1", "doubleClick", "", "var3", "getSize", "isSelected", "Pride"})
public abstract class WrappedGuiSlot {
    @NotNull
    public IGuiSlot represented;

    @NotNull
    public final IGuiSlot getRepresented() {
        IGuiSlot iGuiSlot = this.represented;
        if (iGuiSlot == null) {
            Intrinsics.throwUninitializedPropertyAccessException("represented");
        }
        return iGuiSlot;
    }

    public final void setRepresented(@NotNull IGuiSlot iGuiSlot) {
        Intrinsics.checkParameterIsNotNull(iGuiSlot, "<set-?>");
        this.represented = iGuiSlot;
    }

    public abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

    public abstract void drawBackground();

    public abstract void elementClicked(int var1, boolean var2, int var3, int var4);

    public abstract int getSize();

    public abstract boolean isSelected(int var1);

    public WrappedGuiSlot(@NotNull IMinecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        Intrinsics.checkParameterIsNotNull(mc, "mc");
        WrapperImpl.INSTANCE.getClassProvider().wrapGuiSlot(this, mc, width, height, top, bottom, slotHeight);
    }
}
