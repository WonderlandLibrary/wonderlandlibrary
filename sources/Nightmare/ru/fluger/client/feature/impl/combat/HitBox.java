// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.Fluger;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class HitBox extends Feature
{
    public static NumberSetting expand;
    
    public HitBox() {
        super("HitBox", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0445\u0438\u0442\u0431\u043e\u043a\u0441 \u0443 \u0435\u043d\u0442\u0438\u0442\u0438", Type.Combat);
        HitBox.expand = new NumberSetting("Expand", 0.2f, 0.01f, 2.0f, 0.01f, () -> true);
        this.addSettings(HitBox.expand);
    }
    
    public static float hitBoxExpand(final vg entity) {
        if (entity == HitBox.mc.h || !(entity instanceof aed) || !Fluger.instance.featureManager.getFeatureByClass(HitBox.class).getState()) {
            return 0.0f;
        }
        return HitBox.expand.getCurrentValue();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(HitBox.expand.getCurrentValue(), 2));
    }
}
