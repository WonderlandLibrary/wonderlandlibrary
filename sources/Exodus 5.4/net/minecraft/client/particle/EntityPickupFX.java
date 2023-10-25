/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityPickupFX
extends EntityFX {
    private Entity field_174843_ax;
    private Entity field_174840_a;
    private int maxAge;
    private float field_174841_aA;
    private RenderManager field_174842_aB = Minecraft.getMinecraft().getRenderManager();
    private int age;

    @Override
    public int getFXLayer() {
        return 3;
    }

    public EntityPickupFX(World world, Entity entity, Entity entity2, float f) {
        super(world, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
        this.field_174840_a = entity;
        this.field_174843_ax = entity2;
        this.maxAge = 3;
        this.field_174841_aA = f;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = ((float)this.age + f) / (float)this.maxAge;
        f7 *= f7;
        double d = this.field_174840_a.posX;
        double d2 = this.field_174840_a.posY;
        double d3 = this.field_174840_a.posZ;
        double d4 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.posX - this.field_174843_ax.lastTickPosX) * (double)f;
        double d5 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.posY - this.field_174843_ax.lastTickPosY) * (double)f + (double)this.field_174841_aA;
        double d6 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.posZ - this.field_174843_ax.lastTickPosZ) * (double)f;
        double d7 = d + (d4 - d) * (double)f7;
        double d8 = d2 + (d5 - d2) * (double)f7;
        double d9 = d3 + (d6 - d3) * (double)f7;
        int n = this.getBrightnessForRender(f);
        int n2 = n % 65536;
        int n3 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n2 / 1.0f, (float)n3 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (float)(d7 -= interpPosX), (float)(d8 -= interpPosY), (float)(d9 -= interpPosZ), this.field_174840_a.rotationYaw, f);
    }

    @Override
    public void onUpdate() {
        ++this.age;
        if (this.age == this.maxAge) {
            this.setDead();
        }
    }
}

