// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntitySplashFX extends EntityRainFX
{
    private static final String __OBFID = "CL_00000927";
    
    protected EntitySplashFX(final World worldIn, final double p_i1230_2_, final double p_i1230_4_, final double p_i1230_6_, final double p_i1230_8_, final double p_i1230_10_, final double p_i1230_12_) {
        super(worldIn, p_i1230_2_, p_i1230_4_, p_i1230_6_);
        this.particleGravity = 0.04f;
        this.nextTextureIndexX();
        if (p_i1230_10_ == 0.0 && (p_i1230_8_ != 0.0 || p_i1230_12_ != 0.0)) {
            this.motionX = p_i1230_8_;
            this.motionY = p_i1230_10_ + 0.1;
            this.motionZ = p_i1230_12_;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002580";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntitySplashFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
