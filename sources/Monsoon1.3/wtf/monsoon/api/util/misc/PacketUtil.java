package wtf.monsoon.api.util.misc;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.Iterator;
import java.util.List;

public class PacketUtil {

    public static void sendPacket(Packet p) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(p);
    }

    public static void sendPacketNoEvent(Packet p) {
        //Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(p, null, null);
        if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(p, new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    System.out.println("SHIT");
                }
            }, new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    System.out.println("SHIT");
                }
            });
        } else {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(p);
        }
    }

}
