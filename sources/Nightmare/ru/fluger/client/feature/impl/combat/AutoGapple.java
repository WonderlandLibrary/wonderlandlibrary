// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class AutoGapple extends Feature
{
    public static NumberSetting health;
    public static NumberSetting eatDelay;
    private boolean isActive;
    private final TimerHelper timerHelper;
    
    public AutoGapple() {
        super("AutoGApple", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0435\u0441\u0442 \u044f\u0431\u043b\u043e\u043a\u043e \u043f\u0440\u0438 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u043e\u043c \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435", Type.Combat);
        this.timerHelper = new TimerHelper();
        AutoGapple.eatDelay = new NumberSetting("Eat Delay", 300.0f, 0.0f, 1000.0f, 50.0f, () -> true);
        AutoGapple.health = new NumberSetting("Health Amount", 15.0f, 1.0f, 20.0f, 1.0f, () -> true);
        this.addSettings(AutoGapple.health, AutoGapple.eatDelay);
    }
    
    private boolean isGoldenApple(final aip itemStack) {
        return itemStack != null && !itemStack.b() && itemStack.c() instanceof aik;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix("" + (int)AutoGapple.health.getCurrentValue());
        if (AutoGapple.mc.h == null || AutoGapple.mc.f == null) {
            return;
        }
        if (!(AutoGapple.mc.h.cp().c() instanceof aik)) {
            return;
        }
        if (this.timerHelper.hasReached(AutoGapple.eatDelay.getCurrentValue())) {
            if (this.isGoldenApple(AutoGapple.mc.h.cp()) && AutoGapple.mc.h.cd() + AutoGapple.mc.h.cD() <= AutoGapple.health.getCurrentValue()) {
                if (AutoGapple.mc.m != null) {
                    final blk guiScreen = AutoGapple.mc.m;
                    guiScreen.p = true;
                }
                this.isActive = true;
                bhy.a(AutoGapple.mc.t.ad.j(), true);
            }
            else if (this.isActive) {
                bhy.a(AutoGapple.mc.t.ad.j(), false);
                this.isActive = false;
            }
            this.timerHelper.reset();
        }
    }
}
