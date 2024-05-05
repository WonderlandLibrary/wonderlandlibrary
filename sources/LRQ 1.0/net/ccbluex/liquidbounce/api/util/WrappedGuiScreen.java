/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.util;

import java.io.IOException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public abstract class WrappedGuiScreen
extends MinecraftInstance {
    public IGuiScreen representedScreen;

    public final IGuiScreen getRepresentedScreen() {
        return this.representedScreen;
    }

    public final void setRepresentedScreen(IGuiScreen iGuiScreen) {
        this.representedScreen = iGuiScreen;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.representedScreen.superDrawScreen(mouseX, mouseY, partialTicks);
    }

    public void initGui() {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.representedScreen.superMouseClicked(mouseX, mouseY, mouseButton);
    }

    public void updateScreen() {
    }

    public void handleMouseInput() throws IOException {
        this.representedScreen.superHandleMouseInput();
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        this.representedScreen.superKeyTyped(typedChar, keyCode);
    }

    public void actionPerformed(IGuiButton button) throws IOException {
    }

    public void onGuiClosed() {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.representedScreen.superMouseReleased(mouseX, mouseY, state);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}

