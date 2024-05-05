// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.events.impl.packet.EventSendPacket;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.helpers.world.InventoryHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class Speed extends Feature
{
    public static float ticks;
    public TimerHelper timerHelper;
    private final ListSetting speedMode;
    
    public Speed() {
        super("Speed", "", Type.Movement);
        this.timerHelper = new TimerHelper();
        this.speedMode = new ListSetting("Speed Mode", "Matrix", () -> true, new String[] { "Matrix", "ReallyWorld", "SunriseDamage", "ReallyWorld Exploit" });
        this.addSettings(this.speedMode);
    }
    
    @EventTarget
    public void onElytraSpeed(final EventPreMotion elytra) {
        final String modezxc = this.speedMode.getOptions();
        if (modezxc.equalsIgnoreCase("ReallyWorld Exploit")) {
            InventoryHelper.swapElytraToChestplate();
            if (Speed.mc.h.T % 25 == 0) {
                InventoryHelper.disabler(InventoryHelper.getSlotWithElytra());
            }
            final bud h = Speed.mc.h;
            h.u *= 1.8;
            final bud h2 = Speed.mc.h;
            h2.s *= 1.8;
            MovementHelper.strafePlayer();
            int eIndex = -1;
            for (int i = 0; i < 45; ++i) {
                if (Speed.mc.h.bv.a(i).c() == air.cS && eIndex == -1) {
                    eIndex = i;
                }
            }
            if (eIndex == -1 && Speed.mc.h.cp().c() != air.cS) {
                NotificationManager.publicity("ReallyWorld Exploit", "\u0412\u043e\u0437\u044c\u043c\u0438\u0442\u0435 \u044d\u043b\u0438\u0442\u0440\u044b \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c!", 6, NotificationType.WARNING);
                this.toggle();
            }
        }
    }
    
    @EventTarget
    public void on(final EventSendPacket event) {
        if (this.speedMode.currentMode.equals("ReallyWorld Exploit") && event.getPacket() instanceof lk) {
            final lk cPacketPlayer = (lk)event.getPacket();
            cPacketPlayer.f = false;
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        final String mode = this.speedMode.getOptions();
        this.setSuffix("" + mode);
        if (mode.equalsIgnoreCase("Matrix")) {
            if (Speed.mc.h.E || Speed.mc.h.m_() || Speed.mc.h.isInLiquid()) {
                return;
            }
            final double x = Speed.mc.h.p;
            final double y = Speed.mc.h.q;
            final double z = Speed.mc.h.r;
            final double yaw = Speed.mc.h.v * 0.017453292;
            if (Speed.mc.h.z && !Speed.mc.t.X.i) {
                Speed.mc.h.cu();
                Speed.mc.Y.timerSpeed = 1.3f;
                Speed.ticks = 11.0f;
            }
            else if (Speed.ticks < 11.0f) {
                ++Speed.ticks;
            }
            if (Speed.mc.h.t == -0.4448259643949201 && Speed.mc.f.o(new et(Speed.mc.h.p, Speed.mc.h.q - 0.9, Speed.mc.h.r)).u() != aox.a) {
                final bud h = Speed.mc.h;
                h.s *= 2.05;
                final bud h2 = Speed.mc.h;
                h2.u *= 2.05;
                Speed.mc.h.b(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
            }
            Speed.ticks = 0.0f;
        }
        else if (mode.equalsIgnoreCase("ReallyWorld")) {
            if (Speed.mc.h.E || Speed.mc.h.m_() || Speed.mc.h.isInLiquid()) {
                return;
            }
            if (Speed.mc.h.z && !Speed.mc.t.X.i) {
                Speed.mc.h.cu();
                MovementHelper.strafePlayer();
                Speed.ticks = 15.0f;
            }
            else if (Speed.ticks < 15.0f) {
                ++Speed.ticks;
            }
            final double a = 2.0;
            final double b = 2.0;
            if (Speed.mc.h.t == -0.4448259643949201 && Speed.mc.f.o(new et(Speed.mc.h.p, Speed.mc.h.q - 0.9, Speed.mc.h.r)).u() != aox.a && this.timerHelper.hasReached(500.0)) {
                final bud h3 = Speed.mc.h;
                h3.s *= a;
                final bud h4 = Speed.mc.h;
                h4.u *= b;
                this.timerHelper.reset();
            }
            Speed.ticks = 0.0f;
        }
        else if (mode.equalsIgnoreCase("SunriseDamage") && MovementHelper.isMoving()) {
            if (Speed.mc.h.z) {
                Speed.mc.h.f(-Math.sin(MovementHelper.getAllDirection()) * 9.5 / 24.5, 0.0, Math.cos(MovementHelper.getAllDirection()) * 9.5 / 24.5);
                MovementHelper.strafe2();
            }
            else if (Speed.mc.h.ao()) {
                Speed.mc.h.f(-Math.sin(MovementHelper.getAllDirection()) * 9.5 / 24.5, 0.0, Math.cos(MovementHelper.getAllDirection()) * 9.5 / 24.5);
                MovementHelper.strafe2();
            }
            else if (!Speed.mc.h.z) {
                Speed.mc.h.f(-Math.sin(MovementHelper.getAllDirection()) * 0.11 / 24.5, 0.0, Math.cos(MovementHelper.getAllDirection()) * 0.11 / 24.5);
                MovementHelper.strafe2();
            }
            else {
                Speed.mc.h.f(-Math.sin(MovementHelper.getAllDirection()) * 0.005 * MovementHelper.getSpeed(), 0.0, Math.cos(MovementHelper.getAllDirection()) * 0.005 * MovementHelper.getSpeed());
                MovementHelper.strafe2();
            }
        }
    }
    
    @Override
    public void onDisable() {
        Speed.mc.Y.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    static {
        Speed.ticks = 0.0f;
    }
}
