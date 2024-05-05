package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterWitch
extends ModelAdapter {
    public ModelAdapterWitch() {
        super(EntityWitch.class, "witch", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelWitch(0.0f);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelWitch)) {
            return null;
        }
        ModelWitch modelwitch = (ModelWitch)model;
        return modelPart.equals((Object)"mole") ? (ModelRenderer)Reflector.getFieldValue((Object)modelwitch, (ReflectorField)Reflector.ModelWitch_mole) : (modelPart.equals((Object)"hat") ? (ModelRenderer)Reflector.getFieldValue((Object)modelwitch, (ReflectorField)Reflector.ModelWitch_hat) : (modelPart.equals((Object)"head") ? modelwitch.villagerHead : (modelPart.equals((Object)"body") ? modelwitch.villagerBody : (modelPart.equals((Object)"arms") ? modelwitch.villagerArms : (modelPart.equals((Object)"left_leg") ? modelwitch.leftVillagerLeg : (modelPart.equals((Object)"right_leg") ? modelwitch.rightVillagerLeg : (modelPart.equals((Object)"nose") ? modelwitch.villagerNose : null)))))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"mole", "head", "body", "arms", "right_leg", "left_leg", "nose"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWitch renderwitch = new RenderWitch(rendermanager);
        renderwitch.mainModel = modelBase;
        renderwitch.shadowSize = shadowSize;
        return renderwitch;
    }
}
