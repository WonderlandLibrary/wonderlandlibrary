/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;

public class ArmorStandRenderer
extends RendererLivingEntity<EntityArmorStand> {
    public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

    @Override
    protected void rotateCorpse(EntityArmorStand entityArmorStand, float f, float f2, float f3) {
        GlStateManager.rotate(180.0f - f2, 0.0f, 1.0f, 0.0f);
    }

    @Override
    protected boolean canRenderName(EntityArmorStand entityArmorStand) {
        return entityArmorStand.getAlwaysRenderNameTag();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityArmorStand entityArmorStand) {
        return TEXTURE_ARMOR_STAND;
    }

    @Override
    public ModelArmorStand getMainModel() {
        return (ModelArmorStand)super.getMainModel();
    }

    public ArmorStandRenderer(RenderManager renderManager) {
        super(renderManager, new ModelArmorStand(), 0.0f);
        LayerBipedArmor layerBipedArmor = new LayerBipedArmor(this){

            @Override
            protected void initArmor() {
                this.field_177189_c = new ModelArmorStandArmor(0.5f);
                this.field_177186_d = new ModelArmorStandArmor(1.0f);
            }
        };
        this.addLayer(layerBipedArmor);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerCustomHead(((ModelArmorStand)this.getMainModel()).bipedHead));
    }
}

