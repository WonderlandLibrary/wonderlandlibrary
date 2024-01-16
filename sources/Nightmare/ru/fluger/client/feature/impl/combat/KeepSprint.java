// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class KeepSprint extends Feature
{
    public static NumberSetting speed;
    public static BooleanSetting setSprinting;
    
    public KeepSprint() {
        super("KeepSprint", "\u041f\u043e\u0432\u0437\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430 \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Combat);
        KeepSprint.speed = new NumberSetting("Keep Speed", 1.0f, 0.5f, 2.0f, 0.01f, () -> true);
        KeepSprint.setSprinting = new BooleanSetting("Set Sprinting", true, () -> true);
        this.addSettings(KeepSprint.setSprinting, KeepSprint.speed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(KeepSprint.speed.getCurrentValue(), 2));
    }
}
