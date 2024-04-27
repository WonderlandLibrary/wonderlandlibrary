package net.ccbluex.liquidbounce.api.minecraft.client.gui;

import java.util.List;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiGameOver;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiChest;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiContainer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000P\n\n\n\u0000\n!\n\n\b\n\n\b\n\b\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\f\n\b\bf\u000020J\b0H&J\b0H&J\b0H&J020\fH&J\b0H&J 020\f20\f20H&J\b 0H&J!02\"0#2$0\fH&J %020\f20\f2&0\fH&J '020\f20\f2(0\fH&R\b00X¦¢\bR0\bX¦¢\b\t\nR0\fX¦¢\b\rR0\fX¦¢\b¨)"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGui;", "buttonList", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "getButtonList", "()Ljava/util/List;", "fontRendererObj", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "getFontRendererObj", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "height", "", "getHeight", "()I", "width", "getWidth", "asGuiChest", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiChest;", "asGuiContainer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiContainer;", "asGuiGameOver", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiGameOver;", "drawBackground", "", "i", "drawDefaultBackground", "superDrawScreen", "mouseX", "mouseY", "partialTicks", "", "superHandleMouseInput", "superKeyTyped", "typedChar", "", "keyCode", "superMouseClicked", "mouseButton", "superMouseReleased", "state", "Pride"})
public interface IGuiScreen
extends IGui {
    public int getWidth();

    public int getHeight();

    @NotNull
    public IFontRenderer getFontRendererObj();

    @NotNull
    public List<IGuiButton> getButtonList();

    @NotNull
    public IGuiContainer asGuiContainer();

    @NotNull
    public IGuiGameOver asGuiGameOver();

    @NotNull
    public IGuiChest asGuiChest();

    public void superMouseReleased(int var1, int var2, int var3);

    public void drawBackground(int var1);

    public void drawDefaultBackground();

    public void superKeyTyped(char var1, int var2);

    public void superHandleMouseInput();

    public void superMouseClicked(int var1, int var2, int var3);

    public void superDrawScreen(int var1, int var2, float var3);
}
