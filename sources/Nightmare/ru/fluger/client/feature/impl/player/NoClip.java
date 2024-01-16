// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.Fluger;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class NoClip extends Feature
{
    private final NumberSetting customSpeedXZ;
    private final BooleanSetting customSpeed;
    
    public NoClip() {
        super("NoClip", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Player);
        this.customSpeed = new BooleanSetting("Custom Speed", false, () -> true);
        this.customSpeedXZ = new NumberSetting("NoClip Speed", 1.0f, 0.01f, 1.5f, 0.01f, this.customSpeed::getCurrentValue);
        this.addSettings(this.customSpeed, this.customSpeedXZ);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (NoClip.mc.h != null) {
            NoClip.mc.h.t = 0.0;
            if (this.customSpeed.getCurrentValue()) {
                final bud h = NoClip.mc.h;
                h.s *= this.customSpeedXZ.getCurrentValue();
                final bud h2 = NoClip.mc.h;
                h2.u *= this.customSpeedXZ.getCurrentValue();
            }
            if (NoClip.mc.t.X.e()) {
                final bud h3 = NoClip.mc.h;
                h3.t += 0.4;
            }
            else if (NoClip.mc.t.Y.e()) {
                final bud h4 = NoClip.mc.h;
                h4.t -= 0.4;
            }
        }
    }
    
    public static boolean isNoClip(final vg entity) {
        return (Fluger.instance.featureManager.getFeatureByClass(NoClip.class).getState() && NoClip.mc.h != null && (NoClip.mc.h.bJ() == null || entity == NoClip.mc.h.bJ())) || entity.Q;
    }
    
    @Override
    public void onDisable() {
        NoClip.mc.h.Q = false;
        super.onDisable();
    }
}
