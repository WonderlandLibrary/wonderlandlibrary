package dev.echo.module.impl.render;

import dev.echo.listener.Link;
import dev.echo.listener.Listener;
import dev.echo.utils.tuples.Pair;
import dev.echo.listener.event.impl.player.MotionEvent;
import dev.echo.listener.event.impl.render.Render3DEvent;
import dev.echo.module.Category;
import dev.echo.module.Module;
import dev.echo.module.settings.impl.NumberSetting;
import dev.echo.utils.animations.Animation;
import dev.echo.utils.animations.Direction;
import dev.echo.utils.animations.impl.DecelerateAnimation;
import dev.echo.utils.render.ColorUtil;
import dev.echo.utils.render.GLUtil;
import dev.echo.utils.render.RenderUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class JumpCircle extends Module {

    private static final NumberSetting radius = new NumberSetting("Radius", 2.5, 10, 1, .25);

    public JumpCircle() {
        super("JumpCircle", Category.RENDER, "Draws a circle when you land on the ground.");
        addSettings(radius);
    }

    private boolean inAir = false;

    private final List<Circle> circles = new ArrayList<>();
    private final List<Circle> toRemove = new ArrayList<>();

    @Link
    public Listener<MotionEvent> motionEventListener = e -> {
        if (e.isPre()) {
            if (!e.isOnGround()) {
                inAir = true;
            } else if (e.isOnGround() && inAir) {
                circles.add(new Circle(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
                inAir = false;
            }
        }
    };

    @Link
    public Listener<Render3DEvent> onRender3D = e -> {
        for (Circle circle : circles) {
            circle.drawCircle();
            if (circle.fadeAnimation != null && circle.fadeAnimation.finished(Direction.BACKWARDS)) {
                toRemove.add(circle);
            }
        }

        for (Circle circle : toRemove) {
            circles.remove(circle);
        }
    };


    private static class Circle {
        private final float x, y, z;
        private final Animation expandAnimation;
        private final Animation fadeAnimation;


        public Circle(double x, double y, double z) {
            this.x = (float) x;
            this.y = (float) y;
            this.z = (float) z;
            this.fadeAnimation = new DecelerateAnimation(600, 1);
            this.expandAnimation = new DecelerateAnimation(1000, radius.getValue());
        }


        public void drawCircle() {
            Pair<Color, Color> colors = HUDMod.getClientColors();
            if (expandAnimation.getOutput() > (radius.getValue() * .7f)) {
                fadeAnimation.setDirection(Direction.BACKWARDS);
            }


            glPushMatrix();
            RenderUtil.setAlphaLimit(0);

            float animation = expandAnimation.getOutput().floatValue();
            float fade = fadeAnimation.getOutput().floatValue();
            GLUtil.setup2DRendering();
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            glShadeModel(GL_SMOOTH);
            double pi2 = MathHelper.PI2;

            double xVal = x - mc.getRenderManager().renderPosX;
            double yVal = y - mc.getRenderManager().renderPosY;
            double zVal = z - mc.getRenderManager().renderPosZ;

            glBegin(GL_TRIANGLE_STRIP);

            int color1 = colors.getSecond().getRGB();
            int color2 = colors.getFirst().getRGB();



            float newAnim = (float) Math.max(0, animation - (.3f * (animation / expandAnimation.getEndPoint())));

            for (int i = 0; i <= 90; ++i) {
                float value = (float) Math.sin((i * 4) * (MathHelper.PI / 180));
                int color = ColorUtil.interpolateColor(color1, color2, Math.abs(value));
                RenderUtil.color(color, fade * .6f);
                glVertex3d(xVal + animation * Math.cos(i * pi2 / 45), yVal, zVal + animation * Math.sin(i * pi2 / 45));


                RenderUtil.color(color, fade * .15f);
                glVertex3d(xVal + newAnim * Math.cos(i * pi2 / 45), yVal, zVal + newAnim * Math.sin(i * pi2 / 45));
            }

            glEnd();


            glShadeModel(GL_FLAT);
            glDepthMask(true);
            glEnable(GL_DEPTH_TEST);
            GLUtil.end2DRendering();
            glPopMatrix();
            RenderUtil.resetColor();
            RenderUtil.color(-1);

        }

    }

}
