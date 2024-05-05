/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
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
    private GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
    private GameSettings guiGameSettings;
    private GuiListExtended optionsRowList;
    private static final GameSettings.Options[] videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.ANAGLYPH, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.GAMMA, GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.ENABLE_VSYNC, GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.USE_VBO, GameSettings.Options.ENTITY_SHADOWS};

    public GuiVideoSettings(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
        this.parentGuiScreen = parentScreenIn;
        this.guiGameSettings = gameSettingsIn;
    }

    @Override
    public void initGui() {
        GameSettings.Options gamesettings$options;
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.format("gui.done", new Object[0])));
        if (OpenGlHelper.vboSupported) {
            this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, videoOptions);
            return;
        }
        GameSettings.Options[] agamesettings$options = new GameSettings.Options[videoOptions.length - 1];
        int i = 0;
        GameSettings.Options[] optionsArray = videoOptions;
        int n = optionsArray.length;
        for (int j = 0; j < n && (gamesettings$options = optionsArray[j]) != GameSettings.Options.USE_VBO; ++i, ++j) {
            agamesettings$options[i] = gamesettings$options;
        }
        this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, agamesettings$options);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.optionsRowList.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) return;
        if (button.id != 200) return;
        this.mc.gameSettings.saveOptions();
        this.mc.displayGuiScreen(this.parentGuiScreen);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int i = this.guiGameSettings.guiScale;
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.guiGameSettings.guiScale == i) return;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int j = scaledresolution.getScaledWidth();
        int k = scaledresolution.getScaledHeight();
        this.setWorldAndResolution(this.mc, j, k);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        int i = this.guiGameSettings.guiScale;
        super.mouseReleased(mouseX, mouseY, state);
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);
        if (this.guiGameSettings.guiScale == i) return;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int j = scaledresolution.getScaledWidth();
        int k = scaledresolution.getScaledHeight();
        this.setWorldAndResolution(this.mc, j, k);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 5, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

