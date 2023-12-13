package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class Freecam extends Module {

	private static CPacketPlayer position;

	private BlockPos oldBlockPos;

	private EntityOtherPlayerMP oldPlayer;

	public Freecam() {
		super("Freecam", Keyboard.KEY_NONE, Category.WORLD, "Makes you fly out of your body.");
	}

	@Override
	public void onDisable() {
		mc.player.setPosition(oldBlockPos.getX(), oldBlockPos.getY(), oldBlockPos.getZ());
		mc.player.noClip = false;

		mc.player.rotationYawHead = oldPlayer.rotationYawHead;
		mc.player.rotationPitch = oldPlayer.rotationPitch;

		mc.world.removeEntityFromWorld(-123);
		oldPlayer = null;

		super.onDisable();

	}

	@Override
	public void onEnable() {
		oldBlockPos = new BlockPos(mc.player.getPosition());

		position = new CPacketPlayer();

		oldPlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());

		oldPlayer.setPosition(oldBlockPos.getX(), oldBlockPos.getY(), oldBlockPos.getZ());

		oldPlayer.rotationYawHead = mc.player.rotationYawHead;
		oldPlayer.copyLocationAndAnglesFrom(mc.player);
		oldPlayer.rotationYaw = mc.player.rotationYaw;

		mc.world.addEntityToWorld(-123, oldPlayer);

		super.onEnable();

	}

	@Override
	public void onUpdate() {
		Utils.spectator = true;
		mc.player.motionY = 0;
		mc.player.jumpMovementFactor = 0.1f;
		mc.player.onGround = false;
		oldPlayer.setSneaking(mc.player.isSneaking());
		oldPlayer.setSprinting(mc.player.isSprinting());
		oldPlayer.isSwingInProgress = mc.player.isSwingInProgress;
		oldPlayer.swingProgress = mc.player.swingProgress;
		oldPlayer.swingProgressInt = mc.player.swingProgressInt;
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += 0.4;
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.motionY += -0.4;
		}
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		mc.player.noClip = true;
		super.onLivingUpdate();
	}

	public static CPacketPlayer getPacket() {
		return position;
	}

	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if (packet instanceof CPacketPlayer) {
			event.cancel();
			sendPacketFinal(Freecam.getPacket());
		}
		super.onPacketSent(event);
	}

}
