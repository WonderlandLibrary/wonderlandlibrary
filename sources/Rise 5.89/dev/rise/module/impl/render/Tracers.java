package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Tracers", description = "Draws a line to every player", category = Category.RENDER)
public final class Tracers extends Module {

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        for (final EntityPlayer playerEntity : mc.theWorld.playerEntities) {
            if (playerEntity != mc.thePlayer && !playerEntity.bot && !playerEntity.isDead && !playerEntity.isInvisible()) // Distance check to fix a bug where it renders players far away that have been rendered before
                drawToPlayer(playerEntity);
        }
        RenderUtil.color(Color.WHITE);
    }

    public void drawToPlayer(final EntityLivingBase entity) {
        final Color color = ThemeUtil.getThemeColor(0, ThemeType.GENERAL, 0.5f);

        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;

        final double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().viewerPosX;
        final double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().viewerPosY;
        final double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().viewerPosZ;

        render(red, green, blue, xPos, yPos, zPos);
    }

    public void render(final float red, final float green, final float blue,
                       final double x, final double y, final double z) {
        drawTracerLine(x, y, z, red, green, blue, 0.5F, 1.5F);
    }

    public static void drawTracerLine(final double x, final double y, final double z,
                                      final float red, final float green, final float blue,
                                      final float alpha, final float lineWidth) {
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
