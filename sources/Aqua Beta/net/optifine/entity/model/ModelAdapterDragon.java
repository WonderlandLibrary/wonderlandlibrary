package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorFields;

public class ModelAdapterDragon
extends ModelAdapter {
    public ModelAdapterDragon() {
        super(EntityDragon.class, "dragon", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelDragon(0.0f);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelDragon)) {
            return null;
        }
        ModelDragon modeldragon = (ModelDragon)model;
        return modelPart.equals((Object)"head") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)0) : (modelPart.equals((Object)"spine") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)1) : (modelPart.equals((Object)"jaw") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)2) : (modelPart.equals((Object)"body") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)3) : (modelPart.equals((Object)"rear_leg") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)4) : (modelPart.equals((Object)"front_leg") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)5) : (modelPart.equals((Object)"rear_leg_tip") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)6) : (modelPart.equals((Object)"front_leg_tip") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)7) : (modelPart.equals((Object)"rear_foot") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)8) : (modelPart.equals((Object)"front_foot") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)9) : (modelPart.equals((Object)"wing") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)10) : (modelPart.equals((Object)"wing_tip") ? (ModelRenderer)Reflector.getFieldValue((Object)modeldragon, (ReflectorFields)Reflector.ModelDragon_ModelRenderers, (int)11) : null)))))))))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "spine", "jaw", "body", "rear_leg", "front_leg", "rear_leg_tip", "front_leg_tip", "rear_foot", "front_foot", "wing", "wing_tip"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderDragon renderdragon = new RenderDragon(rendermanager);
        renderdragon.mainModel = modelBase;
        renderdragon.shadowSize = shadowSize;
        return renderdragon;
    }
}
