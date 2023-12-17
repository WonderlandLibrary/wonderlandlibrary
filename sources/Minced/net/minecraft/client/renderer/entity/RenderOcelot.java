// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityOcelot;

public class RenderOcelot extends RenderLiving<EntityOcelot>
{
    private static final ResourceLocation BLACK_OCELOT_TEXTURES;
    private static final ResourceLocation OCELOT_TEXTURES;
    private static final ResourceLocation RED_OCELOT_TEXTURES;
    private static final ResourceLocation SIAMESE_OCELOT_TEXTURES;
    
    public RenderOcelot(final RenderManager p_i47199_1_) {
        super(p_i47199_1_, new ModelOcelot(), 0.4f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityOcelot entity) {
        switch (entity.getTameSkin()) {
            default: {
                return RenderOcelot.OCELOT_TEXTURES;
            }
            case 1: {
                return RenderOcelot.BLACK_OCELOT_TEXTURES;
            }
            case 2: {
                return RenderOcelot.RED_OCELOT_TEXTURES;
            }
            case 3: {
                return RenderOcelot.SIAMESE_OCELOT_TEXTURES;
            }
        }
    }
    
    @Override
    protected void preRenderCallback(final EntityOcelot entitylivingbaseIn, final float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        if (entitylivingbaseIn.isTamed()) {
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
        }
    }
    
    static {
        BLACK_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/black.png");
        OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/ocelot.png");
        RED_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/red.png");
        SIAMESE_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/siamese.png");
    }
}
