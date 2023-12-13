// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityHugeExplodeFX extends EntityFX
{
    private int timeSinceStart;
    private int maximumTime;
    private static final String __OBFID = "CL_00000911";
    
    protected EntityHugeExplodeFX(final World worldIn, final double p_i1214_2_, final double p_i1214_4_, final double p_i1214_6_, final double p_i1214_8_, final double p_i1214_10_, final double p_i1214_12_) {
        super(worldIn, p_i1214_2_, p_i1214_4_, p_i1214_6_, 0.0, 0.0, 0.0);
        this.maximumTime = 8;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
    }
    
    @Override
    public void onUpdate() {
        for (int var1 = 0; var1 < 6; ++var1) {
            final double var2 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            final double var3 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            final double var4 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, var2, var3, var4, this.timeSinceStart / this.maximumTime, 0.0, 0.0, new int[0]);
        }
        ++this.timeSinceStart;
        if (this.timeSinceStart == this.maximumTime) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002597";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityHugeExplodeFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
