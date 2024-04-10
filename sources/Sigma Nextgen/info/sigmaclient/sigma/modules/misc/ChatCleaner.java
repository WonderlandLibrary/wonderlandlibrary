package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.network.play.server.SChatPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ChatCleaner extends Module {
    public ChatCleaner() {
        super("ChatCleaner", Category.Misc, "Dont blame me!");
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        super.onUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof SChatPacket){
            if(((SChatPacket) event.packet).isSystem()) return;
            String n = ((SChatPacket) event.packet).getChatComponent().toString();
            if(n.startsWith("<") || n.contains(": ")){
                event.cancelable = true;
            }
        }
        super.onPacketEvent(event);
    }
}
