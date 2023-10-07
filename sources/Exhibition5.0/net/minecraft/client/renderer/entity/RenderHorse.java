// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class RenderHorse extends RenderLiving
{
    private static final Map field_110852_a;
    private static final ResourceLocation whiteHorseTextures;
    private static final ResourceLocation muleTextures;
    private static final ResourceLocation donkeyTextures;
    private static final ResourceLocation zombieHorseTextures;
    private static final ResourceLocation skeletonHorseTextures;
    private static final String __OBFID = "CL_00001000";
    
    public RenderHorse(final RenderManager p_i46170_1_, final ModelHorse p_i46170_2_, final float p_i46170_3_) {
        super(p_i46170_1_, p_i46170_2_, p_i46170_3_);
    }
    
    protected void func_180580_a(final EntityHorse p_180580_1_, final float p_180580_2_) {
        float var3 = 1.0f;
        final int var4 = p_180580_1_.getHorseType();
        if (var4 == 1) {
            var3 *= 0.87f;
        }
        else if (var4 == 2) {
            var3 *= 0.92f;
        }
        GlStateManager.scale(var3, var3, var3);
        super.preRenderCallback(p_180580_1_, p_180580_2_);
    }
    
    protected ResourceLocation func_180581_a(final EntityHorse p_180581_1_) {
        if (p_180581_1_.func_110239_cn()) {
            return this.func_110848_b(p_180581_1_);
        }
        switch (p_180581_1_.getHorseType()) {
            default: {
                return RenderHorse.whiteHorseTextures;
            }
            case 1: {
                return RenderHorse.donkeyTextures;
            }
            case 2: {
                return RenderHorse.muleTextures;
            }
            case 3: {
                return RenderHorse.zombieHorseTextures;
            }
            case 4: {
                return RenderHorse.skeletonHorseTextures;
            }
        }
    }
    
    private ResourceLocation func_110848_b(final EntityHorse p_110848_1_) {
        final String var2 = p_110848_1_.getHorseTexture();
        if (!p_110848_1_.func_175507_cI()) {
            return null;
        }
        ResourceLocation var3 = RenderHorse.field_110852_a.get(var2);
        if (var3 == null) {
            var3 = new ResourceLocation(var2);
            Minecraft.getMinecraft().getTextureManager().loadTexture(var3, new LayeredTexture(p_110848_1_.getVariantTexturePaths()));
            RenderHorse.field_110852_a.put(var2, var3);
        }
        return var3;
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.func_180580_a((EntityHorse)p_77041_1_, p_77041_2_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180581_a((EntityHorse)p_110775_1_);
    }
    
    static {
        field_110852_a = Maps.newHashMap();
        whiteHorseTextures = new ResourceLocation("textures/entity/horse/horse_white.png");
        muleTextures = new ResourceLocation("textures/entity/horse/mule.png");
        donkeyTextures = new ResourceLocation("textures/entity/horse/donkey.png");
        zombieHorseTextures = new ResourceLocation("textures/entity/horse/horse_zombie.png");
        skeletonHorseTextures = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    }
}
