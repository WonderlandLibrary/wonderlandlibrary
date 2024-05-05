package client.module.impl.other;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.PacketReceiveEvent;
import client.event.impl.other.Render3DEvent;
import client.event.impl.other.UpdateEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.ChatUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import tv.twitch.chat.Chat;

import java.util.UUID;

@ModuleInfo(name = "Yes Anti Ass", description = "Rdemoves bots used by servers to detect Aura", category = Category.OTHER)
public class AntiBotAss extends Module {
    private String detectedEntity;
    @EventLink
    public final Listener<PacketReceiveEvent> eventPacketReceive = event -> {


            final Packet<?> p = event.getPacket();
            if (p instanceof S0CPacketSpawnPlayer) {
                final S0CPacketSpawnPlayer w = (S0CPacketSpawnPlayer) p;
                mc.theWorld.loadedEntityList.stream().filter(entity -> entity.getUniqueID().equals(w.getPlayer())).forEach(Client.INSTANCE.getBotManager()::add);
                ChatUtil.display("gay");

            }


    };
    @EventLink
    public final Listener<UpdateEvent> treter = event -> {

    };
}
