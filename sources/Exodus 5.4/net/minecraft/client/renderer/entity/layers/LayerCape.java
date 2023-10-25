/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;

public class LayerCape
implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    @Override
    public void doRenderLayer(AbstractClientPlayer abstractClientPlayer, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (abstractClientPlayer.hasPlayerInfo() && !abstractClientPlayer.isInvisible() && abstractClientPlayer.isWearing(EnumPlayerModelParts.CAPE) && abstractClientPlayer.getLocationCape() != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.playerRenderer.bindTexture(abstractClientPlayer.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            double d = abstractClientPlayer.prevChasingPosX + (abstractClientPlayer.chasingPosX - abstractClientPlayer.prevChasingPosX) * (double)f3 - (abstractClientPlayer.prevPosX + (abstractClientPlayer.posX - abstractClientPlayer.prevPosX) * (double)f3);
            double d2 = abstractClientPlayer.prevChasingPosY + (abstractClientPlayer.chasingPosY - abstractClientPlayer.prevChasingPosY) * (double)f3 - (abstractClientPlayer.prevPosY + (abstractClientPlayer.posY - abstractClientPlayer.prevPosY) * (double)f3);
            double d3 = abstractClientPlayer.prevChasingPosZ + (abstractClientPlayer.chasingPosZ - abstractClientPlayer.prevChasingPosZ) * (double)f3 - (abstractClientPlayer.prevPosZ + (abstractClientPlayer.posZ - abstractClientPlayer.prevPosZ) * (double)f3);
            float f8 = abstractClientPlayer.prevRenderYawOffset + (abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset) * f3;
            double d4 = MathHelper.sin(f8 * (float)Math.PI / 180.0f);
            double d5 = -MathHelper.cos(f8 * (float)Math.PI / 180.0f);
            float f9 = (float)d2 * 10.0f;
            f9 = MathHelper.clamp_float(f9, -6.0f, 32.0f);
            float f10 = (float)(d * d4 + d3 * d5) * 100.0f;
            float f11 = (float)(d * d5 - d3 * d4) * 100.0f;
            if (f10 < 0.0f) {
                f10 = 0.0f;
            }
            float f12 = abstractClientPlayer.prevCameraYaw + (abstractClientPlayer.cameraYaw - abstractClientPlayer.prevCameraYaw) * f3;
            f9 += MathHelper.sin((abstractClientPlayer.prevDistanceWalkedModified + (abstractClientPlayer.distanceWalkedModified - abstractClientPlayer.prevDistanceWalkedModified) * f3) * 6.0f) * 32.0f * f12;
            if (abstractClientPlayer.isSneaking()) {
                f9 += 25.0f;
            }
            GlStateManager.rotate(6.0f + f10 / 2.0f + f9, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f11 / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-f11 / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            this.playerRenderer.getMainModel().renderCape(0.0625f);
            GlStateManager.popMatrix();
        }
    }

    public LayerCape(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

