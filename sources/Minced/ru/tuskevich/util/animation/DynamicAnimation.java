// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.tuskevich.Minced;

public class DynamicAnimation
{
    private final double speed;
    private double startValue;
    private double targetValue;
    private double outValue;
    private double step;
    private double prevStep;
    private double delta;
    
    public DynamicAnimation(final double speed) {
        this.speed = Minced.getInstance().animationSpeed + speed;
    }
    
    public DynamicAnimation() {
        this(0.0);
    }
    
    public void update() {
        this.prevStep = this.step;
        this.step = MathHelper.clamp(this.step + this.speed, 0.0, 1.0);
        this.outValue = this.startValue + this.delta * Minced.getInstance().createAnimation(this.step);
    }
    
    public double getValue() {
        return this.startValue + this.delta * Minced.getInstance().createAnimation(this.prevStep + (this.step - this.prevStep) * Minecraft.getMinecraft().getRenderPartialTicks());
    }
    
    public void setValue(final double value) {
        if (value == this.targetValue) {
            return;
        }
        this.targetValue = value;
        this.startValue = this.outValue;
        this.prevStep = 0.0;
        this.step = 0.0;
        this.delta = this.targetValue - this.startValue;
    }
    
    public void forceSetValue(final double value) {
        this.targetValue = value;
        this.startValue = value;
        this.outValue = value;
        this.prevStep = 1.0;
        this.step = 1.0;
    }
}
