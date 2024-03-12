// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityIronGolem;

public class ModelAdapterIronGolem extends ModelAdapter
{
    public ModelAdapterIronGolem() {
        super(EntityIronGolem.class, "iron_golem", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelIronGolem();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelIronGolem)) {
            return null;
        }
        final ModelIronGolem modelirongolem = (ModelIronGolem)model;
        return modelPart.equals("head") ? modelirongolem.ironGolemHead : (modelPart.equals("body") ? modelirongolem.ironGolemBody : (modelPart.equals("left_arm") ? modelirongolem.ironGolemLeftArm : (modelPart.equals("right_arm") ? modelirongolem.ironGolemRightArm : (modelPart.equals("left_leg") ? modelirongolem.ironGolemLeftLeg : (modelPart.equals("right_leg") ? modelirongolem.ironGolemRightLeg : null)))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "head", "body", "right_arm", "left_arm", "left_leg", "right_leg" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderIronGolem renderirongolem = new RenderIronGolem(rendermanager);
        renderirongolem.mainModel = modelBase;
        renderirongolem.shadowSize = shadowSize;
        return renderirongolem;
    }
}
