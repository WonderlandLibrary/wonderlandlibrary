// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.util.ResourceLocation;

public class RenderCreeper extends RenderLiving
{
    private static final ResourceLocation creeperTextures;
    private static final String __OBFID = "CL_00000985";
    
    public RenderCreeper(final RenderManager p_i46186_1_) {
        super(p_i46186_1_, new ModelCreeper(), 0.5f);
        this.addLayer(new LayerCreeperCharge(this));
    }
    
    protected void func_180570_a(final EntityCreeper p_180570_1_, final float p_180570_2_) {
        float var3 = p_180570_1_.getCreeperFlashIntensity(p_180570_2_);
        final float var4 = 1.0f + MathHelper.sin(var3 * 100.0f) * var3 * 0.01f;
        var3 = MathHelper.clamp_float(var3, 0.0f, 1.0f);
        var3 *= var3;
        var3 *= var3;
        final float var5 = (1.0f + var3 * 0.4f) * var4;
        final float var6 = (1.0f + var3 * 0.1f) / var4;
        GlStateManager.scale(var5, var6, var5);
    }
    
    protected int func_180571_a(final EntityCreeper p_180571_1_, final float p_180571_2_, final float p_180571_3_) {
        final float var4 = p_180571_1_.getCreeperFlashIntensity(p_180571_3_);
        if ((int)(var4 * 10.0f) % 2 == 0) {
            return 0;
        }
        int var5 = (int)(var4 * 0.2f * 255.0f);
        var5 = MathHelper.clamp_int(var5, 0, 255);
        return var5 << 24 | 0xFFFFFF;
    }
    
    protected ResourceLocation getEntityTexture(final EntityCreeper p_110775_1_) {
        return RenderCreeper.creeperTextures;
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.func_180570_a((EntityCreeper)p_77041_1_, p_77041_2_);
    }
    
    @Override
    protected int getColorMultiplier(final EntityLivingBase p_77030_1_, final float p_77030_2_, final float p_77030_3_) {
        return this.func_180571_a((EntityCreeper)p_77030_1_, p_77030_2_, p_77030_3_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityCreeper)p_110775_1_);
    }
    
    static {
        creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
    }
}
