package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.network.play.client.C01PacketChatMessage;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.command.CommandManager;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
@Mod(displayName = "Commands", enabled = true)
public class Commands extends Module
{
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packet = (C01PacketChatMessage)event.getPacket();
            String message = packet.getMessage();
            if (message.startsWith(".")) {
                event.setCancelled(true);
                message = message.replace(".", "");
                final Command commandFromMessage = CommandManager.getCommandFromMessage(message);
                final String[] args = message.split(" ");
                commandFromMessage.runCommand(args);
            }
        }
    }
}
