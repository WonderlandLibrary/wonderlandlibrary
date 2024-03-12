// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.gui;

import java.util.List;
import java.util.Iterator;
import net.optifine.shaders.config.ShaderOptionProfile;
import net.minecraft.client.gui.IFontRenderer;
import net.optifine.Lang;
import net.minecraft.src.Config;
import net.optifine.shaders.config.ShaderOptionScreen;
import net.optifine.shaders.config.ShaderOption;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.client.resources.I18n;
import net.optifine.shaders.Shaders;
import net.optifine.gui.TooltipProvider;
import net.optifine.gui.TooltipProviderShaderOptions;
import net.optifine.gui.TooltipManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.gui.GuiScreenOF;

public class GuiShaderOptions extends GuiScreenOF
{
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private TooltipManager tooltipManager;
    private String screenName;
    private String screenText;
    private boolean changed;
    public static final String OPTION_PROFILE = "<profile>";
    public static final String OPTION_EMPTY = "<empty>";
    public static final String OPTION_REST = "*";
    
    public GuiShaderOptions(final GuiScreen guiscreen, final GameSettings gamesettings) {
        this.tooltipManager = new TooltipManager(this, new TooltipProviderShaderOptions());
        this.screenName = null;
        this.screenText = null;
        this.changed = false;
        this.title = "Shader Options";
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }
    
    public GuiShaderOptions(final GuiScreen guiscreen, final GameSettings gamesettings, final String screenName) {
        this(guiscreen, gamesettings);
        this.screenName = screenName;
        if (screenName != null) {
            this.screenText = Shaders.translate("screen." + screenName, screenName);
        }
    }
    
    @Override
    public void initGui() {
        this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
        final int i = 100;
        int j = 0;
        final int k = 30;
        final int l = 20;
        final int i2 = 120;
        final int j2 = 20;
        int k2 = Shaders.getShaderPackColumns(this.screenName, 2);
        final ShaderOption[] ashaderoption = Shaders.getShaderPackOptions(this.screenName);
        if (ashaderoption != null) {
            final int l2 = MathHelper.ceiling_double_int(ashaderoption.length / 9.0);
            if (k2 < l2) {
                k2 = l2;
            }
            for (int i3 = 0; i3 < ashaderoption.length; ++i3) {
                final ShaderOption shaderoption = ashaderoption[i3];
                if (shaderoption != null && shaderoption.isVisible()) {
                    final int j3 = i3 % k2;
                    final int k3 = i3 / k2;
                    final int l3 = Math.min(this.width / k2, 200);
                    j = (this.width - l3 * k2) / 2;
                    final int i4 = j3 * l3 + 5 + j;
                    final int j4 = k + k3 * l;
                    final int k4 = l3 - 10;
                    final String s = getButtonText(shaderoption, k4);
                    GuiButtonShaderOption guibuttonshaderoption;
                    if (Shaders.isShaderPackOptionSlider(shaderoption.getName())) {
                        guibuttonshaderoption = new GuiSliderShaderOption(i + i3, i4, j4, k4, j2, shaderoption, s);
                    }
                    else {
                        guibuttonshaderoption = new GuiButtonShaderOption(i + i3, i4, j4, k4, j2, shaderoption, s);
                    }
                    guibuttonshaderoption.enabled = shaderoption.isEnabled();
                    this.buttonList.add(guibuttonshaderoption);
                }
            }
        }
        this.buttonList.add(new GuiButton(201, this.width / 2 - i2 - 20, this.height / 6 + 168 + 11, i2, j2, I18n.format("controls.reset", new Object[0])));
        this.buttonList.add(new GuiButton(200, this.width / 2 + 20, this.height / 6 + 168 + 11, i2, j2, I18n.format("gui.done", new Object[0])));
    }
    
    public static String getButtonText(final ShaderOption so, final int btnWidth) {
        String s = so.getNameText();
        if (so instanceof ShaderOptionScreen) {
            final ShaderOptionScreen shaderoptionscreen = (ShaderOptionScreen)so;
            return s + "...";
        }
        final IFontRenderer fontrenderer = Config.getMinecraft().fontRendererObj;
        for (int i = (int)(fontrenderer.getStringWidth(": " + Lang.getOff()) + 5.0f); fontrenderer.getStringWidth(s) + i >= btnWidth && s.length() > 0; s = s.substring(0, s.length() - 1)) {}
        final String s2 = so.isChanged() ? so.getValueColor(so.getValue()) : "";
        final String s3 = so.getValueText(so.getValue());
        return s + ": " + s2 + s3;
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 200 && guibutton instanceof GuiButtonShaderOption) {
                final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
                final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
                if (shaderoption instanceof ShaderOptionScreen) {
                    final String s = shaderoption.getName();
                    final GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, this.settings, s);
                    this.mc2.displayGuiScreen(guishaderoptions);
                    return;
                }
                if (isShiftKeyDown()) {
                    shaderoption.resetValue();
                }
                else if (guibuttonshaderoption.isSwitchable()) {
                    shaderoption.nextValue();
                }
                this.updateAllButtons();
                this.changed = true;
            }
            if (guibutton.id == 201) {
                final ShaderOption[] ashaderoption = Shaders.getChangedOptions(Shaders.getShaderPackOptions());
                for (int i = 0; i < ashaderoption.length; ++i) {
                    final ShaderOption shaderoption2 = ashaderoption[i];
                    shaderoption2.resetValue();
                    this.changed = true;
                }
                this.updateAllButtons();
            }
            if (guibutton.id == 200) {
                if (this.changed) {
                    Shaders.saveShaderPackOptions();
                    this.changed = false;
                    Shaders.uninit();
                }
                this.mc2.displayGuiScreen(this.prevScreen);
            }
        }
    }
    
    @Override
    protected void actionPerformedRightClick(final GuiButton btn) {
        if (btn instanceof GuiButtonShaderOption) {
            final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
            final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
            if (isShiftKeyDown()) {
                shaderoption.resetValue();
            }
            else if (guibuttonshaderoption.isSwitchable()) {
                shaderoption.prevValue();
            }
            this.updateAllButtons();
            this.changed = true;
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.changed) {
            Shaders.saveShaderPackOptions();
            this.changed = false;
            Shaders.uninit();
        }
    }
    
    private void updateAllButtons() {
        for (final GuiButton guibutton : this.buttonList) {
            if (guibutton instanceof GuiButtonShaderOption) {
                final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
                final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
                if (shaderoption instanceof ShaderOptionProfile) {
                    final ShaderOptionProfile shaderoptionprofile = (ShaderOptionProfile)shaderoption;
                    shaderoptionprofile.updateProfile();
                }
                guibuttonshaderoption.displayString = getButtonText(shaderoption, guibuttonshaderoption.getButtonWidth());
                guibuttonshaderoption.valueChanged();
            }
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        this.drawDefaultBackground();
        if (this.screenText != null) {
            this.drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15, 16777215);
        }
        else {
            this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
        }
        super.drawScreen(x, y, f);
        this.tooltipManager.drawTooltips(x, y, this.buttonList);
    }
}
