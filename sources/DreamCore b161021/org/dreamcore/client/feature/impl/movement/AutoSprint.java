package org.dreamcore.client.feature.impl.movement;

import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.combat.KillAura;
import org.dreamcore.client.feature.impl.misc.TeleportExploit;
import org.dreamcore.client.helpers.player.MovementHelper;

public class AutoSprint extends Feature {

    public AutoSprint() {
        super("AutoSprint", "Зажимает CTRL за вас, что бы быстро бежать", Type.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(dreamcore.instance.featureManager.getFeatureByClass(TeleportExploit.class).getState())) {
            if (!(dreamcore.instance.featureManager.getFeatureByClass(Scaffold.class).getState() && Scaffold.sprintoff.getBoolValue())) {
                if (!(dreamcore.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.sprinting.getBoolValue() && KillAura.target != null)) {
                    mc.player.setSprinting(MovementHelper.isMoving());
                }
            }
        }
    }
}
