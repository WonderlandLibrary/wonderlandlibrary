/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.clickgui.panel.component.impl;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;

public final class NumberOptionComponent
extends Component {
    private boolean dragging = false;
    private int opacity = 120;
    private float animation = 0.0f;
    private float textHoverAnimate = 0.0f;
    private float currentValueAnimate = 0.0f;

    public NumberOptionComponent(Setting option, Panel panel, int x, int y, int width, int height) {
        super(panel, x, y, width, height);
        this.option = option;
    }

    @Override
    public void onDraw(int mouseX, int mouseY) {
        Panel parent = this.getPanel();
        int x = parent.getX() + this.getX();
        int y = parent.getY() + this.getY() + -1;
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        int height = this.getHeight();
        int width = this.getWidth();
        Setting option = this.option;
        double min = option.getMin();
        double max = option.getMax();
        if (this.dragging) {
            option.setValDouble(this.round((double)(mouseX - x) * (max - min) / (double)width + min, 0.01));
            if (Double.valueOf(option.getValDouble()) > max) {
                option.setValDouble(max);
            } else if (Double.valueOf(option.getValDouble()) < min) {
                option.setValDouble(min);
            }
        }
        double optionValue = this.round(option.getValDouble(), 0.01);
        String optionValueStr = String.valueOf(optionValue);
        int color = Color.WHITE.getRGB();
        double kak = (option.getValDouble() - option.getMin()) / (option.getMax() - option.getMin());
        this.currentValueAnimate = AnimationUtil.animation(this.currentValueAnimate, (float)kak, 1.0E-9f);
        double renderPerc = (double)(width - 2) / (max - min);
        double barWidth = renderPerc * optionValue - renderPerc * min;
        if (hovered) {
            if (this.opacity < 200) {
                this.opacity += 5;
            }
        } else if (this.opacity > 120) {
            this.opacity -= 5;
        }
        this.textHoverAnimate = AnimationUtil.animation(this.textHoverAnimate, hovered ? 2.3f : 2.0f, 1.0E-12f);
        this.animation = AnimationUtil.animation(this.animation, this.dragging ? y + height - 6 : y + height - 5, 1.0E-6f);
        RenderUtil.drawRect(x, y, x + width, y + height, parent.dragging ? 0x9000000 : ColorUtils.getColorWithOpacity(BACKGROUND, 255 - this.opacity).getRGB());
        RenderUtil.drawRect(x + 3, this.animation, x + (width - 3), y + height - 2, new Color(45, 44, 44).getRGB());
        RenderUtil.drawGradientSideways(x + 3, y + height - 5, (float)x + (float)width * this.currentValueAnimate, y + height - 2, parent.category.getColor(), parent.category.getColor2());
        Minecraft.getMinecraft().smallfontRenderer.drawStringWithShadow(option.getName(), (float)x + 2.0f, (float)y + (float)height / this.textHoverAnimate - 4.0f, color);
        Minecraft.getMinecraft().smallfontRenderer.drawStringWithShadow(optionValueStr, x + width - Minecraft.getMinecraft().smallfontRenderer.getStringWidth(optionValueStr) - 3, (float)y + (float)height / this.textHoverAnimate - 4.0f, color);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseOver(mouseX, mouseY)) {
            this.dragging = true;
        }
    }

    @Override
    public void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    private double round(double num, double increment) {
        double v = (double)Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

