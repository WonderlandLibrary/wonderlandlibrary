package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class Breadcrumbs extends Module {
    public BooleanValue nomove = new BooleanValue("NoMove", true);
    public BooleanValue fade = new BooleanValue("FadeOut", true);
    public ColorValue color = new ColorValue("Color", -1);
    public NumberValue numberValue = new NumberValue("MaxTime", 5000, 100, 16000, NumberValue.NUMBER_TYPE.INT);
    public Breadcrumbs() {
        super("Breadcrumbs", Category.Render, "Show your walk in the past.");
     registerValue(numberValue);
     registerValue(color);
     registerValue(fade);
     registerValue(nomove);
    }
    ArrayList<OneFrame> vector3ds = new ArrayList<>();
    class OneFrame {
        public long ms;
        public Vector3d vector3d;
        public OneFrame(long ms, Vector3d vector3d){
            this.ms = ms;
            this.vector3d = vector3d;
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
        if(nomove.getValue() || mc.player.lastTickPosX != mc.player.getPosX() || mc.player.lastTickPosY != mc.player.getPosY() || mc.player.lastTickPosZ != mc.player.getPosZ())
            vector3ds.add(new OneFrame(System.currentTimeMillis(), new Vector3d(
                    MathHelper.lerp(mc.timer.renderPartialTicks, mc.player.lastTickPosX, mc.player.getPosX()),
                    MathHelper.lerp(mc.timer.renderPartialTicks, mc.player.lastTickPosY, mc.player.getPosY()),
                    MathHelper.lerp(mc.timer.renderPartialTicks, mc.player.lastTickPosZ, mc.player.getPosZ())
            )));
        while (System.currentTimeMillis() - vector3ds.get(0).ms > numberValue.getValue().intValue()){
            vector3ds.remove(0);
        }
        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        GL11.glDisable(GL_TEXTURE_2D);
//                GL11.glEnable(GL_LINE_SMOOTH);
//                GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//                GlStateManager.alphaFunc(GL_GREATER, 0.0f);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.5f);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        GlStateManager.disableDepth();
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
        GL11.glBegin(GL_LINE_STRIP);
        double camX = RenderUtils.getRenderPos().renderPosX;
        double camY = RenderUtils.getRenderPos().renderPosY;
        double camZ = RenderUtils.getRenderPos().renderPosZ;
        GlStateManager.resetColor();
        int index = 0;
        for (OneFrame vec : vector3ds) {
            float alpha = fade.isEnable() ? ((numberValue.getValue().floatValue() - (System.currentTimeMillis() - vec.ms)) / numberValue.getValue().floatValue()) : 1;
            alpha = Math.max(Math.min(alpha, 1), 0);
            RenderUtils.color(color.getColorInt(), alpha);
            GL11.glVertex3d(vec.vector3d.x - camX, vec.vector3d.y - camY, vec.vector3d.z - camZ);
            index++;
        }
        GL11.glEnd();
//                GL11.glShadeModel(GL_FLAT);
//        GlStateManager.enableDepth();
//        GL11.glDepthMask(true);
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
        GlStateManager.enableDepth();
        GL11.glDepthMask(true);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glDisable(GL_POINT_SMOOTH);
        super.onRender3DEvent(event);
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }
}
