package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.ColorChanger;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Trail extends Module {
//    public BooleanValue fade = new BooleanValue("FadeOut", true);
//    public ColorValue color = new ColorValue("Color", -1);
//    public NumberValue numberValue = new NumberValue("MaxCount", 60, 0, 120, NumberValue.NUMBER_TYPE.INT);
//    public Trail() {
//        super("Trail", Category.Render, "Show your walk in the past.");
//    }
//    ArrayList<Vector3d> vector3ds = new ArrayList<>();
//    HashMap<Vector3d, Long> timeStamp = new HashMap<>();
//    @Override
//    public void onDisable() {
//        vector3ds.clear();
//        super.onDisable();
//    }
//
//    @Override
//    public void onUpdateEvent(UpdateEvent event) {
//        if(event.isPre()){
//            Vector3d v = new Vector3d(
//                    (mc.player.getPosX()),
//                    (mc.player.getPosY()),
//                    (mc.player.getPosZ())
//            );
//            vector3ds.add(v);
//        }
//        super.onUpdateEvent(event);
//    }
//
//    @Override
//    public void onRender3DEvent(Render3DEvent event) {
//        while(vector3ds.size() > numberValue.getValue().intValue()){
//            vector3ds.remove(0);
//        }
//        if(mc.gameSettings.getPointOfView().func_243192_a()) return;
//        GL11.glPushMatrix();
//        GlStateManager.disableLighting();
//        GL11.glDisable(GL_TEXTURE_2D);
//        GL11.glEnable(GL_BLEND);
//        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
////        GL11.glDepthMask(false);
////        GlStateManager.disableDepth();
//        GL11.glEnable(GL_LINE_SMOOTH);
//        GL11.glEnable(GL_POINT_SMOOTH);
//        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
//        GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
//        GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
//        final boolean enableBlend = GL11.glIsEnabled(3042);
//        final boolean disableAlpha = !GL11.glIsEnabled(3008);
//        if (!enableBlend) {
//            GL11.glEnable(3042);
//        }
//        if (!disableAlpha) {
//            GL11.glDisable(3008);
//        }
//        GlStateManager.disableCull();
//        double camX = RenderUtils.getRenderPos().renderPosX;
//        double camY = RenderUtils.getRenderPos().renderPosY;
//        double camZ = RenderUtils.getRenderPos().renderPosZ;
//        GlStateManager.resetColor();
//        int index = 0, actI = 0;
//        float height = mc.player.getHeight();
//        for (Vector3d vec : vector3ds) {
//            if(index == 0) {
//                index ++;
//                continue;
//            }
//            Vector3d next = vector3ds.get(index - 1);
//            if(vec.equals(next)){
//                index ++;
//                continue;
//            }
//            float alpha = fade.isEnable() ? (actI / numberValue.getValue().floatValue()) : 1;
//            alpha = Math.max(Math.min(alpha, 1), 0);
//            float nalpha = fade.isEnable() ? ((actI - 1) / numberValue.getValue().floatValue()) : 1;
//            nalpha = Math.max(Math.min(nalpha, 1), 0);
//            for(double linear = 1.0f; linear >= 0; linear -= 0.2f){
//                int i = actI * 15;
//                int i2 = (actI - 1) * 15;
//                int ll = (int) MathHelper.lerp(linear, i, i2);
//                Vector3d linear1 = new Vector3d(MathHelper.lerp(linear, vec.x, next.x), MathHelper.lerp(linear, vec.y, next.y), MathHelper.lerp(linear, vec.z, next.z));
//                float linear1A = (float) MathHelper.lerp(linear, alpha, nalpha);
//                linear -= 0.2f;
//                Vector3d linear2 = new Vector3d(MathHelper.lerp(linear, vec.x, next.x), MathHelper.lerp(linear, vec.y, next.y), MathHelper.lerp(linear, vec.z, next.z));
////                float linear2A = (float) MathHelper.lerp(linear, alpha, nalpha);
//                linear += 0.2f;
//                GL11.glBegin(GL_QUADS);
//                RenderUtils.color(ColorUtils.blend(ColorChanger.getColor(ll, 15), new Color(255, 255, 255), 0.74f).getRGB(), linear1A);
//                GL11.glVertex3d(linear1.x - camX, linear1.y - camY, linear1.z - camZ);
//                GL11.glVertex3d(linear1.x - camX, linear1.y - camY - 0.01, linear1.z - camZ);
//                GL11.glVertex3d(linear2.x - camX, linear2.y - camY - 0.01, linear2.z - camZ);
//                GL11.glVertex3d(linear2.x - camX, linear2.y - camY, linear2.z - camZ);
//                GL11.glEnd();
//                GL11.glBegin(GL_QUADS);
//                RenderUtils.color(ColorUtils.blend(ColorChanger.getColor(ll, 15), new Color(255, 255, 255), 0.74f).getRGB(), linear1A);
//                GL11.glVertex3d(linear1.x - camX, linear1.y - camY + height, linear1.z - camZ);
//                GL11.glVertex3d(linear1.x - camX, linear1.y - camY + height + 0.01, linear1.z - camZ);
//                GL11.glVertex3d(linear2.x - camX, linear2.y - camY + height + 0.01, linear2.z - camZ);
//                GL11.glVertex3d(linear2.x - camX, linear2.y - camY + height, linear2.z - camZ);
//                GL11.glEnd();
//                GL11.glBegin(GL_QUADS);
//                RenderUtils.color(ColorChanger.getColor(ll, 15).getRGB(), linear1A);
//                GL11.glVertex3d(linear1.x - camX, linear1.y - camY, linear1.z - camZ);
//                GL11.glVertex3d(linear1.x - camX, linear1.y + height - camY, linear1.z - camZ);
//                GL11.glVertex3d(linear2.x - camX, linear2.y + height - camY, linear2.z - camZ);
//                GL11.glVertex3d(linear2.x - camX, linear2.y - camY, linear2.z - camZ);
//                GL11.glEnd();
//            }
//            index++;
//            actI ++;
//        }
////        GlStateManager.enableDepth();
////        GL11.glDepthMask(true);
//        GlStateManager.enableCull();
//        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
//        GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
//        GL11.glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
//        GL11.glEnable(GL_TEXTURE_2D);
//        if (!enableBlend) {
//            GL11.glDisable(3042);
//        }
//        if (!disableAlpha) {
//            GL11.glEnable(3008);
//        }
//        GL11.glPopMatrix();
//        GL11.glColor3f(1, 1, 1);
//        GL11.glDisable(GL_LINE_SMOOTH);
//        GL11.glDisable(GL_POINT_SMOOTH);
//        super.onRender3DEvent(event);
//    }

    public BooleanValue nomove = new BooleanValue("NoMove", true);
//    public BooleanValue fade = new BooleanValue("FadeOut", true);
//    public ColorValue color = new ColorValue("Color", -1);
    public NumberValue numberValue = new NumberValue("MaxTime", 1000, 100, 1000, NumberValue.NUMBER_TYPE.INT);
    public Trail() {
        super("Trails", Category.Render, "Show your walk in the past.");
        registerValue(numberValue);
        registerValue(nomove);
    }
    ArrayList<OneFrame> vector3ds = new ArrayList<>();
    class OneFrame {
        public long ms;
        public Vector3d vector3d;
        public Color color;
        public float height;
        public OneFrame(long ms, Vector3d vector3d){
            this.ms = ms;
            this.vector3d = vector3d;
            this.color = ColorChanger.getColor((int) ((int) (System.nanoTime()) / 9000000), 20);
            this.height = mc.player.getHeight();
        }
    }

    @Override
    public void onDisable() {
        vector3ds.clear();
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if (nomove.getValue() || mc.player.lastTickPosX != mc.player.getPosX() || mc.player.lastTickPosY != mc.player.getPosY() || mc.player.lastTickPosZ != mc.player.getPosZ())
            vector3ds.add(new OneFrame(System.currentTimeMillis(), new Vector3d(
                    MathHelper.lerp(mc.timer.renderPartialTicks, mc.player.lastTickPosX, mc.player.getPosX()),
                    MathHelper.lerp(mc.timer.renderPartialTicks, mc.player.lastTickPosY, mc.player.getPosY()),
                    MathHelper.lerp(mc.timer.renderPartialTicks, mc.player.lastTickPosZ, mc.player.getPosZ())
            )));
        while (System.currentTimeMillis() - vector3ds.get(0).ms > numberValue.getValue().intValue()) {
            vector3ds.remove(0);
        }
        if(mc.gameSettings.getPointOfView().func_243192_a()) return;
        GL11.glPushMatrix();
        GlStateManager.enableDepth();
        GL11.glDepthMask(true);
        GlStateManager.disableLighting();
        GL11.glDisable(GL_TEXTURE_2D);
//                GL11.glEnable(GL_LINE_SMOOTH);
//                GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//                GlStateManager.alphaFunc(GL_GREATER, 0.0f);
//        GL11.glDepthMask(false);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
//        GlStateManager.disableDepth();
//                GL11.glShadeModel(GL_SMOOTH);
        final boolean enableBlend = GL11.glIsEnabled(3042);
        final boolean disableAlpha = !GL11.glIsEnabled(3008);
        if (!enableBlend) {
            GL11.glEnable(3042);
        }
        if (!disableAlpha) {
            GL11.glDisable(3008);
        }
        GlStateManager.disableCull();
//        GL11.glBegin(GL_LINE_STRIP);
        double camX = RenderUtils.getRenderPos().renderPosX;
        double camY = RenderUtils.getRenderPos().renderPosY;
        double camZ = RenderUtils.getRenderPos().renderPosZ;
        GlStateManager.resetColor();
        int index = 0;
        for (OneFrame vec : vector3ds) {
            if (index == 0) {
                index++;
                continue;
            }
            OneFrame v = vector3ds.get(index - 1);
            Vector3d linear1 = v.vector3d;
            Vector3d linear2 = vec.vector3d;
            float alpha = ((numberValue.getValue().floatValue() - (System.currentTimeMillis() - vec.ms)) / numberValue.getValue().floatValue());
            alpha = Math.max(Math.min(alpha, 1), 0);
            GL11.glBegin(GL_QUADS);
            RenderUtils.color(vec.color.getRGB(), alpha);
            GL11.glVertex3d(linear1.x - camX, linear1.y - camY, linear1.z - camZ);
            GL11.glVertex3d(linear1.x - camX, linear1.y + v.height - camY, linear1.z - camZ);
            GL11.glVertex3d(linear2.x - camX, linear2.y + vec.height - camY, linear2.z - camZ);
            GL11.glVertex3d(linear2.x - camX, linear2.y - camY, linear2.z - camZ);
            GL11.glEnd();
            GL11.glLineWidth(2.f);
            GL11.glBegin(GL_LINE_STRIP);
            RenderUtils.color(ColorUtils.blend(vec.color, new Color(200, 200, 200), 0.5f).getRGB(), alpha);
            GL11.glVertex3d(linear1.x - camX, linear1.y - camY, linear1.z - camZ);
            GL11.glVertex3d(linear2.x - camX, linear2.y - camY, linear2.z - camZ);
            GL11.glEnd();
            GL11.glBegin(GL_LINE_STRIP);
            RenderUtils.color(ColorUtils.blend(vec.color, new Color(200, 200, 200), 0.5f).getRGB(), alpha);
            GL11.glVertex3d(linear1.x - camX, linear1.y - camY + v.height, linear1.z - camZ);
            GL11.glVertex3d(linear2.x - camX, linear2.y - camY + vec.height, linear2.z - camZ);
            GL11.glEnd();
//            GL11.glVertex3d(vec.vector3d.x - camX, vec.vector3d.y - camY, vec.vector3d.z - camZ);
            index++;
        }
//        GL11.glEnd();
//                GL11.glShadeModel(GL_FLAT)
//                GL11.glAlphaFunc(GL_GREATER, 0.1f);
        GlStateManager.enableCull();
//                GL11.glDisable(GL_LINE_SMOOTH);
//                GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
        GL11.glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        GL11.glEnable(GL_TEXTURE_2D);
        if (!enableBlend) {
            GL11.glDisable(3042);
        }
        if (!disableAlpha) {
            GL11.glEnable(3008);
        }
        GL11.glPopMatrix();
        GL11.glColor3f(1, 1, 1);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glDisable(GL_POINT_SMOOTH);
        super.onRender3DEvent(event);
    }
}
