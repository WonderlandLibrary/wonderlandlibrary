/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import baritone.events.events.player.EventUpdate;
import net.minecraft.block.BlockCactus;
import org.celestial.client.event.EventTarget;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class CactusLeave
extends Feature {
    private final NumberSetting motion = new NumberSetting("Motion", 15.0f, 1.0f, 20.0f, 1.0f, () -> true);

    public CactusLeave() {
        super("CactusLeave", "\u041f\u043e\u0434\u0431\u0440\u0430\u0441\u044b\u0432\u0430\u0435\u0442 \u0432\u0430\u0441 \u0432\u0432\u0435\u0440\u0445 \u043f\u0440\u0438 \u0443\u0440\u043e\u043d\u0435 \u043e\u0442 \u043a\u0430\u043a\u0442\u0443\u0441\u0430", Type.Movement);
        this.addSettings(this.motion);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (BlockCactus.canLeave && CactusLeave.mc.player.hurtTime > 0) {
            CactusLeave.mc.player.motionY = this.motion.getCurrentValue();
            BlockCactus.canLeave = false;
        }
    }
}

