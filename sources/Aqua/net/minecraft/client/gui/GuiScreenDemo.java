package net.minecraft.client.gui;

import java.io.IOException;
import java.net.URI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiScreenDemo
extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_146348_f = new ResourceLocation("textures/gui/demo_background.png");

    public void initGui() {
        this.buttonList.clear();
        int i = -16;
        this.buttonList.add((Object)new GuiButton(1, width / 2 - 116, height / 2 + 62 + i, 114, 20, I18n.format((String)"demo.help.buy", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(2, width / 2 + 2, height / 2 + 62 + i, 114, 20, I18n.format((String)"demo.help.later", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                button.enabled = false;
                try {
                    Class oclass = Class.forName((String)"java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI("http://www.minecraft.net/store?source=demo")});
                }
                catch (Throwable throwable) {
                    logger.error("Couldn't open link", throwable);
                }
                break;
            }
            case 2: {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
            }
        }
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public void drawDefaultBackground() {
        super.drawDefaultBackground();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(field_146348_f);
        int i = (width - 248) / 2;
        int j = (height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int i = (width - 248) / 2 + 10;
        int j = (height - 166) / 2 + 8;
        this.fontRendererObj.drawString(I18n.format((String)"demo.help.title", (Object[])new Object[0]), i, j, 0x1F1F1F);
        GameSettings gamesettings = this.mc.gameSettings;
        this.fontRendererObj.drawString(I18n.format((String)"demo.help.movementShort", (Object[])new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString((int)gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString((int)gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString((int)gamesettings.keyBindRight.getKeyCode())}), i, j += 12, 0x4F4F4F);
        this.fontRendererObj.drawString(I18n.format((String)"demo.help.movementMouse", (Object[])new Object[0]), i, j + 12, 0x4F4F4F);
        this.fontRendererObj.drawString(I18n.format((String)"demo.help.jump", (Object[])new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindJump.getKeyCode())}), i, j + 24, 0x4F4F4F);
        this.fontRendererObj.drawString(I18n.format((String)"demo.help.inventory", (Object[])new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindInventory.getKeyCode())}), i, j + 36, 0x4F4F4F);
        this.fontRendererObj.drawSplitString(I18n.format((String)"demo.help.fullWrapped", (Object[])new Object[0]), i, j + 68, 218, 0x1F1F1F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
