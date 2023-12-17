// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityCow;

public class RenderCow extends RenderLiving<EntityCow>
{
    private static final ResourceLocation COW_TEXTURES;
    
    public RenderCow(final RenderManager p_i47210_1_) {
        super(p_i47210_1_, new ModelCow(), 0.7f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityCow entity) {
        return RenderCow.COW_TEXTURES;
    }
    
    static {
        COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");
    }
}
