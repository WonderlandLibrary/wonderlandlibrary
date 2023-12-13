/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.util.ResourceLocation;

public class RenderEndermite
extends RenderLiving {
    private static final ResourceLocation field_177108_a = new ResourceLocation("textures/entity/endermite.png");
    private static final String __OBFID = "CL_00002445";

    public RenderEndermite(RenderManager p_i46181_1_) {
        super(p_i46181_1_, new ModelEnderMite(), 0.3f);
    }

    protected float func_177107_a(EntityEndermite p_177107_1_) {
        return 180.0f;
    }

    protected ResourceLocation func_177106_b(EntityEndermite p_177106_1_) {
        return field_177108_a;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
        return this.func_177107_a((EntityEndermite)p_77037_1_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_177106_b((EntityEndermite)p_110775_1_);
    }
}

