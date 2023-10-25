/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterEnderCrystal;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterEnderCrystalNoBase
extends ModelAdapterEnderCrystal {
    public ModelAdapterEnderCrystalNoBase() {
        super("end_crystal_no_base");
    }

    @Override
    public ModelBase makeModel() {
        return new ModelEnderCrystal(0.0f, false);
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        Render render = rendermanager.getEntityRenderMap().get(EntityEnderCrystal.class);
        if (!(render instanceof RenderEnderCrystal)) {
            Config.warn("Not an instance of RenderEnderCrystal: " + render);
            return null;
        }
        RenderEnderCrystal renderendercrystal = (RenderEnderCrystal)render;
        if (!Reflector.RenderEnderCrystal_modelEnderCrystalNoBase.exists()) {
            Config.warn("Field not found: RenderEnderCrystal.modelEnderCrystalNoBase");
            return null;
        }
        Reflector.setFieldValue(renderendercrystal, Reflector.RenderEnderCrystal_modelEnderCrystalNoBase, modelBase);
        renderendercrystal.shadowSize = shadowSize;
        return renderendercrystal;
    }
}

