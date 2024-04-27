package net.ccbluex.liquidbounce.api.minecraft.client.gui;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\n\u0000\n\n\b\n\n\b\n\b\n\b\bf\u000020R0X¦¢\f\b\"\bR\b0\tX¦¢\f\b\n\"\b\f\rR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGui;", "displayString", "", "getDisplayString", "()Ljava/lang/String;", "setDisplayString", "(Ljava/lang/String;)V", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "id", "", "getId", "()I", "Pride"})
public interface IGuiButton
extends IGui {
    @NotNull
    public String getDisplayString();

    public void setDisplayString(@NotNull String var1);

    public int getId();

    public boolean getEnabled();

    public void setEnabled(boolean var1);
}
