// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

public class PotionHealth extends Potion
{
    public PotionHealth(final boolean isBadEffectIn, final int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }
    
    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public boolean isReady(final int duration, final int amplifier) {
        return duration >= 1;
    }
}
