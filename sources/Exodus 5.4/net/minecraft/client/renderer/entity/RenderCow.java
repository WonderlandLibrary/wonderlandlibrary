/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

public class RenderCow
extends RenderLiving<EntityCow> {
    private static final ResourceLocation cowTextures = new ResourceLocation("textures/entity/cow/cow.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityCow entityCow) {
        return cowTextures;
    }

    public RenderCow(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }
}

