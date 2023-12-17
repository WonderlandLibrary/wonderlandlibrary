// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntitySnowman;

public class RenderSnowMan extends RenderLiving<EntitySnowman>
{
    private static final ResourceLocation SNOW_MAN_TEXTURES;
    
    public RenderSnowMan(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSnowMan(), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerSnowmanHead(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySnowman entity) {
        return RenderSnowMan.SNOW_MAN_TEXTURES;
    }
    
    @Override
    public ModelSnowMan getMainModel() {
        return (ModelSnowMan)super.getMainModel();
    }
    
    static {
        SNOW_MAN_TEXTURES = new ResourceLocation("textures/entity/snowman.png");
    }
}
