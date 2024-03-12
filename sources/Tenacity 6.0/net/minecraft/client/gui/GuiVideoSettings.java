// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.List;
import net.optifine.shaders.gui.GuiShaders;
import net.minecraft.src.Config;
import net.optifine.gui.GuiOtherSettingsOF;
import net.optifine.gui.GuiPerformanceSettingsOF;
import net.optifine.gui.GuiAnimationSettingsOF;
import net.optifine.gui.GuiQualitySettingsOF;
import net.optifine.gui.GuiDetailSettingsOF;
import java.io.IOException;
import net.optifine.Lang;
import net.optifine.gui.GuiOptionButtonOF;
import net.optifine.gui.GuiOptionSliderOF;
import net.minecraft.client.resources.I18n;
import net.optifine.gui.TooltipProvider;
import net.optifine.gui.TooltipProviderOptions;
import net.optifine.gui.TooltipManager;
import net.minecraft.client.settings.GameSettings;
import net.optifine.gui.GuiScreenOF;

public class GuiVideoSettings extends GuiScreenOF
{
    private GuiScreen parentGuiScreen;
    protected String screenTitle;
    private GameSettings guiGameSettings;
    private static GameSettings.Options[] videoOptions;
    private TooltipManager tooltipManager;
    
    public GuiVideoSettings(final GuiScreen parentScreenIn, final GameSettings gameSettingsIn) {
        this.screenTitle = "Video Settings";
        this.tooltipManager = new TooltipManager(this, new TooltipProviderOptions());
        this.parentGuiScreen = parentScreenIn;
        this.guiGameSettings = gameSettingsIn;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        for (int i = 0; i < GuiVideoSettings.videoOptions.length; ++i) {
            final GameSettings.Options gamesettings$options = GuiVideoSettings.videoOptions[i];
            if (gamesettings$options != null) {
                final int j = this.width / 2 - 155 + i % 2 * 160;
                final int k = this.height / 6 + 21 * (i / 2) - 12;
                if (gamesettings$options.getEnumFloat()) {
                    this.buttonList.add(new GuiOptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
                }
                else {
                    this.buttonList.add(new GuiOptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.guiGameSettings.getKeyBinding(gamesettings$options)));
                }
            }
        }
        int l = this.height / 6 + 21 * (GuiVideoSettings.videoOptions.length / 2) - 12;
        int i2 = 0;
        i2 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(231, i2, l, Lang.get("of.options.shaders")));
        i2 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(202, i2, l, Lang.get("of.options.quality")));
        l += 21;
        i2 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(201, i2, l, Lang.get("of.options.details")));
        i2 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(212, i2, l, Lang.get("of.options.performance")));
        l += 21;
        i2 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(211, i2, l, Lang.get("of.options.animations")));
        i2 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(222, i2, l, Lang.get("of.options.other")));
        l += 21;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        this.actionPerformed(button, 1);
    }
    
    @Override
    protected void actionPerformedRightClick(final GuiButton p_actionPerformedRightClick_1_) {
        if (p_actionPerformedRightClick_1_.id == GameSettings.Options.GUI_SCALE.ordinal()) {
            this.actionPerformed(p_actionPerformedRightClick_1_, -1);
        }
    }
    
    private void actionPerformed(final GuiButton p_actionPerformed_1_, final int p_actionPerformed_2_) {
        if (p_actionPerformed_1_.enabled) {
            final int i = this.guiGameSettings.guiScale;
            if (p_actionPerformed_1_.id < 200 && p_actionPerformed_1_ instanceof GuiOptionButton) {
                this.guiGameSettings.setOptionValue(((GuiOptionButton)p_actionPerformed_1_).returnEnumOptions(), p_actionPerformed_2_);
                p_actionPerformed_1_.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(p_actionPerformed_1_.id));
            }
            if (p_actionPerformed_1_.id == 200) {
                this.mc2.gameSettings.saveOptions();
                this.mc2.displayGuiScreen(this.parentGuiScreen);
            }
            if (this.guiGameSettings.guiScale != i) {
                final ScaledResolution scaledresolution = new ScaledResolution(this.mc2);
                final int j = scaledresolution.getScaledWidth();
                final int k = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc2, j, k);
            }
            if (p_actionPerformed_1_.id == 201) {
                this.mc2.gameSettings.saveOptions();
                final GuiDetailSettingsOF guidetailsettingsof = new GuiDetailSettingsOF(this, this.guiGameSettings);
                this.mc2.displayGuiScreen(guidetailsettingsof);
            }
            if (p_actionPerformed_1_.id == 202) {
                this.mc2.gameSettings.saveOptions();
                final GuiQualitySettingsOF guiqualitysettingsof = new GuiQualitySettingsOF(this, this.guiGameSettings);
                this.mc2.displayGuiScreen(guiqualitysettingsof);
            }
            if (p_actionPerformed_1_.id == 211) {
                this.mc2.gameSettings.saveOptions();
                final GuiAnimationSettingsOF guianimationsettingsof = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                this.mc2.displayGuiScreen(guianimationsettingsof);
            }
            if (p_actionPerformed_1_.id == 212) {
                this.mc2.gameSettings.saveOptions();
                final GuiPerformanceSettingsOF guiperformancesettingsof = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                this.mc2.displayGuiScreen(guiperformancesettingsof);
            }
            if (p_actionPerformed_1_.id == 222) {
                this.mc2.gameSettings.saveOptions();
                final GuiOtherSettingsOF guiothersettingsof = new GuiOtherSettingsOF(this, this.guiGameSettings);
                this.mc2.displayGuiScreen(guiothersettingsof);
            }
            if (p_actionPerformed_1_.id == 231) {
                if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
                    return;
                }
                if (Config.isAnisotropicFiltering()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
                    return;
                }
                if (Config.getGameSettings().anaglyph) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.an1"), Lang.get("of.message.shaders.an2"));
                    return;
                }
                this.mc2.gameSettings.saveOptions();
                final GuiShaders guishaders = new GuiShaders(this, this.guiGameSettings);
                this.mc2.displayGuiScreen(guishaders);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 15, 16777215);
        String s = Config.getVersion();
        final String s2 = "HD_U";
        if (s2.equals("HD")) {
            s = "OptiFine HD M5";
        }
        if (s2.equals("HD_U")) {
            s = "OptiFine HD M5 Ultra";
        }
        if (s2.equals("L")) {
            s = "OptiFine M5 Light";
        }
        this.drawString(this.fontRendererObj, s, 2, this.height - 10, 8421504);
        final String s3 = "Minecraft 1.8.9";
        final int i = (int)this.fontRendererObj.getStringWidth(s3);
        this.drawString(this.fontRendererObj, s3, this.width - i - 2, this.height - 10, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
    }
    
    public static int getButtonWidth(final GuiButton p_getButtonWidth_0_) {
        return p_getButtonWidth_0_.width;
    }
    
    public static int getButtonHeight(final GuiButton p_getButtonHeight_0_) {
        return p_getButtonHeight_0_.height;
    }
    
    public static void drawGradientRect(final GuiScreen p_drawGradientRect_0_, final int p_drawGradientRect_1_, final int p_drawGradientRect_2_, final int p_drawGradientRect_3_, final int p_drawGradientRect_4_, final int p_drawGradientRect_5_, final int p_drawGradientRect_6_) {
        Gui.drawGradientRect(p_drawGradientRect_1_, p_drawGradientRect_2_, p_drawGradientRect_3_, p_drawGradientRect_4_, p_drawGradientRect_5_, p_drawGradientRect_6_);
    }
    
    public static String getGuiChatText(final GuiChat p_getGuiChatText_0_) {
        return p_getGuiChatText_0_.inputField.getText();
    }
    
    static {
        GuiVideoSettings.videoOptions = new GameSettings.Options[] { GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.DYNAMIC_LIGHTS, GameSettings.Options.DYNAMIC_FOV };
    }
}
