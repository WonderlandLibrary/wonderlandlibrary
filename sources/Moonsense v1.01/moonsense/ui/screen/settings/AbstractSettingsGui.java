// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.settings;

import java.util.Iterator;
import java.util.ArrayList;
import moonsense.ui.elements.Element;
import moonsense.ui.elements.settings.compound.SettingElementCompound;
import moonsense.ui.elements.settings.SettingElementString;
import moonsense.ui.elements.settings.mode.SettingElementMode;
import moonsense.utils.KeyBinding;
import moonsense.ui.elements.settings.SettingElementKeybind;
import moonsense.utils.MathUtil;
import moonsense.ui.elements.settings.SettingElementSlider;
import moonsense.ui.elements.settings.SettingElementCheckbox;
import moonsense.utils.ColorObject;
import moonsense.ui.elements.settings.ElementCategory;
import moonsense.ui.elements.settings.SettingElement;
import java.util.concurrent.atomic.AtomicBoolean;
import moonsense.settings.Setting;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import moonsense.ui.elements.settings.color.SettingElementColor;
import net.minecraft.client.Minecraft;
import moonsense.MoonsenseClient;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.utils.WatermarkRenderer;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.ThemeSettings;
import net.minecraft.client.gui.Gui;
import moonsense.features.modules.type.server.hypixel.HypixelReplayLoggerModule;
import moonsense.ui.elements.button.GuiButtonIcon;
import moonsense.ui.utils.blur.BlurShader;
import moonsense.features.SCModule;
import moonsense.features.modules.type.hud.MenuBlurModule;
import moonsense.config.ModuleConfig;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;
import moonsense.ui.screen.AbstractGuiScreen;

public abstract class AbstractSettingsGui extends AbstractGuiScreen
{
    public static int totalOffset;
    protected final GuiScreen parentScreen;
    protected ScaledResolution sr;
    private GuiScreen nextScreen;
    public static AbstractGuiScreen lastWindow;
    
