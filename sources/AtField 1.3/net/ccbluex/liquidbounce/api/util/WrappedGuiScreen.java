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

    public void actionPerformed(IGuiButton iGuiButton) throws IOException {
    }

    public void mouseClicked(int n, int n2, int n3) throws IOException {
        this.representedScreen.superMouseClicked(n, n2, n3);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final IGuiScreen getRepresentedScreen() {
        return this.representedScreen;
    }

    public void initGui() {
    }

    public void drawScreen(int n, int n2, float f) {
        this.representedScreen.superDrawScreen(n, n2, f);
    }

    public void mouseReleased(int n, int n2, int n3) {
        this.representedScreen.superMouseReleased(n, n2, n3);
    }

    public void keyTyped(char c, int n) throws IOException {
        this.representedScreen.superKeyTyped(c, n);
    }

    public final void setRepresentedScreen(IGuiScreen iGuiScreen) {
        this.representedScreen = iGuiScreen;
    }

    public void handleMouseInput() throws IOException {
        this.representedScreen.superHandleMouseInput();
    }

    public void updateScreen() {
    }

    public void onGuiClosed() {
    }
}

