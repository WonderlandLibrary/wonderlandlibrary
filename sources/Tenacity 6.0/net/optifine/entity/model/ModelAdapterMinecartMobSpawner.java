// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.entity.RenderMinecartMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;

public class ModelAdapterMinecartMobSpawner extends ModelAdapterMinecart
{
    public ModelAdapterMinecartMobSpawner() {
        super(EntityMinecartMobSpawner.class, "spawner_minecart", 0.5f);
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderMinecartMobSpawner renderminecartmobspawner = new RenderMinecartMobSpawner(rendermanager);
        if (!Reflector.RenderMinecart_modelMinecart.exists()) {
            Config.warn("Field not found: RenderMinecart.modelMinecart");
            return null;
        }
        Reflector.setFieldValue(renderminecartmobspawner, Reflector.RenderMinecart_modelMinecart, modelBase);
        renderminecartmobspawner.shadowSize = shadowSize;
        return renderminecartmobspawner;
    }
}
