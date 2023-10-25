/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.input.MouseHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.config.Config;
import org.celestial.client.settings.config.ConfigManager;
import org.celestial.client.ui.button.ConfigGuiButton;
import org.celestial.client.ui.button.ImageButton;
import org.celestial.client.ui.font.MCFontRenderer;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiConfig
extends GuiScreen {
    public static GuiTextField search;
    public static Config selectedConfig;
    public static Config lastConfig;
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private int width;
    private int height;
    private float scrollOffset;

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            if (!search.getText().isEmpty()) {
                Celestial.instance.configManager.saveConfig(search.getText());
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "saved config: " + (Object)((Object)ChatFormatting.RED) + "\"" + search.getText() + "\"");
                NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "created config: " + (Object)((Object)ChatFormatting.RED) + "\"" + search.getText() + "\"", 4, NotificationType.SUCCESS);
                ConfigManager.getLoadedConfigs().clear();
                Celestial.instance.configManager.load();
                search.setFocused(false);
                search.setText("");
            } else {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to create config! Please write your config name!");
                NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to create config! Please write your config name!", 4, NotificationType.ERROR);
            }
        }
        if (selectedConfig != null) {
            if (button.id == 2) {
                if (Celestial.instance.configManager.loadConfig(selectedConfig.getName())) {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "loaded config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "loaded config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                    lastConfig = selectedConfig;
                } else {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "load config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "load config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
                }
            } else if (button.id == 3) {
                if (Celestial.instance.configManager.saveConfig(selectedConfig.getName())) {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "saved config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "saved config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                    lastConfig = selectedConfig;
                    ConfigManager.getLoadedConfigs().clear();
                    Celestial.instance.configManager.load();
                } else {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to save config: " + (Object)((Object)ChatFormatting.RED) + "\"" + search.getText() + "\"");
                    NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to save config: " + (Object)((Object)ChatFormatting.RED) + "\"" + search.getText() + "\"", 4, NotificationType.ERROR);
                }
            } else if (button.id == 4) {
                if (Celestial.instance.configManager.deleteConfig(selectedConfig.getName())) {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "deleted config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "deleted config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                } else {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to delete config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config Manager", (Object)((Object)ChatFormatting.RED) + "Failed " + (Object)((Object)ChatFormatting.WHITE) + "to delete config: " + (Object)((Object)ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
                }
            }
        }
        if (button.id == 5) {
            File file = new File(this.mc.gameDir + "\\celestial", "configs");
            Sys.openURL(file.getAbsolutePath());
        }
        super.actionPerformed(button);
    }

    private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
        return MouseHelper.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth() / 2;
        this.height = sr.getScaledHeight() / 2;
        search = new GuiTextField(228, this.mc.fontRendererObj, this.width - 100, this.height + 62, 85, 13);
        this.buttonList.add(new ConfigGuiButton(1, this.width - 105, this.height + 102, "Create"));
        this.buttonList.add(new ConfigGuiButton(2, this.width - 40, this.height - 48, "Load"));
        this.buttonList.add(new ConfigGuiButton(3, this.width - 40, this.height - 65, "Save"));
        this.buttonList.add(new ConfigGuiButton(4, this.width - 40, this.height - 31, "Delete"));
        this.buttonList.add(new ConfigGuiButton(5, this.width - 40, this.height - 82, "Folder"));
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/close-button.png"), this.width + 106, this.height - 136, 8, 8, "", 19));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.drawWorldBackground(0);
        if (ClickGui.backGroundBlur.getCurrentValue()) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (int)ClickGui.backGroundBlurStrength.getCurrentValue());
        }
        for (Config config : Celestial.instance.configManager.getContents()) {
            if (config == null || !Mouse.hasWheel() || !this.isHoveredConfig(this.width - 100, this.height - 122, 151, this.height + 59, mouseX, mouseY)) continue;
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.scrollOffset += 13.0f;
                if (!(this.scrollOffset < 0.0f)) continue;
                this.scrollOffset = 0.0f;
                continue;
            }
            if (wheel <= 0) continue;
            this.scrollOffset -= 13.0f;
            if (!(this.scrollOffset < 0.0f)) continue;
            this.scrollOffset = 0.0f;
        }
        GlStateManager.pushMatrix();
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(this.width - 125), (double)(this.height - 150), 261.0, 245.0, 45);
        }
        RectHelper.drawSkeetRect(this.width - 70, this.height - 80, this.width + 80, this.height + 20);
        RectHelper.drawSkeetButton(this.width - 70, this.height - 80, this.width + 20, this.height + 90);
        RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), this.width - 110, this.height - 140, 230.0f, 2.0f, ClientHelper.getClientColor());
        MCFontRenderer.drawStringWithOutline(this.mc.robotoRegularFontRender, "Config-Manager", (float)(this.width - 100), (float)(this.height - 134), -1);
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter(), (double)(this.width - 110), (double)(this.height - 140), 235.0, 5.0, 35);
        }
        search.drawTextBox();
        if (search.getText().isEmpty() && !search.isFocused()) {
            MCFontRenderer.drawStringWithOutline(this.mc.robotoRegularFontRender, "Create config...", (float)(this.width - 97), (float)(this.height + 65), PaletteHelper.getColor(200));
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        int yDist = 0;
        GL11.glEnable(3089);
        RenderHelper.scissorRect(0.0f, this.height - 119, this.width + 45, this.height + 60);
        for (Config config : Celestial.instance.configManager.getContents()) {
            int color;
            if (config == null || config.getName().equals("default") || config.getFile().length() <= 0L) continue;
            if (this.isHoveredConfig(this.width - 96, (int)((float)(this.height - 117 + yDist) - this.scrollOffset), this.mc.robotoRegularFontRender.getStringWidth(config.getName()) + 5, 14, mouseX, mouseY)) {
                color = -1;
                if (Mouse.isButtonDown(0)) {
                    selectedConfig = new Config(config.getName());
                }
            } else {
                color = PaletteHelper.getColor(200);
            }
            if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
                RectHelper.drawBorder(this.width - 98, (float)(this.height - 119 + yDist) - this.scrollOffset, this.width + this.mc.robotoRegularFontRender.getStringWidth(config.getName()) - 90, (float)(this.height - 106 + yDist) - this.scrollOffset, 0.65f, new Color(195, 195, 195, 75).getRGB(), new Color(0, 0, 0, 255).getRGB(), true);
            }
            MCFontRenderer.drawStringWithOutline(this.mc.robotoRegularFontRender, config.getName(), (float)(this.width - 95), (float)(this.height - 116 + yDist) - this.scrollOffset, color);
            yDist += 15;
        }
        GL11.glDisable(3089);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        search.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.scrollOffset < 0.0f) {
            this.scrollOffset = 0.0f;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Config config : Celestial.instance.configManager.getContents()) {
            if (config == null) continue;
            if (keyCode == 200) {
                this.scrollOffset += 13.0f;
            } else if (keyCode == 208) {
                this.scrollOffset -= 13.0f;
            }
            if (!(this.scrollOffset < 0.0f)) continue;
            this.scrollOffset = 0.0f;
        }
        search.textboxKeyTyped(typedChar, keyCode);
        search.setText(search.getText().replace(" ", ""));
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        selectedConfig = null;
        super.onGuiClosed();
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

