package tech.atani.client.processor.impl;

import net.minecraft.network.play.client.C03PacketPlayer;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.world.WorldLoadEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.processor.Processor;
import tech.atani.client.processor.data.ProcessorInfo;

@ProcessorInfo(name = "BalanceProcessor")
public class BalanceProcessor extends Processor {

    private static long balance, lastPacket;

    @Listen
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            if (lastPacket == 0) lastPacket = System.currentTimeMillis();
            long delay = System.currentTimeMillis() - lastPacket;
            balance += e.isCancelled() ? -delay : 50 - delay;
            lastPacket = System.currentTimeMillis();
        }
    }

    @Listen
    public void onWorldLoad(WorldLoadEvent e) {
        balance = lastPacket = 0;
    }

    public static long getBalance() {
        return balance;
    }

    public static long getLastPacket() {
        return lastPacket;
    }
}
