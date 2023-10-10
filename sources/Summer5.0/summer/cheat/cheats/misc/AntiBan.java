package summer.cheat.cheats.misc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.ChatUtils;
import summer.base.utilities.TimerUtils;
import summer.cheat.cheats.movement.Flight;
import summer.cheat.cheats.movement.Speed;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventSendPacket;
import summer.cheat.eventsystem.events.player.EventUpdate;

public class AntiBan extends Cheats {

    public AntiBan() {
        super("AntiBan", "", Selection.PLAYER);
        // TODO Auto-generated constructor stub
    }
    private final List<Packet<?>> packets = new ArrayList<>();
    private final TimerUtils timer = new TimerUtils();

    @EventTarget
    public void onUpdate(EventUpdate e) {

    }

    public static double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }

    @EventTarget
    public void onPacket(EventSendPacket eventSendPacket) {
    	if (eventSendPacket.getPacket() instanceof C0FPacketConfirmTransaction) {
            final C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction) eventSendPacket.getPacket();
            if (packetConfirmTransaction.getUid() < 0 ) {
               // eventSendPacket.setCancelled(true);
            }
        }

        if (eventSendPacket.getPacket() instanceof C00PacketKeepAlive) {
          //  eventSendPacket.setCancelled(true);
    }

    }
}
