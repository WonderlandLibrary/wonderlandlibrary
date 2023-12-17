// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;

public class RenderElderGuardian extends RenderGuardian
{
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE;
    
    public RenderElderGuardian(final RenderManager p_i47209_1_) {
        super(p_i47209_1_);
    }
    
    @Override
    protected void preRenderCallback(final EntityGuardian entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(2.35f, 2.35f, 2.35f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityGuardian entity) {
        return RenderElderGuardian.GUARDIAN_ELDER_TEXTURE;
    }
    
    static {
        GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
    }
}
