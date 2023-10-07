// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public abstract class LayerArmorBase implements LayerRenderer
{
    protected static final ResourceLocation field_177188_b;
    protected ModelBase field_177189_c;
    protected ModelBase field_177186_d;
    private final RendererLivingEntity field_177190_a;
    private float field_177187_e;
    private float field_177184_f;
    private float field_177185_g;
    private float field_177192_h;
    private boolean field_177193_i;
    private static final Map field_177191_j;
    private static final String __OBFID = "CL_00002428";
    
    public LayerArmorBase(final RendererLivingEntity p_i46125_1_) {
        this.field_177187_e = 1.0f;
        this.field_177184_f = 1.0f;
        this.field_177185_g = 1.0f;
        this.field_177192_h = 1.0f;
        this.field_177190_a = p_i46125_1_;
        this.func_177177_a();
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 4);
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 3);
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 2);
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 1);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private void func_177182_a(final EntityLivingBase p_177182_1_, final float p_177182_2_, final float p_177182_3_, final float p_177182_4_, final float p_177182_5_, final float p_177182_6_, final float p_177182_7_, final float p_177182_8_, final int p_177182_9_) {
        final ItemStack var10 = this.func_177176_a(p_177182_1_, p_177182_9_);
        if (var10 != null && var10.getItem() instanceof ItemArmor) {
            final ItemArmor var11 = (ItemArmor)var10.getItem();
            final ModelBase var12 = this.func_177175_a(p_177182_9_);
            var12.setModelAttributes(this.field_177190_a.getMainModel());
            var12.setLivingAnimations(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_4_);
            this.func_177179_a(var12, p_177182_9_);
            final boolean var13 = this.func_177180_b(p_177182_9_);
            this.field_177190_a.bindTexture(this.func_177181_a(var11, var13));
            switch (SwitchArmorMaterial.field_178747_a[var11.getArmorMaterial().ordinal()]) {
                case 1: {
                    final int var14 = var11.getColor(var10);
                    final float var15 = (var14 >> 16 & 0xFF) / 255.0f;
                    final float var16 = (var14 >> 8 & 0xFF) / 255.0f;
                    final float var17 = (var14 & 0xFF) / 255.0f;
                    GlStateManager.color(this.field_177184_f * var15, this.field_177185_g * var16, this.field_177192_h * var17, this.field_177187_e);
                    var12.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    this.field_177190_a.bindTexture(this.func_177178_a(var11, var13, "overlay"));
                }
                case 2:
                case 3:
                case 4:
                case 5: {
                    GlStateManager.color(this.field_177184_f, this.field_177185_g, this.field_177192_h, this.field_177187_e);
                    var12.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    break;
                }
            }
            if (!this.field_177193_i && var10.isItemEnchanted()) {
                this.func_177183_a(p_177182_1_, var12, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            }
        }
    }
    
    public ItemStack func_177176_a(final EntityLivingBase p_177176_1_, final int p_177176_2_) {
        return p_177176_1_.getCurrentArmor(p_177176_2_ - 1);
    }
    
    public ModelBase func_177175_a(final int p_177175_1_) {
        return this.func_177180_b(p_177175_1_) ? this.field_177189_c : this.field_177186_d;
    }
    
    private boolean func_177180_b(final int p_177180_1_) {
        return p_177180_1_ == 2;
    }
    
    private void func_177183_a(final EntityLivingBase p_177183_1_, final ModelBase p_177183_2_, final float p_177183_3_, final float p_177183_4_, final float p_177183_5_, final float p_177183_6_, final float p_177183_7_, final float p_177183_8_, final float p_177183_9_) {
        final float var10 = p_177183_1_.ticksExisted + p_177183_5_;
        this.field_177190_a.bindTexture(LayerArmorBase.field_177188_b);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        final float var11 = 0.5f;
        GlStateManager.color(var11, var11, var11, 1.0f);
        for (int var12 = 0; var12 < 2; ++var12) {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            final float var13 = 0.76f;
            GlStateManager.color(0.5f * var13, 0.25f * var13, 0.8f * var13, 1.0f);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            final float var14 = 0.33333334f;
            GlStateManager.scale(var14, var14, var14);
            GlStateManager.rotate(30.0f - var12 * 60.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.0f, var10 * (0.001f + var12 * 0.003f) * 20.0f, 0.0f);
            GlStateManager.matrixMode(5888);
            p_177183_2_.render(p_177183_1_, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
        }
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
    }
    
    private ResourceLocation func_177181_a(final ItemArmor p_177181_1_, final boolean p_177181_2_) {
        return this.func_177178_a(p_177181_1_, p_177181_2_, null);
    }
    
    private ResourceLocation func_177178_a(final ItemArmor p_177178_1_, final boolean p_177178_2_, final String p_177178_3_) {
        final String var4 = String.format("textures/models/armor/%s_layer_%d%s.png", p_177178_1_.getArmorMaterial().func_179242_c(), p_177178_2_ ? 2 : 1, (p_177178_3_ == null) ? "" : String.format("_%s", p_177178_3_));
        ResourceLocation var5 = LayerArmorBase.field_177191_j.get(var4);
        if (var5 == null) {
            var5 = new ResourceLocation(var4);
            LayerArmorBase.field_177191_j.put(var4, var5);
        }
        return var5;
    }
    
    protected abstract void func_177177_a();
    
    protected abstract void func_177179_a(final ModelBase p0, final int p1);
    
    static {
        field_177188_b = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        field_177191_j = Maps.newHashMap();
    }
    
    static final class SwitchArmorMaterial
    {
        static final int[] field_178747_a;
        private static final String __OBFID = "CL_00002427";
        
        static {
            field_178747_a = new int[ItemArmor.ArmorMaterial.values().length];
            try {
                SwitchArmorMaterial.field_178747_a[ItemArmor.ArmorMaterial.LEATHER.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchArmorMaterial.field_178747_a[ItemArmor.ArmorMaterial.CHAIN.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchArmorMaterial.field_178747_a[ItemArmor.ArmorMaterial.IRON.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchArmorMaterial.field_178747_a[ItemArmor.ArmorMaterial.GOLD.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchArmorMaterial.field_178747_a[ItemArmor.ArmorMaterial.DIAMOND.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
        }
    }
}
