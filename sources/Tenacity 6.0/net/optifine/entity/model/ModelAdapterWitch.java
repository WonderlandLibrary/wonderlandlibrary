// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityWitch;

public class ModelAdapterWitch extends ModelAdapter
{
    public ModelAdapterWitch() {
        super(EntityWitch.class, "witch", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelWitch(0.0f);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelWitch)) {
            return null;
        }
        final ModelWitch modelwitch = (ModelWitch)model;
        return (ModelRenderer)(modelPart.equals("mole") ? Reflector.getFieldValue(modelwitch, Reflector.ModelWitch_mole) : (modelPart.equals("hat") ? Reflector.getFieldValue(modelwitch, Reflector.ModelWitch_hat) : (modelPart.equals("head") ? modelwitch.villagerHead : (modelPart.equals("body") ? modelwitch.villagerBody : (modelPart.equals("arms") ? modelwitch.villagerArms : (modelPart.equals("left_leg") ? modelwitch.leftVillagerLeg : (modelPart.equals("right_leg") ? modelwitch.rightVillagerLeg : (modelPart.equals("nose") ? modelwitch.villagerNose : null))))))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "mole", "head", "body", "arms", "right_leg", "left_leg", "nose" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderWitch renderwitch = new RenderWitch(rendermanager);
        renderwitch.mainModel = modelBase;
        renderwitch.shadowSize = shadowSize;
        return renderwitch;
    }
}
