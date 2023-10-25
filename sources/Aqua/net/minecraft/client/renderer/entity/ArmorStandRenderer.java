package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;

public class ArmorStandRenderer
extends RendererLivingEntity<EntityArmorStand> {
    public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

    public ArmorStandRenderer(RenderManager p_i46195_1_) {
        super(p_i46195_1_, (ModelBase)new ModelArmorStand(), 0.0f);
        1 layerbipedarmor = new /* Unavailable Anonymous Inner Class!! */;
        this.addLayer((LayerRenderer)layerbipedarmor);
        this.addLayer((LayerRenderer)new LayerHeldItem((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new LayerCustomHead(this.getMainModel().bipedHead));
    }

    protected ResourceLocation getEntityTexture(EntityArmorStand entity) {
        return TEXTURE_ARMOR_STAND;
    }

    public ModelArmorStand getMainModel() {
        return (ModelArmorStand)super.getMainModel();
    }

    protected void rotateCorpse(EntityArmorStand bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.rotate((float)(180.0f - p_77043_3_), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    protected boolean canRenderName(EntityArmorStand entity) {
        return entity.getAlwaysRenderNameTag();
    }
}
