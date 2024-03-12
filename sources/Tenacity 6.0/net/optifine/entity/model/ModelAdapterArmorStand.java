// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelAdapterArmorStand extends ModelAdapterBiped
{
    public ModelAdapterArmorStand() {
        super(EntityArmorStand.class, "armor_stand", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelArmorStand();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelArmorStand)) {
            return null;
        }
        final ModelArmorStand modelarmorstand = (ModelArmorStand)model;
        return modelPart.equals("right") ? modelarmorstand.standRightSide : (modelPart.equals("left") ? modelarmorstand.standLeftSide : (modelPart.equals("waist") ? modelarmorstand.standWaist : (modelPart.equals("base") ? modelarmorstand.standBase : super.getModelRenderer(modelarmorstand, modelPart))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        String[] astring = super.getModelRendererNames();
        astring = (String[])Config.addObjectsToArray(astring, new String[] { "right", "left", "waist", "base" });
        return astring;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final ArmorStandRenderer armorstandrenderer = new ArmorStandRenderer(rendermanager);
        armorstandrenderer.mainModel = modelBase;
        armorstandrenderer.shadowSize = shadowSize;
        return armorstandrenderer;
    }
}
