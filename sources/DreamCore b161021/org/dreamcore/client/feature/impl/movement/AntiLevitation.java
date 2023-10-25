package org.dreamcore.client.feature.impl.movement;

import net.minecraft.init.MobEffects;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class AntiLevitation extends Feature {

    public AntiLevitation() {
        super("AntiLevitation", "Убирает эффект левитации", Type.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.isPotionActive(MobEffects.LEVITATION)) {
            mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    }
}