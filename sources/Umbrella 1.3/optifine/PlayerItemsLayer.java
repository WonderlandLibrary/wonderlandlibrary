/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.util.Map;
import java.util.Set;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import optifine.Config;
import optifine.PlayerConfigurations;

public class PlayerItemsLayer
implements LayerRenderer {
    private RenderPlayer renderPlayer = null;

    public PlayerItemsLayer(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entityLiving, float limbSwing, float limbSwingAmount, float partialTicks, float ticksExisted, float headYaw, float rotationPitch, float scale) {
        this.renderEquippedItems(entityLiving, scale, partialTicks);
    }

    protected void renderEquippedItems(EntityLivingBase entityLiving, float scale, float partialTicks) {
        if (Config.isShowCapes() && entityLiving instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer)entityLiving;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            ModelBiped modelBipedMain = (ModelBiped)this.renderPlayer.getMainModel();
            PlayerConfigurations.renderPlayerItems(modelBipedMain, player, scale, partialTicks);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public static void register(Map renderPlayerMap) {
        Set keys = renderPlayerMap.keySet();
        boolean registered = false;
        for (Object key : keys) {
            Object renderer = renderPlayerMap.get(key);
            if (!(renderer instanceof RenderPlayer)) continue;
            RenderPlayer renderPlayer = (RenderPlayer)renderer;
            renderPlayer.addLayer(new PlayerItemsLayer(renderPlayer));
            registered = true;
        }
        if (!registered) {
            Config.warn("PlayerItemsLayer not registered");
        }
    }
}

