// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.slider;

import java.math.BigDecimal;
import vip.Resolute.util.font.FontUtil;
import org.lwjgl.opengl.GL11;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public abstract class SliderComponent extends ButtonComponent implements PredicateComponent
{
    private boolean sliding;
    
    public SliderComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    @Override
    public void drawComponent(final ScaledResolution resolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final double min = this.getMin();
        final double max = this.getMax();
        final double dValue = this.getValue();
        final double value = (int)dValue;
        final boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding) {
            if (mouseX >= x - 0.5f && mouseY >= y - 0.5f && mouseX <= x + width + 0.5f && mouseY <= y + height + 0.5f) {
                this.setValue(MathHelper.clamp_double(this.roundToIncrement((mouseX - x) * (max - min) / (width - 1.0f) + min), min, max));
            }
            else {
                this.sliding = false;
            }
        }
        final double sliderPercentage = (this.getValue() - this.getMin()) / (this.getMax() - this.getMin());
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(855309));
        RenderUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, false, SkeetUI.getColor(hovered ? RenderUtils.darker(4802889, 1.4f) : 4802889), SkeetUI.getColor(hovered ? RenderUtils.darker(3158064, 1.4f) : 3158064));
        RenderUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width * sliderPercentage - 0.5, y + height - 0.5f, false, SkeetUI.getColor(), RenderUtils.darker(SkeetUI.getColor(), 0.8f));
        if (SkeetUI.shouldRenderText()) {
            final float stringWidth = (float)SkeetUI.GROUP_BOX_HEADER_RENDERER.getStringWidth(String.valueOf(this.getValue()));
            GL11.glTranslatef(0.0f, 0.0f, 1.0f);
            if (SkeetUI.getAlpha() > 120.0) {
                FontUtil.tahomaVerySmall.drawStringWithShadow(String.valueOf(this.getValue()), x + width * (float)sliderPercentage - stringWidth / 2.0f, y + height / 2.0f + 2.0f, SkeetUI.getColor(14474460));
            }
            else {
                SkeetUI.GROUP_BOX_HEADER_RENDERER.drawString(String.valueOf(this.getValue()), x + width * (float)sliderPercentage - stringWidth / 2.0f, y + height / 2.0f, SkeetUI.getColor(16777215));
            }
            GL11.glTranslatef(0.0f, 0.0f, -1.0f);
        }
    }
    
    @Override
    public void onPress(final int mouseButton) {
        if (!this.sliding && mouseButton == 0) {
            this.sliding = true;
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        this.sliding = false;
    }
    
    private double roundToIncrement(final double value) {
        final double inc = this.getIncrement();
        final double halfOfInc = inc / 2.0;
        final double floored = StrictMath.floor(value / inc) * inc;
        return (value >= floored + halfOfInc) ? new BigDecimal(StrictMath.ceil(value / inc) * inc).setScale(2, 4).doubleValue() : new BigDecimal(floored).setScale(2, 4).doubleValue();
    }
    
    public abstract double getValue();
    
    public abstract void setValue(final double p0);
    
    public abstract double getMin();
    
    public abstract double getMax();
    
    public abstract double getIncrement();
}