    @Override
    public void initGui() {
        super.initGui();
        AbstractSettingsGui.totalOffset = 0;
        this.sr = new ScaledResolution(this.mc);
        if (ModuleConfig.INSTANCE.isEnabled(MenuBlurModule.INSTANCE) && MenuBlurModule.INSTANCE.uiBlur.getBoolean()) {
            BlurShader.INSTANCE.onGuiOpen((float)MenuBlurModule.INSTANCE.uiBlurRadius.getInt());
        }
        this.buttonList.clear();
        this.buttonList.add(new GuiButtonIcon(0, this.width / 2 + this.getWidth() / 2 - 28, this.height / 2 - this.getHeight() / 2 + 2 - 14, 20, 20, "close.png", "", false));
        this.buttonList.add(new GuiButtonIcon(1, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 20, 28, 28, "mods.png", "", false));
        this.buttonList.get(1).enabled = !(this instanceof GuiModules);
        this.buttonList.add(new GuiButtonIcon(2, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 53, 28, 28, "settings.png", "", false));
        this.buttonList.get(2).enabled = !(this instanceof GuiSettings);
        this.buttonList.add(new GuiButtonIcon(3, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 86, 28, 28, "waypoints.png", "", false));
        this.buttonList.get(3).enabled = !(this instanceof GuiWaypoints);
        if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().serverIP.contains("hypixel.net") && HypixelReplayLoggerModule.INSTANCE.isEnabled()) {
            this.buttonList.add(new GuiButtonIcon(4, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 86 + 33, 28, 28, "spotify/play.png", "", false));
            this.buttonList.get(4).enabled = !(this instanceof GuiReplays);
            this.buttonList.add(new GuiButtonIcon(5, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 119 + 33, 28, 28, "theme.png", "", false));
            this.buttonList.get(5).enabled = !(this instanceof GuiThemeEditor);
            this.buttonList.add(new GuiButtonIcon(6, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 152 + 33, 28, 28, "edit.png", "", false));
        }
        else {
            this.buttonList.add(new GuiButtonIcon(5, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 119, 28, 28, "theme.png", "", false));
            this.buttonList.get(4).enabled = !(this instanceof GuiThemeEditor);
            this.buttonList.add(new GuiButtonIcon(6, this.width / 2 - this.getWidth() / 2 + 4 - 35, this.height / 2 - this.getHeight() / 2 + 152, 28, 28, "edit.png", "", false));
        }
    }
    
    protected void drawBackground() {
        Gui.drawRect(0, 0, this.width, this.height, MenuBlurModule.INSTANCE.uiBackgroundColor.getColor());
        GuiUtils.drawSelectRoundedRect((float)(this.width / 2 - this.getWidth() / 2 - 35), (float)(this.height / 2 - this.getHeight() / 2 - 20), (float)(this.width / 2 - this.getWidth() / 2), (float)(this.height / 2 + this.getHeight() / 2 + 20), 5.0f, 0.0f, 0.0f, 5.0f, ThemeSettings.INSTANCE.uiBackgroundSecondary.getColor());
        GuiUtils.drawSelectRoundedRect((float)(this.width / 2 - this.getWidth() / 2), (float)(this.height / 2 - this.getHeight() / 2 - 20), (float)(this.width / 2 + this.getWidth() / 2), (float)(this.height / 2 - this.getHeight() / 2 + 16), 0.0f, 5.0f, 0.0f, 0.0f, ThemeSettings.INSTANCE.uiBackgroundSecondary.getColor());
        GuiUtils.drawRoundedRect((float)(this.width / 2 - this.getWidth() / 2 - 35), (float)(this.height / 2 - this.getHeight() / 2 - 20), (float)(this.width / 2 + this.getWidth() / 2), (float)(this.height / 2 + this.getHeight() / 2 + 20), 3.0f, ThemeSettings.INSTANCE.uiBackgroundMain.getColor());
        GuiUtils.drawRoundedOutline(this.width / 2 - this.getWidth() / 2 - 35, this.height / 2 - this.getHeight() / 2 - 20, this.width / 2 + this.getWidth() / 2, this.height / 2 + this.getHeight() / 2 + 20, 3.0f, 1.0f, ThemeSettings.INSTANCE.uiAccent.getColor());
        WatermarkRenderer.render(this.width, this.height);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.enableBlend();
        GuiUtils.setGlColor(MoonsenseClient.getBrandingColor(255));
        Minecraft.getMinecraft().getTextureManager().bindTexture(MoonsenseClient.CLIENT_LOGO);
        Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - this.getWidth() / 2 + 2 - 35, this.height / 2 - this.getHeight() / 2 - 20 + 2, 0.0f, 0.0f, 35, 35, 35.0f, 35.0f);
        final SettingElementColor color;
        this.enableInput = this.elements.stream().filter(element -> element instanceof SettingElementColor).noneMatch(element -> {
            color = element;
            return color.colorPane.hovered || color.huePane.hovered || color.alphaPane.hovered;
        });
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.nextScreen == null || !(this.nextScreen instanceof AbstractSettingsGui) || this.nextScreen == this.parentScreen || this.parentScreen == null) {
            if (this.nextScreen != this.parentScreen) {
                GuiModules.scrollState = 0;
            }
            if (ModuleConfig.INSTANCE.isEnabled(MenuBlurModule.INSTANCE) && MenuBlurModule.INSTANCE.uiBlur.getBoolean()) {
                BlurShader.INSTANCE.onGuiClose();
            }
        }
        AbstractSettingsGui.lastWindow = null;
        if (this instanceof GuiModuleSettings && this.nextScreen == null) {
            AbstractSettingsGui.lastWindow = this;
        }
        AbstractSettingsGui.totalOffset = 0;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.nextGui(null);
                break;
            }
            case 1: {
                this.nextGui(new GuiModules(this));
                break;
            }
            case 2: {
                this.nextGui(new GuiSettings(this));
                break;
            }
            case 3: {
                this.nextGui(new GuiWaypoints(this));
                break;
            }
            case 5: {
                this.nextGui(new GuiThemeEditor(this));
                break;
            }
            case 6: {
                this.nextGui(new GuiHUDEditor(null));
                break;
            }
            case 4: {
                this.nextGui(new GuiReplays(this));
                break;
            }
        }
    }
    
    protected void nextGui(final GuiScreen screen) {
        this.nextScreen = screen;
        this.mc.displayGuiScreen(screen);
    }
    
    protected int getWidth() {
        return Math.max(356, this.width - this.width / 3);
    }
    
    protected int getHeight() {
        return this.height - this.height / 3;
    }
    
    protected void addSetting(final Setting setting, final int x, final int y) {
        if (setting.hasValue() && !setting.isCompound() && setting.getModule() != null) {
            Element element = null;
            switch (setting.getType()) {
                case COLOR: {
                    final AtomicBoolean found = new AtomicBoolean(false);
                    final AtomicBoolean atomicBoolean;
                    element = new SettingElementColor(x + 1, y + 1, 10, 10, this.getWidth() - 80, 0, setting, (mainElement, expanded) -> {
                        if (expanded) {
                            AbstractSettingsGui.totalOffset += 34;
                            this.addScroll(37);
                        }
                        else {
                            AbstractSettingsGui.totalOffset -= 34;
                            this.addScroll(-37);
                        }
                        this.elements.forEach(settingElement -> {
                            if ((settingElement instanceof SettingElement || settingElement instanceof ElementCategory) && (settingElement == mainElement || atomicBoolean.get())) {
                                atomicBoolean.set(true);
                                if (settingElement != mainElement && this.elements.indexOf(settingElement) > this.elements.indexOf(mainElement)) {
                                    settingElement.addOffsetToY(((boolean)expanded) ? 34 : -34);
                                }
                            }
                        });
                        return;
                    }, (module, settingElement) -> module.setValue(new ColorObject(GuiUtils.getRGB(settingElement.color, settingElement.alpha), settingElement.chroma.active, (int)settingElement.chromaSpeed.getDenormalized())), this);
                    break;
                }
                case CHECKBOX: {
                    element = new SettingElementCheckbox(x + 1, y + 1, 20, 10, setting, (module, settingElement) -> setting.setValue(!setting.getBoolean()), this);
                    break;
                }
                case INT_SLIDER: {
                    element = new SettingElementSlider(x + 1, y + 4, 150, 5, setting.getRange(0), setting.getRange(1), setting.getRange(2), (float)setting.getInt(), setting, (module, settingElement) -> setting.setValue((int)MathUtil.denormalizeValue(settingElement.sliderValue, setting.getRange(0), setting.getRange(1), setting.getRange(2))), this);
                    element.setXOffset((int)MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 6);
                    break;
                }
                case FLOAT_SLIDER: {
                    element = new SettingElementSlider(x + 1, y + 4, 150, 5, setting.getRange(0), setting.getRange(1), setting.getRange(2), setting.getFloat(), setting, (module, settingElement) -> setting.setValue(MathUtil.denormalizeValue(settingElement.sliderValue, setting.getRange(0), setting.getRange(1), setting.getRange(2))), this);
                    element.setXOffset((int)MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 6);
                    break;
                }
                case KEYBIND: {
                    element = new SettingElementKeybind(x + 1, y + 1, 10, 10, setting, (module, settingElement) -> setting.setValue(new KeyBinding(settingElement.keycode)), this);
                    element.setXOffset((int)(MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 4.0f));
                    break;
                }
                case MODE: {
                    element = new SettingElementMode(x + 1, y + 1, this.getWidth() - 125, 100, 10, setting.getInt(), setting, (module, settingElement) -> setting.setValue(settingElement.mode), this);
                    break;
                }
                case STRING: {
                    element = new SettingElementString(x + 1, y + 1, 100, 10, setting.getString(), setting, (module, settingElement) -> setting.setValue(settingElement.getTextField().getText()), this);
                    break;
                }
                case COMPOUND: {
                    final ArrayList<Setting> settings = ((Setting.CompoundSettingGroup)setting.getObject()).getSettings();
                    for (final Setting s : settings) {
                        s.compound(true);
                    }
                    final AtomicBoolean found2 = new AtomicBoolean(false);
                    final AtomicBoolean atomicBoolean2;
                    final Iterator<Setting> iterator2;
                    Setting s2;
                    element = new SettingElementCompound(x + 1, y + 1, 10, 10, this.getWidth() - 32, 0, setting, (mainElement, expanded) -> {
                        if (expanded) {
                            AbstractSettingsGui.totalOffset += mainElement.getSettings().size() * 17;
                            this.expandScroll(mainElement.getSettings().size() * 17);
                        }
                        else {
                            AbstractSettingsGui.totalOffset -= mainElement.getSettings().size() * 17;
                            this.expandScroll(-(mainElement.getSettings().size() * 17));
                        }
                        this.elements.forEach(settingElement -> {
                            if ((settingElement instanceof SettingElement || settingElement instanceof ElementCategory) && (settingElement == mainElement || atomicBoolean2.get())) {
                                atomicBoolean2.set(true);
                                if (settingElement != mainElement && this.elements.indexOf(settingElement) > this.elements.indexOf(mainElement)) {
                                    settingElement.addOffsetToY(((boolean)expanded) ? (mainElement.getSettings().size() * 17) : (-(mainElement.getSettings().size() * 17)));
                                }
                            }
                        });
                        return;
                    }, (set, settingElement) -> {
                        ((Setting.CompoundSettingGroup)set.getObject()).getSettings().iterator();
                        while (iterator2.hasNext()) {
                            s2 = iterator2.next();
                            if (s2.getValue().size() == 0) {
                                continue;
                            }
                            else {
                                s2.setValue(settingElement.getSettings().get(settingElement.getSettings().indexOf(s2)).getObject());
                            }
                        }
                        return;
                    }, this);
                    break;
                }
            }
            if (element == null) {
                return;
            }
            this.elements.add(element);
        }
        else {
            this.elements.add(new ElementCategory(x - 7, y + 3, 0, 10, setting.getDescription(), true));
        }
    }
    
    public AbstractSettingsGui(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
    
    private double getRowHeight(double row, final int buttonHeight) {
        --row;
        return this.height / 2.0f - this.getHeight() / 2.0f + 25.0f + row * buttonHeight;
    }
}
