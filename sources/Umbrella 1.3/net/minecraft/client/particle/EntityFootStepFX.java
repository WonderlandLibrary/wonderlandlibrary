/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFootStepFX
extends EntityFX {
    private static final ResourceLocation field_110126_a = new ResourceLocation("textures/particle/footprint.png");
    private int footstepAge;
    private int footstepMaxAge;
    private TextureManager currentFootSteps;
    private static final String __OBFID = "CL_00000908";

    protected EntityFootStepFX(TextureManager p_i1210_1_, World worldIn, double p_i1210_3_, double p_i1210_5_, double p_i1210_7_) {
        super(worldIn, p_i1210_3_, p_i1210_5_, p_i1210_7_, 0.0, 0.0, 0.0);
        this.currentFootSteps = p_i1210_1_;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.footstepMaxAge = 200;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var10;
        float var9 = ((float)this.footstepAge + p_180434_3_) / (float)this.footstepMaxAge;
        if ((var10 = 2.0f - (var9 *= var9) * 2.0f) > 1.0f) {
            var10 = 1.0f;
        }
        GlStateManager.disableLighting();
        float var11 = 0.125f;
        float var12 = (float)(this.posX - interpPosX);
        float var13 = (float)(this.posY - interpPosY);
        float var14 = (float)(this.posZ - interpPosZ);
        float var15 = this.worldObj.getLightBrightness(new BlockPos(this));
        this.currentFootSteps.bindTexture(field_110126_a);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        p_180434_1_.startDrawingQuads();
        p_180434_1_.func_178960_a(var15, var15, var15, var10 *= 0.2f);
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

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002601";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}

