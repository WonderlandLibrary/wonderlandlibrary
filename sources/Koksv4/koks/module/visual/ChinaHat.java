package koks.module.visual;

import koks.Koks;
import koks.api.PlayerHandler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.RenderUtil;
import koks.event.Render3DEvent;
import koks.event.ValueChangeEvent;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @credits: Getter (Traded)
 */

@Module.Info(name = "ChinaHat", description = "You and your friends have China Hat", category = Module.Category.VISUAL)
public class ChinaHat extends Module implements Module.AlwaysEvent {

    @Value(name = "Radius", minimum = 0.1, maximum = 2)
    double radius = 0.6;

    @Value(name = "Color-1", displayName = "Color", colorPicker = true)
    int color1 = 0x6A9FFC;

    @Value(name = "Color-2", displayName = "Color", colorPicker = true)
    int color2 = 0x6AFCE8;

    @Value(name = "Color-3", displayName = "Color", colorPicker = true)
    int color3 = 0xFC6A8C;

    @Value(name = "Color-4", displayName = "Color", colorPicker = true)
    int color4 = 0x8740F9;

    private final int[] colours = {0x6A9FFC, 0x6AFCE8, 0xFC6A8C, 0x8740F9};
    private final double[][] positions = new double[181][2];
    private final int[] segmentColours = new int[181];
    private final int points = 40;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof final Render3DEvent render3DEvent) {
            final RenderUtil renderUtil = RenderUtil.getInstance();
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_CULL_FACE);
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glDepthMask(false);
            glDisable(GL_DEPTH_TEST);
            glShadeModel(GL_SMOOTH);
            glLineWidth(2);
            glEnable(GL_BLEND);
            getWorld().playerEntities.stream().filter(player -> player == getPlayer() || Koks.getKoks().friendManager.isFriend(player.getName())).forEach(player -> {
                if(getGameSettings().thirdPersonView == 0 && player == getPlayer()) return;
                final double xPos = (player.lastTickPosX
                        + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks)
                        - mc.getRenderManager().renderPosX;
                final double yPos = (player.lastTickPosY
                        + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks)
                        - mc.getRenderManager().renderPosY;
                final double zPos = (player.lastTickPosZ
                        + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks)
                        - mc.getRenderManager().renderPosZ;

                final int pointsPerColour = points / 4;

                for(int i = 0; i < segmentColours.length; i++) {
                    final int colourIndex = i / pointsPerColour;
                    this.segmentColours[i] = fadeBetween(getColourByIndex(colourIndex), getColourByIndex(colourIndex + 1), (float) (i % pointsPerColour) / pointsPerColour);
                }

                glPushMatrix();
                glTranslated(xPos, yPos + (player.isSneaking() ? 1.5 : 2 - radius / 3), zPos);
                glRotated((PlayerHandler.prevYaw
                        + (getYaw() - PlayerHandler.prevYaw) * render3DEvent.getRenderPartialTicks()), 0, -1, 0);
                final double pitch = (PlayerHandler.prevPitch
                        + (getPitch() - PlayerHandler.prevPitch) * render3DEvent.getRenderPartialTicks());
                glRotated(pitch / 3, 1, 0, 0);
                glTranslated(0, 0, pitch / 270);

                glBegin(GL_LINE_LOOP);
                for(int i = 0; i < points; i++) {
                    final double[] pos = this.positions[i];
                    renderUtil.setColor(new Color(this.segmentColours[i] + (0xFF << 24)));
                    glVertex3d(pos[0], 0, pos[1]);
                }
                glEnd();

                glBegin(GL_TRIANGLE_FAN);
                glVertex3d(0, radius / 2, 0);
                for(int i = 0; i <= points; i++) {
                    final double[] pos = this.positions[i];
                    renderUtil.setColor(new Color(this.segmentColours[i] + (0x80 << 24)));
                    glVertex3d(pos[0], 0, pos[1]);
                }

                glEnd();
                glPopMatrix();
            });
            glDisable(GL_BLEND);
            glDepthMask(true);
            glShadeModel(GL_FLAT);
            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
            glEnable(GL_TEXTURE_2D);
            glColor4f(1,1,1,1);
        }
    }

    private void computeChineseHatPoints(final int points, final double radius) {
        for (int i = 0; i <= points; i++) {
            final double circleX = radius * StrictMath.cos(i * Math.PI * 2 / points);
            final double circleZ = radius * StrictMath.sin(i * Math.PI * 2 / points);

            this.positions[i][0] = circleX;
            this.positions[i][1] = circleZ;
        }
    }

    private int getColourByIndex(final int index) {
        return index >= 0 && index < this.colours.length ? this.colours[index] : this.colours[0];
    }

    @Override
    public void onEnable() {
        computeChineseHatPoints(points, radius);
        colours[0] = color1;
        colours[1] = color2;
        colours[2] = color3;
        colours[3] = color4;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void alwaysEvent(Event event) {
        if(event instanceof ValueChangeEvent) {
            colours[0] = color1;
            colours[1] = color2;
            colours[2] = color3;
            colours[3] = color4;
            computeChineseHatPoints(40,radius);
        }
    }
}
