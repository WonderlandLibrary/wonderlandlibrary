package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterArmorStand
extends ModelAdapterBiped {
    public ModelAdapterArmorStand() {
        super(EntityArmorStand.class, "armor_stand", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelArmorStand();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelArmorStand)) {
            return null;
        }
        ModelArmorStand modelarmorstand = (ModelArmorStand)model;
        return modelPart.equals((Object)"right") ? modelarmorstand.standRightSide : (modelPart.equals((Object)"left") ? modelarmorstand.standLeftSide : (modelPart.equals((Object)"waist") ? modelarmorstand.standWaist : (modelPart.equals((Object)"base") ? modelarmorstand.standBase : super.getModelRenderer((ModelBase)modelarmorstand, modelPart))));
    }

    public String[] getModelRendererNames() {
        Object[] astring = super.getModelRendererNames();
        astring = (String[])Config.addObjectsToArray((Object[])astring, (Object[])new String[]{"right", "left", "waist", "base"});
        return astring;
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        ArmorStandRenderer armorstandrenderer = new ArmorStandRenderer(rendermanager);
        armorstandrenderer.mainModel = modelBase;
        armorstandrenderer.shadowSize = shadowSize;
        return armorstandrenderer;
    }
}
