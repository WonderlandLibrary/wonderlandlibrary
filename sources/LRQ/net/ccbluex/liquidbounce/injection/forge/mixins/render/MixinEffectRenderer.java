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
    private final Queue<ParticleEmitter> field_178933_d = Queues.newArrayDeque();
    @Shadow
    @Final
    private Queue<Particle> field_187241_h;
    @Shadow
    @Final
    private ArrayDeque<Particle>[][] field_78876_b;

    @Shadow
    protected abstract void func_178922_a(int var1);

    @Overwrite
    public void func_78868_a() {
        try {
            for (int i = 0; i < 4; ++i) {
                this.func_178922_a(i);
            }
            if (!this.field_178933_d.isEmpty()) {
                ArrayList list = Lists.newArrayList();
                for (ParticleEmitter particleemitter : this.field_178933_d) {
                    particleemitter.func_189213_a();
                    if (particleemitter.func_187113_k()) continue;
                    list.add(particleemitter);
                }
                this.field_178933_d.removeAll(list);
            }
            if (!this.field_187241_h.isEmpty()) {
                Particle particle = this.field_187241_h.poll();
                while (particle != null) {
                    int k;
                    int j = particle.func_70537_b();
                    int n = k = particle.func_187111_c() ? 0 : 1;
                    if (this.field_78876_b[j][k].size() >= 16384) {
                        this.field_78876_b[j][k].removeFirst();
                    }
                    this.field_78876_b[j][k].add(particle);
                    particle = this.field_187241_h.poll();
                }
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }
}

