/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockBush
 *  net.minecraft.block.BlockLiquid
 */
package net.ccbluex.liquidbounce.utils.particles;

import net.ccbluex.liquidbounce.utils.particles.ParticleTimer;
import net.ccbluex.liquidbounce.utils.particles.PlayerParticles;
import net.ccbluex.liquidbounce.utils.particles.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;

public class Particle {
    private final Vec3 delta;
    public final Vec3 position;
    private final ParticleTimer removeTimer = new ParticleTimer();

    public void updateWithoutPhysics() {
        this.position.xCoord += this.delta.xCoord;
        this.position.yCoord += this.delta.yCoord;
        this.position.zCoord += this.delta.zCoord;
        this.delta.xCoord /= (double)0.999997f;
        this.delta.yCoord -= 1.7E-6;
        this.delta.zCoord /= (double)0.999997f;
    }

    public Particle(Vec3 vec3, Vec3 vec32) {
        this.position = vec3;
        this.delta = new Vec3(vec32.xCoord * 0.01, vec32.yCoord * 0.01, vec32.zCoord * 0.01);
        this.removeTimer.reset();
    }

    public Particle(Vec3 vec3) {
        this.position = vec3;
        this.delta = new Vec3((Math.random() * 2.5 - 1.25) * 0.01, (Math.random() * 0.5 - 0.2) * 0.01, (Math.random() * 2.5 - 1.25) * 0.01);
        this.removeTimer.reset();
    }

    public void update() {
        Block block;
        Block block2;
        Block block3 = PlayerParticles.getBlock(this.position.xCoord, this.position.yCoord, this.position.zCoord + this.delta.zCoord);
        if (!(block3 instanceof BlockAir || block3 instanceof BlockBush || block3 instanceof BlockLiquid)) {
            this.delta.zCoord *= -0.8;
        }
        if (!((block2 = PlayerParticles.getBlock(this.position.xCoord, this.position.yCoord + this.delta.yCoord, this.position.zCoord)) instanceof BlockAir || block2 instanceof BlockBush || block2 instanceof BlockLiquid)) {
            this.delta.xCoord *= (double)0.999f;
            this.delta.zCoord *= (double)0.999f;
            this.delta.yCoord *= -0.6;
        }
        if (!((block = PlayerParticles.getBlock(this.position.xCoord + this.delta.xCoord, this.position.yCoord, this.position.zCoord)) instanceof BlockAir || block instanceof BlockBush || block instanceof BlockLiquid)) {
            this.delta.xCoord *= -0.8;
        }
        this.updateWithoutPhysics();
    }
}

