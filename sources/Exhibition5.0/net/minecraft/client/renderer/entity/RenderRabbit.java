// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderRabbit extends RenderLiving
{
    private static final ResourceLocation field_177127_a;
    private static final ResourceLocation field_177126_e;
    private static final ResourceLocation field_177132_j;
    private static final ResourceLocation field_177133_k;
    private static final ResourceLocation field_177130_l;
    private static final ResourceLocation field_177131_m;
    private static final ResourceLocation field_177128_n;
    private static final ResourceLocation field_177129_o;
    private static final String __OBFID = "CL_00002432";
    
    public RenderRabbit(final RenderManager p_i46146_1_, final ModelBase p_i46146_2_, final float p_i46146_3_) {
        super(p_i46146_1_, p_i46146_2_, p_i46146_3_);
    }
    
    protected ResourceLocation func_177125_a(final EntityRabbit p_177125_1_) {
        final String var2 = EnumChatFormatting.getTextWithoutFormattingCodes(p_177125_1_.getName());
        if (var2 != null && var2.equals("Toast")) {
            return RenderRabbit.field_177128_n;
        }
        switch (p_177125_1_.func_175531_cl()) {
            default: {
                return RenderRabbit.field_177127_a;
            }
            case 1: {
                return RenderRabbit.field_177126_e;
            }
            case 2: {
                return RenderRabbit.field_177132_j;
            }
            case 3: {
                return RenderRabbit.field_177131_m;
            }
            case 4: {
                return RenderRabbit.field_177133_k;
            }
            case 5: {
                return RenderRabbit.field_177130_l;
            }
            case 99: {
                return RenderRabbit.field_177129_o;
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_177125_a((EntityRabbit)p_110775_1_);
    }
    
    static {
        field_177127_a = new ResourceLocation("textures/entity/rabbit/brown.png");
        field_177126_e = new ResourceLocation("textures/entity/rabbit/white.png");
        field_177132_j = new ResourceLocation("textures/entity/rabbit/black.png");
        field_177133_k = new ResourceLocation("textures/entity/rabbit/gold.png");
        field_177130_l = new ResourceLocation("textures/entity/rabbit/salt.png");
        field_177131_m = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
        field_177128_n = new ResourceLocation("textures/entity/rabbit/toast.png");
        field_177129_o = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
    }
}
