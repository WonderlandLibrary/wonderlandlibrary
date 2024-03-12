// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.boss.EntityDragon;

public class ModelAdapterDragon extends ModelAdapter
{
    public ModelAdapterDragon() {
        super(EntityDragon.class, "dragon", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelDragon(0.0f);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelDragon)) {
            return null;
        }
        final ModelDragon modeldragon = (ModelDragon)model;
        return (ModelRenderer)(modelPart.equals("head") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 0) : (modelPart.equals("spine") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 1) : (modelPart.equals("jaw") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 2) : (modelPart.equals("body") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 3) : (modelPart.equals("rear_leg") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 4) : (modelPart.equals("front_leg") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 5) : (modelPart.equals("rear_leg_tip") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 6) : (modelPart.equals("front_leg_tip") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 7) : (modelPart.equals("rear_foot") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 8) : (modelPart.equals("front_foot") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 9) : (modelPart.equals("wing") ? Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 10) : (modelPart.equals("wing_tip") ? ((ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 11)) : null))))))))))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "spine", "jaw", "body", "rear_leg", "front_leg", "rear_leg_tip", "front_leg_tip", "rear_foot", "front_foot", "wing", "wing_tip" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderDragon renderdragon = new RenderDragon(rendermanager);
        renderdragon.mainModel = modelBase;
        renderdragon.shadowSize = shadowSize;
        return renderdragon;
    }
}
