package epsilon.util.data;

import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventSendPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class DataCollector {

	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static double[] playerPos = {0, 0, 0, 0, 0, 0}, targetData = {0, 0, 0};
	public static ItemStack clientItem;
	public static int clientItemLoc, serverItemLoc;
	public static boolean serverGroundState;
	public static Entity target;
	
	
	public DataCollector() {}
	
	public static void onData(Event e) {
		
		if(e instanceof EventMotion) {
			clientItem = mc.thePlayer.getHeldItem();
			clientItemLoc = mc.thePlayer.inventory.currentItem;
			if(e.isPre()) {
				
				playerPos[0] = mc.thePlayer.posX;
				playerPos[1] = mc.thePlayer.posY;
				playerPos[2] = mc.thePlayer.posZ;
				
			}else {

				playerPos[3] = mc.thePlayer.posX;
				playerPos[4] = mc.thePlayer.posY;
				playerPos[5] = mc.thePlayer.posZ;
			}
		}
		
		if(e instanceof EventUpdate) {
			
			
			
		}
		
		if(e instanceof EventSendPacket) {
			final Packet packet = e.getPacket();
			if(packet instanceof C09PacketHeldItemChange) {
				final C09PacketHeldItemChange p = (C09PacketHeldItemChange) packet;
				serverItemLoc = p.getSlotId();
			}
			
			if(packet instanceof C03PacketPlayer) {
				final C03PacketPlayer p = (C03PacketPlayer) packet;
				serverGroundState = p.isOnGround();
			}
			if(packet instanceof C02PacketUseEntity) {
				final C02PacketUseEntity p = (C02PacketUseEntity) packet;
				target = p.target;
			}
		}
		
	}
	
	private void sortData() {
		
	}
	
	public static void updateTargetState(Entity target) {
		
	}
	
	
}
