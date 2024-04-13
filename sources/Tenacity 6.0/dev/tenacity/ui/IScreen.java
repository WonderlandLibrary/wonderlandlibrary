package dev.tenacity.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public interface IScreen {

    Minecraft mc = Minecraft.getMinecraft();
    ScaledResolution sr = new ScaledResolution(mc);

    void initGUI();
    void keyTyped(final char typedChar, final int keyCode);
    void drawScreen(final int mouseX, final int mouseY);
    void mouseClicked(final int mouseX, final int mouseY, final int mouseButton);
    void mouseReleased(final int mouseX, final int mouseY, final int state);

}
