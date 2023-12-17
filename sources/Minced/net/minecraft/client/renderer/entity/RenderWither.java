// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWither;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.boss.EntityWither;

public class RenderWither extends RenderLiving<EntityWither>
{
    private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES;
    private static final ResourceLocation WITHER_TEXTURES;
    
    public RenderWither(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelWither(0.0f), 1.0f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerWitherAura(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityWither entity) {
        final int i = entity.getInvulTime();
        return (i > 0 && (i > 80 || i / 5 % 2 != 1)) ? RenderWither.INVULNERABLE_WITHER_TEXTURES : RenderWither.WITHER_TEXTURES;
    }
    
    @Override
    protected void preRenderCallback(final EntityWither entitylivingbaseIn, final float partialTickTime) {
        float f = 2.0f;
        final int i = entitylivingbaseIn.getInvulTime();
        if (i > 0) {
            f -= (i - partialTickTime) / 220.0f * 0.5f;
        }
        GlStateManager.scale(f, f, f);
    }
    
    static {
        INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");
    }
}
