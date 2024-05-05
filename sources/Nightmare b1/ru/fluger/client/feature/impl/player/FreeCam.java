// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class FreeCam extends Feature
{
    private final NumberSetting speed;
    private final BooleanSetting reallyWorld;
    double x;
    double y;
    double z;
    
    public FreeCam() {
        super("FreeCam", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0435\u0442\u0430\u0442\u044c \u0432 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0439 \u043a\u0430\u043c\u0435\u0440\u0435", Type.Player);
        this.speed = new NumberSetting("Flying Speed", 0.5f, 0.1f, 1.0f, 0.1f, () -> true);
        this.reallyWorld = new BooleanSetting("ReallyWorld Bypass", false, () -> true);
        this.addSettings(this.speed, this.reallyWorld);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (FreeCam.mc.h.F) {
            this.toggle();
        }
        if (event.getPacket() instanceof jq && ((this.reallyWorld.getCurrentValue() && FreeCam.mc.h != null) || FreeCam.mc.f != null)) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (FreeCam.mc.h.F) {
            this.toggle();
        }
        this.x = FreeCam.mc.h.p;
        this.y = FreeCam.mc.h.q;
        this.z = FreeCam.mc.h.r;
        final bue ent = new bue(FreeCam.mc.f, FreeCam.mc.h.da());
        ent.bv = FreeCam.mc.h.bv;
        ent.bx = FreeCam.mc.h.bx;
        ent.c(FreeCam.mc.h.cd());
        ent.a(this.x, FreeCam.mc.h.bw().b, this.z, FreeCam.mc.h.v, FreeCam.mc.h.w);
        ent.aP = FreeCam.mc.h.aP;
        FreeCam.mc.f.a(-1, ent);
    }
    
    @EventTarget
    public void onScreen(final EventRender2D e) {
        final bit sr = new bit(FreeCam.mc);
        final String yCoord = "" + Math.round(FreeCam.mc.h.q - this.y);
        final String str = "Y: " + yCoord;
        FreeCam.mc.robotoRegularFontRender.drawStringWithOutline(str, (sr.a() - FreeCam.mc.k.a(str)) / 1.98, sr.b() / 1.8 - 20.0, -1);
    }
    
    @EventTarget
    public void onPreMotion(final EventUpdate e) {
        if (FreeCam.mc.h == null || FreeCam.mc.f == null) {
            return;
        }
        FreeCam.mc.h.t = 0.0;
        if (FreeCam.mc.t.X.i) {
            FreeCam.mc.h.t = this.speed.getCurrentValue();
        }
        if (FreeCam.mc.t.Y.i) {
            FreeCam.mc.h.t = -this.speed.getCurrentValue();
        }
        FreeCam.mc.h.Q = true;
        MovementHelper.setSpeed(this.speed.getCurrentValue());
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        FreeCam.mc.h.b(this.x, this.y, this.z);
        FreeCam.mc.v().a(new lk.a(FreeCam.mc.h.p, FreeCam.mc.h.q + 0.01, FreeCam.mc.h.r, FreeCam.mc.h.z));
        FreeCam.mc.h.bO.b = false;
        FreeCam.mc.h.Q = false;
        FreeCam.mc.f.e(-1);
    }
}
