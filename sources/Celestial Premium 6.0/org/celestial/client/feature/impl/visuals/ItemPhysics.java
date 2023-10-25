/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class ItemPhysics
extends Feature {
    public static NumberSetting physicsSpeed;

    public ItemPhysics() {
        super("ItemPhysics", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0444\u0438\u0437\u0438\u043a\u0443 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u043f\u0440\u0438 \u0438\u0445 \u0432\u044b\u0431\u0440\u0430\u0441\u0438\u0432\u0430\u043d\u0438\u0438", Type.Visuals);
        physicsSpeed = new NumberSetting("Physics Speed", 0.5f, 0.1f, 5.0f, 0.5f, () -> true);
        this.addSettings(physicsSpeed);
    }
}

