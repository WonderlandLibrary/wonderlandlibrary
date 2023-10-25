/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderChicken
extends RenderLiving<EntityChicken> {
    private static final ResourceLocation chickenTextures = new ResourceLocation("textures/entity/chicken.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityChicken entityChicken) {
        return chickenTextures;
    }

    public RenderChicken(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    @Override
    protected float handleRotationFloat(EntityChicken entityChicken, float f) {
        float f2 = entityChicken.field_70888_h + (entityChicken.wingRotation - entityChicken.field_70888_h) * f;
        float f3 = entityChicken.field_70884_g + (entityChicken.destPos - entityChicken.field_70884_g) * f;
        return (MathHelper.sin(f2) + 1.0f) * f3;
    }
}

