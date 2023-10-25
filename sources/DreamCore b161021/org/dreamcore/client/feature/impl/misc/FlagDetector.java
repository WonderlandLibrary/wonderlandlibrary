package org.dreamcore.client.feature.impl.misc;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.combat.KillAura;
import org.dreamcore.client.feature.impl.combat.TargetStrafe;
import org.dreamcore.client.feature.impl.movement.*;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class FlagDetector extends Feature {

    public FlagDetector() {
        super("FlagDetector", "Автоматически выключает модуль при его детекте", Type.Misc);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (this.getState()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                if (dreamcore.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
                    featureAlert("Speed");
                    dreamcore.instance.featureManager.getFeatureByClass(Speed.class).toggle();
                } else if (dreamcore.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
                    featureAlert("Flight");
                    dreamcore.instance.featureManager.getFeatureByClass(Flight.class).toggle();
                } else if (dreamcore.instance.featureManager.getFeatureByClass(FastClimb.class).getState() && mc.player.isOnLadder() && !mc.player.isUsingItem()) {
                    featureAlert("FastClimb");
                    dreamcore.instance.featureManager.getFeatureByClass(FastClimb.class).toggle();
                } else if (dreamcore.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && KillAura.target != null) {
                    featureAlert("TargetStrafe");
                    dreamcore.instance.featureManager.getFeatureByClass(TargetStrafe.class).toggle();
                } else if (dreamcore.instance.featureManager.getFeatureByClass(LongJump.class).getState()) {
                    featureAlert("LongJump");
                    dreamcore.instance.featureManager.getFeatureByClass(LongJump.class).toggle();
                } else if (dreamcore.instance.featureManager.getFeatureByClass(LiquidWalk.class).getState() && mc.player.isInLiquid()) {
                    featureAlert("LiquidWalk");
                    dreamcore.instance.featureManager.getFeatureByClass(LiquidWalk.class).toggle();
                } else if (dreamcore.instance.featureManager.getFeatureByClass(Timer.class).getState()) {
                    featureAlert("Timer");
                    dreamcore.instance.featureManager.getFeatureByClass(Timer.class).toggle();
                }
            }
        }
    }

    public void featureAlert(String feature) {
        NotificationManager.publicity(feature, "Disabling due to lag back", 3, NotificationType.WARNING);
    }
}