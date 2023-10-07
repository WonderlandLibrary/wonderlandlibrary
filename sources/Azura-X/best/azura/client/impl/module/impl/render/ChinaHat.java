package best.azura.client.impl.module.impl.render;

import best.azura.client.impl.events.EventRender3D;
import best.azura.client.impl.events.EventRender3DPost;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "China Hat", description = "Renders a chinese hat", category = Category.RENDER)
public class ChinaHat extends Module {

    private final NumberValue<Double> polygons = new NumberValue<>("Polygons", "Amount of polygons", 25D, 1D, 3D, 25D);
    private final NumberValue<Double> alpha = new NumberValue<>("Alpha", "Alpha", 150D, 1D, 0D, 255D);
    private final BooleanValue renderInFirstPerson = new BooleanValue("Render in first person", "Renders the china hat in first person", false);

    @EventHandler
    public final Listener<EventRender3D> eventRender3DListener = e -> {
        final EntityPlayerSP target = mc.thePlayer;
        final double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX,
                y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY,
                z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ,
                v2 = y + (target.height + 2.5F * (2 * (1 / Math.max(0.1, target.height) * 0.1))),
                v3 = y + (target.height + 0.1 * (2 * (1 / Math.max(0.1, target.height) * 0.1)));
        if (mc.gameSettings.showDebugInfo == 0) return;
        glPushMatrix();
        glTranslated(x, v3, z);
        if (target.isSneaking()) glTranslated(0, -0.25, 0);
        glRotated(-(target.prevRotationYawHead + (target.rotationYawHead - target.prevRotationYawHead) * mc.timer.renderPartialTicks), 0, 1, 0);
        glRotated(target.prevRotationPitchHead + (target.rotationPitchHead - target.prevRotationPitchHead) * mc.timer.renderPartialTicks, 1, 0, 0);
        if (Math.abs(target.rotationPitchHead) < 25)
            glTranslated(0, 0.07 - 0.07 * (Math.abs(target.rotationPitchHead) / 25.0), 0);
        if (Math.abs(target.rotationPitchHead) > 70)
            glTranslated(0, -0.1 + 0.1 * (Math.abs(target.rotationPitchHead) / 50.0), -0.1 + 0.1 * (Math.abs(target.rotationPitchHead) / 50.0));
        final double val = target.rotationPitchHead / 45, val1 = Math.abs(target.rotationPitchHead / 45);
        glTranslated(0, val1 * 0.2, val * 0.2);
        glTranslated(-x, -v3, -z);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_TRIANGLE_STRIP);
        for (double i = -polygons.getObject() / 2; i <= polygons.getObject() / 2; i++) {
            final double angle = Math.PI * i / (polygons.getObject() / 2), cos = Math.cos(angle) * target.width, sin = Math.sin(angle) * target.width;
            int val2 = (int) i;
            if (i == -polygons.getObject()) val2 = polygons.getObject().intValue();
            final Color color0 = ColorUtil.getHudColor(val2), color1 = new Color(color0.getRed(), color0.getGreen(), color0.getBlue(), alpha.getObject().intValue());
            RenderUtil.INSTANCE.color(color1);
            glVertex3d(x + sin, v3, z + cos);
            final Color color2 = ColorUtil.getHudColor(0), color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), alpha.getObject().intValue());
            RenderUtil.INSTANCE.color(color3);
            glVertex3d(x, v2, z);
            RenderUtil.INSTANCE.color(color1);
            glVertex3d(x + sin, v3, z + cos);
        }
        glEnd();
        glLineWidth(0.75f);
        glBegin(GL_LINE_LOOP);
        for (double i = -polygons.getObject() / 2; i <= polygons.getObject() / 2; i++) {
            final double angle = Math.PI * i / (polygons.getObject() / 2), cos = Math.cos(angle) * target.width, sin = Math.sin(angle) * target.width;
            RenderUtil.INSTANCE.color(Color.BLACK.getRGB() / 255F, Color.BLACK.getGreen() / 255F, Color.BLACK.getBlue() / 255F, (alpha.getObject().floatValue()));
            glVertex3d(x + sin, v3, z + cos);
        }
        glEnd();
        glLineWidth(1.0f);
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    };

    @EventHandler
    public final Listener<EventRender3DPost> eventRender3DPostListener = e -> {
        final EntityPlayerSP target = mc.thePlayer;
        final double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX,
                y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY,
                z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ,
                v2 = y + (target.height + 2 * (2 * (1 / Math.max(0.1, target.height) * 0.1))),
                v3 = y + (target.height + 0.1 * (2 * (1 / Math.max(0.1, target.height) * 0.1)));
        if (mc.gameSettings.showDebugInfo != 0 || !renderInFirstPerson.getObject()) return;
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_TRIANGLE_STRIP);
        for (double i = -polygons.getObject() / 2; i <= polygons.getObject() / 2; i++) {
            final double angle = Math.PI * i / (polygons.getObject() / 2), cos = Math.cos(angle) * target.width, sin = Math.sin(angle) * target.width;
            int val2 = (int) i;
            if (i == -polygons.getObject()) val2 = polygons.getObject().intValue();
            final Color color0 = ColorUtil.getHudColor(val2), color1 = new Color(color0.getRed(), color0.getGreen(), color0.getBlue(), alpha.getObject().intValue());
            RenderUtil.INSTANCE.color(color1);
            glVertex3d(x + sin, v3, z + cos);
            final Color color2 = ColorUtil.getHudColor(0), color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), alpha.getObject().intValue());
            RenderUtil.INSTANCE.color(color3);
            glVertex3d(x, v2, z);
            RenderUtil.INSTANCE.color(color1);
            glVertex3d(x + sin, v3, z + cos);
        }
        glEnd();
        glLineWidth(0.75f);
        glBegin(GL_LINE_LOOP);
        for (double i = -polygons.getObject() / 2; i <= polygons.getObject() / 2; i++) {
            final double angle = Math.PI * i / (polygons.getObject() / 2), cos = Math.cos(angle) * target.width, sin = Math.sin(angle) * target.width;
            RenderUtil.INSTANCE.color(Color.BLACK);
            glVertex3d(x + sin, v3, z + cos);
        }
        glEnd();
        glLineWidth(1.0f);
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
    };

}