// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventSendPacket;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class AutoSprint extends Feature
{
    public static ListSetting sprintMode;
    public static BooleanSetting matrixMulti;
    
    public AutoSprint() {
        super("AutoSprint", "\u0417\u0430\u0436\u0438\u043c\u0430\u0435\u0442 CTRL \u0437\u0430 \u0432\u0430\u0441, \u0447\u0442\u043e \u0431\u044b \u0431\u044b\u0441\u0442\u0440\u043e \u0431\u0435\u0436\u0430\u0442\u044c", Type.Movement);
        AutoSprint.sprintMode = new ListSetting("Sprint Mode", "Default", new String[] { "Default", "All Direction" });
        AutoSprint.matrixMulti = new BooleanSetting("Matrix All Direction", false, () -> AutoSprint.sprintMode.currentMode.equals("All Direction"));
        this.addSettings(AutoSprint.sprintMode, AutoSprint.matrixMulti);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (!AutoSprint.matrixMulti.getCurrentValue()) {
            return;
        }
        final lq packet;
        if (AutoSprint.sprintMode.currentMode.equals("All Direction") && event.getPacket() instanceof lq && ((packet = (lq)event.getPacket()).b() == lq.a.d || packet.b() == lq.a.e)) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(AutoSprint.sprintMode.getCurrentMode());
        if (AutoSprint.sprintMode.currentMode.equals("Default")) {
            AutoSprint.mc.h.f(AutoSprint.mc.h.e.moveForward > 0.0f);
        }
        else if (AutoSprint.sprintMode.currentMode.equals("All Direction")) {
            AutoSprint.mc.h.f(MovementHelper.isMoving());
        }
    }
}
