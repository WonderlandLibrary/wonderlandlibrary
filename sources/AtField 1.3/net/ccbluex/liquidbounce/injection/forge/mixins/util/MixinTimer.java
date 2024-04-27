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
    private float field_194149_e;
    @Shadow
    private long field_74277_g;
    private float timerSpeed = 1.0f;
    @Shadow
    public int field_74280_b;
    @Shadow
    public float field_194147_b;

    @Overwrite
    public void func_74275_a() {
        long l = Minecraft.func_71386_F();
        this.field_194148_c = (float)(l - this.field_74277_g) / this.field_194149_e * this.timerSpeed;
        this.field_74277_g = l;
        this.field_194147_b += this.field_194148_c;
        this.field_74280_b = (int)this.field_194147_b;
        this.field_194147_b -= (float)this.field_74280_b;
    }

    @Override
    public void setTimerSpeed(float f) {
        this.timerSpeed = f;
    }

    @Override
    public float getTimerSpeed() {
        return this.timerSpeed;
    }
}

