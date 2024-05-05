package client.module.impl.other;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.combat.KillAura;
import client.module.impl.movement.Flight;
import client.module.impl.movement.Speed;
import client.module.impl.player.ChestStealer;
import client.module.impl.player.InventoryCleaner;
import client.module.impl.player.InventoryManager;
import client.util.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(name = "AutoDisable", description = "", category = Category.PLAYER)
public class AutoDisable extends Module {




    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            final String message = wrapper.getChatComponent().getUnformattedText();
            if (message.contains(mc.thePlayer.getName() + " entrou na sala ")) {
                Client.INSTANCE.getModuleManager().get(KillAura.class).setEnabled(false);
                Client.INSTANCE.getModuleManager().get(Flight.class).setEnabled(false);
                Client.INSTANCE.getModuleManager().get(Speed.class).setEnabled(false);
                Client.INSTANCE.getModuleManager().get(InventoryCleaner.class).setEnabled(false);
                Client.INSTANCE.getModuleManager().get(ChestStealer.class).setEnabled(false);
                Client.INSTANCE.getModuleManager().get(InventoryManager.class).setEnabled(false);
                Client.INSTANCE.getModuleManager().get(Scaffold.class).setEnabled(false);

                ChatUtil.display("AutoDisabled Modules!");
            }
        }
    };
}