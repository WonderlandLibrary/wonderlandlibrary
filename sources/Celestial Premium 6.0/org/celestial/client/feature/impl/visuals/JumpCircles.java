/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

public class JumpCircles
extends Feature {
    private ListSetting circleType;
    private ListSetting circlesMode;
    private ColorSetting circleCustomColor;
    private NumberSetting ticksToRemove;
    private NumberSetting animationSpeed;
    private NumberSetting loopSize;
    private NumberSetting radiusAdd;
    private NumberSetting circleSegments;
    private NumberSetting diskAlpha;
    private BooleanSetting diskAnimation;
    public static boolean canRender;
    private final ArrayList<CircleData> circles = new ArrayList();
    private final ArrayList<CircleDisk> circleDisk = new ArrayList();

    public JumpCircles() {
        super("JumpCircles", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044e \u043f\u0440\u044b\u0436\u043a\u0430 \u043f\u043e\u0434 \u0432\u0430\u043c\u0438", Type.Visuals);
        this.circleType = new ListSetting("Circles Type", "Default", "Default", "Disk");
        this.circlesMode = new ListSetting("Circles Mode", "Astolfo", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
        this.circleCustomColor = new ColorSetting("Circles Color", Color.WHITE.getRGB(), () -> this.circlesMode.currentMode.equals("Custom"));
        this.ticksToRemove = new NumberSetting("Ticks To Remove", 70.0f, 10.0f, 100.0f, 5.0f, () -> true);
        this.animationSpeed = new NumberSetting("Animation Speed", 0.2f, 0.1f, 1.0f, 0.01f, () -> this.circleType.currentMode.equals("Default"));
        this.loopSize = new NumberSetting("Loop Size", 8.0f, 1.0f, 50.0f, 1.0f, () -> this.circleType.currentMode.equals("Default"));
        this.radiusAdd = new NumberSetting("Radius Add", 0.01f, 0.0f, 0.1f, 0.001f, () -> this.circleType.currentMode.equals("Default"));
        this.circleSegments = new NumberSetting("Circle Segments", 50.0f, 3.0f, 50.0f, 1.0f, () -> this.circleType.currentMode.equals("Default"));
        this.diskAnimation = new BooleanSetting("Disk Animation", false, () -> this.circleType.currentMode.equals("Disk"));
        this.diskAlpha = new NumberSetting("Disk Alpha", 0.5f, 0.01f, 1.0f, 0.01f, () -> this.circleType.currentMode.equals("Disk"));
        this.addSettings(this.circleType, this.circlesMode, this.circleCustomColor, this.loopSize, this.radiusAdd, this.ticksToRemove, this.animationSpeed, this.circleSegments, this.diskAnimation, this.diskAlpha);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (JumpCircles.mc.player == null || JumpCircles.mc.world == null) {
            return;
        }
        if (this.circleType.currentMode.equals("Default")) {
            for (CircleData circle : this.circles) {
                if (circle == null) continue;
                int customColor = this.circleCustomColor.getColor();
                int color = 0;
                switch (this.circlesMode.currentMode) {
                    case "Client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "Custom": {
                        color = customColor;
                        break;
                    }
                    case "Astolfo": {
                        color = PaletteHelper.astolfo(false, 1).getRGB();
                        break;
                    }
                    case "Rainbow": {
                        color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                    }
                }
                float smoothAlpha = Math.max(0.0f, 1.0f - circle.getAliveTicks() / 35.0f / this.ticksToRemove.getCurrentValue() * 50.0f);
                float widthValue = 10.0f - circle.getAliveTicks() / 4.0f;
                widthValue = MathHelper.clamp(widthValue, 0.1f, widthValue);
                circle.setAnimationTicks(circle.getAnimationTicks() + this.animationSpeed.getCurrentValue() / 10.0f * (float)Minecraft.frameTime * 0.1f);
                this.renderCircle(circle.getVec(), (double)circle.getAnimationTicks(), widthValue, (int)this.circleSegments.getCurrentValue(), new Color((float)new Color(color).getRed() / 255.0f, (float)new Color(color).getGreen() / 255.0f, (float)new Color(color).getBlue() / 255.0f, smoothAlpha).getRGB(), smoothAlpha);
            }
        } else if (this.circleType.currentMode.equals("Disk")) {
            EntityPlayerSP client = Minecraft.getMinecraft().player;
            double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * (double)event.getPartialTicks());
            double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * (double)event.getPartialTicks());
            double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * (double)event.getPartialTicks());
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glDisable(2929);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(this.circles);
            float time = this.ticksToRemove.getCurrentValue();
            for (CircleDisk c : this.circleDisk) {
                float k = (float)c.existed / time;
                double x = c.position().x;
                double y = c.position().y - (double)k * 0.5;
                double z = c.position().z;
                float start = k;
                float end = start + 1.0f - k;
                GL11.glBegin(8);
                for (int i = 0; i < 361; i += 5) {
                    GlStateManager.color((float)c.color().x, (float)c.color().y, (float)c.color().z, 0.2f * (1.0f - (float)c.existed / time));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * (double)start, y, z + Math.sin(Math.toRadians(i * 4)) * (double)start);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, this.diskAlpha.getCurrentValue() * (1.0f - (float)c.existed / time));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * (double)end, this.diskAnimation.getCurrentValue() ? y + Math.sin(k * 8.0f) * 0.5 : y, z + Math.sin(Math.toRadians(i) * (double)end));
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                }
                GL11.glEnd();
            }
            Collections.reverse(this.circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (JumpCircles.mc.player == null || JumpCircles.mc.world == null) {
            return;
        }
        this.setSuffix(this.circleType.currentMode);
        if (canRender) {
            if (this.circleType.currentMode.equals("Disk")) {
                this.handleEntityJump(JumpCircles.mc.player);
            } else if (this.circleType.currentMode.equals("Default")) {
                this.circles.add(new CircleData(JumpCircles.mc.player.getPositionVector()));
            }
            canRender = false;
        }
        this.circles.removeIf(circleData -> ((CircleData)circleData).canRemove((int)this.ticksToRemove.getCurrentValue()));
        this.circleDisk.removeIf(CircleDisk::update);
    }

    private void handleEntityJump(Entity entity) {
        int customColor = this.circleCustomColor.getColor();
        int color1 = 0;
        switch (this.circlesMode.currentMode) {
            case "Client": {
                color1 = ClientHelper.getClientColor().getRGB();
                break;
            }
            case "Custom": {
                color1 = customColor;
                break;
            }
            case "Astolfo": {
                color1 = PaletteHelper.astolfo(false, 1).getRGB();
                break;
            }
            case "Rainbow": {
                color1 = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        float red = (float)(color1 >> 16 & 0xFF) / 255.0f;
        float green = (float)(color1 >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color1 & 0xFF) / 255.0f;
        Vec3d color = new Vec3d(red, green, blue);
        this.circleDisk.add(new CircleDisk(new Vec3d(JumpCircles.mc.player.posX, JumpCircles.mc.player.posY - 0.4, JumpCircles.mc.player.posZ), color));
    }

    private void renderCircle(Vec3d vec, double radius, double width, int points, int color, float alpha) {
        int i = 0;
        while ((float)i < this.loopSize.getCurrentValue()) {
            this.renderCircle(vec, radius, points, (float)width, color, alpha);
            radius += (double)this.radiusAdd.getCurrentValue();
            ++i;
        }
    }

    private void renderCircle(Vec3d vec, double radius, int points, float width, int color, float alpha) {
        GlStateManager.pushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        double x = vec.x - JumpCircles.mc.getRenderManager().renderPosX;
        double y = vec.y - 0.4 - JumpCircles.mc.getRenderManager().renderPosY;
        double z = vec.z - JumpCircles.mc.getRenderManager().renderPosZ;
        RenderHelper.color(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos((float)i * ((float)Math.PI * 2) / (float)points), y, z + radius * Math.sin((float)i * ((float)Math.PI * 2) / (float)points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private class CircleData {
        private Vec3d vec;
        private float animationTicks;
        private float aliveTicks;

        public CircleData(Vec3d vec) {
            this.vec = vec;
        }

        public Vec3d getVec() {
            return this.vec;
        }

        public void setVec(Vec3d vec) {
            this.vec = vec;
        }

        private boolean canRemove(int ticks) {
            float f;
            this.aliveTicks += 1.0f;
            return f > (float)ticks;
        }

        public float getAnimationTicks() {
            return this.animationTicks;
        }

        public void setAnimationTicks(float animationTicks) {
            this.animationTicks = animationTicks;
        }

        public float getAliveTicks() {
            return this.aliveTicks;
        }

        public void setAliveTicks(float aliveTicks) {
            this.aliveTicks = aliveTicks;
        }
    }

    class CircleDisk {
        private final Vec3d vec;
        private final Vec3d color;
        byte existed;

        CircleDisk(Vec3d vec, Vec3d color) {
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
            byte by;
            this.existed = (byte)(this.existed + 1);
            return (float)by > JumpCircles.this.ticksToRemove.getCurrentValue();
        }
    }
}

