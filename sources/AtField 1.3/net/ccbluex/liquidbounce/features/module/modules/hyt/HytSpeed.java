/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.concurrent.LinkedBlockingQueue;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytSpeed", description="AsOne & \u53f8\u9a6c\u4eba", category=ModuleCategory.HYT)
public final class HytSpeed
extends Module {
    private final LinkedBlockingQueue packets;
    private int stage;
    private final FloatValue speed = new FloatValue("Speed", 0.5f, 0.15f, 8.0f);
    private boolean doAsFly;
    private final FloatValue motionY = new FloatValue("MotionY", 0.42f, 0.1f, 2.0f);
    private int timer;

    public final void setTimer(int n) {
        this.timer = n;
    }

    public final int getTimer() {
        return this.timer;
    }

    public final int getStage() {
        return this.stage;
    }

    public final void setStage(int n) {
        this.stage = n;
    }

    public final void setDoAsFly(boolean bl) {
        this.doAsFly = bl;
    }

    @Override
    public void onEnable() {
        this.timer = 0;
    }

    public final boolean getDoAsFly() {
        return this.doAsFly;
    }

    public final void move() {
        if (MovementUtils.isMoving()) {
            MovementUtils.strafe(((Number)this.speed.get()).floatValue());
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.setMotionY(((Number)this.motionY.get()).floatValue());
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            double d = (double)(iEntityPlayerSP2.getRotationYaw() / (float)180) * Math.PI;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.getMotionY() < 0.0) {
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP4.setMotionY(-0.05);
            }
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP5;
            boolean bl = false;
            double d2 = Math.sin(d);
            iEntityPlayerSP6.setMotionX(-d2 * ((Number)this.speed.get()).doubleValue());
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP6 = iEntityPlayerSP7;
            bl = false;
            d2 = Math.cos(d);
            iEntityPlayerSP6.setMotionZ(d2 * ((Number)this.speed.get()).doubleValue());
        }
    }

    public final LinkedBlockingQueue getPackets() {
        return this.packets;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        if (!MovementUtils.isMoving()) {
            this.timer = 0;
        } else {
            int n = this.timer;
            this.timer = n + 1;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround() && this.timer > 1) {
            this.doAsFly = true;
            this.stage = 0;
            this.move();
        }
        if (this.stage >= 1) {
            this.doAsFly = false;
            if (this.packets.size() > 0) {
                for (IPacket iPacket : this.packets) {
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP2.getSendQueue().addToSendQueue(iPacket);
                    this.packets.remove(iPacket);
                }
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        if (!this.doAsFly) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerPosition(iPacket) || MinecraftInstance.classProvider.isCPacketPlayerPosLook(iPacket) || MinecraftInstance.classProvider.isCPacketPlayer(iPacket)) {
            packetEvent.cancelEvent();
            this.packets.add(iPacket);
            int n = this.stage;
            this.stage = n + 1;
        }
    }

    @Override
    public void onDisable() {
        this.doAsFly = false;
        if (this.packets.size() > 0) {
            for (IPacket iPacket : this.packets) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.getSendQueue().addToSendQueue(iPacket);
                this.packets.remove(iPacket);
            }
        }
    }

    public HytSpeed() {
        this.packets = new LinkedBlockingQueue();
    }
}

