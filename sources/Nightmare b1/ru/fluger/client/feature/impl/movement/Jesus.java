// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class Jesus extends Feature
{
    public TimerHelper timerHelper;
    public static ListSetting mode;
    public static NumberSetting speed;
    public static BooleanSetting useTimer;
    private final NumberSetting timerSpeed;
    private final BooleanSetting speedCheck;
    private final BooleanSetting autoMotionStop;
    private final BooleanSetting autoWaterDown;
    
    public Jesus() {
        super("Jesus", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043f\u0440\u044b\u0433\u0430\u0442\u044c \u043d\u0430 \u0432\u043e\u0434\u0435", Type.Movement);
        this.timerHelper = new TimerHelper();
        this.timerSpeed = new NumberSetting("Timer Speed", 1.05f, 1.01f, 1.5f, 0.01f, () -> Jesus.useTimer.getCurrentValue());
        this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        this.autoMotionStop = new BooleanSetting("Auto Motion Stop", true, () -> Jesus.mode.currentMode.equals("Old Matrix"));
        this.autoWaterDown = new BooleanSetting("Auto Water Down", false, () -> Jesus.mode.currentMode.equals("Old Matrix"));
        this.addSettings(Jesus.mode, Jesus.speed, Jesus.useTimer, this.timerSpeed, this.speedCheck, this.autoWaterDown, this.autoMotionStop);
    }
    
    @Override
    public void onDisable() {
        Jesus.mc.Y.timerSpeed = 1.0f;
        if (Jesus.mode.currentMode.equals("ReallyWorld") && this.autoWaterDown.getCurrentValue()) {
            final bud h = Jesus.mc.h;
            h.t -= 500.0;
        }
        super.onDisable();
    }
    
    private boolean isWater() {
        final et bp1 = new et(Jesus.mc.h.p - 0.5, Jesus.mc.h.q - 0.5, Jesus.mc.h.r - 0.5);
        final et bp2 = new et(Jesus.mc.h.p - 0.5, Jesus.mc.h.q - 0.5, Jesus.mc.h.r + 0.5);
        final et bp3 = new et(Jesus.mc.h.p + 0.5, Jesus.mc.h.q - 0.5, Jesus.mc.h.r + 0.5);
        final et bp4 = new et(Jesus.mc.h.p + 0.5, Jesus.mc.h.q - 0.5, Jesus.mc.h.r - 0.5);
        return (Jesus.mc.h.l.o(bp1).u() == aox.j && Jesus.mc.h.l.o(bp2).u() == aox.j && Jesus.mc.h.l.o(bp3).u() == aox.j && Jesus.mc.h.l.o(bp4).u() == aox.j) || (Jesus.mc.h.l.o(bp1).u() == aox.l && Jesus.mc.h.l.o(bp2).u() == aox.l && Jesus.mc.h.l.o(bp3).u() == aox.l && Jesus.mc.h.l.o(bp4).u() == aox.l);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        this.setSuffix(Jesus.mode.getCurrentMode());
        if (Jesus.mc.h.a(vb.a) || !this.speedCheck.getCurrentValue()) {
            final et blockPos = new et(bib.z().h.p, Jesus.mc.h.q - 0.1, Jesus.mc.h.r);
            final aow block = Jesus.mc.f.o(blockPos).u();
            if (Jesus.useTimer.getCurrentValue()) {
                Jesus.mc.Y.timerSpeed = this.timerSpeed.getCurrentValue();
            }
            if (Jesus.mode.currentMode.equalsIgnoreCase("Old Matrix")) {
                if (Jesus.mc.f.o(new et(Jesus.mc.h.p, Jesus.mc.h.q + 0.09999999747378752, Jesus.mc.h.r)).u() instanceof aru) {
                    Jesus.mc.h.t = 0.07000000074505806;
                }
                if (block instanceof aru && !bib.z().h.z) {
                    if (bib.z().f.o(new et(bib.z().h.p, Jesus.mc.h.q, Jesus.mc.h.r)).u() == aox.j) {
                        Jesus.mc.h.s = 0.0;
                        Jesus.mc.h.t = 0.035999998450279236;
                        Jesus.mc.h.u = 0.0;
                    }
                    else {
                        MovementHelper.setSpeed(Jesus.speed.getCurrentValue());
                    }
                    if (bib.z().h.C) {
                        Jesus.mc.h.t = 0.2;
                    }
                }
            }
            else if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld")) {
                final et blockPos2 = new et(Jesus.mc.h.p, Jesus.mc.h.q - 0.02, Jesus.mc.h.r);
                final aow block2 = Jesus.mc.f.o(blockPos2).u();
                if (Jesus.mc.f.o(new et(Jesus.mc.h.p, Jesus.mc.h.q - 0.5, Jesus.mc.h.r)).u() == aox.j && Jesus.mc.h.z) {
                    Jesus.mc.h.t = 0.2;
                }
                if (block2 instanceof aru && !Jesus.mc.h.z) {
                    if (Jesus.mc.f.o(new et(Jesus.mc.h.p, Jesus.mc.h.q + 0.12999999523162842, Jesus.mc.h.r)).u() == aox.j) {
                        Jesus.mc.h.t = 0.10000000149011612;
                        MovementHelper.setSpeed(0.1f);
                        Jesus.mc.h.aR = 0.0f;
                    }
                    else {
                        MovementHelper.setSpeed(1.1f);
                        Jesus.mc.h.t = -0.10000000149011612;
                    }
                    if (Jesus.mc.h.A) {
                        Jesus.mc.h.t = 0.18000000715255737;
                    }
                }
                if (Jesus.mc.f.o(new et(Jesus.mc.h.p, Jesus.mc.h.q + 0.2, Jesus.mc.h.r)).u() instanceof aru) {
                    Jesus.mc.h.t = 0.18;
                }
            }
        }
    }
    
    static {
        Jesus.mode = new ListSetting("Jesus Mode", "Old Matrix", () -> true, new String[] { "Old Matrix", "ReallyWorld" });
        Jesus.speed = new NumberSetting("Speed", 0.65f, 0.1f, 10.0f, 0.01f, () -> !Jesus.mode.currentMode.equals("ReallyWorld"));
        Jesus.useTimer = new BooleanSetting("Use Timer", false, () -> true);
    }
}
