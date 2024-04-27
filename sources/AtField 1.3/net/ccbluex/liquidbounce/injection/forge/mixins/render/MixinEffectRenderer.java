/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Queues
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleEmitter
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Queue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEmitter;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={ParticleManager.class})
public abstract class MixinEffectRenderer {
    @Shadow
    @Final
    private ArrayDeque[][] field_78876_b;
    @Shadow
    @Final
    private Queue field_187241_h;
    @Shadow
    @Final
    private final Queue field_178933_d = Queues.newArrayDeque();

    @Overwrite
    public void func_78868_a() {
        try {
            for (int i = 0; i < 4; ++i) {
                this.func_178922_a(i);
            }
            if (!this.field_178933_d.isEmpty()) {
                ArrayList arrayList = Lists.newArrayList();
                for (ParticleEmitter particleEmitter : this.field_178933_d) {
                    particleEmitter.func_189213_a();
                    if (particleEmitter.func_187113_k()) continue;
                    arrayList.add(particleEmitter);
                }
                this.field_178933_d.removeAll(arrayList);
            }
            if (!this.field_187241_h.isEmpty()) {
                Particle particle = (Particle)this.field_187241_h.poll();
                while (particle != null) {
                    int n;
                    int n2 = particle.func_70537_b();
                    int n3 = n = particle.func_187111_c() ? 0 : 1;
                    if (this.field_78876_b[n2][n].size() >= 16384) {
                        this.field_78876_b[n2][n].removeFirst();
                    }
                    this.field_78876_b[n2][n].add(particle);
                    particle = (Particle)this.field_187241_h.poll();
                }
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    @Shadow
    protected abstract void func_178922_a(int var1);
}

