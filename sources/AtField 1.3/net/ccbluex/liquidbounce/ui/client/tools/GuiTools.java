/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.tools;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.tools.GuiPortScanner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class GuiTools
extends WrappedGuiScreen {
    private final IGuiScreen prevGui;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.getRepresentedScreen().drawBackground(0);
        Fonts.robotoBold180.drawCenteredString("Tools", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + 5.0f, 4673984, true);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void keyTyped(char c, int n) {
        if (1 == n) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(c, n);
    }

    public GuiTools(IGuiScreen iGuiScreen) {
        this.prevGui = iGuiScreen;
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) {
        switch (iGuiButton.getId()) {
            case 1: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiPortScanner(this.getRepresentedScreen())));
                break;
            }
            case 0: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui);
                break;
            }
        }
    }

    @Override
    public void initGui() {
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 48 + 25, "Port Scanner"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 48 + 50 + 5, "Back"));
    }
}

