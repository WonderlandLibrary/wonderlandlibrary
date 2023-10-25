/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.feature.impl.visuals.EnchantmentColor;
import org.celestial.client.feature.impl.visuals.NoRender;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public abstract class LayerArmorBase<T extends ModelBase>
implements LayerRenderer<EntityLivingBase> {
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected T modelLeggings;
    protected T modelArmor;
    private final RenderLivingBase<?> renderer;
    private float alpha = 1.0f;
    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;
    private boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    public LayerArmorBase(RenderLivingBase<?> rendererIn) {
        this.renderer = rendererIn;
        this.initArmor();
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if ((!CustomModel.onlyMe.getCurrentValue() || entitylivingbaseIn == Minecraft.getMinecraft().player || Celestial.instance.friendManager.isFriend(entitylivingbaseIn.getName()) && CustomModel.friends.getCurrentValue()) && Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crazy Rabbit") || CustomModel.modelMode.currentMode.equals("Sonic") || CustomModel.modelMode.currentMode.equals("CupHead") || CustomModel.modelMode.currentMode.equals("Freddy Bear") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Freddy Bear") || CustomModel.modelMode.currentMode.equals("Amogus") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Demon") || CustomModel.modelMode.currentMode.equals("SirenHead") || CustomModel.modelMode.currentMode.equals("Jeff Killer") || CustomModel.modelMode.currentMode.equals("Crab"))) {
            return;
        }
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
        ItemArmor itemarmor;
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.armor.getCurrentValue()) {
            return;
        }
        ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);
        if (itemstack.getItem() instanceof ItemArmor && (itemarmor = (ItemArmor)itemstack.getItem()).getEquipmentSlot() == slotIn) {
            T t = this.getModelFromSlot(slotIn);
            if (Reflector.ForgeHooksClient.exists()) {
                t = this.getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
            }
            ((ModelBase)t).setModelAttributes(this.renderer.getMainModel());
            ((ModelBase)t).setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.setModelSlotVisible(t, slotIn);
            boolean flag = this.isLegSlot(slotIn);
            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, null)) {
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));
                } else {
                    this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
                }
            }
            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                if (ReflectorForge.armorHasOverlay(itemarmor, itemstack)) {
                    int j = itemarmor.getColor(itemstack);
                    float f3 = (float)(j >> 16 & 0xFF) / 255.0f;
                    float f4 = (float)(j >> 8 & 0xFF) / 255.0f;
                    float f5 = (float)(j & 0xFF) / 255.0f;
                    GlStateManager.color(this.colorR * f3, this.colorG * f4, this.colorB * f5, this.alpha);
                    ((ModelBase)t).render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, "overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
                    }
                }
                GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                ((ModelBase)t).render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (!(this.skipRenderGlint || !itemstack.hasEffect() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))) {
                    if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.glintEffect.getCurrentValue()) {
                        return;
                    }
                    LayerArmorBase.renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
                return;
            }
            switch (itemarmor.getArmorMaterial()) {
                case LEATHER: {
                    int i = itemarmor.getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0f;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                    float f2 = (float)(i & 0xFF) / 255.0f;
                    GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                    ((ModelBase)t).render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, "overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                    }
                }
                case CHAIN: 
                case IRON: 
                case GOLD: 
                case DIAMOND: {
                    GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                    ((ModelBase)t).render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            if (!(this.skipRenderGlint || !itemstack.isItemEnchanted() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))) {
                if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.glintEffect.getCurrentValue()) {
                    return;
                }
                LayerArmorBase.renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    public T getModelFromSlot(EntityEquipmentSlot slotIn) {
        return this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor;
    }

    private boolean isLegSlot(EntityEquipmentSlot slotIn) {
        return slotIn == EntityEquipmentSlot.LEGS;
    }

    public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            float f = (float)p_188364_1_.ticksExisted + p_188364_5_;
            p_188364_0_.bindTexture(ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                GlStateManager.color(0.38f, 0.19f, 0.608f, 1.0f);
                if (Celestial.instance.featureManager.getFeatureByClass(EnchantmentColor.class).getState()) {
                    switch (EnchantmentColor.colorMode.currentMode) {
                        case "Rainbow": {
                            Color rainbow = PaletteHelper.rainbow((int)f, 1.0f, 1.0f);
                            GlStateManager.color((float)rainbow.getRed() / 255.0f, (float)rainbow.getGreen() / 255.0f, (float)rainbow.getBlue() / 255.0f, (float)rainbow.getAlpha() / 255.0f);
                            break;
                        }
                        case "Custom": {
                            Color colorCustom = new Color(EnchantmentColor.customColor.getColor());
                            GlStateManager.color((float)colorCustom.getRed() / 255.0f, (float)colorCustom.getGreen() / 255.0f, (float)colorCustom.getBlue() / 255.0f, (float)colorCustom.getAlpha() / 255.0f);
                            break;
                        }
                        case "Client": {
                            GlStateManager.color((float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f, (float)ClientHelper.getClientColor().getAlpha() / 255.0f);
                        }
                    }
                }
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f3 = 0.33333334f;
                GlStateManager.scale(0.33333334f, 0.33333334f, 0.33333334f);
                GlStateManager.rotate(30.0f - (float)i * 60.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.0f, f * (0.001f + (float)i * 0.003f) * 20.0f, 0.0f);
                GlStateManager.matrixMode(5888);
                model.render(p_188364_1_, p_188364_3_, p_188364_4_, p_188364_6_, p_188364_7_, p_188364_8_, p_188364_9_);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }

    private ResourceLocation getArmorResource(ItemArmor armor, boolean p_177181_2_) {
        return this.getArmorResource(armor, p_177181_2_, null);
    }

    private ResourceLocation getArmorResource(ItemArmor armor, boolean p_177178_2_, String p_177178_3_) {
        Object[] arrobject = new Object[3];
        arrobject[0] = armor.getArmorMaterial().getName();
        arrobject[1] = p_177178_2_ ? 2 : 1;
        arrobject[2] = p_177178_3_ == null ? "" : String.format("_%s", p_177178_3_);
        String s = String.format("textures/models/armor/%s_layer_%d%s.png", arrobject);
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        }
        return resourcelocation;
    }

    protected abstract void initArmor();

    protected abstract void setModelSlotVisible(T var1, EntityEquipmentSlot var2);

    protected T getArmorModelHook(EntityLivingBase p_getArmorModelHook_1_, ItemStack p_getArmorModelHook_2_, EntityEquipmentSlot p_getArmorModelHook_3_, T p_getArmorModelHook_4_) {
        return p_getArmorModelHook_4_;
    }

    public ResourceLocation getArmorResource(Entity p_getArmorResource_1_, ItemStack p_getArmorResource_2_, EntityEquipmentSlot p_getArmorResource_3_, String p_getArmorResource_4_) {
        ItemArmor itemarmor = (ItemArmor)p_getArmorResource_2_.getItem();
        String s = itemarmor.getArmorMaterial().getName();
        String s1 = "minecraft";
        int i = s.indexOf(58);
        if (i != -1) {
            s1 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        Object[] arrobject = new Object[4];
        arrobject[0] = s1;
        arrobject[1] = s;
        arrobject[2] = this.isLegSlot(p_getArmorResource_3_) ? 2 : 1;
        arrobject[3] = p_getArmorResource_4_ == null ? "" : String.format("_%s", p_getArmorResource_4_);
        String s2 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", arrobject);
        s2 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, new Object[]{p_getArmorResource_1_, p_getArmorResource_2_, s2, p_getArmorResource_3_, p_getArmorResource_4_});
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s2);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s2);
            ARMOR_TEXTURE_RES_MAP.put(s2, resourcelocation);
        }
        return resourcelocation;
    }
}

