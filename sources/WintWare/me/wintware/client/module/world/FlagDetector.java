/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.TargetStrafe;
import me.wintware.client.module.movement.Fly;
import me.wintware.client.module.movement.Speed;
import me.wintware.client.module.movement.Timer;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.ui.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FlagDetector
extends Module {
    public boolean disable;
    public boolean disabledModule;
    private final int[] vl = new int[5];

    public FlagDetector() {
        super("LagBack", Category.World);
    }

    @EventTarget
    public void packetIn(EventReceivePacket eventPacketReceive) {
        if (!this.getState()) {
            return;
        }
        if (eventPacketReceive.getPacket() instanceof SPacketPlayerPosLook) {
            if (Main.instance.moduleManager.getModuleByClass(Speed.class).getState()) {
                this.alert("Speed");
                Main.instance.moduleManager.getModuleByClass(Speed.class).toggle();
            }
            if (Main.instance.moduleManager.getModuleByClass(Fly.class).getState()) {
                this.alert("Fly");
                Main.instance.moduleManager.getModuleByClass(Fly.class).toggle();
            }
            if (Main.instance.moduleManager.getModuleByClass(TargetStrafe.class).getState()) {
                this.alert("TargetStrafe");
                Main.instance.moduleManager.getModuleByClass(TargetStrafe.class).toggle();
            }
            if (Main.instance.moduleManager.getModuleByClass(Timer.class).getState()) {
                this.alert("Timer");
                Main.instance.moduleManager.getModuleByClass(Timer.class).toggle();
            }
            if (this.disabledModule) {
                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.disabledModule = false;
            }
            FlagDetector.mc.timer.timerSpeed = 1.0f;
        }
    }

    public void alert(String module) {
        NotificationPublisher.queue("Module", module + " was turned off due to flag", NotificationType.WARNING);
    }
}

