package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.MathematicHelper;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JumpCircle extends Feature {

    static final int TYPE = 0;
    static final byte MAX_JC_TIME = 20;
    static List<Circle> circles = new ArrayList();
    public static ColorSetting jumpCircleColor = new ColorSetting("JumpCircle Color", new Color(0xFFFFFF).getRGB(), () -> true);
    static float pt;
    public static NumberSetting radius = new NumberSetting("Radius", 0.3F, 0.1F, 3, 0.1f, () -> true);
    public static NumberSetting size = new NumberSetting("Shadow Size", 1, 1, 3, 0.1f, () -> true);
    public static NumberSetting speed = new NumberSetting("Speed", 1, 1, 3, 0.1f, () -> true);

    public JumpCircle() {
        super("Jump Circles", "Когда прыгаешь появляется кружочек, хы)", FeatureCategory.Render);
        addSettings(jumpCircleColor, radius, size, speed);
    }public boolean esp;

    @EventTarget
    public void onJump(EventUpdate event) {
        if (mc.player.motionY == 0.33319999363422365 && !mc.player.otherCheck())
            handleEntityJump(mc.player);
            onLocalPlayerUpdate(mc.player);
    }
    @Override
    public void onEnable() {
    	if (Celestial.instance.featureManager.getFeature(ShaderESP.class).isEnabled()) {
    		esp = true;
    		Celestial.instance.featureManager.getFeature(ShaderESP.class).toggle();
    	}
        super.onEnable();
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();
        double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * mc.getRenderPartialTicks());
        double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * mc.getRenderPartialTicks());
        double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * mc.getRenderPartialTicks());
        	float scale = radius.getNumberValue();
        	float size = this.size.getNumberValue();
        	float speed = this.speed.getNumberValue();
        	int n = 0;
            int angle = (int)((System.currentTimeMillis() / (long)2 + (long)4) % 360L);
            float hue = (float)angle / 360.0f;
            angle = (int)((double)angle % 360.0);
        	Color color = Color.getHSBColor((double)((float)((double)n / 360.0)) < 0.5 ? -((float)((double)angle / 360.0)) : (float)((double)angle / 360.0), 0.5f, 1.0f);
        	GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Collections.reverse(circles);
            for (Circle c : JumpCircle.circles) {
                int red = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 16) & 255) / 100.f);
                int green = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 8) & 255) / 100.f);
                int blue = (int) ((JumpCircle.jumpCircleColor.getColorValue() & 255) / 100.f);
                double x = c.position().xCoord;
                double y = c.position().yCoord - MathematicHelper.getRandomInRange(0.3f, 0.31f);
                double z = c.position().zCoord;
                float k = ((float) c.existed / MAX_JC_TIME) * speed;
                float start = (float) (k * size + scale);
                float end = start + scale;
                float end2 = start - scale;
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (int i = 0; i <= 360; i = i + 3) {
                    GL11.glColor4f((float) c.color().xCoord, (float) c.color().yCoord, (float) c.color().zCoord,
                            0.7f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y,
                                    z + Math.sin(Math.toRadians(i)) * start);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 2)) * start, y,
                                    z + Math.sin(Math.toRadians(i * 2)) * start);
                            break;
                    }
                    GL11.glColor4f(red, green, blue, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(-i)) * end, y, z + Math.sin(Math.toRadians(-i)) * end);
                            break;
                    }
                }
                for (int i = 0; i <= 360; i = i + 3) {
                    GL11.glColor4f((float) c.color().xCoord, (float) c.color().yCoord, (float) c.color().zCoord,
                            0.7f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y,
                                    z + Math.sin(Math.toRadians(i)) * start);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 10)) * start, y,
                                    z + Math.sin(Math.toRadians(i * 10)) * start);
                            break;
                    }
                    GL11.glColor4f(red, green, blue, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end2, y, z + Math.sin(Math.toRadians(i)) * end2);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(-i)) * end2, y, z + Math.sin(Math.toRadians(-i)) * end2);
                            break;
                    }
                }
                GL11.glEnd();
            }
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
            if (esp) {
            	Celestial.instance.featureManager.getFeature(ShaderESP.class).toggle();
            	esp = false;
            }
    }

    // EntityPlayerSP.onUpdate
    public static void onLocalPlayerUpdate(EntityPlayerSP instance) {
        circles.removeIf(Circle::update);
    }

    public static void handleEntityJump(Entity entity) {
        int red = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 16) & 255) / 100.f);
        int green = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 8) & 255) / 100.f);
        int blue = (int) ((JumpCircle.jumpCircleColor.getColorValue() & 255) / 100.f);

        Vec3d color = new Vec3d(red, green, blue);
        circles.add(new Circle(entity.getPositionVector(), color));
    }

    static class Circle {
        private final Vec3d vec;
        private final Vec3d color;
        byte existed;

        Circle(Vec3d vec, Vec3d color) {
            this.vec = vec;
            this.color = color;
        }

        Vec3d position() {
            return this.vec;
        }

        Vec3d color() {
            return this.color;
        }

        boolean update() {
            return ++existed > MAX_JC_TIME;
        }
    }
}