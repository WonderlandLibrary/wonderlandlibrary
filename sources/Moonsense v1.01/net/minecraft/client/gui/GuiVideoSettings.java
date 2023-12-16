// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import shadersmod.client.GuiShaders;
import optifine.Config;
import optifine.GuiOtherSettingsOF;
import optifine.GuiPerformanceSettingsOF;
import optifine.GuiAnimationSettingsOF;
import optifine.GuiQualitySettingsOF;
import optifine.GuiDetailSettingsOF;
import optifine.Lang;
import optifine.GuiOptionButtonOF;
import optifine.GuiOptionSliderOF;
import net.minecraft.client.resources.I18n;
import optifine.TooltipManager;
import net.minecraft.client.settings.GameSettings;

public class GuiVideoSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    protected String screenTitle;
    private GameSettings guiGameSettings;
    private static GameSettings.Options[] videoOptions;
    private static final String __OBFID = "CL_00000718";
    private TooltipManager tooltipManager;
    
    static {
        GuiVideoSettings.videoOptions = new GameSettings.Options[] { GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START };
    }
    
    public GuiVideoSettings(final GuiScreen par1GuiScreen, final GameSettings par2GameSettings) {
        this.screenTitle = "Video Settings";
        this.tooltipManager = new TooltipManager(this);
        this.parentGuiScreen = par1GuiScreen;
        this.guiGameSettings = par2GameSettings;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        for (int y = 0; y < GuiVideoSettings.videoOptions.length; ++y) {
            final GameSettings.Options x = GuiVideoSettings.videoOptions[y];
            if (x != null) {
                final int x2 = this.width / 2 - 155 + y % 2 * 160;
                final int y2 = this.height / 6 + 21 * (y / 2) - 12;
                if (x.getEnumFloat()) {
                    this.buttonList.add(new GuiOptionSliderOF(x.returnEnumOrdinal(), x2, y2, x));
                }
                else {
                    this.buttonList.add(new GuiOptionButtonOF(x.returnEnumOrdinal(), x2, y2, x, this.guiGameSettings.getKeyBinding(x)));
                }
            }
        }
        int y = this.height / 6 + 21 * (GuiVideoSettings.videoOptions.length / 2) - 12;
        final boolean var5 = false;
        int var6 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(231, var6, y, Lang.get("of.options.shaders")));
        var6 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(202, var6, y, Lang.get("of.options.quality")));
        y += 21;
        var6 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(201, var6, y, Lang.get("of.options.details")));
        var6 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(212, var6, y, Lang.get("of.options.performance")));
        y += 21;
        var6 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(211, var6, y, Lang.get("of.options.animations")));
        var6 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(222, var6, y, Lang.get("of.options.other")));
        y += 21;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            final int guiScale = this.guiGameSettings.guiScale;
            if (button.id < 200 && button instanceof GuiOptionButton) {
                this.guiGameSettings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            if (this.guiGameSettings.guiScale != guiScale) {
                final ScaledResolution scr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                final int var4 = scr.getScaledWidth();
                final int var5 = scr.getScaledHeight();
                this.setWorldAndResolution(this.mc, var4, var5);
            }
            if (button.id == 201) {
                this.mc.gameSettings.saveOptions();
                final GuiDetailSettingsOF scr2 = new GuiDetailSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr2);
            }
            if (button.id == 202) {
                this.mc.gameSettings.saveOptions();
                final GuiQualitySettingsOF scr3 = new GuiQualitySettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr3);
            }
            if (button.id == 211) {
                this.mc.gameSettings.saveOptions();
                final GuiAnimationSettingsOF scr4 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr4);
            }
            if (button.id == 212) {
                this.mc.gameSettings.saveOptions();
                final GuiPerformanceSettingsOF scr5 = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr5);
            }
            if (button.id == 222) {
                this.mc.gameSettings.saveOptions();
                final GuiOtherSettingsOF scr6 = new GuiOtherSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr6);
            }
            if (button.id == 231) {
                if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
                    return;
                }
                if (Config.isAnisotropicFiltering()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
                    return;
                }
                if (Config.isFastRender()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
                    return;
                }
                this.mc.gameSettings.saveOptions();
                final GuiShaders scr7 = new GuiShaders(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr7);
            }
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 15, 16777215);
        String ver = Config.getVersion();
        final String ed = "HD_U";
        if (ed.equals("HD")) {
            ver = "OptiFine HD H6";
        }
        if (ed.equals("HD_U")) {
            ver = "OptiFine HD H6 Ultra";
        }
        if (ed.equals("L")) {
            ver = "OptiFine H6 Light";
        }
        this.drawString(this.fontRendererObj, ver, 2, this.height - 10, 8421504);
        final String verMc = "Minecraft 1.8";
        final int lenMc = this.fontRendererObj.getStringWidth(verMc);
        this.drawString(this.fontRendererObj, verMc, this.width - lenMc - 2, this.height - 10, 8421504);
        super.drawScreen(x, y, z);
        this.tooltipManager.drawTooltips(x, y, this.buttonList);
    }
    
    public static int getButtonWidth(final GuiButton btn) {
        return btn.width;
    }
    
    public static int getButtonHeight(final GuiButton btn) {
        return btn.height;
    }
    
    public static void drawGradientRect(final GuiScreen guiScreen, final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        guiScreen.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }
}
