package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "ChatBypass")
public class ChatBypass extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	 @EventTarget
	 public void onPacketSend(final PacketSendEvent event) {
	        if (event.getPacket() instanceof C01PacketChatMessage) {
	            final C01PacketChatMessage packet = (C01PacketChatMessage)event.getPacket();
	            final String message = packet.getMessage();
	            if (message.contains("fuck")) {
	                packet.setMessage(message.replace("fuck", "ff uu cc kk"));
	            }
	            else if (message.contains("faggot")) {
	                packet.setMessage(message.replace("faggot", "ff aa gg oo tt"));
	            }
	            else if (message.contains("nigger")) {
	                packet.setMessage(message.replace("nigger", "nn ii gggg ee rr"));
	            }
	        }
	    }
	}