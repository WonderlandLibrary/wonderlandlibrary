/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;
import tk.rektsky.Client;
import tk.rektsky.event.impl.EventRender3D;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class NameTags
extends Module {
    public NameTags() {
        super("NameTags", "NameTags", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(EventRender3D event) {
        float partialTicks = this.mc.timer.renderPartialTicks;
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (player.getEntityId() == this.mc.thePlayer.getEntityId()) continue;
            double x2 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks - this.mc.renderManager.renderPosX;
            double y2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks - this.mc.renderManager.renderPosY;
            double z2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks - this.mc.renderManager.renderPosZ;
            this.renderLivingLabel(player, player.getName(), x2, y2, z2, 600);
        }
    }

    protected void renderLivingLabel(EntityPlayer entityIn, String str, double x2, double y2, double z2, int maxDistance) {
        RenderManager renderManager = this.mc.getRenderManager();
        double d0 = entityIn.getDistanceSqToEntity(renderManager.livingPlayer);
        if (d0 <= (double)(maxDistance * maxDistance)) {
            float f2 = 1.6f;
            float f1 = 0.016666668f * f2;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x2 + 0.0f, (float)y2 + entityIn.height + 0.5f, (float)z2);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i2 = 0;
            if (str.equals("deadmau5")) {
                i2 = -10;
            }
            int j2 = (int)(Client.getFont().getWidth(str) / 2.0f);
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-j2 - 1, -1 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(-j2 - 1, 8 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j2 + 1, 8 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j2 + 1, -1 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            Client.getFont().drawString(str, -Client.getFont().getWidth(str) / 2.0f, i2 - 1, 0x20FFFFFF);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            Client.getFont().drawString(str, -Client.getFont().getWidth(str) / 2.0f, i2 - 1, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
}

