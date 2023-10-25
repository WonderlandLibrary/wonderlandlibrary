/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.combat.TargetStrafe;
import org.celestial.client.feature.impl.movement.FastClimb;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.feature.impl.movement.Jesus;
import org.celestial.client.feature.impl.movement.LongJump;
import org.celestial.client.feature.impl.movement.Speed;
import org.celestial.client.feature.impl.movement.Timer;
import org.celestial.client.feature.impl.movement.WaterLeave;
import org.celestial.client.feature.impl.movement.WaterSpeed;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class FlagDetector
extends Feature {
    public FlagDetector() {
        super("FlagDetector", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u044b\u043a\u043b\u044e\u0447\u0430\u0435\u0442 \u043c\u043e\u0434\u0443\u043b\u044c \u043f\u0440\u0438 \u0435\u0433\u043e \u0434\u0435\u0442\u0435\u043a\u0442\u0435", Type.Misc);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (FlagDetector.mc.player.ticksExisted < 5) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (Celestial.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
                this.featureAlert("Speed");
                Celestial.instance.featureManager.getFeatureByClass(Speed.class).toggle();
            } else if (!(!Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState() || Flight.flyMode.currentMode.equals("Matrix Pearl") || Flight.flyMode.currentMode.equals("JetMine") || Flight.flyMode.currentMode.equals("Matrix Pearl 6.6.0") || Flight.flyMode.currentMode.equals("Matrix Pearl Test") || Flight.flyMode.currentMode.equals("Sunrise Drop") || Flight.flyMode.currentMode.equals("WellMore New"))) {
                this.featureAlert("Flight");
                Celestial.instance.featureManager.getFeatureByClass(Flight.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(FastClimb.class).getState() && FlagDetector.mc.player.isOnLadder() && !FlagDetector.mc.player.isUsingItem()) {
                this.featureAlert("FastClimb");
                Celestial.instance.featureManager.getFeatureByClass(FastClimb.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && KillAura.target != null) {
                this.featureAlert("TargetStrafe");
                Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(LongJump.class).getState() && !LongJump.mode.currentMode.equals("Matrix Hurt")) {
                this.featureAlert("LongJump");
                Celestial.instance.featureManager.getFeatureByClass(LongJump.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(Jesus.class).getState() && FlagDetector.mc.player.isInLiquid()) {
                this.featureAlert("LiquidWalk");
                Celestial.instance.featureManager.getFeatureByClass(Jesus.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(Timer.class).getState()) {
                this.featureAlert("Timer");
                Celestial.instance.featureManager.getFeatureByClass(Timer.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(WaterLeave.class).getState()) {
                this.featureAlert("WaterLeave");
                Celestial.instance.featureManager.getFeatureByClass(WaterLeave.class).toggle();
            } else if (Celestial.instance.featureManager.getFeatureByClass(WaterSpeed.class).getState() && FlagDetector.mc.player.isInLiquid()) {
                this.featureAlert("WaterSpeed");
                Celestial.instance.featureManager.getFeatureByClass(WaterSpeed.class).toggle();
            }
        }
    }

    public void featureAlert(String feature) {
        NotificationManager.publicity("Feature", "Disabling " + feature + " due to lag back", 2, NotificationType.WARNING);
    }
}

