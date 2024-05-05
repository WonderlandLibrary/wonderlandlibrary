package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public static class EntityBlockDustFX.Factory
implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
        IBlockState iblockstate = Block.getStateById((int)p_178902_15_[0]);
        return iblockstate.getBlock().getRenderType() == -1 ? null : new EntityBlockDustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, iblockstate).func_174845_l();
    }
}
