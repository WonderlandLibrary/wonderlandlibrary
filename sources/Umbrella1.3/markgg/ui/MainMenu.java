/*
 * Decompiled with CFR 0.150.
 */
package markgg.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import markgg.Client;
import markgg.alts.GuiAltManager;
import markgg.utilities.ColorUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainMenu
extends GuiScreen {
    @Override
    public void initGui() {
        Client.getDiscordRP().update("Main Menu", "");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String[] buttons;
        this.mc.getTextureManager().bindTexture(new ResourceLocation("Umbrella/background.jpg"));
        MainMenu.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        this.drawGradientRect(0, this.height - 100, this.width, this.height, 0, -16777216);
        int count = 0;
        String[] arrstring = buttons = new String[]{"Singleplayer", "Multiplayer", "Options", "Alts", "Discord", "Quit"};
        int n = buttons.length;
        for (int i = 0; i < n; ++i) {
            String name = arrstring[i];
            float x = (float)(this.width / buttons.length * count) + (float)(this.width / buttons.length) / 2.0f + 8.0f - (float)this.mc.fontRendererObj.getStringWidth(name) / 2.0f;
            float y = this.height - 20;
            boolean hovered = (float)mouseX >= x && (float)mouseY >= y && (float)mouseX < x + (float)this.mc.fontRendererObj.getStringWidth(name) && (float)mouseY < y + (float)this.mc.fontRendererObj.FONT_HEIGHT;
            this.drawCenteredString(this.mc.fontRendererObj, name, (float)(this.width / buttons.length * count) + (float)(this.width / buttons.length) / 2.0f + 8.0f, this.height - 20, hovered ? ColorUtil.getRainbow(4.0f, 1.0f, 1.0f) : -1);
            ++count;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)this.width / 2.0f, (float)this.height / 2.0f, 0.0f);
        GlStateManager.scale(4.0f, 4.0f, 1.0f);
        GlStateManager.translate(-((float)this.width / 2.0f), -((float)this.height / 2.0f), 0.0f);
        this.drawCenteredString(this.mc.fontRendererObj, Client.name, (float)this.width / 2.0f, (float)this.height / 2.0f - (float)Client.customFontBig.getHeight() / 2.0f, ColorUtil.getRainbow(4.0f, 1.0f, 1.0f));
        GlStateManager.popMatrix();
    }

    public void playPressSound() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public void playDeepPressSound() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.7f));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        String[] buttons = new String[]{"Singleplayer", "Multiplayer", "Options", "Alts", "Discord", "Quit"};
        int count = 0;
        String[] arrstring = buttons;
        int n = buttons.length;
        for (int i = 0; i < n; ++i) {
            block27: {
                String name = arrstring[i];
                float x = (float)(this.width / buttons.length * count) + (float)(this.width / buttons.length) / 2.0f + 8.0f - (float)this.mc.fontRendererObj.getStringWidth(name) / 2.0f;
                float y = this.height - 20;
                if (!((float)mouseX >= x) || !((float)mouseY >= y) || !((float)mouseX < x + (float)this.mc.fontRendererObj.getStringWidth(name)) || !((float)mouseY < y + (float)this.mc.fontRendererObj.FONT_HEIGHT)) break block27;
                switch (name) {
                    case "Singleplayer": {
                        this.playPressSound();
                        this.mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    }
                    case "Multiplayer": {
                        this.playPressSound();
                        this.mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    }
                    case "Options": {
                        this.playPressSound();
                        this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                        break;
                    }
                    case "Alts": {
                        this.playPressSound();
                        this.mc.displayGuiScreen(new GuiAltManager());
                        break;
                    }
                    case "Discord": {
                        this.playPressSound();
                        URI uri = null;
                        try {
                            uri = new URI("https://discord.gg/VbJMMwkuMm");
                        }
                        catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            Desktop.getDesktop().browse(uri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "Quit": {
                        this.playDeepPressSound();
                        this.mc.shutdown();
                    }
                }
            }
            ++count;
        }
    }

    @Override
    public void onGuiClosed() {
    }
}

