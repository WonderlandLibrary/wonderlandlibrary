/*
 * Decompiled with CFR 0.150.
 */
package markgg.utilities;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {
    public static void sendPacket(Packet p) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(p);
    }

    public static void sendPacketNoEvent(Packet p) {
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(p, new GenericFutureListener(){

                public void operationComplete(Future future) throws Exception {
                    System.out.println("SHIT");
                }
            }, new GenericFutureListener(){

                public void operationComplete(Future future) throws Exception {
                    System.out.println("SHIT");
                }
            });
        } else {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(p);
        }
    }
}

