package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "SimsESP", description = "ESP designed like the game Sims", category = Category.RENDER)
public class SimsESP extends Module {
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glFrontFace(GL11.GL_CW);
        int i = 0;
//        GlStateManager.disableCull();

        for (final EntityPlayer en : mc.theWorld.playerEntities) {
            if (en.isInvisible()) continue;

            if (en == mc.thePlayer) continue;

            i++;

            Color color = Color.GREEN;

            if (en.hurtTime > 0) color = Color.RED;

            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            RenderUtil.color(color);

            final double x = en.lastTickPosX + (en.posX - en.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
            final double y = (en.lastTickPosY + (en.posY - en.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY) + en.getEyeHeight() + .4 + Math.sin((System.currentTimeMillis() % 1000000) / 333f + i) / 10;
            final double z = en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;

            RenderUtil.color(color.darker().darker());
            GL11.glVertex3d(x, y, z);
            GL11.glVertex3d(x - 0.1, y + 0.3, z - 0.1);
            GL11.glVertex3d(x - 0.1, y + 0.3, z + 0.1);
            RenderUtil.color(color);
            GL11.glVertex3d(x + 0.1, y + 0.3, z);
            RenderUtil.color(color.darker().darker());
            GL11.glVertex3d(x, y, z);
            RenderUtil.color(color.darker().darker().darker());
            GL11.glVertex3d(x + 0.1, y + 0.3, z);
            GL11.glVertex3d(x - 0.1, y + 0.3, z - 0.1);

            GL11.glEnd();
        }


        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glFrontFace(GL11.GL_CCW);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glCullFace(GL11.GL_BACK);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();

        GL11.glColor3f(255, 255, 255);
    }
}
