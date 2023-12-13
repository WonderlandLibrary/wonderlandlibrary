package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.TickTimer;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class NoSlowdown extends Module {
	
	boolean xd = false;
	
	int timer = 0;
	
	TickTimer ticcTimer = new TickTimer();

	public NoSlowdown() {
		super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT, "When using items, you don't get slowed down.");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		
		ticcTimer.tick();
		
		if (!this.currentMode.equals("NCP")) {
			return;
		}
		xd = mc.player.isActiveItemStackBlocking() && mc.player.isMovingXZ(0.1);
		if(xd) {
			mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onLateUpdate() {
		if (!this.currentMode.equals("NCP")) {
			return;
		}
		if(xd) {
			this.mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
			xd = false;
		}
		super.onLateUpdate();
	}
	
	@Override
	public void onPacketRecieved(PacketEvent event) {
//		Packet packet = event.getPacket();
//		if(packet instanceof SPacketEntityEquipment) {
//			event.cancel();
//		}
		super.onPacketRecieved(event);
	}

	@Override
	public String[] getModes() {
		return new String[] { "NCP", "Vanilla" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

}
