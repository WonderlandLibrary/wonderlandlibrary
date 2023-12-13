// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.util.ResourceLocation;

public class LayerWolfCollar implements LayerRenderer
{
    private static final ResourceLocation field_177147_a;
    private final RenderWolf field_177146_b;
    private static final String __OBFID = "CL_00002405";
    
    public LayerWolfCollar(final RenderWolf p_i46104_1_) {
        this.field_177146_b = p_i46104_1_;
    }
    
    public void func_177145_a(final EntityWolf p_177145_1_, final float p_177145_2_, final float p_177145_3_, final float p_177145_4_, final float p_177145_5_, final float p_177145_6_, final float p_177145_7_, final float p_177145_8_) {
        if (p_177145_1_.isTamed() && !p_177145_1_.isInvisible()) {
            this.field_177146_b.bindTexture(LayerWolfCollar.field_177147_a);
            final EnumDyeColor var9 = EnumDyeColor.func_176764_b(p_177145_1_.func_175546_cu().func_176765_a());
            final float[] var10 = EntitySheep.func_175513_a(var9);
            GlStateManager.color(var10[0], var10[1], var10[2]);
            this.field_177146_b.getMainModel().render(p_177145_1_, p_177145_2_, p_177145_3_, p_177145_5_, p_177145_6_, p_177145_7_, p_177145_8_);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.func_177145_a((EntityWolf)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
    
    static {
        field_177147_a = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    }
}
