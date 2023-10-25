/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class AntiVoid
extends Module {
    private double y;
    private double z;
    private double x;

    private boolean isOverVoid() {
        return Minecraft.theWorld.getBlockState(new BlockPos((int)Minecraft.thePlayer.posX, 0, (int)Minecraft.thePlayer.posZ)).getBlock() == Blocks.air;
    }

    @EventTarget
    public void onUpdate(EventMotion eventMotion) {
        if (Minecraft.thePlayer.fallDistance > 5.0f) {
            if (Minecraft.thePlayer.posY < 0.0) {
                eventMotion.setY(EventMotion.getY() + 6.0);
            } else {
                int n = (int)Math.ceil(Minecraft.thePlayer.posY);
                while (n >= 0) {
                    if (Minecraft.theWorld.isAirBlock(new BlockPos(Minecraft.thePlayer.posX, (double)n, Minecraft.thePlayer.posZ))) {
                        return;
                    }
                    --n;
                }
                eventMotion.setY(EventMotion.getY() + 6.0);
            }
        }
    }

    @Override
    public void onDisable() {
        AntiVoid.mc.timer.timerSpeed = 1.0f;
    }

    public AntiVoid() {
        super("AntiVoid", 0, Category.PLAYER, "Helps you with your parkinsons.");
    }
}

