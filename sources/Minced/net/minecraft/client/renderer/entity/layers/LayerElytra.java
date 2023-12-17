// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.item.ItemStack;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.optifine.CustomItems;
import net.minecraft.src.Config;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;

public class LayerElytra implements LayerRenderer<EntityLivingBase>
{
    private static final ResourceLocation TEXTURE_ELYTRA;
    protected final RenderLivingBase<?> renderPlayer;
    private final ModelElytra modelElytra;
    
    public LayerElytra(final RenderLivingBase<?> p_i47185_1_) {
        this.modelElytra = new ModelElytra();
        this.renderPlayer = p_i47185_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemstack.getItem() == Items.ELYTRA) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (entitylivingbaseIn instanceof AbstractClientPlayer) {
                final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbaseIn;
                if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null) {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationElytra());
                }
                else if (abstractclientplayer.hasElytraCape() && abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)) {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationCape());
                }
                else {
                    ResourceLocation resourcelocation1 = LayerElytra.TEXTURE_ELYTRA;
                    if (Config.isCustomItems()) {
                        resourcelocation1 = CustomItems.getCustomElytraTexture(itemstack, resourcelocation1);
                    }
                    this.renderPlayer.bindTexture(resourcelocation1);
                }
            }
            else {
                ResourceLocation resourcelocation2 = LayerElytra.TEXTURE_ELYTRA;
                if (Config.isCustomItems()) {
                    resourcelocation2 = CustomItems.getCustomElytraTexture(itemstack, resourcelocation2);
                }
                this.renderPlayer.bindTexture(resourcelocation2);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (itemstack.isItemEnchanted()) {
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entitylivingbaseIn, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    static {
        TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    }
}
