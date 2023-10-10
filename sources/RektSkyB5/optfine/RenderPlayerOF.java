/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import optfine.Config;
import optfine.PlayerConfigurations;
import optfine.Reflector;

public class RenderPlayerOF
extends RenderPlayer {
    public RenderPlayerOF(RenderManager p_i62_1_, boolean p_i62_2_) {
        super(p_i62_1_, p_i62_2_);
    }

    @Override
    protected void renderLayers(AbstractClientPlayer entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
        super.renderLayers(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
        this.renderEquippedItems(entitylivingbaseIn, p_177093_8_, partialTicks);
    }

    protected void renderEquippedItems(EntityLivingBase p_renderEquippedItems_1_, float p_renderEquippedItems_2_, float p_renderEquippedItems_3_) {
        if (p_renderEquippedItems_1_ instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_renderEquippedItems_1_;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            ModelBiped modelbiped = (ModelBiped)this.mainModel;
            PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, p_renderEquippedItems_2_, p_renderEquippedItems_3_);
        }
    }

    public static void register() {
        RenderManager rendermanager = Config.getMinecraft().getRenderManager();
        Map map = RenderPlayerOF.getMapRenderTypes(rendermanager);
        if (map == null) {
            Config.warn("RenderPlayerOF init() failed: RenderManager.MapRenderTypes not found");
        } else {
            map.put("default", new RenderPlayerOF(rendermanager, false));
            map.put("slim", new RenderPlayerOF(rendermanager, true));
        }
    }

    private static Map getMapRenderTypes(RenderManager p_getMapRenderTypes_0_) {
        try {
            Field[] afield = Reflector.getFields(RenderManager.class, Map.class);
            for (int i2 = 0; i2 < afield.length; ++i2) {
                Object object;
                Field field = afield[i2];
                Map map = (Map)field.get(p_getMapRenderTypes_0_);
                if (map == null || !((object = map.get("default")) instanceof RenderPlayer)) continue;
                return map;
            }
            return null;
        }
        catch (Exception exception) {
            Config.warn("Error getting RenderManager.mapRenderTypes");
            Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
            return null;
        }
    }
}

