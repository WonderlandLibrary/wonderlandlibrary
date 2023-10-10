/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.skinlayers.renderLayers;

import com.google.common.collect.Sets;
import com.wallhacks.losebypass.systems.module.modules.render.ThreeDSkins;
import com.wallhacks.losebypass.utils.skinlayers.SkinUtil;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HeadLayerFeatureRenderer
implements LayerRenderer<AbstractClientPlayer> {
    private Set<Item> hideHeadLayers = Sets.newHashSet(Items.skull);
    private final boolean thinArms;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private RenderPlayer playerRenderer;

    public HeadLayerFeatureRenderer(RenderPlayer playerRenderer) {
        this.thinArms = playerRenderer.smallArms;
        this.playerRenderer = playerRenderer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float paramFloat1, float paramFloat2, float paramFloat3, float deltaTick, float paramFloat5, float paramFloat6, float paramFloat7) {
        if (!player.hasSkin()) return;
        if (player.isInvisible()) {
            return;
        }
        if (HeadLayerFeatureRenderer.mc.thePlayer == null) return;
        if (HeadLayerFeatureRenderer.mc.thePlayer.getPositionVector().squareDistanceTo(player.getPositionVector()) > (double)ThreeDSkins.range()) {
            return;
        }
        ItemStack itemStack = player.getEquipmentInSlot(1);
        if (itemStack != null && this.hideHeadLayers.contains(itemStack.getItem())) {
            return;
        }
        if (player.getHeadLayers() == null && !this.setupModel(player)) {
            return;
        }
        this.renderCustomHelmet(player, deltaTick);
    }

    private boolean setupModel(AbstractClientPlayer abstractClientPlayerEntity) {
        if (!SkinUtil.hasCustomSkin(abstractClientPlayerEntity)) {
            return false;
        }
        SkinUtil.setup3dLayers(abstractClientPlayerEntity, this.thinArms, null);
        return true;
    }

    public void renderCustomHelmet(AbstractClientPlayer abstractClientPlayer, float deltaTick) {
        if (abstractClientPlayer.getHeadLayers() == null) {
            return;
        }
        if (this.playerRenderer.getMainModel().bipedHead.isHidden) {
            return;
        }
        float voxelSize = 1.18f;
        GlStateManager.pushMatrix();
        if (abstractClientPlayer.isSneaking()) {
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
        }
        this.playerRenderer.getMainModel().bipedHead.postRender(0.0625f);
        GlStateManager.scale(0.0625, 0.0625, 0.0625);
        GlStateManager.scale(voxelSize, voxelSize, voxelSize);
        boolean tintRed = abstractClientPlayer.hurtTime > 0 || abstractClientPlayer.deathTime > 0;
        abstractClientPlayer.getHeadLayers().render(tintRed);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

