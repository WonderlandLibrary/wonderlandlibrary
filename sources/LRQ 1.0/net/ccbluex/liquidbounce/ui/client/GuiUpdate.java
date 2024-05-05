/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.GuiMainMenu;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import org.lwjgl.opengl.GL11;

public final class GuiUpdate
extends WrappedGuiScreen {
    @Override
    public void initGui() {
        int j = this.getRepresentedScreen().getHeight() / 4 + 48;
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 + 2, j + 48, 98, 20, "OK"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, j + 48, 98, 20, "Download"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        Fonts.font35.drawCenteredString("" + 'b' + LiquidBounce.INSTANCE.getLatestVersion() + " got released!", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80, 0xFFFFFF);
        Fonts.font35.drawCenteredString("Press \"Download\" to visit our website or dismiss this message by pressing \"OK\".", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)Fonts.font35.getFontHeight(), 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        Fonts.font35.drawCenteredString("New update available!", (float)this.getRepresentedScreen().getWidth() / 4.0f, (float)this.getRepresentedScreen().getHeight() / 16.0f + (float)20, new Color(255, 0, 0).getRGB());
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        switch (button.getId()) {
            case 1: {
                MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiMainMenu()));
                break;
            }
            case 2: {
                MiscUtils.showURL("https://liquidbounce.net/download");
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}

