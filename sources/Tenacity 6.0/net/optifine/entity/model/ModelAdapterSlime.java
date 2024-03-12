// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntitySlime;

public class ModelAdapterSlime extends ModelAdapter
{
    public ModelAdapterSlime() {
        super(EntitySlime.class, "slime", 0.25f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelSlime(16);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelSlime)) {
            return null;
        }
        final ModelSlime modelslime = (ModelSlime)model;
        return (ModelRenderer)(modelPart.equals("body") ? Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 0) : (modelPart.equals("left_eye") ? Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 1) : (modelPart.equals("right_eye") ? Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 2) : (modelPart.equals("mouth") ? ((ModelRenderer)Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 3)) : null))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "body", "left_eye", "right_eye", "mouth" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderSlime renderslime = new RenderSlime(rendermanager, modelBase, shadowSize);
        return renderslime;
    }
}
