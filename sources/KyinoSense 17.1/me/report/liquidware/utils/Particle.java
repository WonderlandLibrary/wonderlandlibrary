/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockBush
 *  net.minecraft.block.BlockLiquid
 */
package me.report.liquidware.utils;

import me.report.liquidware.utils.Vec3;
import net.ccbluex.liquidbounce.utils.PlayerUtil;
import net.ccbluex.liquidbounce.utils.time.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;

public class Particle {
    private final TimerUtil removeTimer = new TimerUtil();
    public final Vec3 position;
    private final Vec3 delta;

    public Particle(Vec3 position) {
        this.position = position;
        this.delta = new Vec3((Math.random() * 0.5 - 0.25) * 0.01, Math.random() * 0.25 * 0.01, (Math.random() * 0.5 - 0.25) * 0.01);
        this.removeTimer.reset();
    }

    public Particle(Vec3 position, Vec3 velocity) {
        this.position = position;
        this.delta = new Vec3(velocity.xCoord * 0.01, velocity.yCoord * 0.01, velocity.zCoord * 0.01);
        this.removeTimer.reset();
    }

    public void update() {
        Block block3;
        Block block2;
        Block block1 = PlayerUtil.getBlock(this.position.xCoord, this.position.yCoord, this.position.zCoord + this.delta.zCoord);
        if (!(block1 instanceof BlockAir || block1 instanceof BlockBush || block1 instanceof BlockLiquid)) {
            this.delta.zCoord *= -0.8;
        }
        if (!((block2 = PlayerUtil.getBlock(this.position.xCoord, this.position.yCoord + this.delta.yCoord, this.position.zCoord)) instanceof BlockAir || block2 instanceof BlockBush || block2 instanceof BlockLiquid)) {
            this.delta.xCoord *= (double)0.999f;
            this.delta.zCoord *= (double)0.999f;
            this.delta.yCoord *= -0.6;
        }
        if (!((block3 = PlayerUtil.getBlock(this.position.xCoord + this.delta.xCoord, this.position.yCoord, this.position.zCoord)) instanceof BlockAir || block3 instanceof BlockBush || block3 instanceof BlockLiquid)) {
            this.delta.xCoord *= -0.8;
        }
        this.updateWithoutPhysics();
    }

    public void updateWithoutPhysics() {
        this.position.xCoord += this.delta.xCoord;
        this.position.yCoord += this.delta.yCoord;
        this.position.zCoord += this.delta.zCoord;
        this.delta.xCoord /= (double)0.999998f;
        this.delta.yCoord -= 1.5E-6;
        this.delta.zCoord /= (double)0.999998f;
    }
}

