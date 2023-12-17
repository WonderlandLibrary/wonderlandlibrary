// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.tuskevich.Minced;

public class Animation
{
    private final double animationSpeed;
    private double animationState;
    private double prevAnimationState;
    
    public Animation() {
        this(0.0);
    }
    
    public Animation(final double animationSpeed) {
        this.animationSpeed = Minced.getInstance().animationSpeed + animationSpeed;
    }
    
    public void update(final boolean add) {
        this.prevAnimationState = this.animationState;
        this.animationState = MathHelper.clamp(this.animationState + (add ? this.animationSpeed : (-this.animationSpeed)), 0.0, 1.0);
    }
    
    public void set(final double animation) {
        this.animationState = animation;
        this.prevAnimationState = animation;
    }
    
    public double get() {
        return Minced.getInstance().createAnimation(this.prevAnimationState + (this.animationState - this.prevAnimationState) * Minecraft.getMinecraft().getRenderPartialTicks());
    }
    
    public double getDrop() {
        return Minced.getInstance().dropAnimation(this.prevAnimationState + (this.animationState - this.prevAnimationState) * Minecraft.getMinecraft().getRenderPartialTicks());
    }
    
    public void reset() {
        this.prevAnimationState = 0.0;
        this.animationState = 0.0;
    }
}
