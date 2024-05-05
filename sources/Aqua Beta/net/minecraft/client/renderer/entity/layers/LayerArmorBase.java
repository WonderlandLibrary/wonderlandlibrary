package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;

public abstract class LayerArmorBase<T extends ModelBase>
implements LayerRenderer<EntityLivingBase> {
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected T modelLeggings;
    protected T modelArmor;
    private final RendererLivingEntity<?> renderer;
    private float alpha = 1.0f;
    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;
    private boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    public LayerArmorBase(RendererLivingEntity<?> rendererIn) {
        this.renderer = rendererIn;
        this.initArmor();
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 4);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 3);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 2);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 1);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderLayer(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot) {
        ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);
        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            T t = this.getArmorModel(armorSlot);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entitylivingbaseIn, p_177182_2_, p_177182_3_, partialTicks);
            if (Reflector.ForgeHooksClient.exists()) {
                t = this.getArmorModelHook(entitylivingbaseIn, itemstack, armorSlot, t);
            }
            this.setModelPartVisible(t, armorSlot);
            boolean flag = this.isSlotForLeggings(armorSlot);
            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture((ItemStack)itemstack, (int)(flag ? 2 : 1), (String)null)) {
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    this.renderer.bindTexture(this.getArmorResource((Entity)entitylivingbaseIn, itemstack, flag ? 2 : 1, null));
                } else {
                    this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
                }
            }
            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                if (ReflectorForge.armorHasOverlay((ItemArmor)itemarmor, (ItemStack)itemstack)) {
                    int j = itemarmor.getColor(itemstack);
                    float f3 = (float)(j >> 16 & 0xFF) / 255.0f;
                    float f4 = (float)(j >> 8 & 0xFF) / 255.0f;
                    float f5 = (float)(j & 0xFF) / 255.0f;
                    GlStateManager.color((float)(this.colorR * f3), (float)(this.colorG * f4), (float)(this.colorB * f5), (float)this.alpha);
                    t.render((Entity)entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture((ItemStack)itemstack, (int)(flag ? 2 : 1), (String)"overlay")) {
                        this.renderer.bindTexture(this.getArmorResource((Entity)entitylivingbaseIn, itemstack, flag ? 2 : 1, "overlay"));
                    }
                }
                GlStateManager.color((float)this.colorR, (float)this.colorG, (float)this.colorB, (float)this.alpha);
                t.render((Entity)entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                if (!(this.skipRenderGlint || !itemstack.hasEffect() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect((EntityLivingBase)entitylivingbaseIn, (ItemStack)itemstack, t, (float)p_177182_2_, (float)p_177182_3_, (float)partialTicks, (float)p_177182_5_, (float)p_177182_6_, (float)p_177182_7_, (float)scale))) {
                    this.renderGlint(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, partialTicks, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                }
                return;
            }
            switch (1.$SwitchMap$net$minecraft$item$ItemArmor$ArmorMaterial[itemarmor.getArmorMaterial().ordinal()]) {
                case 1: {
                    int i = itemarmor.getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0f;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                    float f2 = (float)(i & 0xFF) / 255.0f;
                    GlStateManager.color((float)(this.colorR * f), (float)(this.colorG * f1), (float)(this.colorB * f2), (float)this.alpha);
                    t.render((Entity)entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture((ItemStack)itemstack, (int)(flag ? 2 : 1), (String)"overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                    }
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: {
                    GlStateManager.color((float)this.colorR, (float)this.colorG, (float)this.colorB, (float)this.alpha);
                    t.render((Entity)entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                }
            }
            if (!(this.skipRenderGlint || !itemstack.isItemEnchanted() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect((EntityLivingBase)entitylivingbaseIn, (ItemStack)itemstack, t, (float)p_177182_2_, (float)p_177182_3_, (float)partialTicks, (float)p_177182_5_, (float)p_177182_6_, (float)p_177182_7_, (float)scale))) {
                this.renderGlint(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, partialTicks, p_177182_5_, p_177182_6_, p_177182_7_, scale);
            }
        }
    }

    public ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot) {
        return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
    }

    public T getArmorModel(int armorSlot) {
        return this.isSlotForLeggings(armorSlot) ? this.modelLeggings : this.modelArmor;
    }

    private boolean isSlotForLeggings(int armorSlot) {
        return armorSlot == 2;
    }

    private void renderGlint(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float partialTicks, float p_177183_6_, float p_177183_7_, float p_177183_8_, float scale) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            this.renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            GlStateManager.enableBlend();
            GlStateManager.depthFunc((int)514);
            GlStateManager.depthMask((boolean)false);
            float f1 = 0.5f;
            GlStateManager.color((float)f1, (float)f1, (float)f1, (float)1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc((int)768, (int)1);
                float f2 = 0.76f;
                GlStateManager.color((float)(0.5f * f2), (float)(0.25f * f2), (float)(0.8f * f2), (float)1.0f);
                GlStateManager.matrixMode((int)5890);
                GlStateManager.loadIdentity();
                float f3 = 0.33333334f;
                GlStateManager.scale((float)f3, (float)f3, (float)f3);
                GlStateManager.rotate((float)(30.0f - (float)i * 60.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.translate((float)0.0f, (float)(f * (0.001f + (float)i * 0.003f) * 20.0f), (float)0.0f);
                GlStateManager.matrixMode((int)5888);
                modelbaseIn.render((Entity)entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, scale);
            }
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.depthFunc((int)515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }

    private ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_) {
        return this.getArmorResource(p_177181_1_, p_177181_2_, null);
    }

    private ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_) {
        String s = String.format((String)"textures/models/armor/%s_layer_%d%s.png", (Object[])new Object[]{p_177178_1_.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, p_177178_3_ == null ? "" : String.format((String)"_%s", (Object[])new Object[]{p_177178_3_})});
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get((Object)s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            ARMOR_TEXTURE_RES_MAP.put((Object)s, (Object)resourcelocation);
        }
        return resourcelocation;
    }

    protected abstract void initArmor();

    protected abstract void setModelPartVisible(T var1, int var2);

    protected T getArmorModelHook(EntityLivingBase p_getArmorModelHook_1_, ItemStack p_getArmorModelHook_2_, int p_getArmorModelHook_3_, T p_getArmorModelHook_4_) {
        return p_getArmorModelHook_4_;
    }

    public ResourceLocation getArmorResource(Entity p_getArmorResource_1_, ItemStack p_getArmorResource_2_, int p_getArmorResource_3_, String p_getArmorResource_4_) {
        ItemArmor itemarmor = (ItemArmor)p_getArmorResource_2_.getItem();
        String s = itemarmor.getArmorMaterial().getName();
        String s1 = "minecraft";
        int i = s.indexOf(58);
        if (i != -1) {
            s1 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        String s2 = String.format((String)"%s:textures/models/armor/%s_layer_%d%s.png", (Object[])new Object[]{s1, s, this.isSlotForLeggings(p_getArmorResource_3_) ? 2 : 1, p_getArmorResource_4_ == null ? "" : String.format((String)"_%s", (Object[])new Object[]{p_getArmorResource_4_})});
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get((Object)(s2 = Reflector.callString((ReflectorMethod)Reflector.ForgeHooksClient_getArmorTexture, (Object[])new Object[]{p_getArmorResource_1_, p_getArmorResource_2_, s2, p_getArmorResource_3_, p_getArmorResource_4_})));
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s2);
            ARMOR_TEXTURE_RES_MAP.put((Object)s2, (Object)resourcelocation);
        }
        return resourcelocation;
    }
}
