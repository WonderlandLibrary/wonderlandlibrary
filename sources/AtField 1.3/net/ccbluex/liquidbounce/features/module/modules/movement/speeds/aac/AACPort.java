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
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!MovementUtils.isMoving()) {
            return;
        }
        float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
        double d = 0.2;
        while (true) {
            Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
            if (speed == null) {
                Intrinsics.throwNpe();
            }
            if (!(d <= ((Number)speed.getPortMax().get()).doubleValue())) break;
            double d2 = iEntityPlayerSP2.getPosX();
            boolean bl = false;
            float f2 = (float)Math.sin(f);
            double d3 = d2 - (double)f2 * d;
            d2 = iEntityPlayerSP2.getPosZ();
            boolean bl2 = false;
            f2 = (float)Math.cos(f);
            double d4 = d2 + (double)f2 * d;
            if (iEntityPlayerSP2.getPosY() < (double)((int)iEntityPlayerSP2.getPosY()) + 0.5 && !MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(d3, iEntityPlayerSP2.getPosY(), d4)))) break;
            iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d3, iEntityPlayerSP2.getPosY(), d4, true));
            d += 0.2;
        }
    }

    public AACPort() {
        super("AACPort");
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    @Override
    public void onMotion() {
    }
}

