/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderOcelot
extends RenderLiving<EntityOcelot> {
    private static final ResourceLocation redOcelotTextures;
    private static final ResourceLocation ocelotTextures;
    private static final ResourceLocation siameseOcelotTextures;
    private static final ResourceLocation blackOcelotTextures;

    static {
        blackOcelotTextures = new ResourceLocation("textures/entity/cat/black.png");
        ocelotTextures = new ResourceLocation("textures/entity/cat/ocelot.png");
        redOcelotTextures = new ResourceLocation("textures/entity/cat/red.png");
        siameseOcelotTextures = new ResourceLocation("textures/entity/cat/siamese.png");
    }

    @Override
    protected void preRenderCallback(EntityOcelot entityOcelot, float f) {
        super.preRenderCallback(entityOcelot, f);
        if (entityOcelot.isTamed()) {
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
        }
    }

    public RenderOcelot(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityOcelot entityOcelot) {
        switch (entityOcelot.getTameSkin()) {
            default: {
                return ocelotTextures;
            }
            case 1: {
                return blackOcelotTextures;
            }
            case 2: {
                return redOcelotTextures;
            }
            case 3: 
        }
        return siameseOcelotTextures;
    }
}

