/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventType;
import me.Tengoku.Terror.event.events.EventReceivePacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packet, final T t, IThreadListener iThreadListener) throws ThreadQuickExitException {
        if (!iThreadListener.isCallingFromMinecraftThread()) {
            iThreadListener.addScheduledTask(new Runnable(){

                @Override
                public void run() {
                    EventReceivePacket eventReceivePacket = new EventReceivePacket(packet);
                    Exodus.onEvent(eventReceivePacket);
                    eventReceivePacket.setType(EventType.PRE);
                    eventReceivePacket.call();
                    if (eventReceivePacket.isCancelled()) {
                        return;
                    }
                    packet.processPacket(t);
                    eventReceivePacket.setType(EventType.POST);
                }
            });
            throw ThreadQuickExitException.field_179886_a;
        }
    }
}

