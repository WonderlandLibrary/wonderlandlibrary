// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.Utils;
import net.minecraft.util.MathHelper;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import java.util.Iterator;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import java.util.List;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class JumpCircle extends Module
{
    private static final NumberSetting radius;
    private boolean inAir;
    private final List<Circle> circles;
    private final List<Circle> toRemove;
    
    public JumpCircle() {
        super("JumpCircle", "Jump Circle", Category.RENDER, "Draws a circle when you land on the ground.");
        this.inAir = false;
        this.circles = new ArrayList<Circle>();
        this.toRemove = new ArrayList<Circle>();
        this.addSettings(JumpCircle.radius);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (event.isPre()) {
            if (!event.isOnGround()) {
                this.inAir = true;
            }
            else if (event.isOnGround() && this.inAir) {
                this.circles.add(new Circle(JumpCircle.mc.thePlayer.posX, JumpCircle.mc.thePlayer.posY, JumpCircle.mc.thePlayer.posZ));
                this.inAir = false;
            }
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        for (final Circle circle : this.circles) {
            circle.drawCircle();
            if (circle.fadeAnimation != null && circle.fadeAnimation.finished(Direction.BACKWARDS)) {
                this.toRemove.add(circle);
            }
        }
        for (final Circle circle : this.toRemove) {
            this.circles.remove(circle);
        }
    }
    
    static {
        radius = new NumberSetting("Radius", 2.5, 10.0, 1.0, 0.25);
    }
    
    private static class Circle
    {
        private final float x;
        private final float y;
        private final float z;
        private final Animation expandAnimation;
        private final Animation fadeAnimation;
        
        public Circle(final double x, final double y, final double z) {
            this.x = (float)x;
            this.y = (float)y;
            this.z = (float)z;
            this.fadeAnimation = new DecelerateAnimation(600, 1.0);
            this.expandAnimation = new DecelerateAnimation(1000, JumpCircle.radius.getValue());
        }
        
        public void drawCircle() {
            final Pair<Color, Color> colors = HUDMod.getClientColors();
            if (this.expandAnimation.getOutput() > JumpCircle.radius.getValue() * 0.699999988079071) {
                this.fadeAnimation.setDirection(Direction.BACKWARDS);
            }
            GL11.glPushMatrix();
            RenderUtil.setAlphaLimit(0.0f);
            final float animation = this.expandAnimation.getOutput().floatValue();
            final float fade = this.fadeAnimation.getOutput().floatValue();
            GLUtil.setup2DRendering();
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glShadeModel(7425);
            final double pi2 = MathHelper.PI2;
            final double xVal = this.x - Utils.mc.getRenderManager().renderPosX;
            final double yVal = this.y - Utils.mc.getRenderManager().renderPosY;
            final double zVal = this.z - Utils.mc.getRenderManager().renderPosZ;
            GL11.glBegin(5);
            final int color1 = colors.getSecond().getRGB();
            final int color2 = colors.getFirst().getRGB();
            final float newAnim = (float)Math.max(0.0, animation - 0.30000001192092896 * (animation / this.expandAnimation.getEndPoint()));
            for (int i = 0; i <= 90; ++i) {
                final float value = (float)Math.sin(i * 4 * (MathHelper.PI / 180.0f));
                final int color3 = ColorUtil.interpolateColor(color1, color2, Math.abs(value));
                RenderUtil.color(color3, fade * 0.6f);
                GL11.glVertex3d(xVal + animation * Math.cos(i * pi2 / 45.0), yVal, zVal + animation * Math.sin(i * pi2 / 45.0));
                RenderUtil.color(color3, fade * 0.15f);
                GL11.glVertex3d(xVal + newAnim * Math.cos(i * pi2 / 45.0), yVal, zVal + newAnim * Math.sin(i * pi2 / 45.0));
            }
            GL11.glEnd();
            GL11.glShadeModel(7424);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GLUtil.end2DRendering();
            GL11.glPopMatrix();
            RenderUtil.resetColor();
            RenderUtil.color(-1);
        }
    }
}
