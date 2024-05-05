package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityEnderman;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterBiped;

public class ModelAdapterEnderman
extends ModelAdapterBiped {
    public ModelAdapterEnderman() {
        super(EntityEnderman.class, "enderman", 0.5f);
    }

    public ModelBase makeModel() {
        return new ModelEnderman(0.0f);
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderEnderman renderenderman = new RenderEnderman(rendermanager);
        renderenderman.mainModel = modelBase;
        renderenderman.shadowSize = shadowSize;
        return renderenderman;
    }
}
