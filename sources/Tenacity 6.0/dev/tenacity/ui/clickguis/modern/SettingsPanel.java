// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern;

import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.utils.misc.StringUtils;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RoundedUtil;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.module.settings.impl.KeybindSetting;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.ui.GuiEvents;
import java.util.Map;
import java.util.Iterator;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.Setting;
import java.util.HashMap;
import dev.tenacity.module.Module;

public class SettingsPanel extends Panel
{
    private final Module module;
    private final HashMap<Setting, Animation> booleanSettingHashMap;
    private final HashMap<Setting, Animation> modeSettingHashMap;
    private final HashMap<Setting, Boolean> modeSettingClick;
    private final HashMap<Setting, HashMap<String, Animation>> modesHoverAnimation;
    private final HashMap<Setting, Animation> multiBoolSettingMap;
    private final HashMap<Setting, Boolean> multiBoolSettingClickMap;
    private final HashMap<NumberSetting, Float> sliderMap;
    private final HashMap<Setting, HashMap<BooleanSetting, Animation>> boolHoverAnimation;
    private final HashMap<StringSetting, TextField> textFieldMap;
    public Setting draggingNumber;
    public float maxScroll;
    private boolean hueFlag;
    private boolean saturationFlag;
    public boolean typing;
    
    public SettingsPanel(final Module module) {
        this.maxScroll = 0.0f;
        this.module = module;
        this.booleanSettingHashMap = new HashMap<Setting, Animation>();
        this.multiBoolSettingMap = new HashMap<Setting, Animation>();
        this.multiBoolSettingClickMap = new HashMap<Setting, Boolean>();
        this.boolHoverAnimation = new HashMap<Setting, HashMap<BooleanSetting, Animation>>();
        this.sliderMap = new HashMap<NumberSetting, Float>();
        this.modeSettingHashMap = new HashMap<Setting, Animation>();
        this.modeSettingClick = new HashMap<Setting, Boolean>();
        this.modesHoverAnimation = new HashMap<Setting, HashMap<String, Animation>>();
        this.textFieldMap = new HashMap<StringSetting, TextField>();
        for (final Setting setting : module.getSettingsList()) {
            if (setting instanceof BooleanSetting) {
                this.booleanSettingHashMap.put(setting, new DecelerateAnimation(250, 1.0));
            }
            if (setting instanceof MultipleBoolSetting) {
                this.multiBoolSettingMap.put(setting, new DecelerateAnimation(250, 1.0));
                this.multiBoolSettingClickMap.put(setting, false);
                final HashMap<BooleanSetting, Animation> boolMap = new HashMap<BooleanSetting, Animation>();
                for (final BooleanSetting booleanSetting : ((MultipleBoolSetting)setting).getBoolSettings()) {
                    boolMap.put(booleanSetting, new DecelerateAnimation(250, 1.0));
                }
                this.boolHoverAnimation.put(setting, boolMap);
            }
            if (setting instanceof NumberSetting) {
                this.sliderMap.put((NumberSetting)setting, 0.0f);
            }
            if (setting instanceof ModeSetting) {
                final HashMap<String, Animation> modesHashmap = new HashMap<String, Animation>();
                this.modeSettingHashMap.put(setting, new DecelerateAnimation(250, 1.0));
                this.modeSettingClick.put(setting, false);
                final ModeSetting modeSetting = (ModeSetting)setting;
                for (final String mode : modeSetting.modes) {
                    modesHashmap.put(mode, new DecelerateAnimation(250, 1.0));
                }
                this.modesHoverAnimation.put(setting, modesHashmap);
            }
            if (setting instanceof StringSetting) {
                final StringSetting stringSetting = (StringSetting)setting;
                final TextField textField = new TextField(SettingsPanel.tenacityFont18);
                textField.setText(stringSetting.getString());
                textField.setCursorPositionZero();
                this.textFieldMap.put(stringSetting, textField);
            }
        }
    }
    
