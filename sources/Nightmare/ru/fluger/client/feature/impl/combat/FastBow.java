// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class FastBow extends Feature
{
    private final NumberSetting ticks;
    
    public FastBow() {
        super("FastBow", "\u041f\u0440\u0438 \u0437\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u041f\u041a\u041c \u0438\u0433\u0440\u043e\u043a \u0431\u044b\u0441\u0442\u0440\u043e \u0441\u0442\u0440\u0435\u043b\u044f\u0435\u0442 \u0438\u0437 \u043b\u0443\u043a\u0430", Type.Combat);
        this.ticks = new NumberSetting("Bow Ticks", 1.5f, 1.5f, 10.0f, 0.5f, () -> true);
        this.addSettings(this.ticks);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (FastBow.mc.h.bv.i().c() instanceof ahg && FastBow.mc.h.cG() && FastBow.mc.h.cL() >= this.ticks.getCurrentValue()) {
            FastBow.mc.h.d.a(new lp(lp.a.f, et.a, FastBow.mc.h.bt()));
            FastBow.mc.h.d.a(new mb(ub.a));
            FastBow.mc.h.cM();
        }
    }
}
