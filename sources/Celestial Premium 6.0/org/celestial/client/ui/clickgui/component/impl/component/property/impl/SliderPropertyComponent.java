/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.component.property.impl;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.render.AnimationHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.Setting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.component.property.PropertyComponent;

public class SliderPropertyComponent
extends Component
implements PropertyComponent {
    public NumberSetting doubleProperty;
    private float currentValueAnimate = 0.0f;
    private boolean sliding;
    public static boolean sliding2 = true;
    private final TimerHelper descTimer = new TimerHelper();

    public SliderPropertyComponent(Component parent, NumberSetting property, float x, float y, float width, float height) {
        super(parent, property.getName(), x, y, width, height);
        this.doubleProperty = property;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        double min = this.doubleProperty.getMinimum();
        double max = this.doubleProperty.getMaximum();
        boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding && sliding2) {
            this.doubleProperty.setCurrentValue((float)MathematicHelper.round((double)((float)mouseX - x) * (max - min) / (double)width + min, this.doubleProperty.getIncrement()));
            if ((double)this.doubleProperty.getCurrentValue() > max) {
                this.doubleProperty.setCurrentValue((float)max);
            } else if ((double)this.doubleProperty.getCurrentValue() < min) {
                this.doubleProperty.setCurrentValue((float)min);
            }
        }
        float optionValue = (float)MathematicHelper.round((double)this.doubleProperty.getCurrentValue(), this.doubleProperty.getIncrement());
        float amountWidth = (float)(((double)this.doubleProperty.getCurrentValue() - min) / (max - min));
        String optionValueStr = String.valueOf(optionValue);
        if (this.doubleProperty.getName().equalsIgnoreCase("Rotation Speed") && this.doubleProperty.getDesc().startsWith("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c") && this.doubleProperty.getCurrentValue() >= 5.0f) {
            optionValueStr = optionValue + " (Max speed)";
        }
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        this.currentValueAnimate = AnimationHelper.animation2(this.currentValueAnimate, amountWidth, (float)((double)0.001f * Minecraft.frameTime * (double)0.1f));
        this.currentValueAnimate = MathematicHelper.clamp(this.currentValueAnimate, 0.001f, this.currentValueAnimate);
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        RectHelper.drawBorderedRect(x + 3.0f, y + height - 5.0f + -1.0f, x + (width - 3.0f), y + height - 2.0f, new Color(40, 39, 39).getRGB(), 2.0);
        RectHelper.drawGradientRect(x + 4.0f, y + height - 5.0f, x + width * this.currentValueAnimate - 4.0f, y + height - 3.0f, color, new Color(color).brighter().getRGB());
        if (ClickGui.glow.getCurrentValue()) {
            float widthValue = width * this.currentValueAnimate - 6.0f;
            if (widthValue <= 0.0f) {
                widthValue = 0.001f;
            }
            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)(x + 4.0f), (double)(y + height - 5.0f), (double)widthValue, 3.0, 15);
        }
        SliderPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(this.doubleProperty.getName(), x + 2.0f, y + height / 2.5f - 3.0f, -1);
        SliderPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(optionValueStr, x + width - (float)SliderPropertyComponent.mc.smallfontRenderer.getStringWidth(optionValueStr) - 5.0f, y + height / 2.5f - 3.0f, Color.GRAY.getRGB());
        if (hovered) {
            if (this.doubleProperty.getDesc() != null && this.descTimer.hasReached(250.0)) {
                RectHelper.drawBorder(x + 120.0f, y + height / 1.5f + 3.5f, x + 138.0f + (float)SliderPropertyComponent.mc.fontRenderer.getStringWidth(this.doubleProperty.getDesc()) - 5.0f, y + 3.5f, 0.5, new Color(30, 30, 30, 255).getRGB(), color, true);
                SliderPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.doubleProperty.getDesc(), x + 124.0f, y + height / 1.5f - 5.0f, -1);
            }
        } else {
            this.descTimer.reset();
        }
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!this.sliding && button == 0 && this.isHovered(mouseX, mouseY)) {
            this.sliding = true;
            sliding2 = true;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        this.sliding = false;
        sliding2 = false;
    }

    @Override
    public Setting getProperty() {
        return this.doubleProperty;
    }
}

