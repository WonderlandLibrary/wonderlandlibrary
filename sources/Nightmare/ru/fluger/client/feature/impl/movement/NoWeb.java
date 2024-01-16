// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.events.impl.player.EventMove;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class NoWeb extends Feature
{
    public ListSetting noWebMode;
    public NumberSetting webSpeed;
    
    public NoWeb() {
        super("NoWeb", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u0445\u043e\u0434\u0438\u0442\u044c \u0432 \u043f\u0430\u0443\u0442\u0438\u043d\u0435", Type.Player);
        this.noWebMode = new ListSetting("NoWeb Mode", "Matrix", () -> true, new String[] { "Matrix", "Matrix New" });
        this.webSpeed = new NumberSetting("Web Speed", 0.8f, 0.1f, 2.0f, 0.1f, () -> this.noWebMode.currentMode.equals("Matrix New"));
        this.addSettings(this.noWebMode, this.webSpeed);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        final String mode = this.noWebMode.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Matrix New")) {
            final et blockPos = new et(NoWeb.mc.h.p, NoWeb.mc.h.q - 0.6, NoWeb.mc.h.r);
            final aow block = NoWeb.mc.f.o(blockPos).u();
            if (NoWeb.mc.h.E) {
                final bud h = NoWeb.mc.h;
                h.t += 2.0;
            }
            else if (aow.a(block) == 30) {
                MovementHelper.setSpeed(this.webSpeed.getCurrentValue());
                if (NoWeb.mc.t.X.e()) {
                    return;
                }
                NoWeb.mc.h.E = false;
                NoWeb.mc.t.X.i = false;
            }
        }
    }
    
    @EventTarget
    public void onMove(final EventMove event) {
        final String mode = this.noWebMode.getOptions();
        this.setSuffix(mode);
        if (this.getState() && mode.equalsIgnoreCase("Matrix")) {
            if (NoWeb.mc.h.E) {
                final bud h = NoWeb.mc.h;
                h.t += 2.0;
            }
            else {
                if (NoWeb.mc.t.X.e()) {
                    return;
                }
                NoWeb.mc.h.E = false;
            }
            if (NoWeb.mc.t.X.e()) {
                return;
            }
            if (NoWeb.mc.h.E && !NoWeb.mc.t.Y.e()) {
                MovementHelper.setEventSpeed(event, 0.483);
            }
        }
    }
}
