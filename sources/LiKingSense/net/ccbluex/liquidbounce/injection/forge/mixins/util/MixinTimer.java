/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Timer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.util;

import net.ccbluex.liquidbounce.injection.implementations.IMixinTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Timer.class})
public class MixinTimer
implements IMixinTimer {
    @Shadow
    public float field_194148_c;
    @Shadow
    public float field_194147_b;
    @Shadow
    public int field_74280_b;
    private float timerSpeed = 1.0f;
    @Shadow
    private long field_74277_g;
    @Shadow
    private float field_194149_e;

    @Overwrite
    public void func_74275_a() {
        long i = Minecraft.func_71386_F();
        this.field_194148_c = (float)(i - this.field_74277_g) / this.field_194149_e * this.timerSpeed;
        this.field_74277_g = i;
        this.field_194147_b += this.field_194148_c;
        this.field_74280_b = (int)this.field_194147_b;
        this.field_194147_b -= (float)this.field_74280_b;
    }

    @Override
    public float getTimerSpeed() {
        return this.timerSpeed;
    }

    @Override
    public void setTimerSpeed(float timerSpeed) {
        this.timerSpeed = timerSpeed;
    }
}

