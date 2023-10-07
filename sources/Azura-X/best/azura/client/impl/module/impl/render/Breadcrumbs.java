package best.azura.client.impl.module.impl.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.math.CustomVec3;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.impl.events.EventRender3D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "Breadcrumbs", category = Category.RENDER, description = "Renders a line based on your last positions")
public class Breadcrumbs extends Module {

    private final NumberValue<Long> stayLength = new NumberValue<>("Stay time", "Time for the positions to stay", 1000L, 250L, 250L, 10000L);
    private final ArrayList<CustomVec3> vectors = new ArrayList<>();
    private final BooleanValue betterFPS = new BooleanValue("Better FPS", "Changes rendering of the positions to improve fps on worse computers", false);
    private final BooleanValue line = new BooleanValue("Line", "Render the breadcrumbs as a classical line", true);
    private int lastTick;

    @EventHandler
    public final Listener<EventRender3D> eventRender3DListener = e -> {
        if (lastTick != mc.thePlayer.ticksExisted) {
            vectors.add(new CustomVec3(mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks));
            if (betterFPS.getObject()) lastTick = mc.thePlayer.ticksExisted;
        }
        vectors.removeIf(v -> System.currentTimeMillis() - v.getCreationTime() >= stayLength.getObject());
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted == 0) vectors.clear();
        if (vectors.isEmpty()) return;
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        glLineWidth(2);
        CustomVec3 prev = vectors.get(0);
        for (CustomVec3 vec3 : vectors) {
            glColor4d(1, 1, 1, Math.sin(Math.min(1, Math.max(0, 1 - (System.currentTimeMillis() - vec3.getCreationTime()) / stayLength.getObject().floatValue()))));
            double x = prev.getX() - RenderManager.renderPosX,
                    y = prev.getY() - RenderManager.renderPosY,
                    z = prev.getZ() - RenderManager.renderPosZ,
                    x1 = vec3.getX() - RenderManager.renderPosX,
                    y1 = vec3.getY() - RenderManager.renderPosY,
                    z1 = vec3.getZ() - RenderManager.renderPosZ;
            //noinspection IfStatementWithIdenticalBranches
            if (!line.getObject()) {
                if (vec3.getDistance(mc.thePlayer) < mc.thePlayer.width * 0.5) continue;
                if (System.currentTimeMillis() - vec3.getCreationTime() < 50) continue;
                GlStateManager.disableCull();
                glBegin(GL_POLYGON);
                glVertex3d(x, y + mc.thePlayer.height * 0.7, z);
                glVertex3d(x, y + mc.thePlayer.height * 0.3, z);
                glVertex3d(x1, y1 + mc.thePlayer.height * 0.3, z1);
                glVertex3d(x1, y1 + mc.thePlayer.height * 0.7, z1);
                glEnd();
            } else {
                glBegin(GL_LINES);
                glVertex3d(x, y, z);
                glVertex3d(x1, y1, z1);
                glEnd();
            }
            prev = vec3;
        }
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        GlStateManager.resetColor();
    };

}