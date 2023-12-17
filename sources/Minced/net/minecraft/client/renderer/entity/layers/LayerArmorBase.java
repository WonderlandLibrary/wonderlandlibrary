// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.awt.Color;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import ru.tuskevich.modules.impl.RENDER.EnchantmentColor;
import net.minecraft.client.Minecraft;
import net.optifine.shaders.ShadersRender;
import net.optifine.shaders.Shaders;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.entity.Entity;
import net.optifine.CustomItems;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.item.ItemArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import java.util.Map;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBase;

public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase>
{
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES;
    protected T modelLeggings;
    protected T modelArmor;
    private final RenderLivingBase<?> renderer;
    private float alpha;
    private float colorR;
    private float colorG;
    private float colorB;
    private boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP;
    
    public LayerArmorBase(final RenderLivingBase<?> rendererIn) {
        this.alpha = 1.0f;
        this.colorR = 1.0f;
        this.colorG = 1.0f;
        this.colorB = 1.0f;
        this.renderer = rendererIn;
        this.initArmor();
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private void renderArmorLayer(final EntityLivingBase entityLivingBaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final EntityEquipmentSlot slotIn) {
        final ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);
        if (itemstack.getItem() instanceof ItemArmor) {
            final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            if (itemarmor.getEquipmentSlot() == slotIn) {
                T t = this.getModelFromSlot(slotIn);
                if (Reflector.ForgeHooksClient.exists()) {
                    t = this.getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
                }
                t.setModelAttributes(this.renderer.getMainModel());
                t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.setModelSlotVisible(t, slotIn);
                final boolean flag = this.isLegSlot(slotIn);
                if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, null)) {
                    if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                        this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));
                    }
                    else {
                        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
                    }
                }
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    if (ReflectorForge.armorHasOverlay(itemarmor, itemstack)) {
                        final int j = itemarmor.getColor(itemstack);
                        final float f3 = (j >> 16 & 0xFF) / 255.0f;
                        final float f4 = (j >> 8 & 0xFF) / 255.0f;
                        final float f5 = (j & 0xFF) / 255.0f;
                        GlStateManager.color(this.colorR * f3, this.colorG * f4, this.colorB * f5, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, "overlay")) {
                            this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
                        }
                    }
                    GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                    t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!this.skipRenderGlint && itemstack.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))) {
                        renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                    }
                    return;
                }
                switch (itemarmor.getArmorMaterial()) {
                    case LEATHER: {
                        final int i = itemarmor.getColor(itemstack);
                        final float f6 = (i >> 16 & 0xFF) / 255.0f;
                        final float f7 = (i >> 8 & 0xFF) / 255.0f;
                        final float f8 = (i & 0xFF) / 255.0f;
                        GlStateManager.color(this.colorR * f6, this.colorG * f7, this.colorB * f8, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, "overlay")) {
                            this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                        }
                    }
                    case CHAIN:
                    case IRON:
                    case GOLD:
                    case DIAMOND: {
                        GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        break;
                    }
                }
                if (!this.skipRenderGlint && itemstack.isItemEnchanted() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))) {
                    renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
        }
    }
    
    public T getModelFromSlot(final EntityEquipmentSlot slotIn) {
        return this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor;
    }
    
    private boolean isLegSlot(final EntityEquipmentSlot slotIn) {
        return slotIn == EntityEquipmentSlot.LEGS;
    }
    
    public static void renderEnchantedGlint(final RenderLivingBase<?> p_188364_0_, final EntityLivingBase p_188364_1_, final ModelBase model, final float p_188364_3_, final float p_188364_4_, final float p_188364_5_, final float p_188364_6_, final float p_188364_7_, final float p_188364_8_, final float p_188364_9_) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            final float f = p_188364_1_.ticksExisted + p_188364_5_;
            p_188364_0_.bindTexture(LayerArmorBase.ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            final float f2 = 0.5f;
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                final float f3 = 0.76f;
                final EnchantmentColor c = (EnchantmentColor)Minced.getInstance().manager.getModule(EnchantmentColor.class);
                if (c.state) {
                    final Color cc = new Color(c.enchantColor.getColorValue());
                    GlStateManager.color(cc.getRed() / 255.0f, cc.getGreen() / 255.0f, cc.getBlue() / 255.0f, cc.getAlpha() / 255.0f);
                }
                else {
                    GlStateManager.color(0.38f, 0.19f, 0.608f, 1.0f);
                }
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                final float f4 = 0.33333334f;
                GlStateManager.scale(0.33333334f, 0.33333334f, 0.33333334f);
                GlStateManager.rotate(30.0f - i * 60.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.0f, f * (0.001f + i * 0.003f) * 20.0f, 0.0f);
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
    
    private ResourceLocation getArmorResource(final ItemArmor armor, final boolean p_177181_2_) {
        return this.getArmorResource(armor, p_177181_2_, null);
    }
    
    private ResourceLocation getArmorResource(final ItemArmor armor, final boolean p_177178_2_, final String p_177178_3_) {
        final String s = String.format("textures/models/armor/%s_layer_%d%s.png", armor.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, (p_177178_3_ == null) ? "" : String.format("_%s", p_177178_3_));
        ResourceLocation resourcelocation = LayerArmorBase.ARMOR_TEXTURE_RES_MAP.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            LayerArmorBase.ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        }
        return resourcelocation;
    }
    
    protected abstract void initArmor();
    
    protected abstract void setModelSlotVisible(final T p0, final EntityEquipmentSlot p1);
    
    protected T getArmorModelHook(final EntityLivingBase p_getArmorModelHook_1_, final ItemStack p_getArmorModelHook_2_, final EntityEquipmentSlot p_getArmorModelHook_3_, final T p_getArmorModelHook_4_) {
        return p_getArmorModelHook_4_;
    }
    
    public ResourceLocation getArmorResource(final Entity p_getArmorResource_1_, final ItemStack p_getArmorResource_2_, final EntityEquipmentSlot p_getArmorResource_3_, final String p_getArmorResource_4_) {
        final ItemArmor itemarmor = (ItemArmor)p_getArmorResource_2_.getItem();
        String s = itemarmor.getArmorMaterial().getName();
        String s2 = "minecraft";
        final int i = s.indexOf(58);
        if (i != -1) {
            s2 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        String s3 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", s2, s, this.isLegSlot(p_getArmorResource_3_) ? 2 : 1, (p_getArmorResource_4_ == null) ? "" : String.format("_%s", p_getArmorResource_4_));
        s3 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, p_getArmorResource_1_, p_getArmorResource_2_, s3, p_getArmorResource_3_, p_getArmorResource_4_);
        ResourceLocation resourcelocation = LayerArmorBase.ARMOR_TEXTURE_RES_MAP.get(s3);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s3);
            LayerArmorBase.ARMOR_TEXTURE_RES_MAP.put(s3, resourcelocation);
        }
        return resourcelocation;
    }
    
    static {
        ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
    }
}
