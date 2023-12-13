package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class AntiFire extends Module {

	public AntiFire() {
		super("AntiFire", Keyboard.KEY_NONE, Category.PLAYER, "Removes fire instantly.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (!mc.player.capabilities.isCreativeMode && mc.player.onGround && mc.player.isBurning()) {
			for (int i = 0; i < 100; i++) {
				sendPacket(new CPacketPlayer(true));
			}
		}
		super.onUpdate();
	}

}
