/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.EffectRenderer
 *  net.minecraft.client.particle.EntityParticleEmitter
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EffectRenderer.class})
public abstract class MixinEffectRenderer {
    @Shadow
    private List<EntityParticleEmitter> field_178933_d;

    @Shadow
    protected abstract void func_178922_a(int var1);

    @Overwrite
    public void func_78868_a() {
        try {
            for (int i = 0; i < 4; ++i) {
                this.func_178922_a(i);
            }
            Iterator<EntityParticleEmitter> it = this.field_178933_d.iterator();
            while (it.hasNext()) {
                EntityParticleEmitter entityParticleEmitter = it.next();
                entityParticleEmitter.func_70071_h_();
                if (!entityParticleEmitter.field_70128_L) continue;
                it.remove();
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }
}

