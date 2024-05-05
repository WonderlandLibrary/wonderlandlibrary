package com.initial.modules.impl.visual;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import net.minecraft.client.gui.*;
import com.initial.utils.astolfo.*;
import com.initial.utils.movement.*;
import com.initial.utils.render.*;
import java.awt.*;

public class Crosshair extends Module
{
    public BooleanSet dynamic;
    public DoubleSet gap;
    public DoubleSet width;
    public DoubleSet length;
    public DoubleSet dynamicgap;
    public BooleanSet rainbow;
    public BooleanSet staticRainbow;
    
    public Crosshair() {
        super("Crosshair", 0, Category.VISUAL);
        this.dynamic = new BooleanSet("Dynamic", true);
        this.gap = new DoubleSet("Gap", 2.0, 0.5, 10.0, 1.0);
        this.width = new DoubleSet("Width", 0.75, 0.75, 10.0, 1.0);
        this.length = new DoubleSet("Length", 5.0, 0.5, 100.0, 1.0);
        this.dynamicgap = new DoubleSet("DynamicGap", 2.0, 0.5, 10.0, 1.0);
        this.rainbow = new BooleanSet("Rainbow", true);
        this.staticRainbow = new BooleanSet("Static Rainbow", true);
        this.addSettings(this.dynamic, this.gap, this.width, this.length, this.dynamicgap, this.rainbow, this.staticRainbow);
    }
    
    @EventTarget
    public void onCrosshair(final EventCrosshair event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    public void onRender2D(final Event2D event) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int count = 0;
        final int color = this.staticRainbow.isEnabled() ? this.color(2, 100) : (this.rainbow.isEnabled() ? AstolfoUtils.rainbow(count * -100, 1.0f, 0.47f) : -1);
        final double middlex = sr.getScaledWidth() / 2;
        final double middley = sr.getScaledHeight() / 2;
        RenderUtil.drawBordered(middlex - this.width.getValue(), middley - (this.gap.getValue() + this.length.getValue()) - ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), middlex + this.width.getValue(), middley - this.gap.getValue() - ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex - this.width.getValue(), middley + this.gap.getValue() + ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), middlex + this.width.getValue(), middley + (this.gap.getValue() + this.length.getValue()) + ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex - (this.gap.getValue() + this.length.getValue()) - ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), middley - this.width.getValue(), middlex - this.gap.getValue() - ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), middley + this.width.getValue(), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex + this.gap.getValue() + ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), middley - this.width.getValue(), middlex + (this.gap.getValue() + this.length.getValue()) + ((MovementUtils.isMoving() && this.dynamic.isEnabled()) ? this.dynamicgap.getValue() : 0.0), middley + this.width.getValue(), 0.5, color, -16777216);
    }
    
    public int color(final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(new Color(255, 255, 255, 255).getRed(), new Color(255, 255, 255, 255).getGreen(), new Color(255, 255, 255, 255).getBlue(), hsb);
        float brightness = Math.abs((this.getOffset() + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.4f + 0.4f * brightness;
        hsb[2] = brightness % 1.0f;
        final Color clr = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), new Color(255, 255, 255, 255).getAlpha()).getRGB();
    }
    
    private float getOffset() {
        return System.currentTimeMillis() % 2000L / 1000.0f;
    }
}
