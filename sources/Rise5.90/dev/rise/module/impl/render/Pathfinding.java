package dev.rise.module.impl.render;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Pathfinding", description = "Made for testing Rises custom pathfinder", category = Category.COMBAT)
public class Pathfinding extends Module {
    private ArrayList<Vec3> path;
    private Entity target;
    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        /* Getting target */

        final List<EntityLivingBase> targets = PlayerUtil.getEntities(100, true, false, true, false, false);

        if (targets.isEmpty()) {
            target = null;
            return;
        }

        target = targets.get(0);

        /* Setting useful variables */
        final EntityPlayer player = mc.thePlayer;
        final double x = player.posX;
        final double y = player.posY;
        final double z = player.posZ;
        final double targetX = target.posX;
        final double targetY = target.posY;
        final double targetZ = target.posZ;

        if (path == null) return;
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    public Vec3 getPoint(final ArrayList<Vec3> path) {
        for (final Vec3 p : path) {
            final double distance = mc.thePlayer.getDistance(p.xCoord, mc.thePlayer.posY, p.zCoord);
            if (distance > 1) return p;
        }
        return path.get(0);
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        /* Drawing path to target */
        if (path == null || target == null) return;

        Vec3 lastPoint = null;

        final Color c = new Color(Rise.CLIENT_THEME_COLOR).brighter();

        for (final Vec3 point : path) {
            if (lastPoint != null) {
                RenderUtil.draw3DLine(lastPoint.xCoord, lastPoint.yCoord + 0.01, lastPoint.zCoord, point.xCoord, point.yCoord + 0.01, point.zCoord, c.getRed(), c.getGreen(), c.getBlue(), 255, 1);
            }
            lastPoint = point;
        }
        RenderUtil.renderBreadCrumbs(path);
    }
}