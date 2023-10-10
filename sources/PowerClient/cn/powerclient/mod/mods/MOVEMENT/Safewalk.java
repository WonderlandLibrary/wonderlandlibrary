/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import me.AveReborn.events.EventMove;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class Safewalk
extends Mod {
    public Safewalk() {
        super("Safewalk", Category.MOVEMENT);
    }

    @EventTarget
    public void onMove(EventMove event) {
        double x2 = event.getX();
        double y2 = event.getY();
        double z2 = event.getZ();
        if (Minecraft.thePlayer.onGround) {
            double increment = 0.05;
            while (x2 != 0.0) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(x2, -1.0, 0.0)).isEmpty()) break;
                if (x2 < increment && x2 >= - increment) {
                    x2 = 0.0;
                    continue;
                }
                if (x2 > 0.0) {
                    x2 -= increment;
                    continue;
                }
                x2 += increment;
            }
            while (z2 != 0.0) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z2)).isEmpty()) break;
                if (z2 < increment && z2 >= - increment) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= increment;
                    continue;
                }
                z2 += increment;
            }
            while (x2 != 0.0 && z2 != 0.0 && this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(x2, -1.0, z2)).isEmpty()) {
                x2 = x2 < increment && x2 >= - increment ? 0.0 : (x2 > 0.0 ? (x2 -= increment) : (x2 += increment));
                if (z2 < increment && z2 >= - increment) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= increment;
                    continue;
                }
                z2 += increment;
            }
        }
        event.setX(x2);
        event.setY(y2);
        event.setZ(z2);
    }
}

