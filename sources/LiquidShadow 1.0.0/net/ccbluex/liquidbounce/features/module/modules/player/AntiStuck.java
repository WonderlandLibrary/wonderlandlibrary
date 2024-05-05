package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotificationType;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "AntiStuck",description = "Anti you stuck on ground or air.",category = ModuleCategory.PLAYER)
public class AntiStuck extends Module {
    private int flags;
    private int ticks;
    private int ticks1;
    private boolean unStucking;

    private final IntegerValue maxFlagsValue = new IntegerValue("MaxFlags",3,1,10);
    private final IntegerValue resetFlagTicksValue = new IntegerValue("ResetFlagTicks",200,1,300);
    private final IntegerValue unStuckTicksValue = new IntegerValue("UnStuckTicks",50,1,150);

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (ticks >= resetFlagTicksValue.get()) {
            ticks = 0;
            flags = 0;
        }
        if (ticks1 >= unStuckTicksValue.get() && unStucking) {
            LiquidBounce.moduleManager.getModule(Freeze.class).setState(false);
            LiquidBounce.hud.addNotification(new Notification("You are been unstucked!",NotificationType.NORMAL));
            unStucking = false;
            flags = 0;
            ticks1 = 0;
        }
        if (flags >= maxFlagsValue.get()) {
            LiquidBounce.moduleManager.getModule(Freeze.class).setState(true);
            LiquidBounce.moduleManager.getModule(Scaffold.class).setState(false);
            LiquidBounce.hud.addNotification(new Notification("Trying to unstuck you...", NotificationType.NORMAL));
            unStucking = true;
            flags = 0;
            ticks1 = 0;
        }
        if (unStucking) {
            LiquidBounce.moduleManager.getModule(Freeze.class).setState(true);
            LiquidBounce.moduleManager.getModule(Scaffold.class).setState(false);
        }
        ticks1++;
        ticks++;
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            flags++;
        }
    }
}
