// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class Flight extends Feature
{
    public boolean damage;
    public boolean flaging;
    public TimerHelper timerHelper;
    public static ListSetting flyMode;
    public final NumberSetting speed;
    public final NumberSetting motionY;
    public float ticks;
    
    public Flight() {
        super("Flight", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0435\u0442\u0430\u0442\u044c \u0431\u0435\u0437 \u043a\u0440\u0435\u0430\u0442\u0438\u0432 \u0440\u0435\u0436\u0438\u043c\u0430", Type.Movement);
        this.damage = false;
        this.flaging = false;
        this.timerHelper = new TimerHelper();
        this.speed = new NumberSetting("Flight Speed", 5.0f, 0.1f, 15.0f, 0.1f, () -> Flight.flyMode.currentMode.equals("Vanilla") || Flight.flyMode.currentMode.equals("Matrix Glide"));
        this.motionY = new NumberSetting("Motion Y", 0.05f, 0.01f, 0.1f, 0.01f, () -> Flight.flyMode.currentMode.equals("Matrix Elytra"));
        this.ticks = 0.0f;
        this.addSettings(Flight.flyMode, this.motionY, this.speed);
    }
    
    @EventTarget
    public void onPreUpdate(final EventPreMotion event) {
        final String mode = Flight.flyMode.getOptions();
        this.setSuffix("" + mode);
        if (mode.equalsIgnoreCase("Matrix Glide")) {
            if (Flight.mc.h.z) {
                Flight.mc.h.cu();
                this.timerHelper.reset();
            }
            else if (!Flight.mc.h.z && this.timerHelper.hasReached(280.0)) {
                Flight.mc.h.t = -0.04;
                MovementHelper.setSpeed(this.speed.getCurrentValue());
            }
        }
        else if (mode.equalsIgnoreCase("Vanilla")) {
            Flight.mc.h.bO.b = true;
            Flight.mc.h.bO.c = true;
            if (Flight.mc.t.X.i) {
                Flight.mc.h.t = 2.0;
            }
            if (Flight.mc.t.Y.i) {
                Flight.mc.h.t = -2.0;
            }
            MovementHelper.setSpeed(this.speed.getCurrentValue());
        }
        else if (mode.equalsIgnoreCase("Glide Fly")) {
            int eIndex = -1;
            for (int i = 0; i < 45; ++i) {
                if (Flight.mc.h.bv.a(i).c() == air.cS && eIndex == -1) {
                    eIndex = i;
                }
            }
            if (Flight.mc.h.T % 25 == 0) {
                Flight.mc.c.a(0, eIndex, 0, afw.a, Flight.mc.h);
                Flight.mc.c.a(0, 6, 0, afw.a, Flight.mc.h);
                Flight.mc.h.d.a(new lq(Flight.mc.h, lq.a.i));
                Flight.mc.h.d.a(new lq(Flight.mc.h, lq.a.i));
                Flight.mc.c.a(0, 6, 0, afw.a, Flight.mc.h);
                Flight.mc.c.a(0, eIndex, 0, afw.a, Flight.mc.h);
            }
            if (!this.getState()) {
                return;
            }
            Flight.mc.h.bD = 0;
            if (Flight.mc.h.e.g) {
                Flight.mc.h.cu();
            }
            MovementHelper.setSpeed(MovementHelper.getSpeed() * 1.0f + 0.2f);
        }
        else if (mode.equalsIgnoreCase("Matrix Pearl")) {
            if (Flight.mc.h.ay > 0 && Flight.mc.h.ay <= 9) {
                final bud h = Flight.mc.h;
                h.t += 0.13;
                if (Flight.mc.t.T.i && !Flight.mc.h.z) {
                    final bud h2 = Flight.mc.h;
                    h2.s -= rk.a((float)Math.toRadians(Flight.mc.h.v));
                    final bud h3 = Flight.mc.h;
                    h3.u += rk.b((float)Math.toRadians(Flight.mc.h.v));
                }
            }
        }
        else if (mode.equalsIgnoreCase("Matrix Web")) {
            if (Flight.mc.h.E) {
                Flight.mc.h.E = false;
                final bud h4 = Flight.mc.h;
                h4.t *= ((Flight.mc.h.T % 2 == 0) ? -100.0 : -0.05);
            }
        }
        else if (mode.equalsIgnoreCase("Matrix Fall")) {
            if (this.damage && Flight.mc.h.L > 0.0f && Flight.mc.h.T % 4 == 0) {
                Flight.mc.h.t = -0.003;
            }
            if (Flight.mc.h.L >= 3.0f) {
                this.damage = true;
                Flight.mc.Y.timerSpeed = 2.0f;
                Flight.mc.h.t = 0.0;
                Flight.mc.h.d.a(new lk.a(Flight.mc.h.p, Flight.mc.h.q, Flight.mc.h.r, false));
                Flight.mc.h.d.a(new lk.a(Flight.mc.h.p, Flight.mc.h.q, Flight.mc.h.r, true));
                Flight.mc.h.t = -0.003;
                Flight.mc.Y.timerSpeed = 1.0f;
                Flight.mc.h.L = 0.0f;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Flight.mc.h.bO.b = false;
        Flight.mc.h.bO.c = false;
        Flight.mc.Y.timerSpeed = 1.0f;
    }
    
    static {
        Flight.flyMode = new ListSetting("Flight Mode", "Matrix Glide", () -> true, new String[] { "Vanilla", "Matrix Fall", "Matrix Glide", "Matrix Pearl", "Glide Fly", "Matrix Web" });
    }
}
