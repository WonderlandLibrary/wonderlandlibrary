package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.potion.Potion;

public class SolidLiquids extends Module {

	public static boolean enabled;
	private double moveSpeed;
	private WaitTimer timer = new WaitTimer();

	public SolidLiquids() {
		super("SolidLiquids", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to walk on liquids");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer.reset();
		enabled = true;
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		enabled = !(mc.gameSettings.keyBindSneak.pressed || mc.thePlayer.isInWater() || mc.thePlayer.isInLava());
		
		if (Utils.isPlayerOnLiqud() && timer.hasTimeElapsed(100, false) && enabled) {
			event.y -= 0.005;
			timer.reset();
		}
		else if(!Utils.isPlayerOnLiqud()) {
			timer.reset();
		}
		super.onUpdate(event);
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {

		super.onPacketRecieved(packetIn);
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		super.onPacketSent(packet);
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if(!enabled && (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) && !mc.gameSettings.keyBindSneak.pressed) {
			event.y = 0.1;
			mc.thePlayer.motionY = 0;
		}
		super.onPreMotion(event);
	}

	@Override
	public String[] getModes() {
		return super.getModes();
	}

	@Override
	public String getAddonText() {
		return super.getAddonText();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

}
