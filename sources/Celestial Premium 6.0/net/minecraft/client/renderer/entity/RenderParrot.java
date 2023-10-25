/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderParrot
extends RenderLiving<EntityParrot> {
    public static final ResourceLocation[] field_192862_a = new ResourceLocation[]{new ResourceLocation("textures/entity/parrot/parrot_red_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_green.png"), new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_grey.png")};

    public RenderParrot(RenderManager p_i47375_1_) {
        super(p_i47375_1_, new ModelParrot(), 0.3f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityParrot entity) {
        return field_192862_a[entity.func_191998_ds()];
    }

    @Override
    public float handleRotationFloat(EntityParrot livingBase, float partialTicks) {
        return this.func_192861_b(livingBase, partialTicks);
    }

    private float func_192861_b(EntityParrot p_192861_1_, float p_192861_2_) {
        float f = p_192861_1_.field_192011_bE + (p_192861_1_.field_192008_bB - p_192861_1_.field_192011_bE) * p_192861_2_;
        float f1 = p_192861_1_.field_192010_bD + (p_192861_1_.field_192009_bC - p_192861_1_.field_192010_bD) * p_192861_2_;
        return (MathHelper.sin(f) + 1.0f) * f1;
    }
}

