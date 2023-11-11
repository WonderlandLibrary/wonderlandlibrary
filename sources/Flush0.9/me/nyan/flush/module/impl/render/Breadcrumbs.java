package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Breadcrumbs extends Module {
    private final NumberSetting lineWidth = new NumberSetting("Line Width", this, 0.6, 0.2, 5, 0.1);

    private final ArrayList<Vec3> positions = new ArrayList<>();
    private final Timer timer = new Timer();

    public Breadcrumbs() {
        super("Breadcrumbs", Category.RENDER);
    }

    @Override
    public void onEnable() {
        positions.clear();
        positions.add(mc.thePlayer.getPositionVector());
        timer.reset();
        super.onEnable();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (positions.get(positions.size() - 1).distanceTo(mc.thePlayer.getPositionVector()) < 0.2) {
            return;
        }

        positions.add(mc.thePlayer.getPositionVector());
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        double renderPosX = mc.getRenderManager().renderPosX;
        double renderPosY = mc.getRenderManager().renderPosY;
        double renderPosZ = mc.getRenderManager().renderPosZ;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        RenderUtils.glColor(0xAAFFFFFF);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth.getValueFloat());

        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (Vec3 position : positions) {
            GL11.glVertex3d(position.xCoord - renderPosX, position.yCoord - renderPosY, position.zCoord - renderPosZ);
        }
        GL11.glEnd();

        GlStateManager.enableDepth();
        RenderUtils.glColor(-1);
    }
}