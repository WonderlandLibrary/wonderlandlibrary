package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventCollideWithBlock;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

/**
 * P Jesus
 * 
 * @author purity
 */

public class Jesus extends Module {

	public Jesus() {
		super("Jesus", new String[] { "waterwalk", "float" }, ModuleType.Movement);
		setColor(new Color(188,233,248).getRGB());
	}

	// can ya jebus
	private boolean canJeboos() {
		// if ya not fucking sneaking ya not fucking jumping and ya not jumping from a
		// 20 story fucking building that will make you break you fucking neck if you
		// hit the water continue jesusususing
		return mc.thePlayer.fallDistance < 3.0f && !mc.gameSettings.keyBindJump.isPressed() && !BlockHelper.isInLiquid()
				&& !mc.thePlayer.isSneaking();
	}

	// bop your player up when he in the water fam ye
	@EventHandler
	public void onPre(EventPreUpdate e) {
		if (BlockHelper.isInLiquid() && !mc.thePlayer.isSneaking() && !mc.gameSettings.keyBindJump.isPressed()) {
			mc.thePlayer.motionY = 0.05;
			mc.thePlayer.onGround = true;
		}
	}

	//jebus code
	@EventHandler
	public void onPacket(EventPacketSend e) {
		if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
			C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
			packet.y = ((mc.thePlayer.ticksExisted % 2 == 0) ? (packet.y + 0.01) : (packet.y - 0.01));
		}
	}

	//this works on ncp for some reason
	@EventHandler
	public void onBB(EventCollideWithBlock e) {
		if (e.getBlock() instanceof BlockLiquid && this.canJeboos()) {
			e.setBoundingBox(new AxisAlignedBB(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(),
					e.getPos().getX() + 1.0, e.getPos().getY() + 1.0, e.getPos().getZ() + 1.0));
		}
	}
}
