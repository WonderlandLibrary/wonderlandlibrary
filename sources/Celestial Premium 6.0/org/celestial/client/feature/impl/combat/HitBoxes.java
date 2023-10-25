/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class HitBoxes
extends Feature {
    public static NumberSetting expand;

    public HitBoxes() {
        super("HitBoxes", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0445\u0438\u0442\u0431\u043e\u043a\u0441 \u0443 \u0435\u043d\u0442\u0438\u0442\u0438", Type.Combat);
        expand = new NumberSetting("Expand", 0.2f, 0.01f, 2.0f, 0.01f, () -> true);
        this.addSettings(expand);
    }

    public static float hitBoxExpand(Entity entity) {
        if (entity == HitBoxes.mc.player || !(entity instanceof EntityPlayer) || !Celestial.instance.featureManager.getFeatureByClass(HitBoxes.class).getState()) {
            return 0.0f;
        }
        return expand.getCurrentValue();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(expand.getCurrentValue(), 2));
    }
}

