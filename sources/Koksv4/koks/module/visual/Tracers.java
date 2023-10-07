package koks.module.visual;

import koks.Koks;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.Render3DEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Tracers", description = "It shows where other players are", category = Module.Category.VISUAL)
public class Tracers extends Module {

    @Value(name = "Width", minimum = 1, maximum = 5)
    double width = 2;

    @Value(name = "Player-Mode",modes = {"Head","Foot"})
    String playerMode = "Head";

    @Value(name = "Target-Mode",modes = {"Head","Foot"})
    String targetMode = "Foot";

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof Render3DEvent) {
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity instanceof EntityPlayer && entity != getPlayer() && !entity.isInvisible()) {
                    GL11.glPushMatrix();

                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glLineWidth((float) width);

                    drawLine(entity, Koks.getKoks().clientColor);

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);

                    GL11.glPopMatrix();
                }
            }
        }
    }

    public void drawLine(Entity entity, Color color) {
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(getTimer().renderPartialTicks);

        if (entity instanceof EntityPlayer player) {
            ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) player.getTeam();
            int i = 16777215;

            if (scoreplayerteam != null) {
                String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
                if (s.length() >= 2) {
                    if (mc.getRenderManager().getFontRenderer() != null && mc.getRenderManager().getFontRenderer().getColorCode(s.charAt(1)) != 0)
                        i = mc.getRenderManager().getFontRenderer().getColorCode(s.charAt(1));
                }
            }
            final float f1 = (float) (i >> 16 & 255) / 255.0F;
            final float f2 = (float) (i >> 8 & 255) / 255.0F;
            final float f = (float) (i & 255) / 255.0F;

            color = new Color(f1, f2, f);
        }

        final double distance = entity.getDistanceToEntity(getPlayer());
        if (distance <= 10)
            GL11.glColor4f(color.brighter().getRed() / 255F, color.brighter().getGreen() / 255F, color.brighter().getBlue() / 255F, color.brighter().getAlpha() / 255F);
        else if (distance >= 35)
            GL11.glColor4f(color.darker().getRed() / 255F, color.darker().getGreen() / 255F, color.darker().getBlue() / 255F, color.darker().getAlpha() / 255F);
        else
            GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        GL11.glBegin(GL11.GL_LINE_STRIP);


        final double xPos = (entity.lastTickPosX
                + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks)
                - mc.getRenderManager().renderPosX;
        final double yPos = (entity.lastTickPosY
                + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks)
                - mc.getRenderManager().renderPosY;
        final double zPos = (entity.lastTickPosZ
                + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks)
                - mc.getRenderManager().renderPosZ;

        final float targetEye = targetMode.equalsIgnoreCase("Head") ? entity.getEyeHeight() : 0;
        final float playerEye = playerMode.equalsIgnoreCase("Head") ? getPlayer().getEyeHeight() : 0;

        GL11.glVertex3d(xPos, yPos + targetEye, zPos);

        GL11.glVertex3d(0, playerEye, 0);
        GL11.glEnd();
        GL11.glColor4f(1, 1, 1, 1);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
