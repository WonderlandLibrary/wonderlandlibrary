package net.minecraft.client.renderer.entity.layers;

import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerCape extends LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;

    public LayerCape(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }

    private static final ResourceLocation CUSTOM_CAPE = new ResourceLocation("ketamine/cape.png");

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (!entitylivingbaseIn.hasPlayerInfo() || entitylivingbaseIn.isInvisible()) return;

        final ResourceLocation capeLocation = entitylivingbaseIn instanceof EntityPlayerSP ? CUSTOM_CAPE :
            entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) ? entitylivingbaseIn.getLocationCape() : null;

        if (capeLocation != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.playerRenderer.bindTexture(capeLocation);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
            double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
            double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
            final float f;
            final UpdatePositionEvent event;
            if (entitylivingbaseIn instanceof EntityPlayerSP && (event = ((EntityPlayerSP) entitylivingbaseIn).lastEvent) != null && event.isRotating()) {
                f = DrawUtil.interpolate(event.getLastTickYaw(), event.getYaw(), partialTicks);
            } else {
                f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            }
            double d3 = (double)MathHelper.sin(f * (float)Math.PI / 180.0F);
            double d4 = (double)(-MathHelper.cos(f * (float)Math.PI / 180.0F));
            float f1 = (float)d1 * 10.0F;
            f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

            if (entitylivingbaseIn.isSneaking())
            {
                f1 += 25.0F;
            }

            GL11.glRotatef(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.playerRenderer.getMainModel().renderCape(0.0625F);
            GL11.glPopMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
