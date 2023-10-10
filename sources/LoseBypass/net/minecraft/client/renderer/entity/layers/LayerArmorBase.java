/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class LayerArmorBase<T extends ModelBase>
implements LayerRenderer<EntityLivingBase> {
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected T field_177189_c;
    protected T field_177186_d;
    private final RendererLivingEntity<?> renderer;
    private float alpha = 1.0f;
    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;
    private boolean field_177193_i;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    public LayerArmorBase(RendererLivingEntity<?> rendererIn) {
        this.renderer = rendererIn;
        this.initArmor();
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 4);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 3);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 2);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 1);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderLayer(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float p_177182_4_, float p_177182_5_, float p_177182_6_, float p_177182_7_, float p_177182_8_, int armorSlot) {
        ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);
        if (itemstack == null) return;
        if (!(itemstack.getItem() instanceof ItemArmor)) return;
        ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
        T t = this.func_177175_a(armorSlot);
        ((ModelBase)t).setModelAttributes(this.renderer.getMainModel());
        ((ModelBase)t).setLivingAnimations(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_4_);
        this.func_177179_a(t, armorSlot);
        boolean flag = this.isSlotForLeggings(armorSlot);
        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
        switch (1.$SwitchMap$net$minecraft$item$ItemArmor$ArmorMaterial[itemarmor.getArmorMaterial().ordinal()]) {
            case 1: {
                int i = itemarmor.getColor(itemstack);
                float f = (float)(i >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(i & 0xFF) / 255.0f;
                GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                ((ModelBase)t).render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                ((ModelBase)t).render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                break;
            }
        }
        if (this.field_177193_i) return;
        if (!itemstack.isItemEnchanted()) return;
        this.func_177183_a(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
    }

    public ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot) {
        return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
    }

    public T func_177175_a(int p_177175_1_) {
        T t;
        if (this.isSlotForLeggings(p_177175_1_)) {
            t = this.field_177189_c;
            return t;
        }
        t = this.field_177186_d;
        return t;
    }

    private boolean isSlotForLeggings(int armorSlot) {
        if (armorSlot != 2) return false;
        return true;
    }

    private void func_177183_a(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_) {
        float f = (float)entitylivingbaseIn.ticksExisted + p_177183_5_;
        this.renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5f;
        GlStateManager.color(f1, f1, f1, 1.0f);
        int i = 0;
        while (true) {
            if (i >= 2) {
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.enableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.depthFunc(515);
                GlStateManager.disableBlend();
                return;
            }
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            float f2 = 0.76f;
            GlStateManager.color(0.5f * f2, 0.25f * f2, 0.8f * f2, 1.0f);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334f;
            GlStateManager.scale(f3, f3, f3);
            GlStateManager.rotate(30.0f - (float)i * 60.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, f * (0.001f + (float)i * 0.003f) * 20.0f, 0.0f);
            GlStateManager.matrixMode(5888);
            ((ModelBase)modelbaseIn).render(entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
            ++i;
        }
    }

    private ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_) {
        return this.getArmorResource(p_177181_1_, p_177181_2_, null);
    }

    private ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_) {
        String s = String.format("textures/models/armor/%s_layer_%d%s.png", p_177178_1_.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, p_177178_3_ == null ? "" : String.format("_%s", p_177178_3_));
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s);
        if (resourcelocation != null) return resourcelocation;
        resourcelocation = new ResourceLocation(s);
        ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        return resourcelocation;
    }

    protected abstract void initArmor();

    protected abstract void func_177179_a(T var1, int var2);
}

