/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;

public final class AACPort
extends SpeedMode {
    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!MovementUtils.isMoving()) {
            return;
        }
        float f = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
        double d = 0.2;
        while (true) {
            Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
            if (speed == null) {
                Intrinsics.throwNpe();
            }
            if (!(d <= ((Number)speed.getPortMax().get()).doubleValue())) break;
            double d2 = thePlayer.getPosX();
            boolean bl = false;
            float f2 = (float)Math.sin(f);
            double x = d2 - (double)f2 * d;
            d2 = thePlayer.getPosZ();
            boolean bl2 = false;
            f2 = (float)Math.cos(f);
            double z = d2 + (double)f2 * d;
            if (thePlayer.getPosY() < (double)((int)thePlayer.getPosY()) + 0.5 && !MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(x, thePlayer.getPosY(), z)))) break;
            thePlayer.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, thePlayer.getPosY(), z, true));
            d += 0.2;
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public AACPort() {
        super("AACPort");
    }
}

