// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.animations.Direction;
import java.util.ArrayList;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.List;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class ColorComponent extends SettingComponent<ColorSetting>
{
    private final Animation hoverAnimation;
    private final Animation openAnimation;
    private final Animation rainbowAnimation;
    private final Pair<Animation, Animation> errorAnimations;
    public float realHeight;
    public float openedHeight;
    private boolean opened;
    private final TextField hexField;
    private boolean draggingPicker;
    private boolean draggingHue;
    private final NumberSetting speedSetting;
    private final NumberSetting saturationSetting;
    private List<NumberComponent> rainbowSettings;
    String hexLetters;
    
    public ColorComponent(final ColorSetting setting) {
        super(setting);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.openAnimation = new DecelerateAnimation(250, 1.0);
        this.rainbowAnimation = new DecelerateAnimation(250, 1.0);
        this.errorAnimations = (Pair<Animation, Animation>)Pair.of(new DecelerateAnimation(1000, 1.0), new DecelerateAnimation(250, 1.0));
        this.hexField = new TextField(FontUtil.tenacityFont16);
        this.speedSetting = new NumberSetting("Speed", 15.0, 40.0, 1.0, 1.0);
        this.saturationSetting = new NumberSetting("Saturation", 1.0, 1.0, 0.0, 0.05);
        this.hexLetters = "abcdef0123456789";
        if (setting.isRainbow()) {
            (this.rainbowSettings = new ArrayList<NumberComponent>()).add(new NumberComponent(this.speedSetting));
            this.rainbowSettings.add(new NumberComponent(this.saturationSetting));
            this.speedSetting.setValue(setting.getRainbow().getSpeed());
            this.saturationSetting.setValue(setting.getRainbow().getSaturation());
        }
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        char c = typedChar;
        if (!this.hexLetters.contains(Character.toString(typedChar))) {
            c = '§';
        }
        this.hexField.keyTyped(c, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final ColorSetting colorSetting = this.getSetting();
        this.openAnimation.setDirection(this.opened ? Direction.FORWARDS : Direction.BACKWARDS);
        ColorComponent.tenacityFont16.drawString(colorSetting.name, this.x + 5.0f, this.y + ColorComponent.tenacityFont16.getMiddleOfBox(this.realHeight), this.textColor);
        final float spacing = 4.0f;
        float colorHeight = 6.5f;
        float colorWidth = 30.0f;
        float colorX = this.x + this.width - (colorWidth + spacing);
        float colorY = this.y + this.realHeight / 2.0f - colorHeight / 2.0f;
        float colorRadius = 3.0f;
        final float openAnim = this.openAnimation.getOutput().floatValue();
        final float newColorY = this.y + this.realHeight - 1.0f;
        final float newColorHeight = 5.0f + ((this.openAnimation.finished(Direction.FORWARDS) || !this.openAnimation.isDone()) ? (5.0f * this.hoverAnimation.getOutput().floatValue()) : 0.0f);
        colorX = MathUtils.interpolateFloat(colorX, this.x + 6.0f, openAnim);
        colorY = MathUtils.interpolateFloat(colorY, newColorY, openAnim);
        colorWidth = MathUtils.interpolateFloat(colorWidth, this.width - 12.0f, openAnim);
        colorHeight = MathUtils.interpolateFloat(colorHeight, newColorHeight, openAnim);
        colorRadius = MathUtils.interpolateFloat(colorRadius, 2.0f, openAnim);
        final boolean hovered = HoveringUtil.isHovering(colorX - 4.0f, colorY - 4.0f, colorWidth + 8.0f, colorHeight + 8.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
        final Color actualColor = ColorUtil.applyOpacity(colorSetting.getColor(), this.alpha);
        RoundedUtil.drawRound(colorX, colorY, colorWidth, colorHeight, colorRadius, ColorUtil.interpolateColorC(actualColor, actualColor.darker(), this.hoverAnimation.getOutput().floatValue()));
        final String text = colorSetting.isRainbow() ? "Shift + Click for picker" : "Shift + Click for rainbow";
        ColorComponent.tenacityFont14.drawCenteredStringWithShadow(text, colorX + colorWidth / 2.0f, colorY + ColorComponent.tenacityFont14.getMiddleOfBox(colorHeight), ColorUtil.applyOpacity(-1, this.hoverAnimation.getOutput().floatValue() * (openAnim * openAnim)));
        this.rainbowAnimation.setDirection(colorSetting.isRainbow() ? Direction.FORWARDS : Direction.BACKWARDS);
        final float rainbowAnim = this.rainbowAnimation.getOutput().floatValue();
        float rainbowCount = 0.0f;
        if (this.opened || !this.openAnimation.isDone()) {
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
            final float gradientX = this.x + 6.0f;
            final float gradientY = newColorY + colorHeight + 4.0f;
            final float gradientWidth = this.width - 12.0f;
            final float gradientHeight = 10.0f + 55.0f * openAnim - 20.0f * rainbowAnim;
            final float radius = 2.0f;
            final float colorAlpha = this.alpha * openAnim;
            if (this.draggingHue) {
                colorSetting.setHue(Math.min(1.0f, Math.max(0.0f, (mouseX - gradientX) / gradientWidth)));
            }
            if (this.draggingPicker) {
                colorSetting.setBrightness(Math.min(1.0f, Math.max(0.0f, 1.0f - (mouseY - gradientY) / gradientHeight)));
                colorSetting.setSaturation(Math.min(1.0f, Math.max(0.0f, (mouseX - gradientX) / gradientWidth)));
            }
            final Color firstColor = ColorUtil.applyOpacity(Color.getHSBColor(hsb2[0], 1.0f, 1.0f), colorAlpha);
            RoundedUtil.drawRound(gradientX, gradientY, gradientWidth, gradientHeight, radius, ColorUtil.applyOpacity(firstColor, colorAlpha));
            final Color secondColor = Color.getHSBColor(hsb2[0], 0.0f, 1.0f);
            RoundedUtil.drawGradientHorizontal(gradientX, gradientY, gradientWidth, gradientHeight, radius + 0.5f, ColorUtil.applyOpacity(secondColor, colorAlpha), ColorUtil.applyOpacity(secondColor, 0.0f));
            final Color thirdColor = Color.getHSBColor(hsb2[0], 1.0f, 0.0f);
            RoundedUtil.drawGradientVertical(gradientX, gradientY, gradientWidth, gradientHeight, radius, ColorUtil.applyOpacity(thirdColor, 0.0f), ColorUtil.applyOpacity(thirdColor, colorAlpha));
            float pickerY = gradientY - 2.0f + gradientHeight * (1.0f - hsb2[2]);
            float pickerX = gradientX + (gradientWidth * hsb2[1] - 1.0f);
            pickerY = Math.max(Math.min(gradientY + gradientHeight - 2.0f, pickerY), gradientY - 2.0f);
            pickerX = Math.max(Math.min(gradientX + gradientWidth - 2.0f, pickerX), gradientX - 2.0f);
            Color whiteColor = ColorUtil.applyOpacity(Color.WHITE, colorAlpha);
            RenderUtil.color(whiteColor.getRGB());
            GLUtil.startBlend();
            RenderUtil.drawImage(new ResourceLocation("Tenacity/colorpicker2.png"), pickerX, pickerY, 4.0f, 4.0f);
            GLUtil.endBlend();
            final float hueY = gradientY + gradientHeight + 5.0f;
            final float hueHeight = 4.0f;
            RenderUtil.resetColor();
            ColorComponent.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/hue.png"));
            RoundedUtil.drawRoundTextured(gradientX, hueY, gradientWidth, hueHeight, 1.5f, colorAlpha);
            final float sliderSize = 6.5f;
            final float sliderX = gradientX + gradientWidth * hsb2[0] - sliderSize / 2.0f;
            RoundedUtil.drawRound(sliderX, hueY + (hueHeight / 2.0f - sliderSize / 2.0f), sliderSize, sliderSize, sliderSize / 2.0f - 0.5f, whiteColor);
            final float miniSize = 4.25f;
            final float movement = sliderSize / 2.0f - miniSize / 2.0f;
            RoundedUtil.drawRound(sliderX + movement, hueY + (hueHeight / 2.0f - miniSize / 2.0f), miniSize, miniSize, miniSize / 2.0f - 0.5f, firstColor);
            final Animation error2Anim = this.errorAnimations.getSecond();
            error2Anim.setDirection(colorSetting.isRainbow() ? Direction.BACKWARDS : error2Anim.getDirection());
            final float newYVal = hueY + hueHeight + 4.0f + 5.0f * error2Anim.getOutput().floatValue();
            final float heightLeft = this.height - (newYVal - this.y);
            if (!this.rainbowAnimation.finished(Direction.FORWARDS)) {
                final float rainbowInverse = 1.0f - rainbowAnim;
                whiteColor = ColorUtil.applyOpacity(whiteColor, rainbowInverse);
                ColorComponent.tenacityFont16.drawString("Hex", gradientX, newYVal + ColorComponent.tenacityFont16.getMiddleOfBox(heightLeft), whiteColor);
                this.hexField.setWidth(50.0f);
                this.hexField.setHeight(12.0f);
                this.hexField.setXPosition(gradientX + (gradientWidth - this.hexField.getWidth() - 5.0f));
                this.hexField.setYPosition(newYVal + heightLeft / 2.0f - this.hexField.getHeight() / 2.0f);
                final Color settingColor = ColorUtil.applyOpacity(this.settingRectColor.brighter(), openAnim * rainbowInverse);
                this.hexField.setOutline(settingColor.brighter().brighter());
                this.hexField.setFill(settingColor);
                this.hexField.setTextAlpha(colorAlpha * rainbowInverse);
                this.hexField.setMaxStringLength(6);
                if (!this.hexField.isFocused()) {
                    this.hexField.setText(colorSetting.getHexCode());
                    error2Anim.setDirection(Direction.BACKWARDS);
                }
                else {
                    try {
                        final Color textFieldColor = Color.decode("#" + this.hexField.getText());
                        colorSetting.setColor(textFieldColor);
                        error2Anim.setDirection(Direction.BACKWARDS);
                    }
                    catch (Exception e) {
                        final Animation blinkAnimation = this.errorAnimations.getFirst();
                        error2Anim.setDirection(Direction.FORWARDS);
                        if (blinkAnimation.isDone()) {
                            blinkAnimation.changeDirection();
                        }
                        ColorComponent.tenacityFont14.drawString("Invalid Hex Code", this.hexField.getXPosition() - 0.5f, newYVal - (ColorComponent.tenacityFont14.getHeight() - 0.5f), ColorUtil.applyOpacity(Color.RED, blinkAnimation.getOutput().floatValue()));
                    }
                }
                this.hexField.drawTextBox();
            }
            if (!this.rainbowAnimation.isDone() || this.rainbowAnimation.finished(Direction.FORWARDS)) {
                final Color textColor = ColorUtil.applyOpacity(Color.WHITE, colorAlpha * rainbowAnim);
                if (this.rainbowSettings != null) {
                    final float realHeightLeft = this.openedHeight - (newYVal - this.y);
                    final float componentHeight = realHeightLeft / 2.0f;
                    rainbowCount = 0.0f;
                    int count = 0;
                    for (final NumberComponent numberComponent : this.rainbowSettings) {
                        numberComponent.x = gradientX - 3.0f;
                        numberComponent.y = newYVal + count * componentHeight + componentHeight * rainbowCount;
                        numberComponent.width = gradientWidth + 6.0f;
                        numberComponent.height = componentHeight;
                        numberComponent.settingRectColor = ColorUtil.applyOpacity(this.settingRectColor, colorAlpha * rainbowAnim);
                        numberComponent.textColor = textColor;
                        numberComponent.panelLimitY = this.panelLimitY;
                        final Object o;
                        final Object o2;
                        numberComponent.clientColors = this.clientColors.apply((c1, c2) -> Pair.of(ColorUtil.applyOpacity(c1, (float)(o * o2)), ColorUtil.applyOpacity(c2, (float)(o * o2))));
                        numberComponent.alpha = openAnim * this.alpha * rainbowAnim;
                        numberComponent.drawScreen(mouseX, mouseY);
                        ++count;
                        rainbowCount += numberComponent.clickCountAdd;
                    }
                    if (colorSetting.isRainbow()) {
                        colorSetting.getRainbow().setSpeed(this.speedSetting.getValue().intValue());
                        colorSetting.getRainbow().setSaturation(this.saturationSetting.getValue().floatValue());
                    }
                }
            }
        }
        if (!this.typing) {
            this.typing = this.hexField.isFocused();
        }
        final Animation errorAnimation = this.errorAnimations.getSecond();
        this.openedHeight = this.realHeight * (1.0f + 6.75f * openAnim);
        this.countSize = 1.0f + 6.75f * openAnim + errorAnimation.getOutput().floatValue() * 0.25f + Math.max(0.0f, rainbowCount * 2.0f);
    }
    
    private String getHexCode(final ColorSetting colorSetting) {
        final Color color = colorSetting.getColor();
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final float spacing = 4.0f;
        float colorHeight = 6.5f;
        float colorWidth = 30.0f;
        float colorX = this.x + this.width - (colorWidth + spacing);
        float colorY = this.y + this.realHeight / 2.0f - colorHeight / 2.0f;
        final float newColorY = this.y + this.realHeight - 1.0f;
        final float openAnim = this.openAnimation.getOutput().floatValue();
        colorX = MathUtils.interpolateFloat(colorX, this.x + 6.0f, openAnim);
        colorY = MathUtils.interpolateFloat(colorY, newColorY, openAnim);
        colorWidth = MathUtils.interpolateFloat(colorWidth, this.width - 12.0f, openAnim);
        colorHeight = MathUtils.interpolateFloat(colorHeight, 5.0f, openAnim);
        final boolean hovered = this.isClickable(colorY + colorHeight) && HoveringUtil.isHovering(colorX - 4.0f, colorY - 4.0f, colorWidth + 8.0f, colorHeight + 8.0f, mouseX, mouseY);
        if (hovered && button == 1) {
            this.opened = !this.opened;
            this.hexField.mouseClicked(mouseX, mouseY, button);
        }
        if (this.opened) {
            if (hovered && button == 0 && Keyboard.isKeyDown(42)) {
                this.getSetting().setRainbow(!this.getSetting().isRainbow());
                if (this.getSetting().isRainbow()) {
                    (this.rainbowSettings = new ArrayList<NumberComponent>()).add(new NumberComponent(this.speedSetting));
                    this.rainbowSettings.add(new NumberComponent(this.saturationSetting));
                    this.getSetting().getRainbow().setSpeed(this.speedSetting.getValue().intValue());
                    this.getSetting().getRainbow().setSaturation(this.saturationSetting.getValue().floatValue());
                }
            }
            if (this.getSetting().isRainbow() && this.rainbowSettings != null) {
                for (final NumberComponent numberComponent : this.rainbowSettings) {
                    numberComponent.mouseClicked(mouseX, mouseY, button);
                }
                return;
            }
            final float gradientX = this.x + 6.0f;
            final float gradientY = newColorY + colorHeight + 4.0f;
            final float gradientWidth = this.width - 12.0f;
            final float gradientHeight = 10.0f + 55.0f * openAnim;
            final float radius = 2.0f;
            if (button == 0) {
                final float hueY = gradientY + gradientHeight + 5.0f;
                if (this.isClickable(hueY + 4.0f) && HoveringUtil.isHovering(gradientX, hueY, gradientWidth, 4.0f, mouseX, mouseY)) {
                    this.draggingHue = true;
                }
                if (this.isClickable(gradientY + gradientHeight) && HoveringUtil.isHovering(gradientX, gradientY, gradientWidth, gradientHeight, mouseX, mouseY)) {
                    this.draggingPicker = true;
                }
            }
            this.hexField.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.draggingHue = false;
        this.draggingPicker = false;
        if (this.getSetting().isRainbow() && this.rainbowSettings != null) {
            for (final NumberComponent numberComponent : this.rainbowSettings) {
                numberComponent.mouseReleased(mouseX, mouseY, state);
            }
        }
    }
}