    @Override
    public void initGui() {
        if (this.textFieldMap != null) {
            for (final Map.Entry<StringSetting, TextField> entry : this.textFieldMap.entrySet()) {
                entry.getValue().setText(entry.getKey().getString());
                entry.getValue().setCursorPositionZero();
            }
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.textFieldMap != null) {
            for (final Map.Entry<StringSetting, TextField> entry : this.textFieldMap.entrySet()) {
                entry.getValue().keyTyped(typedChar, keyCode);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.handle(mouseX, mouseY, -1, GuiEvents.DRAW);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.handle(mouseX, mouseY, button, GuiEvents.CLICK);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.handle(mouseX, mouseY, state, GuiEvents.RELEASE);
    }
    
    public void handle(final int mouseX, final int mouseY, final int button, final GuiEvents type) {
        this.typing = false;
        if (type == GuiEvents.RELEASE && button == 0) {
            this.draggingNumber = null;
        }
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        float count = 0.0f;
        final float settingHeight = 22.0f;
        for (final Setting setting : this.module.getSettingsList()) {
            if (setting.cannotBeShown()) {
                continue;
            }
            if (setting instanceof KeybindSetting) {
                continue;
            }
            final float settingY = this.y + 30.0f + count * settingHeight;
            final boolean isHoveringSetting = HoveringUtil.isHovering(this.x + 5.0f, settingY, 130.0f, settingHeight - 2.0f, mouseX, mouseY);
            if (setting instanceof BooleanSetting) {
                final BooleanSetting booleanSetting = (BooleanSetting)setting;
                final Animation animation = this.booleanSettingHashMap.get(setting);
                animation.setDirection(booleanSetting.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
                if (type == GuiEvents.CLICK && isHoveringSetting && button == 0) {
                    booleanSetting.toggle();
                }
                final int color = ColorUtil.interpolateColor(new Color(30, 31, 35), colors.getFirst(), animation.getOutput().floatValue());
                SettingsPanel.tenacityFont16.drawString(setting.name, this.x + 13.0f, settingY + SettingsPanel.tenacityFont16.getMiddleOfBox(8.0f), -1);
                GL11.glEnable(3042);
                RoundedUtil.drawRound(this.x + 109.0f, settingY, 18.0f, 8.0f, 4.0f, ColorUtil.interpolateColorC(new Color(30, 31, 35), colors.getFirst(), animation.getOutput().floatValue()));
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                RenderUtil.drawGoodCircle(this.x + 113.0f + 10.0f * animation.getOutput().floatValue(), settingY + 4.0f, 4.0f, -1);
                count -= 0.2f;
            }
            if (setting instanceof ModeSetting) {
                final ModeSetting modeSetting = (ModeSetting)setting;
                final Animation animation = this.modeSettingHashMap.get(setting);
                if (type == GuiEvents.CLICK && isHoveringSetting && button == 1) {
                    this.modeSettingClick.put(setting, !this.modeSettingClick.get(setting));
                }
                final float stringWidth = SettingsPanel.tenacityFont16.getStringWidth(StringUtils.getLongestModeName(modeSetting.modes));
                animation.setDirection(this.modeSettingClick.get(setting) ? Direction.FORWARDS : Direction.BACKWARDS);
                final float modeWidth = 15.0f + stringWidth;
                final float math = (float)((modeSetting.modes.size() - 1) * 15);
                final float boxX = this.x + 114.0f - stringWidth - 1.0f;
                final float nameWidth = SettingsPanel.tenacityFont16.getStringWidth(modeSetting.name);
                final float stringX = this.x + 13.0f;
                if (nameWidth > boxX - stringX) {
                    SettingsPanel.tenacityFont16.drawWrappedText(modeSetting.name, stringX, settingY - SettingsPanel.tenacityFont16.getMiddleOfBox(12.0f) / 2.0f, -1, boxX - stringX, 2.0f);
                }
                else {
                    SettingsPanel.tenacityFont16.drawString(modeSetting.name, this.x + 13.0f, settingY + 2.0f, -1);
                }
                RoundedUtil.drawRound(boxX, settingY - 1.0f, modeWidth + 2.0f, 12.0f + math * animation.getOutput().floatValue(), 4.0f, new Color(68, 71, 78));
                RoundedUtil.drawRound(this.x + 114.0f - stringWidth, settingY, modeWidth, 10.0f + math * animation.getOutput().floatValue(), 3.0f, new Color(30, 31, 35));
                final float modeX = this.x + 114.0f - stringWidth + modeWidth / 2.0f;
                RenderUtil.resetColor();
                SettingsPanel.tenacityFont16.drawCenteredString(modeSetting.getMode(), modeX, settingY + SettingsPanel.tenacityFont16.getMiddleOfBox(10.0f), -1);
                int modeCount = 1;
                for (final String mode : modeSetting.modes) {
                    if (!mode.equalsIgnoreCase(modeSetting.getMode())) {
                        final Animation modeAnimation = this.modesHoverAnimation.get(modeSetting).get(mode);
                        final boolean isHoveringMode = animation.getDirection().equals(Direction.FORWARDS) && HoveringUtil.isHovering(this.x + 115.0f - stringWidth, settingY + modeCount * 15, modeWidth, 11.0f, mouseX, mouseY);
                        if (type == GuiEvents.CLICK && button == 0 && isHoveringMode) {
                            this.modeSettingClick.put(setting, !this.modeSettingClick.get(setting));
                            modeSetting.setCurrentMode(mode);
                        }
                        modeAnimation.setDirection(isHoveringMode ? Direction.FORWARDS : Direction.BACKWARDS);
                        modeAnimation.setDuration(isHoveringMode ? 200 : 350);
                        final int colorInterpolate = ColorUtil.interpolateColor(new Color(128, 134, 141), colors.getSecond(), modeAnimation.getOutput().floatValue());
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        SettingsPanel.tenacityFont16.drawCenteredString(mode, modeX, settingY + 2.0f + modeCount * 15 * animation.getOutput().floatValue(), ColorUtil.interpolateColor(new Color(40, 40, 40, 10), new Color(colorInterpolate), animation.getOutput().floatValue()));
                        ++modeCount;
                    }
                }
                count += math / settingHeight * animation.getOutput().floatValue();
            }
            if (setting instanceof ColorSetting) {
                final ColorSetting colorSetting = (ColorSetting)setting;
                final float height = (float)(colorSetting.isRainbow() ? 100 : 90);
                RoundedUtil.drawRound(this.x + 12.0f, settingY - 1.0f, 117.0f, height, 4.0f, new Color(68, 71, 78));
                RoundedUtil.drawRound(this.x + 13.0f, settingY, 115.0f, height - 2.0f, 3.0f, new Color(30, 31, 35));
                SettingsPanel.tenacityFont16.drawCenteredString(colorSetting.name, this.x + 13.0f + 57.5f, settingY + 3.0f, -1);
                if (colorSetting.isRainbow()) {
                    final Color color2 = colorSetting.getColor();
                    final int red = color2.getRed();
                    final int green = color2.getGreen();
                    final int blue = color2.getBlue();
                    final float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                    colorSetting.setHue(hsb[0]);
                    colorSetting.setSaturation(hsb[1]);
                    colorSetting.setBrightness(hsb[2]);
                }
                final float[] hsb2 = { (float)colorSetting.getHue(), (float)colorSetting.getSaturation(), (float)colorSetting.getBrightness() };
                final float gradientX = this.x + 17.0f;
                final float gradientY = settingY + 15.0f;
                final float gradientWidth = 97.0f;
                final float gradientHeight = 50.0f;
                if (button == 0 && type == GuiEvents.CLICK) {
                    if (HoveringUtil.isHovering(gradientX, gradientY + gradientHeight + 3.0f, gradientWidth + 10.0f, 6.0f, mouseX, mouseY)) {
                        this.draggingNumber = setting;
                        this.hueFlag = true;
                    }
                    if (HoveringUtil.isHovering(gradientX, gradientY, gradientWidth, gradientHeight, mouseX, mouseY)) {
                        this.draggingNumber = setting;
                        this.hueFlag = false;
                    }
                }
                if (this.draggingNumber != null && this.draggingNumber.equals(setting)) {
                    if (this.hueFlag) {
                        colorSetting.setHue(Math.min(1.0f, Math.max(0.0f, (mouseX - gradientX) / (gradientWidth + 10.0f))));
                    }
                    else {
                        colorSetting.setBrightness(Math.min(1.0f, Math.max(0.0f, 1.0f - (mouseY - gradientY) / gradientHeight)));
                        colorSetting.setSaturation(Math.min(1.0f, Math.max(0.0f, (mouseX - gradientX) / gradientWidth)));
                    }
                }
                Gui.drawRect2(gradientX, gradientY, gradientWidth, gradientHeight, Color.getHSBColor(hsb2[0], 1.0f, 1.0f).getRGB());
                Gui.drawGradientRectSideways2(gradientX, gradientY, gradientWidth, gradientHeight, Color.getHSBColor(hsb2[0], 0.0f, 1.0f).getRGB(), ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 0.0f, 1.0f).getRGB(), 0.0f));
                Gui.drawGradientRect2(gradientX, gradientY, gradientWidth, gradientHeight, ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 1.0f, 0.0f).getRGB(), 0.0f), Color.getHSBColor(hsb2[0], 1.0f, 0.0f).getRGB());
                float pickerY = gradientY + gradientHeight * (1.0f - hsb2[2]);
                float pickerX = gradientX + (gradientWidth * hsb2[1] - 1.0f);
                pickerY = Math.max(Math.min(gradientY + gradientHeight - 2.0f, pickerY), gradientY);
                pickerX = Math.max(Math.min(gradientX + gradientWidth - 2.0f, pickerX), gradientX);
                GL11.glEnable(3042);
                RenderUtil.color(-1);
                SettingsPanel.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/colorpicker2.png"));
                Gui.drawModalRectWithCustomSizedTexture(pickerX, pickerY, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Gui.drawRect2(gradientX + gradientWidth + 5.0f, gradientY, 5.0, gradientHeight, colorSetting.getColor().getRGB());
                RenderUtil.color(-1);
                SettingsPanel.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/hue.png"));
                Gui.drawModalRectWithCustomSizedTexture(gradientX, gradientY + gradientHeight + 4.5f, 0.0f, 0.0f, gradientWidth + 10.0f, 4.0f, gradientWidth + 10.0f, 4.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Gui.drawRect2(gradientX + (gradientWidth + 10.0f) * hsb2[0], gradientY + gradientHeight + 3.5, 1.0, 6.0, -1);
                SettingsPanel.tenacityFont14.drawString("Rainbow: ", gradientX, gradientY + gradientHeight + 14.0f, -1);
                final String text = colorSetting.isRainbow() ? "Enabled" : "Disabled";
                final float textX = gradientX + SettingsPanel.tenacityFont14.getStringWidth("Rainbow: ") + 4.0f;
                final float textY = gradientY + gradientHeight + 13.0f;
                final boolean hoveringRound = HoveringUtil.isHovering(textX, textY, SettingsPanel.tenacityFont14.getStringWidth(text) + 4.0f, (float)(SettingsPanel.tenacityFont14.getHeight() + 2), mouseX, mouseY);
                if (type == GuiEvents.CLICK && hoveringRound && button == 0) {
                    colorSetting.setRainbow(!colorSetting.isRainbow());
                }
                final Color roundColor = colorSetting.isRainbow() ? new Color(0, 203, 33) : new Color(203, 0, 33);
                RoundedUtil.drawRound(textX, textY, SettingsPanel.tenacityFont14.getStringWidth(text) + 4.0f, (float)(SettingsPanel.tenacityFont14.getHeight() + 2), 3.0f, hoveringRound ? roundColor.brighter() : roundColor);
                SettingsPanel.tenacityFont14.drawCenteredString(text, textX + (SettingsPanel.tenacityFont14.getStringWidth(text) + 4.0f) / 2.0f, textY + SettingsPanel.tenacityFont14.getMiddleOfBox((float)(SettingsPanel.tenacityFont14.getHeight() + 2)), -1);
                if (colorSetting.isRainbow()) {
                    Gui.drawGradientRectSideways2(gradientX, textY + 12.0f, gradientWidth + 10.0f, 4.0, Color.WHITE.getRGB(), Color.RED.getRGB());
                    Gui.drawRect2(gradientX + (gradientWidth + 10.0f) * hsb2[1], textY + 11.0f, 1.0, 6.0, Color.BLACK.getRGB());
                    final boolean hoveringSat = HoveringUtil.isHovering(gradientX, textY + 10.0f, gradientWidth + 10.0f, 6.0f, mouseX, mouseY);
                    if (type == GuiEvents.CLICK && hoveringSat && button == 0) {
                        this.saturationFlag = true;
                        this.draggingNumber = setting;
                    }
                    if (type == GuiEvents.RELEASE && this.saturationFlag) {
                        this.saturationFlag = false;
                    }
                    if (this.saturationFlag) {
                        colorSetting.getRainbow().setSaturation(Math.min(1.0f, Math.max(0.0f, (mouseX - gradientX) / (gradientWidth + 10.0f))));
                    }
                }
                count += (float)(3.5 + (colorSetting.isRainbow() ? 0.5f : 0.0f));
            }
            if (setting instanceof NumberSetting) {
                final NumberSetting numberSetting = (NumberSetting)setting;
                SettingsPanel.tenacityFont16.drawString(setting.name, this.x + 13.0f, settingY + 2.0f, -1);
                if (type == GuiEvents.CLICK && isHoveringSetting && button == 0) {
                    this.draggingNumber = numberSetting;
                }
                final double currentValue = numberSetting.getValue();
                if (this.draggingNumber != null && this.draggingNumber == setting) {
                    final float percent = Math.min(1.0f, Math.max(0.0f, (mouseX - (this.x + 14.0f)) / 108.0f));
                    final double newValue = percent * (numberSetting.getMaxValue() - numberSetting.getMinValue()) + numberSetting.getMinValue();
                    numberSetting.setValue(newValue);
                }
                final String value = Double.toString(MathUtils.round(currentValue, 2));
                SettingsPanel.tenacityFont16.drawString(value, this.x + 120.0f - SettingsPanel.tenacityFont16.getStringWidth(value), settingY + 2.0f, -1);
                final float sliderMath = (float)((currentValue - numberSetting.getMinValue()) / (numberSetting.getMaxValue() - numberSetting.getMinValue()));
                RoundedUtil.drawRound(this.x + 12.0f, settingY + 13.0f, 110.0f, 5.0f, 2.0f, new Color(30, 31, 35));
                final Color sliderColor = colors.getFirst();
                final float[] hsb = Color.RGBtoHSB(sliderColor.getRed(), sliderColor.getGreen(), sliderColor.getBlue(), null);
                final float saturation = hsb[1] * sliderMath;
                final float totalSliderWidth = 108.0f;
                this.sliderMap.put(numberSetting, (float)RenderUtil.animate(totalSliderWidth * sliderMath, this.sliderMap.get(numberSetting), 0.1));
                RoundedUtil.drawRound(this.x + 13.0f, settingY + 14.0f, Math.max(4.0f, this.sliderMap.get(numberSetting)), 3.0f, 1.5f, new Color(Color.HSBtoRGB(hsb[0], saturation, hsb[2])));
                count += 0.5;
            }
            if (setting instanceof StringSetting) {
                final StringSetting stringSetting = (StringSetting)setting;
                SettingsPanel.tenacityFont16.drawString(setting.name, this.x + 16.0f, settingY + 1.0f, -1);
                final TextField textField = this.textFieldMap.get(stringSetting);
                textField.setBackgroundText("Type here...");
                textField.setXPosition(this.x + 17.0f);
                textField.setYPosition(settingY + 12.0f);
                textField.setWidth(100.0f);
                textField.setHeight(15.0f);
                textField.setOutline(new Color(68, 71, 78));
                textField.setFill(new Color(30, 31, 35));
                stringSetting.setString(textField.getText());
                if (type == GuiEvents.CLICK) {
                    textField.mouseClicked(mouseX, mouseY, button);
                }
                if (!this.typing) {
                    this.typing = textField.isFocused();
                }
                textField.drawTextBox();
                count += 0.5f;
            }
            if (setting instanceof MultipleBoolSetting) {
                final Animation animation2 = this.multiBoolSettingMap.get(setting);
                final MultipleBoolSetting multiBoolSetting = (MultipleBoolSetting)setting;
                if (type == GuiEvents.CLICK && isHoveringSetting && button == 1) {
                    this.multiBoolSettingClickMap.put(setting, !this.multiBoolSettingClickMap.get(setting));
                }
                animation2.setDirection(this.multiBoolSettingClickMap.get(setting) ? Direction.FORWARDS : Direction.BACKWARDS);
                final float math2 = (float)(multiBoolSetting.getBoolSettings().size() * 15);
                final float adjustment = math2 * animation2.getOutput().floatValue();
                RenderUtil.renderRoundedRect(this.x + 11.5f, settingY - 4.5f, 118.0f, 19.0f + adjustment, 4.0f, new Color(68, 71, 78).getRGB());
                RenderUtil.renderRoundedRect(this.x + 13.0f, settingY - 3.0f, 115.0f, 16.0f + adjustment, 3.0f, new Color(30, 31, 35).getRGB());
                SettingsPanel.tenacityFont16.drawCenteredString(multiBoolSetting.name, this.x + 70.5f, settingY - 3.0f + 8.0f - SettingsPanel.tenacityFont16.getHeight() / 2.0f, -1);
                int boolCount = 1;
                for (final BooleanSetting booleanSetting2 : multiBoolSetting.getBoolSettings()) {
                    final Animation boolAnimation = this.boolHoverAnimation.get(multiBoolSetting).get(booleanSetting2);
                    final boolean isHoveringBool = animation2.getDirection().equals(Direction.FORWARDS) && HoveringUtil.isHovering(this.x + 17.0f, settingY + boolCount * 15, SettingsPanel.tenacityFont16.getStringWidth(booleanSetting2.name) + 13.0f, 11.0f, mouseX, mouseY);
                    if (type == GuiEvents.CLICK && button == 0 && isHoveringBool) {
                        multiBoolSetting.getSetting(booleanSetting2.name).toggle();
                    }
                    boolAnimation.setDirection(isHoveringBool ? Direction.FORWARDS : Direction.BACKWARDS);
                    final Color boolColor = booleanSetting2.isEnabled() ? colors.getSecond() : new Color(128, 134, 141);
                    final int colorInterpolate2 = ColorUtil.interpolateColor(boolColor, boolColor.brighter(), boolAnimation.getOutput().floatValue());
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderUtil.drawGoodCircle(this.x + 20.0f, settingY + 5.0f + boolCount * 15 * animation2.getOutput().floatValue(), 2.0f, ColorUtil.interpolateColor(new Color(40, 40, 40, 10), new Color(colorInterpolate2), animation2.getOutput().floatValue()));
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    SettingsPanel.tenacityFont16.drawString(booleanSetting2.name, this.x + 28.0f, settingY + 2.0f + boolCount * 15 * animation2.getOutput().floatValue(), ColorUtil.interpolateColor(new Color(40, 40, 40, 10), new Color(colorInterpolate2), animation2.getOutput().floatValue()));
                    ++boolCount;
                }
                count += math2 / settingHeight * animation2.getOutput().floatValue();
            }
            ++count;
        }
        this.maxScroll = count * settingHeight;
    }
}
