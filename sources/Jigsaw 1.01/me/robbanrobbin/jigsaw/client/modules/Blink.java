package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;

public class Blink extends Module {

	private EntityOtherPlayerMP oldPlayer;
	public static ArrayList<Packet> blinkList = new ArrayList<Packet>();
	public static boolean disabling = false;

	public Blink() {
		super("Blink", Keyboard.KEY_B, Category.MOVEMENT, "Simulates lag. For other players it looks like you are teleporting.");
	}

	@Override
	public void onDisable() {
		mc.world.removeEntityFromWorld(-123);
		oldPlayer = null;
		disabling = true;
		for (Packet packet : blinkList) {
			sendPacketFinal(packet);
		}
		blinkList.clear();
		super.onDisable();
		disabling = false;
	}

	@Override
	public void onEnable() {

		oldPlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());

		oldPlayer.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);

		oldPlayer.rotationYawHead = mc.player.rotationYawHead;
		oldPlayer.copyLocationAndAnglesFrom(mc.player);
		oldPlayer.rotationYaw = mc.player.rotationYaw;

		mc.world.addEntityToWorld(-123, oldPlayer);

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (oldPlayer == null) {
			return;
		}
//		oldPlayer.setSneaking(mc.player.isSneaking());
//		oldPlayer.setSprinting(mc.player.isSprinting());
//		oldPlayer.isSwingInProgress = mc.player.isSwingInProgress;
//		oldPlayer.swingProgress = mc.player.swingProgress;
//		oldPlayer.swingProgressInt = mc.player.swingProgressInt;
		super.onUpdate();
	}

	public static boolean isBlinking() {
		return Jigsaw.getModuleByName("Blink").isToggled();
	}

	public static void packet(Packet packetIn) {
		blinkList.add(packetIn);
	}

}
