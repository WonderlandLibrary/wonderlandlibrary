/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
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
    private float alpha = 1.0f;
    private float colorB = 1.0f;
    private final RendererLivingEntity<?> renderer;
    protected T field_177189_c;
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
    private float colorG = 1.0f;
    private boolean field_177193_i;
    private float colorR = 1.0f;
    protected T field_177186_d;

    public LayerArmorBase(RendererLivingEntity<?> rendererLivingEntity) {
        this.renderer = rendererLivingEntity;
        this.initArmor();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private boolean isSlotForLeggings(int n) {
        return n == 2;
    }

    public T func_177175_a(int n) {
        return this.isSlotForLeggings(n) ? this.field_177189_c : this.field_177186_d;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.renderLayer(entityLivingBase, f, f2, f3, f4, f5, f6, f7, 4);
        this.renderLayer(entityLivingBase, f, f2, f3, f4, f5, f6, f7, 3);
        this.renderLayer(entityLivingBase, f, f2, f3, f4, f5, f6, f7, 2);
        this.renderLayer(entityLivingBase, f, f2, f3, f4, f5, f6, f7, 1);
    }

    protected abstract void initArmor();

    private ResourceLocation getArmorResource(ItemArmor itemArmor, boolean bl) {
        return this.getArmorResource(itemArmor, bl, null);
    }

    private void renderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7, int n) {
        ItemStack itemStack = this.getCurrentArmor(entityLivingBase, n);
        if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            T t = this.func_177175_a(n);
            ((ModelBase)t).setModelAttributes(this.renderer.getMainModel());
            ((ModelBase)t).setLivingAnimations(entityLivingBase, f, f2, f3);
            this.func_177179_a(t, n);
            boolean bl = this.isSlotForLeggings(n);
            this.renderer.bindTexture(this.getArmorResource(itemArmor, bl));
            switch (itemArmor.getArmorMaterial()) {
                case LEATHER: {
                    int n2 = itemArmor.getColor(itemStack);
                    float f8 = (float)(n2 >> 16 & 0xFF) / 255.0f;
                    float f9 = (float)(n2 >> 8 & 0xFF) / 255.0f;
                    float f10 = (float)(n2 & 0xFF) / 255.0f;
                    GlStateManager.color(this.colorR * f8, this.colorG * f9, this.colorB * f10, this.alpha);
                    ((ModelBase)t).render(entityLivingBase, f, f2, f4, f5, f6, f7);
                    this.renderer.bindTexture(this.getArmorResource(itemArmor, bl, "overlay"));
                }
                case CHAIN: 
                case IRON: 
                case GOLD: 
                case DIAMOND: {
                    GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                    ((ModelBase)t).render(entityLivingBase, f, f2, f4, f5, f6, f7);
                }
            }
            if (!this.field_177193_i && itemStack.isItemEnchanted()) {
                this.func_177183_a(entityLivingBase, t, f, f2, f3, f4, f5, f6, f7);
            }
        }
    }

    protected abstract void func_177179_a(T var1, int var2);

    private ResourceLocation getArmorResource(ItemArmor itemArmor, boolean bl, String string) {
        String string2 = String.format("textures/models/armor/%s_layer_%d%s.png", itemArmor.getArmorMaterial().getName(), bl ? 2 : 1, string == null ? "" : String.format("_%s", string));
        ResourceLocation resourceLocation = ARMOR_TEXTURE_RES_MAP.get(string2);
        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(string2);
            ARMOR_TEXTURE_RES_MAP.put(string2, resourceLocation);
        }
        return resourceLocation;
    }

    public ItemStack getCurrentArmor(EntityLivingBase entityLivingBase, int n) {
        return entityLivingBase.getCurrentArmor(n - 1);
    }

    private void func_177183_a(EntityLivingBase entityLivingBase, T t, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = (float)entityLivingBase.ticksExisted + f3;
        this.renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f9 = 0.5f;
        GlStateManager.color(f9, f9, f9, 1.0f);
        int n = 0;
        while (n < 2) {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            float f10 = 0.76f;
            GlStateManager.color(0.5f * f10, 0.25f * f10, 0.8f * f10, 1.0f);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f11 = 0.33333334f;
            GlStateManager.scale(f11, f11, f11);
            GlStateManager.rotate(30.0f - (float)n * 60.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, f8 * (0.001f + (float)n * 0.003f) * 20.0f, 0.0f);
            GlStateManager.matrixMode(5888);
            ((ModelBase)t).render(entityLivingBase, f, f2, f4, f5, f6, f7);
            ++n;
        }
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
    }
}

