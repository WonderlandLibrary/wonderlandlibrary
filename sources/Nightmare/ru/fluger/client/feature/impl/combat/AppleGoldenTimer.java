// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import org.lwjgl.input.Mouse;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class AppleGoldenTimer extends Feature
{
    public static boolean cooldown;
    private boolean isEated;
    
    public AppleGoldenTimer() {
        super("AppleGoldenTimer", "\u0421\u0442\u0430\u0432\u0438\u0442 \u043a\u0443\u043b-\u0434\u0430\u0443\u043d \u043d\u0430 \u0433\u0435\u043f\u043b\u044b", Type.Combat);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (AppleGoldenTimer.mc.h.cp().isOnFinish() || (AppleGoldenTimer.mc.h.co().isOnFinish() && AppleGoldenTimer.mc.h.cJ().c() == air.ar)) {
            this.isEated = true;
        }
        if (this.isEated) {
            AppleGoldenTimer.mc.h.dt().a(air.ar, 55);
            this.isEated = false;
        }
        if (AppleGoldenTimer.mc.h.dt().a(air.ar)) {
            AppleGoldenTimer.mc.t.ad.setPressed(false);
        }
        else if (Mouse.isButtonDown(1) && !(AppleGoldenTimer.mc.m instanceof bmg)) {
            AppleGoldenTimer.mc.t.ad.setPressed(true);
        }
    }
    
    private boolean isGoldenApple(final aip itemStack) {
        return itemStack != null && !itemStack.b() && itemStack.c() instanceof aik;
    }
}
