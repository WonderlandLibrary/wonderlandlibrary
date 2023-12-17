// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelBase;

public class ModelAdapterEnderCrystalNoBase extends ModelAdapterEnderCrystal
{
    public ModelAdapterEnderCrystalNoBase() {
        super("end_crystal_no_base");
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelEnderCrystal(0.0f, false);
    }
    
    @Override
    public String[] getModelRendererNames() {
        String[] astring = super.getModelRendererNames();
        astring = (String[])Config.removeObjectFromArray(astring, "base");
        return astring;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final Render render = rendermanager.getEntityRenderMap().get(EntityEnderCrystal.class);
        if (!(render instanceof RenderEnderCrystal)) {
            Config.warn("Not an instance of RenderEnderCrystal: " + render);
            return null;
        }
        final RenderEnderCrystal renderendercrystal = (RenderEnderCrystal)render;
        if (!Reflector.RenderEnderCrystal_modelEnderCrystalNoBase.exists()) {
            Config.warn("Field not found: RenderEnderCrystal.modelEnderCrystalNoBase");
            return null;
        }
        Reflector.setFieldValue(renderendercrystal, Reflector.RenderEnderCrystal_modelEnderCrystalNoBase, modelBase);
        renderendercrystal.shadowSize = shadowSize;
        return renderendercrystal;
    }
}
