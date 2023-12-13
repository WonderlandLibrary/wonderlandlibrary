// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class EntityPickupFX extends EntityFX
{
    private Entity field_174840_a;
    private Entity field_174843_ax;
    private int age;
    private int maxAge;
    private float field_174841_aA;
    private RenderManager field_174842_aB;
    private static final String __OBFID = "CL_00000930";
    
    public EntityPickupFX(final World worldIn, final Entity p_i1233_2_, final Entity p_i1233_3_, final float p_i1233_4_) {
        super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
        this.field_174842_aB = Minecraft.getMinecraft().getRenderManager();
        this.field_174840_a = p_i1233_2_;
        this.field_174843_ax = p_i1233_3_;
        this.maxAge = 3;
        this.field_174841_aA = p_i1233_4_;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.age + p_180434_3_) / this.maxAge;
        var9 *= var9;
        final double var10 = this.field_174840_a.posX;
        final double var11 = this.field_174840_a.posY;
        final double var12 = this.field_174840_a.posZ;
        final double var13 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.posX - this.field_174843_ax.lastTickPosX) * p_180434_3_;
        final double var14 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.posY - this.field_174843_ax.lastTickPosY) * p_180434_3_ + this.field_174841_aA;
        final double var15 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.posZ - this.field_174843_ax.lastTickPosZ) * p_180434_3_;
        double var16 = var10 + (var13 - var10) * var9;
        double var17 = var11 + (var14 - var11) * var9;
        double var18 = var12 + (var15 - var12) * var9;
        final int var19 = this.getBrightnessForRender(p_180434_3_);
        final int var20 = var19 % 65536;
        final int var21 = var19 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var20 / 1.0f, var21 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        var16 -= EntityPickupFX.interpPosX;
        var17 -= EntityPickupFX.interpPosY;
        var18 -= EntityPickupFX.interpPosZ;
        this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (float)var16, (float)var17, (float)var18, this.field_174840_a.rotationYaw, p_180434_3_);
    }
    
    @Override
    public void onUpdate() {
        ++this.age;
        if (this.age == this.maxAge) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
}
