package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTntMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.src.Config;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapterMinecart;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterMinecartTnt
extends ModelAdapterMinecart {
    public ModelAdapterMinecartTnt() {
        super(EntityMinecartTNT.class, "tnt_minecart", 0.5f);
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderTntMinecart rendertntminecart = new RenderTntMinecart(rendermanager);
        if (!Reflector.RenderMinecart_modelMinecart.exists()) {
            Config.warn((String)"Field not found: RenderMinecart.modelMinecart");
            return null;
        }
        Reflector.setFieldValue((Object)rendertntminecart, (ReflectorField)Reflector.RenderMinecart_modelMinecart, (Object)modelBase);
        rendertntminecart.shadowSize = shadowSize;
        return rendertntminecart;
    }
}
