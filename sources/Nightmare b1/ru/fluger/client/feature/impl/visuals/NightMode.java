// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class NightMode extends Feature
{
    public static NumberSetting darkModifier;
    
    public NightMode() {
        super("NightMode", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0433\u0430\u043c\u043c\u0443 \u0442\u0435\u043c\u043d\u0435\u0435\u0435", Type.Visuals);
        NightMode.darkModifier = new NumberSetting("Dark Modifier", 0.2f, 0.0f, 1.0f, 0.01f, () -> true);
        this.addSettings(NightMode.darkModifier);
    }
    
    @Override
    public void onEnable() {
        NightMode.mc.t.aE = 1.0f;
        super.onEnable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(NightMode.darkModifier.getCurrentValue(), 2));
    }
}
