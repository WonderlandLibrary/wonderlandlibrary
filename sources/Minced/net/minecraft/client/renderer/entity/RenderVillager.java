// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityVillager;

public class RenderVillager extends RenderLiving<EntityVillager>
{
    private static final ResourceLocation VILLAGER_TEXTURES;
    private static final ResourceLocation FARMER_VILLAGER_TEXTURES;
    private static final ResourceLocation LIBRARIAN_VILLAGER_TEXTURES;
    private static final ResourceLocation PRIEST_VILLAGER_TEXTURES;
    private static final ResourceLocation SMITH_VILLAGER_TEXTURES;
    private static final ResourceLocation BUTCHER_VILLAGER_TEXTURES;
    
    public RenderVillager(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVillager(0.0f), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }
    
    @Override
    public ModelVillager getMainModel() {
        return (ModelVillager)super.getMainModel();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityVillager entity) {
        switch (entity.getProfession()) {
            case 0: {
                return RenderVillager.FARMER_VILLAGER_TEXTURES;
            }
            case 1: {
                return RenderVillager.LIBRARIAN_VILLAGER_TEXTURES;
            }
            case 2: {
                return RenderVillager.PRIEST_VILLAGER_TEXTURES;
            }
            case 3: {
                return RenderVillager.SMITH_VILLAGER_TEXTURES;
            }
            case 4: {
                return RenderVillager.BUTCHER_VILLAGER_TEXTURES;
            }
            default: {
                return RenderVillager.VILLAGER_TEXTURES;
            }
        }
    }
    
    @Override
    protected void preRenderCallback(final EntityVillager entitylivingbaseIn, final float partialTickTime) {
        float f = 0.9375f;
        if (entitylivingbaseIn.getGrowingAge() < 0) {
            f *= 0.5;
            this.shadowSize = 0.25f;
        }
        else {
            this.shadowSize = 0.5f;
        }
        GlStateManager.scale(f, f, f);
    }
    
    static {
        VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/villager.png");
        FARMER_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/farmer.png");
        LIBRARIAN_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/librarian.png");
        PRIEST_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/priest.png");
        SMITH_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/smith.png");
        BUTCHER_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/butcher.png");
    }
}
