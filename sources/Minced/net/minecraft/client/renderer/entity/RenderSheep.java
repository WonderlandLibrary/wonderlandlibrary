// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntitySheep;

public class RenderSheep extends RenderLiving<EntitySheep>
{
    private static final ResourceLocation SHEARED_SHEEP_TEXTURES;
    
    public RenderSheep(final RenderManager p_i47195_1_) {
        super(p_i47195_1_, new ModelSheep2(), 0.7f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerSheepWool(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySheep entity) {
        return RenderSheep.SHEARED_SHEEP_TEXTURES;
    }
    
    static {
        SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");
    }
}
