// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.compact.impl;

import java.util.Collection;
import dev.tenacity.utils.misc.StringUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.module.settings.impl.KeybindSetting;
import dev.tenacity.ui.GuiEvents;
import java.util.Map;
import java.util.Iterator;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.module.settings.Setting;
import java.awt.Color;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import java.util.HashMap;
import dev.tenacity.module.Module;
import dev.tenacity.ui.Screen;

public class SettingComponents implements Screen
{
    private final Module module;
    private final HashMap<NumberSetting, Float> numberSettingMap;
    private final HashMap<StringSetting, TextField> textFieldMap;
    private final HashMap<ColorSetting, Animation> colorSettingMap;
    private final HashMap<ModeSetting, Animation> modeSettingMap;
    private final HashMap<MultipleBoolSetting, Animation> multiBoolMap;
    public float size;
    public Color actualColor;
    public float x;
    public float y;
    public float rectWidth;
    public Setting draggingNumber;
    private boolean hueFlag;
    private boolean saturationFlag;
    public boolean typing;
    
    public SettingComponents(final Module module) {
        this.numberSettingMap = new HashMap<NumberSetting, Float>();
        this.textFieldMap = new HashMap<StringSetting, TextField>();
        this.colorSettingMap = new HashMap<ColorSetting, Animation>();
        this.modeSettingMap = new HashMap<ModeSetting, Animation>();
        this.multiBoolMap = new HashMap<MultipleBoolSetting, Animation>();
        this.module = module;
        for (final Setting setting : module.getSettingsList()) {
            if (setting instanceof NumberSetting) {
                this.numberSettingMap.put((NumberSetting)setting, 0.0f);
            }
            if (setting instanceof StringSetting) {
                final StringSetting stringSetting = (StringSetting)setting;
                final TextField textField = new TextField(SettingComponents.tenacityFont14);
                textField.setText(stringSetting.getString());
                textField.setCursorPositionZero();
                this.textFieldMap.put(stringSetting, textField);
            }
            if (setting instanceof ColorSetting) {
                final ColorSetting colorSetting = (ColorSetting)setting;
                final Animation animation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
                this.colorSettingMap.put(colorSetting, animation);
            }
            if (setting instanceof ModeSetting) {
                final ModeSetting modeSetting = (ModeSetting)setting;
                final Animation animation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
                this.modeSettingMap.put(modeSetting, animation);
            }
            if (setting instanceof MultipleBoolSetting) {
                final MultipleBoolSetting multipleBoolSetting = (MultipleBoolSetting)setting;
                final Animation animation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
                this.multiBoolMap.put(multipleBoolSetting, animation);
            }
        }
    }
    
