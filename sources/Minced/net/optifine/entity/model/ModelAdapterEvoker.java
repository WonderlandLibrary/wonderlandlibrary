// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderEvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityEvoker;

public class ModelAdapterEvoker extends ModelAdapterIllager
{
    public ModelAdapterEvoker() {
        super(EntityEvoker.class, "evocation_illager", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelIllager(0.0f, 0.0f, 64, 64);
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderEvoker renderevoker = new RenderEvoker(rendermanager);
        renderevoker.mainModel = modelBase;
        renderevoker.shadowSize = shadowSize;
        return renderevoker;
    }
}
