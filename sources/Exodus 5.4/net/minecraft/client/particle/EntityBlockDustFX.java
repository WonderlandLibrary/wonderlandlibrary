/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public class EntityBlockDustFX
extends EntityDiggingFX {
    protected EntityBlockDustFX(World world, double d, double d2, double d3, double d4, double d5, double d6, IBlockState iBlockState) {
        super(world, d, d2, d3, d4, d5, d6, iBlockState);
        this.motionX = d4;
        this.motionY = d5;
        this.motionZ = d6;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            IBlockState iBlockState = Block.getStateById(nArray[0]);
            return iBlockState.getBlock().getRenderType() == -1 ? null : new EntityBlockDustFX(world, d, d2, d3, d4, d5, d6, iBlockState).func_174845_l();
        }
    }
}

