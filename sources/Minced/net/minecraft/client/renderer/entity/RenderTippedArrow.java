// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityTippedArrow;

public class RenderTippedArrow extends RenderArrow<EntityTippedArrow>
{
    public static final ResourceLocation RES_ARROW;
    public static final ResourceLocation RES_TIPPED_ARROW;
    
    public RenderTippedArrow(final RenderManager manager) {
        super(manager);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityTippedArrow entity) {
        return (entity.getColor() > 0) ? RenderTippedArrow.RES_TIPPED_ARROW : RenderTippedArrow.RES_ARROW;
    }
    
    static {
        RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
        RES_TIPPED_ARROW = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");
    }
}
