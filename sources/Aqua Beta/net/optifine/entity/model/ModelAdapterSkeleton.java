package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterSkeleton
extends ModelAdapterBiped {
    public ModelAdapterSkeleton() {
        super(EntitySkeleton.class, "skeleton", 0.7f);
    }

    public ModelBase makeModel() {
        return new ModelSkeleton();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderSkeleton renderskeleton = new RenderSkeleton(rendermanager);
        Render.setModelBipedMain((RenderBiped)renderskeleton, (ModelBiped)((ModelBiped)modelBase));
        renderskeleton.mainModel = modelBase;
        renderskeleton.shadowSize = shadowSize;
        return renderskeleton;
    }
}
