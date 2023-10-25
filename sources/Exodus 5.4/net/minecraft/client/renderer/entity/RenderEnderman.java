/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;

public class RenderEnderman
extends RenderLiving<EntityEnderman> {
    private static final ResourceLocation endermanTextures = new ResourceLocation("textures/entity/enderman/enderman.png");
    private Random rnd = new Random();
    private ModelEnderman endermanModel = (ModelEnderman)this.mainModel;

    @Override
    protected ResourceLocation getEntityTexture(EntityEnderman entityEnderman) {
        return endermanTextures;
    }

    public RenderEnderman(RenderManager renderManager) {
        super(renderManager, new ModelEnderman(0.0f), 0.5f);
        this.addLayer(new LayerEndermanEyes(this));
        this.addLayer(new LayerHeldBlock(this));
    }

    @Override
    public void doRender(EntityEnderman entityEnderman, double d, double d2, double d3, float f, float f2) {
        this.endermanModel.isCarrying = entityEnderman.getHeldBlockState().getBlock().getMaterial() != Material.air;
        this.endermanModel.isAttacking = entityEnderman.isScreaming();
        if (entityEnderman.isScreaming()) {
            double d4 = 0.02;
            d += this.rnd.nextGaussian() * d4;
            d3 += this.rnd.nextGaussian() * d4;
        }
        super.doRender(entityEnderman, d, d2, d3, f, f2);
    }
}

