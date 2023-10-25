/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterHeadDragon
extends ModelAdapter {
    public ModelAdapterHeadDragon() {
        super(TileEntitySkull.class, "head_dragon", 0.0f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelDragonHead(0.0f);
    }

    @Override
    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelDragonHead)) {
            return null;
        }
        ModelDragonHead modeldragonhead = (ModelDragonHead)model;
        if (modelPart.equals("head")) {
            return (ModelRenderer)Reflector.getFieldValue(modeldragonhead, Reflector.ModelDragonHead_head);
        }
        return modelPart.equals("jaw") ? (ModelRenderer)Reflector.getFieldValue(modeldragonhead, Reflector.ModelDragonHead_jaw) : null;
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySkullRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntitySkull.class);
        if (!(tileentityspecialrenderer instanceof TileEntitySkullRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntitySkullRenderer();
            ((TileEntitySpecialRenderer)tileentityspecialrenderer).setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntitySkullRenderer_dragonHead.exists()) {
            Config.warn("Field not found: TileEntitySkullRenderer.dragonHead");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntitySkullRenderer_dragonHead, modelBase);
        return tileentityspecialrenderer;
    }
}

