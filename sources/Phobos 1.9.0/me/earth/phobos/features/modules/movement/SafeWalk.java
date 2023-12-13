package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.features.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafeWalk
        extends Module {
    public SafeWalk() {
        super("SafeWalk", "Walks safe", Module.Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (event.getStage() == 0) {
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            if (SafeWalk.mc.player.onGround) {
                double increment = 0.05;
                while (x != 0.0 && this.isOffsetBBEmpty(x, -1.0, 0.0)) {
                    if (x < increment && x >= -increment) {
                        x = 0.0;
                        continue;
                    }
                    if (x > 0.0) {
                        x -= increment;
                        continue;
                    }
                    x += increment;
                }
                while (z != 0.0 && this.isOffsetBBEmpty(0.0, -1.0, z)) {
                    if (z < increment && z >= -increment) {
                        z = 0.0;
                        continue;
                    }
                    if (z > 0.0) {
                        z -= increment;
                        continue;
                    }
                    z += increment;
                }
                while (x != 0.0 && z != 0.0 && this.isOffsetBBEmpty(x, -1.0, z)) {
                    x = x < increment && x >= -increment ? 0.0 : (x > 0.0 ? (x -= increment) : (x += increment));
                    if (z < increment && z >= -increment) {
                        z = 0.0;
                        continue;
                    }
                    if (z > 0.0) {
                        z -= increment;
                        continue;
                    }
                    z += increment;
                }
            }
            event.setX(x);
            event.setY(y);
            event.setZ(z);
        }
    }

    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        EntityPlayerSP playerSP = SafeWalk.mc.player;
        return SafeWalk.mc.world.getCollisionBoxes(playerSP, playerSP.getEntityBoundingBox().offset(offsetX, offsetY, offsetZ)).isEmpty();
    }
}

