/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.visuals.Tracers;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.lwjgl.opengl.GL11;

public class PearlESP
extends Feature {
    public static BooleanSetting pearlPrediction;
    private final BooleanSetting triangleESP;
    public static List<PredictionLine> lines;
    public static EntityEnderPearl entityPearl;

    public PearlESP() {
        super("PearlESP", "\u0420\u0438\u0441\u0443\u0435\u0442 \u0435\u0441\u043f \u0438 \u043b\u0438\u043d\u0438\u044e \u043a \u044d\u043d\u0434\u0435\u0440-\u043f\u0435\u0440\u043b\u0443", Type.Visuals);
        pearlPrediction = new BooleanSetting("Pearl Prediction", false, () -> true);
        this.triangleESP = new BooleanSetting("Triangle ESP", true, () -> true);
        this.addSettings(this.triangleESP, pearlPrediction);
    }

    public static void handleEntityPrediction(IProjectile proj) {
        if (proj instanceof EntityEnderPearl) {
            EntityEnderPearl ent;
            entityPearl = ent = (EntityEnderPearl)proj;
            double sx = ent.posX;
            double sy = ent.posY;
            double sz = ent.posZ;
            double mx = ent.motionX;
            double my = ent.motionY;
            double mz = ent.motionZ;
            mx += ent.getThrower().motionX;
            mz += ent.getThrower().motionZ;
            if (!ent.getThrower().onGround) {
                my += ent.getThrower().motionY;
            }
            int maxUpdateTicks = 250;
            int updateTicks = 250;
            ArrayList<PredictionPosition> positions = new ArrayList<PredictionPosition>();
            while (updateTicks > 0) {
                Vec3d vec3d = new Vec3d(sx, sy, sz);
                if (--updateTicks != 250) {
                    int cnt = updateTicks % 83;
                    float p = (float)cnt / 83.333336f;
                    float trg = 0.0f;
                    trg = p > 0.5f ? 1.0f - p * 2.0f : p * 2.0f;
                    Vec3d color = new Vec3d(0.3f + 0.4f * trg, 0.5f - 0.4f * trg, 0.9f);
                    PredictionPosition pos = new PredictionPosition(vec3d, color);
                    positions.add(pos);
                }
                Vec3d vec3d1 = new Vec3d(sx + mx, sy + my, sz + mz);
                RayTraceResult raytraceresult = ent.world.rayTraceBlocks(vec3d, vec3d1);
                sx += mx;
                sy += my;
                sz += mz;
                float f1 = 0.99f;
                float f2 = ent.getGravityVelocity();
                mx *= (double)f1;
                my *= (double)f1;
                mz *= (double)f1;
                if (!ent.hasNoGravity()) {
                    my -= (double)f2;
                }
                if (raytraceresult == null) continue;
                Vec3d color = new Vec3d(1.0, 1.0, 1.0);
                PredictionPosition pos = new PredictionPosition(new Vec3d(sx + mx, sy + my, sz + mz), color);
                positions.add(pos);
                break;
            }
            PearlESP.addLine(positions, ent);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!pearlPrediction.getCurrentValue()) {
            return;
        }
        lines.removeIf(PredictionLine::remove);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (!this.triangleESP.getCurrentValue()) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        float size = 50.0f;
        float xOffset = (float)sr.getScaledWidth() / 2.0f - 24.5f;
        float yOffset = (float)sr.getScaledHeight() / 2.0f - 25.2f;
        for (Entity entity : PearlESP.mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityEnderPearl)) continue;
            GlStateManager.pushMatrix();
            GlStateManager.disableBlend();
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)PearlESP.mc.timer.renderPartialTicks - PearlESP.mc.getRenderManager().renderPosX;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)PearlESP.mc.timer.renderPartialTicks - PearlESP.mc.getRenderManager().renderPosZ;
            double cos = Math.cos((double)PearlESP.mc.player.rotationYaw * (Math.PI / 180));
            double sin = Math.sin((double)PearlESP.mc.player.rotationYaw * (Math.PI / 180));
            double rotY = -(z * cos - x * sin);
            double rotX = -(x * cos + z * sin);
            float angle = (float)(Math.atan2(rotY - 0.0, rotX - 0.0) * 180.0 / Math.PI);
            double xPos = (double)(size / 2.0f) * Math.cos(Math.toRadians(angle)) + (double)xOffset + (double)(size / 2.0f);
            double y = (double)(size / 2.0f) * Math.sin(Math.toRadians(angle)) + (double)yOffset + (double)(size / 2.0f);
            GlStateManager.translate(xPos, y, 0.0);
            GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
            String distance = MathematicHelper.round(PearlESP.mc.player.getDistanceToEntity(entity), 2) + "m";
            RenderHelper.drawTriangle(5.0f, 1.0f, 7.0f, 90.0f, new Color(5, 5, 5, 150).getRGB());
            RenderHelper.drawTriangle(5.0f, 1.0f, 6.0f, 90.0f, ClientHelper.getClientColor().getRGB());
            PearlESP.mc.fontRendererObj.drawStringWithShadow(distance, -2.0f, 9.0f, -1);
            GlStateManager.popMatrix();
        }
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        if (!pearlPrediction.getCurrentValue()) {
            return;
        }
        double ix = -(PearlESP.mc.player.lastTickPosX + (PearlESP.mc.player.posX - PearlESP.mc.player.lastTickPosX) * (double)event.getPartialTicks());
        double iy = -(PearlESP.mc.player.lastTickPosY + (PearlESP.mc.player.posY - PearlESP.mc.player.lastTickPosY) * (double)event.getPartialTicks());
        double iz = -(PearlESP.mc.player.lastTickPosZ + (PearlESP.mc.player.posZ - PearlESP.mc.player.lastTickPosZ) * (double)event.getPartialTicks());
        GL11.glPushMatrix();
        GL11.glTranslated(ix, iy, iz);
        GL11.glDisable(3008);
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        for (PredictionLine line : lines) {
            List<PredictionPosition> positions = line.positions;
            for (int i = 0; i < positions.size(); ++i) {
                if (positions.size() <= i + 1) continue;
                PredictionPosition c = positions.get(i);
                PredictionPosition n = positions.get(i + 1);
                int color = ClientHelper.getClientColor().getRGB();
                GlStateManager.color((float)new Color(color).getRed() / 255.0f, (float)new Color(color).getGreen() / 255.0f, (float)new Color(color).getBlue() / 255.0f, (float)new Color(color).getAlpha() / 255.0f);
                GL11.glVertex3d(c.vector.x, c.vector.y, c.vector.z);
                GL11.glVertex3d(n.vector.x, n.vector.y, n.vector.z);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    static void addLine(List<PredictionPosition> positions, Entity predictable) {
        lines.add(new PredictionLine(positions, predictable));
    }

    @EventTarget
    public void onRender3D(EventRender3D eventRender3D) {
        if (PearlESP.mc.player == null || PearlESP.mc.world == null) {
            return;
        }
        for (Entity entity : PearlESP.mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityEnderPearl)) continue;
            GlStateManager.pushMatrix();
            RenderHelper.drawEntityBox(entity, ClientHelper.getClientColor(), true, 1.0f);
            if (!pearlPrediction.getCurrentValue()) {
                PearlESP.tracersEsp(entity, eventRender3D.getPartialTicks(), ClientHelper.getClientColor().getRGB());
            }
            GlStateManager.popMatrix();
        }
    }

    public static void tracersEsp(Entity entity, float partialTicks, int color) {
        boolean old = PearlESP.mc.gameSettings.viewBobbing;
        PearlESP.mc.gameSettings.viewBobbing = false;
        PearlESP.mc.entityRenderer.setupCameraTransform(partialTicks, 2);
        PearlESP.mc.gameSettings.viewBobbing = old;
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - PearlESP.mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - PearlESP.mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - PearlESP.mc.getRenderManager().viewerPosZ;
        if (Celestial.instance.friendManager.isFriend(entity.getName()) && Tracers.friend.getCurrentValue()) {
            GlStateManager.color(0.0f, 255.0f, 0.0f, 255.0f);
        } else {
            GlStateManager.color((float)new Color(color).getRed() / 255.0f, (float)new Color(color).getGreen() / 255.0f, (float)new Color(color).getBlue() / 255.0f, (float)new Color(color).getAlpha() / 255.0f);
        }
        Vec3d vec3d = new Vec3d(0.0, 0.0, 1.0);
        vec3d = vec3d.rotatePitch((float)(-Math.toRadians(PearlESP.mc.player.rotationPitch)));
        Vec3d vec3d2 = vec3d.rotateYaw(-((float)Math.toRadians(PearlESP.mc.player.rotationYaw)));
        GL11.glBegin(2);
        GL11.glVertex3d(vec3d2.x, (double)PearlESP.mc.player.getEyeHeight() + vec3d2.y, vec3d2.z);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    static {
        lines = new ArrayList<PredictionLine>();
    }

    static class PredictionPosition {
        Vec3d vector;
        Vec3d color;

        PredictionPosition(Vec3d vector, Vec3d color) {
            this.vector = vector;
            this.color = color;
        }
    }

    static class PredictionLine {
        List<PredictionPosition> positions;
        int ownerID;

        PredictionLine(List<PredictionPosition> positions, Entity predictable) {
            this.positions = positions;
            this.ownerID = predictable.getEntityId();
        }

        boolean remove() {
            Entity target = Minecraft.getMinecraft().world.getEntityByID(this.ownerID);
            if (!this.positions.isEmpty()) {
                this.positions.remove(0);
            }
            return this.positions.isEmpty() || target == null || target.isDead;
        }
    }
}

