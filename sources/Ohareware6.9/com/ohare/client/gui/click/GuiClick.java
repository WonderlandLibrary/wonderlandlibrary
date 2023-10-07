package com.ohare.client.gui.click;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * made by oHare for oHareWare
 *
 * @since 6/1/2019
 **/
public class GuiClick extends GuiScreen {
    private Frame frame;
    public void init() {
        GL11.glColor4f(1, 1, 1, 1);
        frame = new Frame("oHareWare", 2, 2, 320, 220);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        frame.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        frame.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        frame.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        frame.keyTyped(typedChar, key);
        if (key == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }
}