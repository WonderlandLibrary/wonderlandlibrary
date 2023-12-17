// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.item.EntityBoat;

public class ModelAdapterBoat extends ModelAdapter
{
    public ModelAdapterBoat() {
        super(EntityBoat.class, "boat", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBoat();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBoat)) {
            return null;
        }
        final ModelBoat modelboat = (ModelBoat)model;
        if (modelPart.equals("bottom")) {
            return modelboat.boatSides[0];
        }
        if (modelPart.equals("back")) {
            return modelboat.boatSides[1];
        }
        if (modelPart.equals("front")) {
            return modelboat.boatSides[2];
        }
        if (modelPart.equals("right")) {
            return modelboat.boatSides[3];
        }
        if (modelPart.equals("left")) {
            return modelboat.boatSides[4];
        }
        if (modelPart.equals("paddle_left")) {
            return modelboat.paddles[0];
        }
        if (modelPart.equals("paddle_right")) {
            return modelboat.paddles[1];
        }
        return modelPart.equals("bottom_no_water") ? modelboat.noWater : null;
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "bottom", "back", "front", "right", "left", "paddle_left", "paddle_right", "bottom_no_water" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderBoat renderboat = new RenderBoat(rendermanager);
        if (!Reflector.RenderBoat_modelBoat.exists()) {
            Config.warn("Field not found: RenderBoat.modelBoat");
            return null;
        }
        Reflector.setFieldValue(renderboat, Reflector.RenderBoat_modelBoat, modelBase);
        renderboat.shadowSize = shadowSize;
        return renderboat;
    }
}
