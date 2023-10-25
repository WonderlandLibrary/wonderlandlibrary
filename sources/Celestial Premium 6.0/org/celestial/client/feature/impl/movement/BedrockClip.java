/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import baritone.events.events.player.EventUpdate;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.misc.FreeCam;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class BedrockClip
extends Feature {
    private final BooleanSetting autoFreecam = new BooleanSetting("Auto Freecam", false, () -> true);
    private final BooleanSetting damageOnly = new BooleanSetting("Damage Only", false, () -> true);

    public BedrockClip() {
        super("BedrockClip", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u0430\u0441 \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a", Type.Movement);
        this.addSettings(this.autoFreecam, this.damageOnly);
    }

    @Override
    public void onEnable() {
        if (!this.damageOnly.getCurrentValue()) {
            if (!MovementHelper.isUnderBedrock()) {
                BedrockClip.mc.player.setPositionAndUpdate(BedrockClip.mc.player.posX, -3.0, BedrockClip.mc.player.posZ);
            } else {
                this.toggle();
            }
        }
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.damageOnly.getCurrentValue()) {
            if (BedrockClip.mc.player.hurtTime > 0) {
                if (!MovementHelper.isUnderBedrock()) {
                    BedrockClip.mc.player.setPositionAndUpdate(BedrockClip.mc.player.posX, -3.0, BedrockClip.mc.player.posZ);
                } else {
                    NotificationManager.publicity("BedrockClip", "Successfully teleported under bedrock!", 5, NotificationType.SUCCESS);
                    if (this.autoFreecam.getCurrentValue()) {
                        Celestial.instance.featureManager.getFeatureByClass(FreeCam.class).toggle();
                    }
                    this.toggle();
                }
            }
        } else if (MovementHelper.isUnderBedrock() && this.autoFreecam.getCurrentValue() && !Celestial.instance.featureManager.getFeatureByClass(FreeCam.class).getState()) {
            NotificationManager.publicity("BedrockClip", "Successfully teleported under bedrock!", 5, NotificationType.SUCCESS);
            Celestial.instance.featureManager.getFeatureByClass(FreeCam.class).toggle();
        }
    }
}

