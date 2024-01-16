// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class NoSlowDown extends Feature
{
    public static NumberSetting percentage;
    private final ListSetting noSlowMode;
    public int usingTicks;
    
    public NoSlowDown() {
        super("NoSlowDown", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u043c\u0435\u0434\u043b\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0438 \u0435\u0434\u044b \u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", Type.Player);
        this.noSlowMode = new ListSetting("NoSlow Mode", "Matrix", () -> true, new String[] { "Vanilla", "Matrix" });
        this.addSettings(this.noSlowMode, NoSlowDown.percentage);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(this.noSlowMode.getCurrentMode() + ", " + NoSlowDown.percentage.getCurrentValue() + "%");
        this.usingTicks = (NoSlowDown.mc.h.isUsingItem() ? (++this.usingTicks) : 0);
        if (!this.getState() || !NoSlowDown.mc.h.isUsingItem()) {
            return;
        }
        if (this.noSlowMode.currentMode.equals("Matrix") && NoSlowDown.mc.h.isUsingItem()) {
            if (NoSlowDown.mc.h.z && !NoSlowDown.mc.t.X.e()) {
                if (NoSlowDown.mc.h.T % 2 == 0) {
                    final bud h = NoSlowDown.mc.h;
                    h.s *= 0.35;
                    final bud h2 = NoSlowDown.mc.h;
                    h2.u *= 0.35;
                }
            }
            else if (NoSlowDown.mc.h.L > 0.2) {
                final bud h3 = NoSlowDown.mc.h;
                h3.s *= 0.9100000262260437;
                final bud h4 = NoSlowDown.mc.h;
                h4.u *= 0.9100000262260437;
            }
        }
    }
    
    static {
        NoSlowDown.percentage = new NumberSetting("Percentage", 100.0f, 0.0f, 100.0f, 1.0f);
    }
}
