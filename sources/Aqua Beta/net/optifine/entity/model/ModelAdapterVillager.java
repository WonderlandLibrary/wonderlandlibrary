package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;

public class ModelAdapterVillager
extends ModelAdapter {
    public ModelAdapterVillager() {
        super(EntityVillager.class, "villager", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelVillager(0.0f);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelVillager)) {
            return null;
        }
        ModelVillager modelvillager = (ModelVillager)model;
        return modelPart.equals((Object)"head") ? modelvillager.villagerHead : (modelPart.equals((Object)"body") ? modelvillager.villagerBody : (modelPart.equals((Object)"arms") ? modelvillager.villagerArms : (modelPart.equals((Object)"left_leg") ? modelvillager.leftVillagerLeg : (modelPart.equals((Object)"right_leg") ? modelvillager.rightVillagerLeg : (modelPart.equals((Object)"nose") ? modelvillager.villagerNose : null)))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "body", "arms", "right_leg", "left_leg", "nose"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderVillager rendervillager = new RenderVillager(rendermanager);
        rendervillager.mainModel = modelBase;
        rendervillager.shadowSize = shadowSize;
        return rendervillager;
    }
}
