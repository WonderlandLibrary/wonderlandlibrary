/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.LazyLoadBase
 */
package net.dev.important.injection.forge.mixins.bugfixes.network;

import net.minecraft.util.LazyLoadBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={LazyLoadBase.class})
public abstract class LazyLoadBaseMixin_RaceCondition<T> {
    @Shadow
    private boolean field_179282_b;
    @Shadow
    private T field_179283_a;

    @Shadow
    protected abstract T func_179280_b();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Overwrite
    public T func_179281_c() {
        if (!this.field_179282_b) {
            LazyLoadBaseMixin_RaceCondition lazyLoadBaseMixin_RaceCondition = this;
            synchronized (lazyLoadBaseMixin_RaceCondition) {
                if (!this.field_179282_b) {
                    this.field_179283_a = this.func_179280_b();
                    this.field_179282_b = true;
                }
            }
        }
        return this.field_179283_a;
    }
}

