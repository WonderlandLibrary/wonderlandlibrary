// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.feature.impl.player.FreeCam;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class BedrockClip extends Feature
{
    private final BooleanSetting autoFreecam;
    
    public BedrockClip() {
        super("BedrockClip", "\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u0430\u0441 \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a \u043a\u043e\u0433\u0434\u0430 \u0432\u044b \u043d\u0430 \u0432\u043e\u0434\u0435, \u0438\u043b\u0438 \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 \u0443\u0440\u043e\u043d", Type.Movement);
        this.autoFreecam = new BooleanSetting("Auto Freecam", false, () -> true);
        this.addSettings(this.autoFreecam);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (BedrockClip.mc.h.ao() || BedrockClip.mc.h.ay > 0) {
            for (int i = 0; i < 10; ++i) {
                BedrockClip.mc.h.d.a(new lk.a(BedrockClip.mc.h.p, BedrockClip.mc.h.q, BedrockClip.mc.h.r, false));
            }
            for (int i = 0; i < 10; ++i) {
                BedrockClip.mc.h.d.a(new lk.a(BedrockClip.mc.h.p, BedrockClip.mc.h.q - (BedrockClip.mc.h.q + 2.0), BedrockClip.mc.h.r, false));
            }
            BedrockClip.mc.h.b(BedrockClip.mc.h.p, BedrockClip.mc.h.q - (BedrockClip.mc.h.q + 2.0), BedrockClip.mc.h.r);
            if (this.autoFreecam.getCurrentValue()) {
                Fluger.instance.featureManager.getFeatureByClass(FreeCam.class).toggle();
            }
            this.toggle();
        }
    }
}
