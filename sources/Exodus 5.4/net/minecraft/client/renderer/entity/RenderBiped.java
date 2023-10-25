/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderBiped<T extends EntityLiving>
extends RenderLiving<T> {
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;
    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");

    public RenderBiped(RenderManager renderManager, ModelBiped modelBiped, float f) {
        this(renderManager, modelBiped, f, 1.0f);
        this.addLayer(new LayerHeldItem(this));
    }

    public RenderBiped(RenderManager renderManager, ModelBiped modelBiped, float f, float f2) {
        super(renderManager, modelBiped, f);
        this.modelBipedMain = modelBiped;
        this.field_77070_b = f2;
        this.addLayer(new LayerCustomHead(modelBiped.bipedHead));
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }

    @Override
    protected ResourceLocation getEntityTexture(T t) {
        return DEFAULT_RES_LOC;
    }
}

