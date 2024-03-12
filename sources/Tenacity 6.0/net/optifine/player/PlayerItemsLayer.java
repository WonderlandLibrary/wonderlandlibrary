// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.src.Config;
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
            final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entityLiving;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            final ModelBiped modelbiped = this.renderPlayer.getMainModel();
            PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, scale, partialTicks);
            GlStateManager.disableCull();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    public static void register(final Map renderPlayerMap) {
        final Set set = renderPlayerMap.keySet();
        boolean flag = false;
        for (final Object object : set) {
            final Object object2 = renderPlayerMap.get(object);
            if (object2 instanceof RenderPlayer) {
                final RenderPlayer renderplayer = (RenderPlayer)object2;
                ((RendererLivingEntity<EntityLivingBase>)renderplayer).addLayer(new PlayerItemsLayer(renderplayer));
                flag = true;
            }
        }
        if (!flag) {
            Config.warn("PlayerItemsLayer not registered");
        }
    }
}
