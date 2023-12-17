// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.cosmetics;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public abstract class Cosmetic implements LayerRenderer<AbstractClientPlayer>
{
    protected final ModelBiped playerModel;
    protected final RenderPlayer playerRenderer;
    
    public Cosmetic(final RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
        this.playerModel = playerRendererIn.getMainModel();
    }
    
    public abstract void render(final AbstractClientPlayer p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible()) {
            this.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
