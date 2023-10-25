/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;

public class ModelAdapterCreeper
extends ModelAdapter {
    public ModelAdapterCreeper() {
        super(EntityCreeper.class, "creeper", 0.5f);
    }

    @Override
    public ModelBase makeModel() {
        return new ModelCreeper();
    }

    @Override
    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelCreeper)) {
            return null;
        }
        ModelCreeper modelcreeper = (ModelCreeper)model;
        if (modelPart.equals("head")) {
            return modelcreeper.head;
        }
        if (modelPart.equals("armor")) {
            return modelcreeper.creeperArmor;
        }
        if (modelPart.equals("body")) {
            return modelcreeper.body;
        }
        if (modelPart.equals("leg1")) {
            return modelcreeper.leg1;
        }
        if (modelPart.equals("leg2")) {
            return modelcreeper.leg2;
        }
        if (modelPart.equals("leg3")) {
            return modelcreeper.leg3;
        }
        return modelPart.equals("leg4") ? modelcreeper.leg4 : null;
    }

    @Override
    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderCreeper rendercreeper = new RenderCreeper(rendermanager);
        rendercreeper.mainModel = modelBase;
        rendercreeper.shadowSize = shadowSize;
        return rendercreeper;
    }
}

