/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderArmorStand
extends RenderLivingBase<EntityArmorStand> {
    public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

    public RenderArmorStand(RenderManager manager) {
        super(manager, new ModelArmorStand(), 0.0f);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this){

            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelArmorStandArmor(0.5f);
                this.modelArmor = new ModelArmorStandArmor(1.0f);
            }
        };
        this.addLayer(layerbipedarmor);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityArmorStand entity) {
        return TEXTURE_ARMOR_STAND;
    }

    @Override
    public ModelArmorStand getMainModel() {
        return (ModelArmorStand)super.getMainModel();
    }

    @Override
    protected void rotateCorpse(EntityArmorStand entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        float f = (float)(entityLiving.world.getTotalWorldTime() - entityLiving.punchCooldown) + partialTicks;
        if (f < 5.0f) {
            GlStateManager.rotate(MathHelper.sin(f / 1.5f * (float)Math.PI) * 3.0f, 0.0f, 1.0f, 0.0f);
        }
    }

    @Override
    protected boolean canRenderName(EntityArmorStand entity) {
        return entity.getAlwaysRenderNameTag();
    }

    @Override
    public void doRender(EntityArmorStand entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.hasMarker()) {
            this.renderMarker = true;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.hasMarker()) {
            this.renderMarker = false;
        }
    }
}