    @Override
    public void initGui() {
        for (final Map.Entry<StringSetting, TextField> entry : this.textFieldMap.entrySet()) {
            entry.getValue().setText(entry.getKey().getString());
            entry.getValue().setCursorPositionZero();
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        for (final Map.Entry<StringSetting, TextField> entry : this.textFieldMap.entrySet()) {
            entry.getValue().keyTyped(typedChar, keyCode);
        }
    }
    
    public void handle(final int mouseX, final int mouseY, final int button, final GuiEvents type) {
        this.typing = false;
        final float settingHeight = 16.0f;
        float count = 0.0f;
        final Color accentColor = this.actualColor;
        final Color disabledColor = new Color(64, 68, 75);
        for (final Setting setting : this.module.getSettingsList()) {
            if (setting.cannotBeShown()) {
                continue;
            }
            if (setting instanceof KeybindSetting) {
                continue;
            }
            final float settingY = this.y + count * settingHeight;
            final float middleSettingY = (float)MathUtils.roundToHalf(this.y + SettingComponents.tenacityFont16.getMiddleOfBox(settingHeight) + count * settingHeight);
            if (setting instanceof NumberSetting) {
                final NumberSetting numberSetting = (NumberSetting)setting;
                SettingComponents.tenacityFont16.drawString(setting.name, this.x + 5.0f, middleSettingY, -1);
                String value = String.valueOf(MathUtils.round(numberSetting.getValue(), 2));
                value = (value.contains(".") ? value.replaceAll("0*$", "").replaceAll("\\.$", "") : value);
                final String maxValue = Double.toString(MathUtils.round(numberSetting.getMaxValue(), 2));
                final float valueWidth = SettingComponents.tenacityFont14.getStringWidth(maxValue);
                Gui.drawRect2(this.x + this.rectWidth - (valueWidth + 7.0f), settingY + 4.0f, valueWidth + 4.0f, 8.0, disabledColor.getRGB());
                SettingComponents.tenacityFont14.drawCenteredString(value, this.x + this.rectWidth - (valueWidth + 5.0f) + valueWidth / 2.0f, settingY + 6.0f, -1);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final float sliderWidth = 50.0f;
                final float sliderHeight = 2.0f;
                final float sliderX = this.x + this.rectWidth - (valueWidth + 4.0f + 10.0f + sliderWidth);
                final float sliderY = settingY + settingHeight / 2.0f - sliderHeight / 2.0f;
                final float sliderRadius = 1.0f;
                final boolean hoveringSlider = HoveringUtil.isHovering(sliderX, sliderY - 2.0f, sliderWidth, sliderHeight + 4.0f, mouseX, mouseY);
                if (type == GuiEvents.RELEASE) {
                    this.draggingNumber = null;
                }
                if (type == GuiEvents.CLICK && hoveringSlider && button == 0) {
                    this.draggingNumber = numberSetting;
                }
                final double currentValue = numberSetting.getValue();
                if (this.draggingNumber != null && this.draggingNumber == setting) {
                    final float percent = Math.min(1.0f, Math.max(0.0f, (mouseX - sliderX) / sliderWidth));
                    final double newValue = percent * (numberSetting.getMaxValue() - numberSetting.getMinValue()) + numberSetting.getMinValue();
                    numberSetting.setValue(newValue);
                }
                final float sliderMath = (float)((currentValue - numberSetting.getMinValue()) / (numberSetting.getMaxValue() - numberSetting.getMinValue()));
                this.numberSettingMap.put(numberSetting, (float)RenderUtil.animate(sliderWidth * sliderMath, this.numberSettingMap.get(numberSetting), 0.1));
                Gui.drawRect2(sliderX, sliderY, sliderWidth, sliderHeight, disabledColor.getRGB());
                Gui.drawRect2(sliderX, sliderY, Math.max(3.0f, this.numberSettingMap.get(numberSetting)), sliderHeight, accentColor.getRGB());
                final float whiteRectWidth = 1.5f;
                final float whiteRectHeight = 6.0f;
                RenderUtil.resetColor();
                Gui.drawRect2(sliderX + Math.max(3.0f, this.numberSettingMap.get(numberSetting)), sliderY + sliderHeight / 2.0f - whiteRectHeight / 2.0f, whiteRectWidth, whiteRectHeight, -1);
            }
            if (setting instanceof BooleanSetting) {
                final BooleanSetting booleanSetting = (BooleanSetting)setting;
                SettingComponents.tenacityFont16.drawString(setting.name, this.x + 5.0f, middleSettingY, -1);
                final boolean enabled = booleanSetting.isEnabled();
                final float boolWH = 10.0f;
                final float boolX = this.x + this.rectWidth - (boolWH + 6.0f);
                final float boolY = settingY + settingHeight / 2.0f - boolWH / 2.0f;
                final boolean hoveringBool = HoveringUtil.isHovering(boolX - 2.0f, boolY - 2.0f, boolWH + 4.0f, boolWH + 4.0f, mouseX, mouseY);
                if (type == GuiEvents.CLICK && hoveringBool && button == 0) {
                    booleanSetting.toggle();
                }
                final Color rectColor = enabled ? accentColor : disabledColor.brighter();
                Gui.drawRect2(boolX, boolY, boolWH, boolWH, rectColor.getRGB());
                Gui.drawRect2(boolX + 0.5f, boolY + 0.5f, boolWH - 1.0f, boolWH - 1.0f, disabledColor.getRGB());
                if (booleanSetting.isEnabled()) {
                    SettingComponents.iconFont16.drawCenteredString("o", boolX + boolWH / 2.0f, boolY + SettingComponents.iconFont16.getMiddleOfBox(boolWH) + 0.5f, Color.WHITE);
                }
            }
            if (setting instanceof ColorSetting) {
                final ColorSetting colorSetting = (ColorSetting)setting;
                final Animation clickAnimation = this.colorSettingMap.get(colorSetting);
                SettingComponents.tenacityFont16.drawString(setting.name, this.x + 5.0f, middleSettingY, -1);
                final float colorWidth = 20.0f;
                final boolean hovered = HoveringUtil.isHovering(this.x + this.rectWidth - (colorWidth + 5.0f), middleSettingY - 1.0f, colorWidth, 6.0f, mouseX, mouseY);
                if (hovered && button == 1 && type == GuiEvents.CLICK) {
                    clickAnimation.changeDirection();
                }
                Gui.drawRect2(this.x + this.rectWidth - (colorWidth + 5.0f), middleSettingY - 1.0f, colorWidth, 6.0, hovered ? ColorUtil.darker(colorSetting.getColor(), 0.7f).getRGB() : colorSetting.getColor().getRGB());
                if ((clickAnimation.isDone() && clickAnimation.getDirection().equals(Direction.FORWARDS)) || !clickAnimation.isDone()) {
                    if (colorSetting.isRainbow()) {
                        final Color color = colorSetting.getColor();
                        final int red = color.getRed();
                        final int green = color.getGreen();
                        final int blue = color.getBlue();
                        final float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                        colorSetting.setHue(hsb[0]);
                        colorSetting.setSaturation(hsb[1]);
                        colorSetting.setBrightness(hsb[2]);
                    }
                    final float[] hsb2 = { (float)colorSetting.getHue(), (float)colorSetting.getSaturation(), (float)colorSetting.getBrightness() };
                    final float gradientX = this.x + 5.0f;
                    final float gradientY = settingY + 20.0f;
                    final float gradientWidth = 115.0f;
                    final float gradientHeight = 10.0f + 35.0f * clickAnimation.getOutput().floatValue();
                    if (type == GuiEvents.CLICK && button == 0) {
                        if (HoveringUtil.isHovering(gradientX + gradientWidth + 5.0f, gradientY, 5.0f, gradientHeight, mouseX, mouseY)) {
                            this.draggingNumber = setting;
                            this.hueFlag = true;
                        }
                        if (HoveringUtil.isHovering(gradientX, gradientY, gradientWidth, gradientHeight, mouseX, mouseY)) {
                            this.draggingNumber = setting;
                            this.hueFlag = false;
                        }
                    }
                    if (type == GuiEvents.RELEASE) {
                        this.draggingNumber = null;
                    }
                    if (this.draggingNumber != null && this.draggingNumber.equals(setting)) {
                        if (this.hueFlag) {
                            colorSetting.setHue(Math.min(1.0f, Math.max(0.0f, (mouseY - gradientY) / gradientHeight)));
                        }
                        else {
                            colorSetting.setBrightness(Math.min(1.0f, Math.max(0.0f, 1.0f - (mouseY - gradientY) / gradientHeight)));
                            colorSetting.setSaturation(Math.min(1.0f, Math.max(0.0f, (mouseX - gradientX) / gradientWidth)));
                        }
                    }
                    final int hsbZeroOneOne = ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 1.0f, 1.0f).getRGB(), clickAnimation.getOutput().floatValue());
                    final int hsbZeroZeroOne = ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 0.0f, 1.0f).getRGB(), clickAnimation.getOutput().floatValue());
                    final int hsbZeroOneZero = ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 1.0f, 0.0f).getRGB(), clickAnimation.getOutput().floatValue());
                    final int hue = ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 0.0f, 0.0f).getRGB(), clickAnimation.getOutput().floatValue());
                    Gui.drawRect2(gradientX, gradientY, gradientWidth, gradientHeight, hsbZeroOneOne);
                    Gui.drawGradientRectSideways2(gradientX, gradientY, gradientWidth, gradientHeight, hsbZeroZeroOne, ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 0.0f, 1.0f).getRGB(), 0.0f));
                    Gui.drawGradientRect2(gradientX, gradientY, gradientWidth, gradientHeight, ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 1.0f, 0.0f).getRGB(), 0.0f), hsbZeroOneZero);
                    final int rectColor2 = ColorUtil.applyOpacity(disabledColor.getRGB(), clickAnimation.getOutput().floatValue());
                    final int textColor = ColorUtil.applyOpacity(-1, clickAnimation.getOutput().floatValue());
                    final float colorInfoWidth = SettingComponents.tenacityFont14.getStringWidth("R: 255") + 6.0f;
                    Gui.drawRect2(gradientX, gradientY + gradientHeight + 4.0f, colorInfoWidth, 8.0, rectColor2);
                    Gui.drawRect2(gradientX + colorInfoWidth + 5.0f, gradientY + gradientHeight + 4.0f, colorInfoWidth, 8.0, rectColor2);
                    Gui.drawRect2(gradientX + colorInfoWidth * 2.0f + 10.0f, gradientY + gradientHeight + 4.0f, colorInfoWidth, 8.0, rectColor2);
                    final int redColor = new Color(255, 0, 0, (int)(255.0f * clickAnimation.getOutput().floatValue())).getRGB();
                    final int greenColor = new Color(0, 255, 0, (int)(255.0f * clickAnimation.getOutput().floatValue())).getRGB();
                    final int blueColor = new Color(0, 0, 255, (int)(255.0f * clickAnimation.getOutput().floatValue())).getRGB();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    SettingComponents.tenacityFont14.drawCenteredString("R§f: " + colorSetting.getColor().getRed(), gradientX + 2.5f + colorWidth / 2.0f, gradientY + gradientHeight + 6.0f, redColor);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    SettingComponents.tenacityFont14.drawCenteredString("G§f: " + colorSetting.getColor().getGreen(), gradientX + colorInfoWidth + 5.0f + colorInfoWidth / 2.0f, gradientY + gradientHeight + 6.0f, greenColor);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    SettingComponents.tenacityFont14.drawCenteredString("B§f: " + colorSetting.getColor().getBlue(), gradientX + colorInfoWidth * 2.0f + 12.5f + colorWidth / 2.0f, gradientY + gradientHeight + 6.0f, blueColor);
                    final float rainbowX = gradientX + colorInfoWidth * 2.0f + 10.0f + colorInfoWidth + 3.0f;
                    SettingComponents.tenacityFont14.drawString("Rainbow: ", rainbowX, gradientY + gradientHeight + 6.0f, textColor);
                    final float clickAnim = clickAnimation.getOutput().floatValue();
                    RoundedUtil.drawRound(rainbowX + SettingComponents.tenacityFont14.getStringWidth("Rainbow: ") + 4.0f, gradientY + gradientHeight + 5.5f, 6.0f, 6.0f, 2.5f, ColorUtil.applyOpacity(colorSetting.isRainbow() ? this.actualColor : disabledColor, clickAnim));
                    if (type == GuiEvents.CLICK && button == 0 && HoveringUtil.isHovering(rainbowX + SettingComponents.tenacityFont14.getStringWidth("Rainbow: ") + 3.0f, gradientY + gradientHeight + 5.0f, 7.0f, 7.0f, mouseX, mouseY)) {
                        colorSetting.setRainbow(!colorSetting.isRainbow());
                    }
                    float pickerY = gradientY + gradientHeight * (1.0f - hsb2[2]);
                    float pickerX = gradientX + (gradientWidth * hsb2[1] - 1.0f);
                    pickerY = Math.max(Math.min(gradientY + gradientHeight - 2.0f, pickerY), gradientY);
                    pickerX = Math.max(Math.min(gradientX + gradientWidth - 2.0f, pickerX), gradientX);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glEnable(3042);
                    RenderUtil.color(textColor);
                    SettingComponents.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/colorpicker2.png"));
                    Gui.drawModalRectWithCustomSizedTexture(pickerX, pickerY, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderUtil.color(textColor);
                    SettingComponents.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/hue2.png"));
                    Gui.drawModalRectWithCustomSizedTexture(gradientX + gradientWidth + 5.0f, gradientY, 0.0f, 0.0f, 5.0f, gradientHeight, 5.0f, gradientHeight);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    Gui.drawRect2(gradientX + gradientWidth + 5.0f, gradientY + gradientHeight * hsb2[0], 5.0, 1.0, textColor);
                    if (colorSetting.isRainbow()) {
                        Gui.drawGradientRect2(gradientX + gradientWidth + 15.0f, gradientY, 5.0, gradientHeight, textColor, Color.RED.getRGB());
                        Gui.drawRect2(gradientX + gradientWidth + 15.0f, gradientY + gradientHeight * hsb2[1], 5.0, 1.0, Color.BLACK.getRGB());
                        final boolean hoveringSat = HoveringUtil.isHovering(gradientX + gradientWidth + 15.0f, gradientY, 5.0f, gradientHeight, mouseX, mouseY);
                        if (type == GuiEvents.CLICK && button == 0 && hoveringSat) {
                            this.saturationFlag = true;
                            this.draggingNumber = setting;
                        }
                        if (type == GuiEvents.RELEASE && this.saturationFlag) {
                            this.saturationFlag = false;
                            this.draggingNumber = null;
                        }
                        if (this.saturationFlag) {
                            colorSetting.getRainbow().setSaturation(Math.min(1.0f, Math.max(0.0f, (mouseY - gradientY) / gradientHeight)));
                        }
                    }
                }
                count += 4.0f * clickAnimation.getOutput().floatValue();
            }
            if (setting instanceof StringSetting) {
                final StringSetting stringSetting = (StringSetting)setting;
                RenderUtil.resetColor();
                SettingComponents.tenacityFont16.drawString(setting.name, this.x + 5.0f, middleSettingY, -1);
                final TextField textField = this.textFieldMap.get(stringSetting);
                final float textFieldWidth = 60.0f;
                final float textFieldHeight = 10.0f;
                textField.setBackgroundText("Type Here...");
                textField.setXPosition(this.x + this.rectWidth - (textFieldWidth + 5.0f));
                textField.setYPosition(settingY + settingHeight / 2.0f - textFieldHeight / 2.0f);
                textField.setWidth(textFieldWidth);
                textField.setHeight(textFieldHeight);
                textField.setDrawingBackground(false);
                if (type == GuiEvents.CLICK) {
                    textField.mouseClicked(mouseX, mouseY, button);
                }
                textField.drawTextBox();
                this.typing = textField.isFocused();
                stringSetting.setString(textField.getText());
            }
            if (setting instanceof ModeSetting) {
                final ModeSetting modeSetting = (ModeSetting)setting;
                SettingComponents.tenacityFont16.drawString(setting.name, this.x + 5.0f, middleSettingY, -1);
                final float modeRectWidth = SettingComponents.tenacityFont14.getStringWidth(StringUtils.getLongestModeName(modeSetting.modes)) + 10.0f;
                final float modeSize = 10.0f;
                final float realY = settingY + settingHeight / 2.0f - modeSize / 2.0f;
                final boolean hovered2 = HoveringUtil.isHovering(this.x + this.rectWidth - (modeRectWidth + 5.0f), realY, modeRectWidth, modeSize, mouseX, mouseY);
                final Animation openAnimation = this.modeSettingMap.get(modeSetting);
                if (!openAnimation.isDone() || openAnimation.getDirection().equals(Direction.FORWARDS)) {
                    final Color dropdownColor = ColorUtil.darker(disabledColor, 0.8f);
                    Gui.drawRect2(this.x + this.rectWidth - (modeRectWidth + 5.0f), realY + modeSize, modeRectWidth, (modeSetting.modes.size() - 1) * modeSize * openAnimation.getOutput().floatValue(), dropdownColor.getRGB());
                    float seperation = 0.0f;
                    for (final String mode : modeSetting.modes) {
                        if (mode.equals(modeSetting.getMode())) {
                            continue;
                        }
                        final float modeY = realY + 3.5f + 6.0f * openAnimation.getOutput().floatValue() + SettingComponents.tenacityFont14.getMiddleOfBox(modeSize) + seperation;
                        final boolean hoveringMode = HoveringUtil.isHovering(this.x + this.rectWidth - (modeRectWidth + 5.0f), modeY - SettingComponents.tenacityFont14.getMiddleOfBox(modeSize), modeRectWidth, modeSize, mouseX, mouseY) && openAnimation.isDone();
                        if (hoveringMode && button == 0 && type == GuiEvents.CLICK) {
                            modeSetting.setCurrentMode(mode);
                            openAnimation.setDirection(Direction.BACKWARDS);
                            return;
                        }
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        Gui.drawRect2(this.x + this.rectWidth - (modeRectWidth + 5.0f), modeY - SettingComponents.tenacityFont14.getMiddleOfBox(modeSize), modeRectWidth, modeSize, ColorUtil.applyOpacity(hoveringMode ? accentColor : dropdownColor, openAnimation.getOutput().floatValue()).getRGB());
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        SettingComponents.tenacityFont14.drawString(mode, this.x + this.rectWidth - (modeRectWidth + 3.0f), modeY, ColorUtil.applyOpacity(-1, openAnimation.getOutput().floatValue()));
                        seperation += modeSize * openAnimation.getOutput().floatValue();
                    }
                }
                Gui.drawRect2(this.x + this.rectWidth - (modeRectWidth + 5.0f), realY, modeRectWidth, modeSize, disabledColor.getRGB());
                SettingComponents.tenacityFont14.drawString(modeSetting.getMode(), this.x + this.rectWidth - (modeRectWidth + 5.0f) + 2.0f, realY + SettingComponents.tenacityFont14.getMiddleOfBox(modeSize), -1);
                if (hovered2 && button == 1 && type == GuiEvents.CLICK) {
                    openAnimation.changeDirection();
                }
                RenderUtil.drawClickGuiArrow(this.x + this.rectWidth - 11.0f, realY + modeSize / 2.0f - 1.0f, 4.0f, openAnimation, -1);
                count += (2.0f + (modeSetting.modes.size() - 1) * modeSize) / settingHeight * openAnimation.getOutput().floatValue();
            }
            if (setting instanceof MultipleBoolSetting) {
                final MultipleBoolSetting multipleBoolSetting = (MultipleBoolSetting)setting;
                final Animation openingAnimation = this.multiBoolMap.get(multipleBoolSetting);
                SettingComponents.tenacityFont16.drawString(setting.name, this.x + 5.0f, middleSettingY, -1);
                final float width = 65.0f;
                final float boolRectSize = 10.0f;
                final float realY2 = settingY + settingHeight / 2.0f - boolRectSize / 2.0f;
                final Collection<BooleanSetting> booleanSettings = multipleBoolSetting.getBoolSettings();
                final int settingsCount = booleanSettings.size();
                final int enabledCount = (int)booleanSettings.stream().filter(BooleanSetting::isEnabled).count();
                if (!openingAnimation.isDone() || openingAnimation.getDirection().equals(Direction.FORWARDS)) {
                    final Color dropdownColor2 = ColorUtil.darker(disabledColor, 0.9f);
                    Gui.drawRect2(this.x + this.rectWidth - (width + 5.0f), realY2 + boolRectSize, width, (1.0f + settingsCount * boolRectSize) * openingAnimation.getOutput().floatValue(), dropdownColor2.getRGB());
                    float seperation2 = 0.0f;
                    for (final BooleanSetting booleanSetting2 : booleanSettings) {
                        final boolean enabled2 = booleanSetting2.isEnabled();
                        final float boolSettingY = realY2 + 4.0f + 7.0f * openingAnimation.getOutput().floatValue() + SettingComponents.tenacityFont14.getMiddleOfBox(boolRectSize) + seperation2;
                        final boolean hovered3 = HoveringUtil.isHovering(this.x + this.rectWidth - (width + 5.0f), boolSettingY - SettingComponents.tenacityFont14.getMiddleOfBox(boolRectSize), width, boolRectSize, mouseX, mouseY);
                        if (hovered3 && button == 0 && type == GuiEvents.CLICK) {
                            booleanSetting2.toggle();
                        }
                        final Color toggleColor = enabled2 ? accentColor : dropdownColor2;
                        final int hoverColor = hovered3 ? ColorUtil.darker(toggleColor, 0.8f).getRGB() : toggleColor.getRGB();
                        RenderUtil.resetColor();
                        Gui.drawRect2(this.x + this.rectWidth - (width + 5.0f), boolSettingY - SettingComponents.tenacityFont14.getMiddleOfBox(boolRectSize), width, boolRectSize, ColorUtil.applyOpacity(hoverColor, openingAnimation.getOutput().floatValue()));
                        RenderUtil.resetColor();
                        SettingComponents.tenacityFont14.drawString(booleanSetting2.name, this.x + this.rectWidth - (width + 2.0f), boolSettingY, ColorUtil.applyOpacity(-1, openingAnimation.getOutput().floatValue()));
                        seperation2 += boolRectSize * openingAnimation.getOutput().floatValue();
                    }
                }
                Gui.drawRect2(this.x + this.rectWidth - (width + 5.0f), realY2, width, boolRectSize, disabledColor.getRGB());
                SettingComponents.tenacityFont14.drawCenteredString(enabledCount + "/" + settingsCount + " enabled", this.x + this.rectWidth - (width + 5.0f) + width / 2.0f, realY2 + SettingComponents.tenacityFont14.getMiddleOfBox(boolRectSize), -1);
                final boolean hovering = HoveringUtil.isHovering(this.x + this.rectWidth - (width + 5.0f), realY2, width, boolRectSize, mouseX, mouseY);
                if (hovering && button == 1 && type == GuiEvents.CLICK) {
                    openingAnimation.changeDirection();
                }
                RenderUtil.drawClickGuiArrow(this.x + this.rectWidth - 11.0f, realY2 + boolRectSize / 2.0f - 1.0f, 4.0f, openingAnimation, -1);
                count += (2.0f + multipleBoolSetting.getBoolSettings().size() * boolRectSize) / settingHeight * openingAnimation.getOutput().floatValue();
            }
            ++count;
        }
        this.size = count * settingHeight;
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
}
