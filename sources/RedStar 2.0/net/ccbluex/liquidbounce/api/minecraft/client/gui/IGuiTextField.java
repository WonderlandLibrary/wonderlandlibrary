package net.ccbluex.liquidbounce.api.minecraft.client.gui;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\n\u0000\n\n\b\n\b\n\b\n\n\b\n\n\b\n\f\n\b\b\bf\u000020J\b0H&J02020\bH&J 020\b20\b20\bH&J02020\bH&J\b 0H&R0X¦¢\f\b\"\bR0\bX¦¢\f\b\t\n\"\b\fR\r0X¦¢\f\b\"\bR0\bX¦¢\b\n¨!"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiTextField;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGui;", "isFocused", "", "()Z", "setFocused", "(Z)V", "maxStringLength", "", "getMaxStringLength", "()I", "setMaxStringLength", "(I)V", "text", "", "getText", "()Ljava/lang/String;", "setText", "(Ljava/lang/String;)V", "xPosition", "getXPosition", "drawTextBox", "", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseX", "mouseY", "mouseButton", "textboxKeyTyped", "updateCursorCounter", "Pride"})
public interface IGuiTextField
extends IGui {
    public int getXPosition();

    @NotNull
    public String getText();

    public void setText(@NotNull String var1);

    public boolean isFocused();

    public void setFocused(boolean var1);

    public int getMaxStringLength();

    public void setMaxStringLength(int var1);

    public void updateCursorCounter();

    public boolean textboxKeyTyped(char var1, int var2);

    public void drawTextBox();

    public void mouseClicked(int var1, int var2, int var3);

    public boolean keyTyped(char var1, int var2);
}
