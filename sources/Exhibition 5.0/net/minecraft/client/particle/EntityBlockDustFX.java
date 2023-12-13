// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class EntityBlockDustFX extends EntityDiggingFX
{
    private static final String __OBFID = "CL_00000931";
    
    protected EntityBlockDustFX(final World worldIn, final double p_i46281_2_, final double p_i46281_4_, final double p_i46281_6_, final double p_i46281_8_, final double p_i46281_10_, final double p_i46281_12_, final IBlockState p_i46281_14_) {
        super(worldIn, p_i46281_2_, p_i46281_4_, p_i46281_6_, p_i46281_8_, p_i46281_10_, p_i46281_12_, p_i46281_14_);
        this.motionX = p_i46281_8_;
        this.motionY = p_i46281_10_;
        this.motionZ = p_i46281_12_;
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002576";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            final IBlockState var16 = Block.getStateById(p_178902_15_[0]);
            return (var16.getBlock().getRenderType() == -1) ? null : new EntityBlockDustFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, var16).func_174845_l();
        }
    }
}
