/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.RandomUtils
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.EventMove;
import markgg.events.listeners.EventPacket;
import markgg.modules.Module;
import markgg.modules.player.disabler.PacketSleepThread;
import markgg.settings.ModeSetting;
import markgg.utilities.Random;
import markgg.utilities.TimerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.apache.commons.lang3.RandomUtils;

public class Disabler
extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Verus", "Verus", "Mineplex");
    public TimerUtil timer = new TimerUtil();
    public TimerUtil bedlesstimer = new TimerUtil();

    public Disabler() {
        super("Disabler", 0, Module.Category.PLAYER);
        this.addSettings(this.mode);
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventPacket && e.isIncoming()) {
            if (this.mode.is("Verus")) {
                EventPacket cfr_ignored_0 = (EventPacket)e;
                Packet packet = EventPacket.getPacket();
                if (packet instanceof C0FPacketConfirmTransaction) {
                    EventPacket cfr_ignored_1 = (EventPacket)e;
                    C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)EventPacket.getPacket();
                    new PacketSleepThread(packetConfirmTransaction, RandomUtils.nextInt((int)6000, (int)12000)).start();
                    e.setCancelled(true);
                }
            }
            if (this.mode.is("Mineplex")) {
                EventPacket event = (EventPacket)e;
                if (EventPacket.getPacket() instanceof C00PacketKeepAlive) {
                    ((C00PacketKeepAlive)EventPacket.getPacket()).key -= Random.randomNumber(2.147483647E9, 1000.0);
                }
            }
        }
        if (e instanceof EventMove && this.mode.is("Verus")) {
            this.timer.hasTimeElapsed(5000L, false);
        }
    }
}

