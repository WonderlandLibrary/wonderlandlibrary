package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.network.play.server.SChatPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ChatFilter extends Module {
    BooleanValue L = new BooleanValue("L", true);
    BooleanValue chinese = new BooleanValue("Chinese", true);
    public ChatFilter() {
        super("ChatFilter", Category.Misc, "?!");
     registerValue(L);
     registerValue(chinese);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        super.onUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof SChatPacket){
            if(((SChatPacket) event.packet).isSystem()) return;
            String n = ((SChatPacket) event.packet).getChatComponent().getUnformattedComponentText();
            if(L.getValue()) {
                if (n.endsWith("L") || n.contains("noob") || n.contains("hacker") || n.contains("loser")) {
                    event.cancelable = true;
                }
            }
            if(chinese.getValue()) {
                if (n.contains("你妈") || n.contains("死") || n.contains("NM") || n.contains("nm") || n.contains("傻") || n.contains("啥b")) {
                    event.cancelable = true;
                }
            }
            if(n.contains("㐁㔁㘁")){
                event.cancelable = true;
            }
        }
        super.onPacketEvent(event);
    }
}
