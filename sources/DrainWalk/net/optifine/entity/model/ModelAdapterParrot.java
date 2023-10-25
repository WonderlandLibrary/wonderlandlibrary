package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.optifine.reflect.Reflector;

public class ModelAdapterParrot extends ModelAdapter
{
    public ModelAdapterParrot()
    {
        super(EntityParrot.class, "parrot", 0.3F);
    }

    public ModelBase makeModel()
    {
        return new ModelParrot();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelParrot))
        {
            return null;
        }
        else
        {
            ModelParrot modelparrot = (ModelParrot)model;

            if (modelPart.equals("body"))
            {
                return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 0);
            }
            else if (modelPart.equals("tail"))
            {
                return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 1);
            }
            else if (modelPart.equals("left_wing"))
            {
                return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 2);
            }
            else if (modelPart.equals("right_wing"))
            {
                return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 3);
            }
            else if (modelPart.equals("head"))
            {
                return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 4);
            }
            else if (modelPart.equals("left_leg"))
            {
                return (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 9);
            }
            else
            {
                return modelPart.equals("right_leg") ? (ModelRenderer)Reflector.getFieldValue(modelparrot, Reflector.ModelParrot_ModelRenderers, 10) : null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"body", "tail", "left_wing", "right_wing", "head", "left_leg", "right_leg"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderParrot renderparrot = new RenderParrot(rendermanager);
        renderparrot.mainModel = modelBase;
        renderparrot.shadowSize = shadowSize;
        return renderparrot;
    }
}
