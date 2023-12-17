// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGhast;

public class RenderGhast extends RenderLiving<EntityGhast>
{
    private static final ResourceLocation GHAST_TEXTURES;
    private static final ResourceLocation GHAST_SHOOTING_TEXTURES;
    
    public RenderGhast(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGhast(), 0.5f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityGhast entity) {
        return entity.isAttacking() ? RenderGhast.GHAST_SHOOTING_TEXTURES : RenderGhast.GHAST_TEXTURES;
    }
    
    @Override
    protected void preRenderCallback(final EntityGhast entitylivingbaseIn, final float partialTickTime) {
        final float f = 1.0f;
        final float f2 = 4.5f;
        final float f3 = 4.5f;
        GlStateManager.scale(4.5f, 4.5f, 4.5f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        GHAST_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast.png");
        GHAST_SHOOTING_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
    }
}
