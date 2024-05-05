package net.optifine.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.optifine.gui.GuiOptionButtonOF;
import net.optifine.gui.GuiOptionSliderOF;
import net.optifine.gui.TooltipManager;
import net.optifine.gui.TooltipProvider;
import net.optifine.gui.TooltipProviderOptions;

public class GuiOtherSettingsOF
extends GuiScreen
implements GuiYesNoCallback {
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions = new GameSettings.Options[]{GameSettings.Options.LAGOMETER, GameSettings.Options.PROFILER, GameSettings.Options.SHOW_FPS, GameSettings.Options.ADVANCED_TOOLTIPS, GameSettings.Options.WEATHER, GameSettings.Options.TIME, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.FULLSCREEN_MODE, GameSettings.Options.ANAGLYPH, GameSettings.Options.AUTOSAVE_TICKS, GameSettings.Options.SCREENSHOT_SIZE, GameSettings.Options.SHOW_GL_ERRORS};
    private TooltipManager tooltipManager = new TooltipManager((GuiScreen)this, (TooltipProvider)new TooltipProviderOptions());

    public GuiOtherSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public void initGui() {
        this.title = I18n.format((String)"of.options.otherTitle", (Object[])new Object[0]);
        this.buttonList.clear();
        for (int i = 0; i < enumOptions.length; ++i) {
            GameSettings.Options gamesettings$options = enumOptions[i];
            int j = width / 2 - 155 + i % 2 * 160;
            int k = height / 6 + 21 * (i / 2) - 12;
            if (!gamesettings$options.getEnumFloat()) {
                this.buttonList.add((Object)new GuiOptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
                continue;
            }
            this.buttonList.add((Object)new GuiOptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
        }
        this.buttonList.add((Object)new GuiButton(210, width / 2 - 100, height / 6 + 168 + 11 - 44, I18n.format((String)"of.options.other.reset", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
                this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
                guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions((int)guibutton.id));
            }
            if (guibutton.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.prevScreen);
            }
            if (guibutton.id == 210) {
                this.mc.gameSettings.saveOptions();
                GuiYesNo guiyesno = new GuiYesNo((GuiYesNoCallback)this, I18n.format((String)"of.message.other.reset", (Object[])new Object[0]), "", 9999);
                this.mc.displayGuiScreen((GuiScreen)guiyesno);
            }
        }
    }

    public void confirmClicked(boolean flag, int i) {
        if (flag) {
            this.mc.gameSettings.resetSettings();
        }
        this.mc.displayGuiScreen((GuiScreen)this);
    }

    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, width / 2, 15, 0xFFFFFF);
        super.drawScreen(x, y, f);
        this.tooltipManager.drawTooltips(x, y, this.buttonList);
    }
}
