// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.gui;

import net.minecraft.client.gui.GuiVideoSettings;
import java.util.List;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenOF extends GuiScreen
{
    protected void actionPerformedRightClick(final GuiButton button) throws IOException {
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1) {
            final GuiButton guibutton = getSelectedButton(mouseX, mouseY, this.buttonList);
            if (guibutton != null && guibutton.enabled) {
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformedRightClick(guibutton);
            }
        }
    }
    
    public static GuiButton getSelectedButton(final int x, final int y, final List<GuiButton> listButtons) {
        for (int i = 0; i < listButtons.size(); ++i) {
            final GuiButton guibutton = listButtons.get(i);
            if (guibutton.visible) {
                final int j = GuiVideoSettings.getButtonWidth(guibutton);
                final int k = GuiVideoSettings.getButtonHeight(guibutton);
                if (x >= guibutton.xPosition && y >= guibutton.yPosition && x < guibutton.xPosition + j && y < guibutton.yPosition + k) {
                    return guibutton;
                }
            }
        }
        return null;
    }
}
