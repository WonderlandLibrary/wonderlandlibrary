// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.AbstractSkeleton;

public class RenderSkeleton extends RenderBiped<AbstractSkeleton>
{
    private static final ResourceLocation SKELETON_TEXTURES;
    
    public RenderSkeleton(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSkeleton(), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = (T)new ModelSkeleton(0.5f, true);
                this.modelArmor = (T)new ModelSkeleton(1.0f, true);
            }
        });
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.09375f, 0.1875f, 0.0f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final AbstractSkeleton entity) {
        return RenderSkeleton.SKELETON_TEXTURES;
    }
    
    static {
        SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    }
}
