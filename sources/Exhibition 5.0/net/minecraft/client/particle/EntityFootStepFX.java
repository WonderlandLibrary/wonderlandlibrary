// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class EntityFootStepFX extends EntityFX
{
    private static final ResourceLocation field_110126_a;
    private int footstepAge;
    private int footstepMaxAge;
    private TextureManager currentFootSteps;
    private static final String __OBFID = "CL_00000908";
    
    protected EntityFootStepFX(final TextureManager p_i1210_1_, final World worldIn, final double p_i1210_3_, final double p_i1210_5_, final double p_i1210_7_) {
        super(worldIn, p_i1210_3_, p_i1210_5_, p_i1210_7_, 0.0, 0.0, 0.0);
        this.currentFootSteps = p_i1210_1_;
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        this.footstepMaxAge = 200;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.footstepAge + p_180434_3_) / this.footstepMaxAge;
        var9 *= var9;
        float var10 = 2.0f - var9 * 2.0f;
        if (var10 > 1.0f) {
            var10 = 1.0f;
        }
        var10 *= 0.2f;
        GlStateManager.disableLighting();
        final float var11 = 0.125f;
        final float var12 = (float)(this.posX - EntityFootStepFX.interpPosX);
        final float var13 = (float)(this.posY - EntityFootStepFX.interpPosY);
        final float var14 = (float)(this.posZ - EntityFootStepFX.interpPosZ);
        final float var15 = this.worldObj.getLightBrightness(new BlockPos(this));
        this.currentFootSteps.bindTexture(EntityFootStepFX.field_110126_a);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        p_180434_1_.startDrawingQuads();
        p_180434_1_.setColorRGBA(var15, var15, var15, var10);
        p_180434_1_.addVertexWithUV(var12 - var11, var13, var14 + var11, 0.0, 1.0);
        p_180434_1_.addVertexWithUV(var12 + var11, var13, var14 + var11, 1.0, 1.0);
        p_180434_1_.addVertexWithUV(var12 + var11, var13, var14 - var11, 1.0, 0.0);
        p_180434_1_.addVertexWithUV(var12 - var11, var13, var14 - var11, 0.0, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }
    
    @Override
    public void onUpdate() {
        ++this.footstepAge;
        if (this.footstepAge == this.footstepMaxAge) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
    
    static {
        field_110126_a = new ResourceLocation("textures/particle/footprint.png");
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002601";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}
