// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class ParticleBlockDust extends ParticleDigging
{
    protected ParticleBlockDust(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Nullable
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final IBlockState iblockstate = Block.getStateById(p_178902_15_[0]);
            return (iblockstate.getRenderType() == EnumBlockRenderType.INVISIBLE) ? null : new ParticleBlockDust(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, iblockstate).init();
        }
    }
}
