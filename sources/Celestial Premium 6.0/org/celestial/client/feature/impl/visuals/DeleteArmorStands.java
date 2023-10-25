/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import baritone.events.events.player.EventUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class DeleteArmorStands
extends Feature {
    public DeleteArmorStands() {
        super("DeleteArmorStands", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0443\u0434\u0430\u043b\u044f\u0435\u0442 \u0432\u0441\u0435 \u0430\u0440\u043c\u043e\u0440-\u0441\u0442\u0435\u043d\u0434\u044b \u0441 \u043c\u0438\u0440\u0430", Type.Misc);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (DeleteArmorStands.mc.player == null || DeleteArmorStands.mc.world == null) {
            return;
        }
        for (Entity entity : DeleteArmorStands.mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityArmorStand)) continue;
            DeleteArmorStands.mc.world.removeEntity(entity);
        }
    }
}

