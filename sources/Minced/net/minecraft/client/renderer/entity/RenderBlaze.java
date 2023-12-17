// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityBlaze;

public class RenderBlaze extends RenderLiving<EntityBlaze>
{
    private static final ResourceLocation BLAZE_TEXTURES;
    
    public RenderBlaze(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBlaze(), 0.5f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityBlaze entity) {
        return RenderBlaze.BLAZE_TEXTURES;
    }
    
    static {
        BLAZE_TEXTURES = new ResourceLocation("textures/entity/blaze.png");
    }
}
