/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.ui.client.GuiContributors;
import net.ccbluex.liquidbounce.ui.client.GuiModsMenu;
import net.ccbluex.liquidbounce.ui.client.GuiServerStatus;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class GuiMainMenu
extends WrappedGuiScreen {
    @Override
    public void initGui() {
        int defaultHeight = this.getRepresentedScreen().getHeight() / 4 + 48;
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(100, this.getRepresentedScreen().getWidth() / 2 - 100, defaultHeight + 24, 98, 20, "AltManager"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(103, this.getRepresentedScreen().getWidth() / 2 + 2, defaultHeight + 24, 98, 20, "Mods"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(101, this.getRepresentedScreen().getWidth() / 2 - 100, defaultHeight + 48, 98, 20, "Server Status"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(102, this.getRepresentedScreen().getWidth() / 2 + 2, defaultHeight + 48, 98, 20, "Background"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, defaultHeight, 98, 20, MinecraftInstance.functions.formatI18n("menu.singleplayer", new String[0])));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 + 2, defaultHeight, 98, 20, MinecraftInstance.functions.formatI18n("menu.multiplayer", new String[0])));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(108, this.getRepresentedScreen().getWidth() / 2 - 100, defaultHeight + 72, "Contributors"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 - 100, defaultHeight + 96, 98, 20, MinecraftInstance.functions.formatI18n("menu.options", new String[0])));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(4, this.getRepresentedScreen().getWidth() / 2 + 2, defaultHeight + 96, 98, 20, MinecraftInstance.functions.formatI18n("menu.quit", new String[0])));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        this.getRepresentedScreen().superDrawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        switch (button.getId()) {
            case 0: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.createGuiOptions(this.getRepresentedScreen(), MinecraftInstance.mc.getGameSettings()));
                break;
            }
            case 1: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.createGuiSelectWorld(this.getRepresentedScreen()));
                break;
            }
            case 2: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.createGuiMultiplayer(this.getRepresentedScreen()));
                break;
            }
            case 4: {
                MinecraftInstance.mc.shutdown();
                break;
            }
            case 100: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiAltManager(this.getRepresentedScreen())));
                break;
            }
            case 101: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiServerStatus(this.getRepresentedScreen())));
                break;
            }
            case 102: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiBackground(this.getRepresentedScreen())));
                break;
            }
            case 103: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiModsMenu(this.getRepresentedScreen())));
                break;
            }
            case 108: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiContributors(this.getRepresentedScreen())));
            }
        }
    }
}

