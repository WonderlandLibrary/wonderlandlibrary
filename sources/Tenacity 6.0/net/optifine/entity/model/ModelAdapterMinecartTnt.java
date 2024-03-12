// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.entity.RenderTntMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.item.EntityMinecartTNT;

public class ModelAdapterMinecartTnt extends ModelAdapterMinecart
{
    public ModelAdapterMinecartTnt() {
        super(EntityMinecartTNT.class, "tnt_minecart", 0.5f);
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderTntMinecart rendertntminecart = new RenderTntMinecart(rendermanager);
        if (!Reflector.RenderMinecart_modelMinecart.exists()) {
            Config.warn("Field not found: RenderMinecart.modelMinecart");
            return null;
        }
        Reflector.setFieldValue(rendertntminecart, Reflector.RenderMinecart_modelMinecart, modelBase);
        rendertntminecart.shadowSize = shadowSize;
        return rendertntminecart;
    }
}
