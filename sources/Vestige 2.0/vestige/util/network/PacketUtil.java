package vestige.util.network;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import lombok.experimental.UtilityClass;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import vestige.util.base.IMinecraft;
import vestige.util.player.HotbarUtil;

@UtilityClass
public class PacketUtil implements IMinecraft {

    public void sendPacket(Packet packet) {
    	mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public void sendPacketNoEvent(Packet packet) {
        try {
        	mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
        } catch (NullPointerException e) {
        	
        }
    }
    
    public static void sendBlocking(boolean callEvent, boolean placement) {
		if(mc.thePlayer == null)
			return;
		
		if(placement) {
			C08PacketPlayerBlockPlacement packet = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0);
			
			if(callEvent) {
				sendPacket(packet);
			} else {
				sendPacketNoEvent(packet);
			}
		} else {
			C08PacketPlayerBlockPlacement packet = new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem());
			if(callEvent) {
				sendPacket(packet);
			} else {
				sendPacketNoEvent(packet);
			}
		}
	}
	
	public static void releaseUseItem(boolean callEvent) {
		if(mc.thePlayer == null)
			return;
		
		C07PacketPlayerDigging packet = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
		if(callEvent) {
			sendPacket(packet);
		} else {
			sendPacketNoEvent(packet);
		}
	}
	
	public static void releaseUseItem(long delay) {
		if(mc.thePlayer == null)
			return;
		
		C07PacketPlayerDigging packet = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
		DelayedPacketThread thread = new DelayedPacketThread(packet, delay);
		thread.run();
	}
	
	public static void startSprinting() {
		sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
	}
	
	public static void stopSprinting() {
		sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
	}
	
	public static void startSneaking() {
		sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING));
	}
	
	public static void stopSneaking() {
		sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING));
	}
	
	public static boolean sendInteract(Entity target) {
		mc.playerController.syncCurrentPlayItem();
        sendPacketNoEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
        return mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR && mc.thePlayer.interactWith(target);
	}
	
	public static void attackFakePlayer() {
		EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.randomUUID(), "a"));
		
		sendPacketNoEvent(new C0APacketAnimation());
		sendPacketNoEvent(new C02PacketUseEntity(fakePlayer, C02PacketUseEntity.Action.ATTACK));
	}
	
	public static void verusRightClick() {
		sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 0, HotbarUtil.getAirSlot() != -1 ? mc.thePlayer.inventory.getStackInSlot(HotbarUtil.getAirSlot()) : mc.thePlayer.getHeldItem(), 0.5F, 0.5F, 0.5F));
	}
    
}
