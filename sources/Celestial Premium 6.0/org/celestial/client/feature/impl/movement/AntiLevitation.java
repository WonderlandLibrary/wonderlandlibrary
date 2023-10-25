/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import java.util.Objects;
import net.minecraft.potion.Potion;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class AntiLevitation
extends Feature {
    public AntiLevitation() {
        super("AntiLevitation", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u044d\u0444\u0444\u0435\u043a\u0442 \u043b\u0435\u0432\u0438\u0442\u0430\u0446\u0438\u0438", Type.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (AntiLevitation.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(25)))) {
            AntiLevitation.mc.player.removeActivePotionEffect(Potion.getPotionById(25));
        }
    }
}

