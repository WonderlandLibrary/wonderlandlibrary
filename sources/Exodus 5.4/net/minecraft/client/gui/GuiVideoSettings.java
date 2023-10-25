/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiVideoSettings
extends GuiScreen {
    protected String screenTitle = "Video Settings";
    private GuiListExtended optionsRowList;
    private GameSettings guiGameSettings;
    private static final GameSettings.Options[] videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.ANAGLYPH, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.GAMMA, GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.ENABLE_VSYNC, GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.USE_VBO, GameSettings.Options.ENTITY_SHADOWS};
    private GuiScreen parentGuiScreen;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.optionsRowList.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 5, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled && guiButton.id == 200) {
            Minecraft.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentGuiScreen);
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        int n4 = this.guiGameSettings.guiScale;
        super.mouseClicked(n, n2, n3);
        this.optionsRowList.mouseClicked(n, n2, n3);
        if (this.guiGameSettings.guiScale != n4) {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int n5 = scaledResolution.getScaledWidth();
            int n6 = scaledResolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, n5, n6);
        }
    }

    public GuiVideoSettings(GuiScreen guiScreen, GameSettings gameSettings) {
        this.parentGuiScreen = guiScreen;
        this.guiGameSettings = gameSettings;
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        int n4 = this.guiGameSettings.guiScale;
        super.mouseReleased(n, n2, n3);
        this.optionsRowList.mouseReleased(n, n2, n3);
        if (this.guiGameSettings.guiScale != n4) {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int n5 = scaledResolution.getScaledWidth();
            int n6 = scaledResolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, n5, n6);
        }
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(200, width / 2 - 100, height - 27, I18n.format("gui.done", new Object[0])));
        if (!OpenGlHelper.vboSupported) {
            GameSettings.Options[] optionsArray = new GameSettings.Options[videoOptions.length - 1];
            int n = 0;
            GameSettings.Options[] optionsArray2 = videoOptions;
            int n2 = videoOptions.length;
            int n3 = 0;
            while (n3 < n2) {
                GameSettings.Options options = optionsArray2[n3];
                if (options == GameSettings.Options.USE_VBO) break;
                optionsArray[n] = options;
                ++n;
                ++n3;
            }
            this.optionsRowList = new GuiOptionsRowList(this.mc, width, height, 32, height - 32, 25, optionsArray);
        } else {
            this.optionsRowList = new GuiOptionsRowList(this.mc, width, height, 32, height - 32, 25, videoOptions);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.optionsRowList.handleMouseInput();
    }
}

