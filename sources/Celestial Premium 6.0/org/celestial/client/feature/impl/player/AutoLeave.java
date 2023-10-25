/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.misc.FreeCam;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoLeave
extends Feature {
    private final BooleanSetting friendCheck = new BooleanSetting("Friend check", true, () -> true);
    private final NumberSetting distanceToEntity = new NumberSetting("Distance to entity", 50.0f, 5.0f, 50.0f, 5.0f, () -> true);

    public AutoLeave() {
        super("AutoLeave", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u044b\u0445\u043e\u0434\u0438\u0442 \u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u0435\u0441\u043b\u0438 \u0440\u044f\u0434\u043e\u043c \u0435\u0441\u0442\u044c \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u044c", Type.Player);
        this.addSettings(this.friendCheck, this.distanceToEntity);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Celestial.instance.featureManager.getFeatureByClass(FreeCam.class).getState()) {
            return;
        }
        for (Entity entity : AutoLeave.mc.world.loadedEntityList) {
            if (entity == null || entity == AutoLeave.mc.player || !(entity instanceof EntityPlayer) || Celestial.instance.friendManager.isFriend(entity.getName()) && this.friendCheck.getCurrentValue() || !(AutoLeave.mc.player.getDistanceToEntity(entity) <= this.distanceToEntity.getCurrentValue())) continue;
            AutoLeave.mc.world.sendAutoLeavePacket(entity);
        }
    }
}

