package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S14PacketEntity;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "FlagDetect")
public class FlagDetect extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    public void onPacketRecieve(final PacketReceiveEvent event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			ClientUtils.sendMessage("Flag Detected");
		}
	}
}