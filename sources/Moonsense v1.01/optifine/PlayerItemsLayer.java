// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class PlayerItemsLayer implements LayerRenderer
{
    private RenderPlayer renderPlayer;
    
    public PlayerItemsLayer(final RenderPlayer renderPlayer) {
        this.renderPlayer = null;
        this.renderPlayer = renderPlayer;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entityLiving, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ticksExisted, final float headYaw, final float rotationPitch, final float scale) {
        this.renderEquippedItems(entityLiving, scale, partialTicks);
    }
    
    protected void renderEquippedItems(final EntityLivingBase entityLiving, final float scale, final float partialTicks) {
        if (Config.isShowCapes() && entityLiving instanceof AbstractClientPlayer) {
            final AbstractClientPlayer player = (AbstractClientPlayer)entityLiving;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            final ModelBiped modelBipedMain = (ModelBiped)this.renderPlayer.getMainModel();
            PlayerConfigurations.renderPlayerItems(modelBipedMain, player, scale, partialTicks);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    public static void register(final Map renderPlayerMap) {
        final Set keys = renderPlayerMap.keySet();
        boolean registered = false;
        for (final Object key : keys) {
            final Object renderer = renderPlayerMap.get(key);
            if (renderer instanceof RenderPlayer) {
                final RenderPlayer renderPlayer = (RenderPlayer)renderer;
                renderPlayer.addLayer(new PlayerItemsLayer(renderPlayer));
                registered = true;
            }
        }
        if (!registered) {
            Config.warn("PlayerItemsLayer not registered");
        }
    }
}
