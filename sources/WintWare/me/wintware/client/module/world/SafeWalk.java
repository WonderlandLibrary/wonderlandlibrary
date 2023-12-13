/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.MoveEvent;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3d;

public class SafeWalk
extends Module {
    private final Vector3d vec = new Vector3d();

    public SafeWalk() {
        super("SafeWalk", Category.World);
    }

    @EventTarget
    public void event(MoveEvent event) {
        double x = event.getX();
        double y = event.getY();
        double z = event.getZ();
        if (Minecraft.player.onGround) {
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
    }

    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        this.vec.x = offsetX;
        this.vec.y = offsetY;
        this.vec.z = offsetZ;
        return SafeWalk.mc.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(this.vec.x, this.vec.y, this.vec.z)).isEmpty();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

