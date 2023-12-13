package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "WTap")
public class WTap extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onLatest(final PacketSendEvent event) {
		 if (event.getPacket() instanceof C02PacketUseEntity) {
	            final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
	            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && ClientUtils.player().isSwingInProgress) {
	                ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
	                ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
	                ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
	            }
	        }
	    }
	}
