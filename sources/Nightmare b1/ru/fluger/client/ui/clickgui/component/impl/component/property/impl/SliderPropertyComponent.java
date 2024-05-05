// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import ru.fluger.client.settings.Setting;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.helpers.render.AnimationHelper;
import java.awt.Color;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import ru.fluger.client.ui.clickgui.component.Component;

public class SliderPropertyComponent extends Component implements PropertyComponent
{
    public NumberSetting doubleProperty;
    private float currentValueAnimate;
    private boolean sliding;
    public static boolean sliding2;
    private final TimerHelper descTimer;
    
    public SliderPropertyComponent(final Component parent, final NumberSetting property, final float x, final float y, final float width, final float height) {
        super(parent, property.getName(), x, y, width, height);
        this.currentValueAnimate = 0.0f;
        this.descTimer = new TimerHelper();
        this.doubleProperty = property;
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        final float x = (float)this.getX();
        final float y = (float)this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final double min = this.doubleProperty.getMinimum();
        final double max = this.doubleProperty.getMaximum();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding && SliderPropertyComponent.sliding2) {
            this.doubleProperty.setCurrentValue((float)MathematicHelper.round((mouseX - x) * (max - min) / width + min, this.doubleProperty.getIncrement()));
            if (this.doubleProperty.getCurrentValue() > max) {
                this.doubleProperty.setCurrentValue((float)max);
            }
            else if (this.doubleProperty.getCurrentValue() < min) {
                this.doubleProperty.setCurrentValue((float)min);
            }
        }
        final float optionValue = (float)MathematicHelper.round(this.doubleProperty.getCurrentValue(), this.doubleProperty.getIncrement());
        final float amountWidth = (float)((this.doubleProperty.getCurrentValue() - min) / (max - min));
        String optionValueStr = String.valueOf(optionValue);
        if (this.doubleProperty.getName().equalsIgnoreCase("Rotation Speed") && this.doubleProperty.getDesc().startsWith("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c") && this.doubleProperty.getCurrentValue() >= 5.0f) {
            optionValueStr = optionValue + " (Max speed)";
        }
        final Color onecolor = new Color(ClickGui.color.getColor());
        final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        final int color = c.getRGB();
        this.currentValueAnimate = AnimationHelper.animation2(this.currentValueAnimate, amountWidth, (float)(0.0010000000474974513 * bib.frameTime * 0.10000000149011612));
        this.currentValueAnimate = MathematicHelper.clamp(this.currentValueAnimate, 0.001f, this.currentValueAnimate);
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker().darker(), 120).getRGB());
        RectHelper.drawBorderedRect(x + 3.0f, y + height - 5.0f - 1.0f, x + (width - 3.0f), y + height - 2.0f, new Color(40, 39, 39).getRGB(), 2.0);
        RectHelper.drawGradientRect(x + 4.0f, y + height - 5.0f, x + width * this.currentValueAnimate - 4.0f, y + height - 3.0f, color, new Color(color).brighter().getRGB());
        if (ClickGui.glow.getCurrentValue()) {
            float widthValue = width * this.currentValueAnimate - 6.0f;
            if (widthValue <= 0.0f) {
                widthValue = 0.001f;
            }
            RenderHelper.renderBlurredShadow(new Color(color).brighter(), x + 4.0f, y + height - 5.0f, widthValue, 3.0, 15);
        }
        SliderPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(this.doubleProperty.getName(), x + 2.0f, y + height / 2.5f - 3.0f, -1);
        SliderPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(optionValueStr, x + width - SliderPropertyComponent.mc.smallfontRenderer.getStringWidth(optionValueStr) - 5.0f, y + height / 2.5f - 3.0f, Color.GRAY.getRGB());
        if (hovered) {
            if (this.doubleProperty.getDesc() != null && this.descTimer.hasReached(250.0)) {
                RenderHelper.drawBlurredShadow(x + width + 20.0f, y + height / 1.5f - 5.0f, (float)(5 + SliderPropertyComponent.mc.fontRenderer.getStringWidth(this.doubleProperty.getDesc())), 10.0f, 20, new Color(0, 0, 0, 180));
                RectHelper.drawSmoothRect(x + width + 20.0f, y + height / 1.5f + 4.5f, x + width + 25.0f + SliderPropertyComponent.mc.fontRenderer.getStringWidth(this.doubleProperty.getDesc()), y + 6.5f, new Color(0, 0, 0, 80).getRGB());
                SliderPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.doubleProperty.getDesc(), x + width + 22.0f, y + height / 1.35f - 5.0f, -1);
            }
        }
        else {
            this.descTimer.reset();
        }
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (!this.sliding && button == 0 && this.isHovered(mouseX, mouseY)) {
            this.sliding = true;
            SliderPropertyComponent.sliding2 = true;
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        this.sliding = false;
        SliderPropertyComponent.sliding2 = false;
    }
    
    @Override
    public Setting getProperty() {
        return this.doubleProperty;
    }
    
    static {
        SliderPropertyComponent.sliding2 = true;
    }
}
