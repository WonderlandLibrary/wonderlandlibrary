// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderSheep extends RenderLiving
{
    private static final ResourceLocation shearedSheepTextures;
    private static final String __OBFID = "CL_00001021";
    
    public RenderSheep(final RenderManager p_i46145_1_, final ModelBase p_i46145_2_, final float p_i46145_3_) {
        super(p_i46145_1_, p_i46145_2_, p_i46145_3_);
        this.addLayer(new LayerSheepWool(this));
    }
    
    protected ResourceLocation getEntityTexture(final EntitySheep p_110775_1_) {
        return RenderSheep.shearedSheepTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntitySheep)p_110775_1_);
    }
    
    static {
        shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");
    }
}
