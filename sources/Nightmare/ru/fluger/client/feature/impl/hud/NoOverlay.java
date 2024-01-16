// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class NoOverlay extends Feature
{
    public static BooleanSetting rain;
    public static BooleanSetting noHurtCam;
    public static BooleanSetting cameraClip;
    public static BooleanSetting antiTotem;
    public static BooleanSetting noFire;
    public static BooleanSetting noBossBar;
    public static BooleanSetting noArmorStand;
    public static BooleanSetting blindness;
    
    public NoOverlay() {
        super("NoOverlay", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u044b\u0435 \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u044b \u0440\u0435\u043d\u0434\u0435\u0440\u0430 \u0432 \u0438\u0433\u0440\u0435", Type.Hud);
        this.addSettings(NoOverlay.rain, NoOverlay.noArmorStand, NoOverlay.noHurtCam, NoOverlay.cameraClip, NoOverlay.antiTotem, NoOverlay.noFire, NoOverlay.blindness, NoOverlay.noBossBar);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (NoOverlay.rain.getCurrentValue() && NoOverlay.mc.f.Y()) {
            NoOverlay.mc.f.k(0.0f);
            NoOverlay.mc.f.i(0.0f);
        }
        if ((NoOverlay.blindness.getCurrentValue() && NoOverlay.mc.h.a(vb.o)) || NoOverlay.mc.h.a(vb.i)) {
            NoOverlay.mc.h.d(vb.i);
            NoOverlay.mc.h.d(vb.o);
        }
        if (NoOverlay.noArmorStand.getCurrentValue()) {
            if (NoOverlay.mc.h == null || NoOverlay.mc.f == null) {
                return;
            }
            for (final vg entity : NoOverlay.mc.f.e) {
                if (entity != null) {
                    if (!(entity instanceof abz)) {
                        continue;
                    }
                    NoOverlay.mc.f.e(entity);
                }
            }
        }
    }
    
    static {
        NoOverlay.rain = new BooleanSetting("Rain", true, () -> true);
        NoOverlay.noHurtCam = new BooleanSetting("HurtCam", true, () -> true);
        NoOverlay.cameraClip = new BooleanSetting("Camera Clip", true, () -> true);
        NoOverlay.antiTotem = new BooleanSetting("AntiTotemAnimation", false, () -> true);
        NoOverlay.noFire = new BooleanSetting("NoFireOverlay", false, () -> true);
        NoOverlay.noBossBar = new BooleanSetting("NoBossBar", false, () -> true);
        NoOverlay.noArmorStand = new BooleanSetting("ArmorStand", false, () -> true);
        NoOverlay.blindness = new BooleanSetting("Blindness", true, () -> true);
    }
}
