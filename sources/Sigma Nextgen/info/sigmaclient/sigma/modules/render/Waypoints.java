package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.render.M3DUtil;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static info.sigmaclient.sigma.utils.render.M3DUtil.projectOn2D;
import static info.sigmaclient.sigma.utils.render.RenderUtils.createFrameBuffer;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Waypoints extends Module {
    BooleanValue magnify = new BooleanValue("Death Position", true);
    boolean dead = false;
    Vector3d pos = null;
    private final Map<String, Vector3f> points = new HashMap<>();

    public Waypoints() {
        super("Waypoints", Category.Render, "Render death postition");
//        registerValue(magnify);
    }

    @Override
    public void onEnable() {
        this.enabled = false;
        NotificationManager.notify("Waypoints", "this module is unstable", 4000);
        dead = false;
        super.onEnable();
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        if (pos != null) {
            for (String entity : points.keySet()) {
                Vector3f pos = points.get(entity);
                float x = pos.getX(),
                        y = pos.getY();
                JelloFontUtil.jelloFontBold20.drawCenteredString("Death position ("+((int)Math.sqrt(mc.player.getDistanceSq(this.pos)))+"m)", x, y, -1);
            }
        }
        super.onRenderEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (event.isPre()) {
            if (!mc.player.isAlive()) {
                if (!dead) {
                    if (magnify.isEnable()) {
                        pos = mc.player.getPositionVec();
                        ChatUtils.sendMessageWithPrefix("Save death position " + pos);
                    }
                    dead = true;
                }
            } else {
                dead = false;
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if (pos == null) return;
        points.clear();
        ScaledResolution sr = new ScaledResolution(mc);
        points.put("Death Position", projectOn2D((float) pos.x, (float) pos.y, (float) pos.z, sr.getScaleFactor()));
        super.onRender3DEvent(event);
    }
}
