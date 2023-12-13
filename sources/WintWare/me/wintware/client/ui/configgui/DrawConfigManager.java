/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.configgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.ui.configgui.Config;
import me.wintware.client.ui.configgui.DrawConfigs;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class DrawConfigManager
extends GuiScreen {
    public Color guiColor4;
    String currentScreen = "";
    ScaledResolution sr;
    ArrayList<Config> configs = new ArrayList();
    int x = 250;
    int y;
    int width;
    int center;

    public DrawConfigManager() {
        this.configs.addAll(Main.instance.configManager.getConfigs());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        this.y = 60;
        this.width = this.sr.getScaledWidth() - this.x;
        this.center = this.sr.getScaledWidth() / 2;
        int difference = 16;
        Color guiColor3 = new Color(255 - difference * 2, 255 - difference * 2, 255 - difference * 2, 255);
        this.guiColor4 = new Color(26, 26, 26, 255);
        RenderUtil.relativeRect(0.0f, 0.0f, this.sr.getScaledWidth(), this.sr.getScaledHeight(), new Color(0, 0, 0, 125).getRGB());
        RenderUtil.drawSmoothRect(this.x, 50.0f, this.width, this.sr.getScaledHeight() - 60, this.guiColor4.getRGB());
        for (Config config : this.configs) {
            DrawConfigs drawConfigs = new DrawConfigs(config, this.x, this.y, this.width);
            drawConfigs.drawScreen(mouseX, mouseY, partialTicks, this.guiColor4, new Color(172, 161, 161));
            this.y += 20;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean isHoveredClient(float mouseX, float mouseY) {
        return mouseX > (float)this.x && mouseX < (float)(this.x / 2 + this.width / 2) && mouseY > 30.0f && mouseY < 50.0f;
    }

    @Override
    public void initGui() {
        for (Config config : this.configs) {
            DrawConfigs drawConfigs = new DrawConfigs(config, this.x, this.y, this.width);
            drawConfigs.initGui();
            this.y += 20;
        }
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int y = 60;
        if (mouseButton == 0 && this.isHoveredClient(mouseX, mouseY)) {
            this.currentScreen = "Client Configs";
        }
        for (Config config : this.configs) {
            DrawConfigs drawConfigs = new DrawConfigs(config, this.x, y, this.width);
            drawConfigs.mouseClicked(mouseX, mouseY, mouseButton);
            y += 20;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}

