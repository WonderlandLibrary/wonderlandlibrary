// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityAreaEffectCloud;

public class RenderAreaEffectCloud extends Render<EntityAreaEffectCloud>
{
    public RenderAreaEffectCloud(final RenderManager manager) {
        super(manager);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final EntityAreaEffectCloud entity) {
        return null;
    }
}
