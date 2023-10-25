/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomItems;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.feature.impl.visuals.NoRender;

public class LayerElytra
implements LayerRenderer<EntityLivingBase> {
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    protected final RenderLivingBase<?> renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytra(RenderLivingBase<?> p_i47185_1_) {
        this.renderPlayer = p_i47185_1_;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if ((!CustomModel.onlyMe.getCurrentValue() || entitylivingbaseIn == Minecraft.getMinecraft().player || Celestial.instance.friendManager.isFriend(entitylivingbaseIn.getName()) && CustomModel.friends.getCurrentValue()) && Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crazy Rabbit") || CustomModel.modelMode.currentMode.equals("Sonic") || CustomModel.modelMode.currentMode.equals("CupHead") || CustomModel.modelMode.currentMode.equals("Freddy Bear") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Amogus") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Demon") || CustomModel.modelMode.currentMode.equals("SirenHead") || CustomModel.modelMode.currentMode.equals("Jeff Killer") || CustomModel.modelMode.currentMode.equals("Crab"))) {
            return;
        }
        ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemstack.getItem() == Items.ELYTRA) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (entitylivingbaseIn instanceof AbstractClientPlayer) {
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbaseIn;
                if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null) {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationElytra());
                } else if (abstractclientplayer.hasElytraCape() && abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)) {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationCape());
                } else {
                    ResourceLocation resourcelocation1 = TEXTURE_ELYTRA;
                    if (Config.isCustomItems()) {
                        resourcelocation1 = CustomItems.getCustomElytraTexture(itemstack, resourcelocation1);
                    }
                    this.renderPlayer.bindTexture(resourcelocation1);
                }
            } else {
                ResourceLocation resourcelocation = TEXTURE_ELYTRA;
                if (Config.isCustomItems()) {
                    resourcelocation = CustomItems.getCustomElytraTexture(itemstack, resourcelocation);
                }
                this.renderPlayer.bindTexture(resourcelocation);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (itemstack.isItemEnchanted()) {
                if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.glintEffect.getCurrentValue()) {
                    return;
                }
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
}

