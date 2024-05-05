package best.azura.client.impl.module.impl.render.cape;

import best.azura.client.impl.ui.AnimatedTexture;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.textures.JordanTextureUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class AnimatedCapeRendererImpl extends LayerCape {

    private final RenderPlayer playerRenderer;
    private AnimatedTexture animatedTexture;
    private long lastUpdateTime;

    public AnimatedCapeRendererImpl(RenderPlayer playerRendererIn) {
        super(playerRendererIn);
        this.playerRenderer = playerRendererIn;
    }

    public void setAnimatedTexture(AnimatedTexture animatedTexture) {
        this.animatedTexture = animatedTexture;
    }

    public AnimatedTexture getAnimatedTexture() {
        return animatedTexture;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) && entitylivingbaseIn.getLocationCape() != null) {
            if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE)) {

                if (animatedTexture == null) {
                    if (entitylivingbaseIn.getLocationCape() == null) return;
                    this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
                } else {
                    if (lastUpdateTime == 0) lastUpdateTime = System.currentTimeMillis() - (500L / animatedTexture.getTextures().size());
                    if (!animatedTexture.getTextures().isEmpty() &&
                            (animatedTexture.getCurrentTexture() == null || System.currentTimeMillis() - lastUpdateTime > ((10L * animatedTexture.getCurrentTexture().getDelay())))) {
                        animatedTexture.updateIndex();
                        lastUpdateTime = System.currentTimeMillis();
                    }
                    if (animatedTexture.getCurrentTexture() == null) return;
                    entitylivingbaseIn.setLocationOfCape(JordanTextureUtil.getResourceFromImage(animatedTexture.getCurrentTexture().getBufferedImage()));
                    this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
                }

                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.125F);
                double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double) partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double) partialTicks);
                double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double) partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double) partialTicks);
                double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double) partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double) partialTicks);
                float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
                double d3 = MathHelper.sin(f * (float) Math.PI / 180.0F);
                double d4 = (-MathHelper.cos(f * (float) Math.PI / 180.0F));
                float f1 = (float) d1 * 10.0F;
                f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
                float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                if (f2 > 165.0F) {
                    f2 = 165.0F;
                }

                if (f1 < -5.0F) {
                    f1 = -5.0F;
                }

                float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
                f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

                if (entitylivingbaseIn.isSneaking()) {
                    f1 += 25.0F;
                    GlStateManager.translate(0.0F, 0.142F, -0.0178F);
                }

                GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                this.playerRenderer.getMainModel().renderCape(0.0625F);
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.resetColor();
                GlStateManager.popMatrix();
            }
        }
    }
}
