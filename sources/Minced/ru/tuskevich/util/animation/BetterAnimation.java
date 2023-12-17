// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animation;

import net.minecraft.client.Minecraft;
import ru.tuskevich.Minced;
import net.minecraft.util.math.MathHelper;

public class BetterAnimation
{
    private int prevTick;
    private int tick;
    private int maxTick;
    
    public BetterAnimation(final int maxTick) {
        this.maxTick = maxTick;
    }
    
    public BetterAnimation() {
        this(10);
    }
    
    public void update(final boolean update) {
        this.prevTick = this.tick;
        this.tick = MathHelper.clamp(this.tick + (update ? 1 : -1), 0, this.maxTick);
    }
    
    public float getAnimationf() {
        return (float)Minced.getInstance().dropAnimation((this.prevTick + (this.tick - this.prevTick) * Minecraft.getMinecraft().getRenderPartialTicks()) / this.maxTick);
    }
    
    public double getAnimationd() {
        return Minced.getInstance().dropAnimation((this.prevTick + (this.tick - this.prevTick) * Minecraft.getMinecraft().getRenderPartialTicks()) / this.maxTick);
    }
}
