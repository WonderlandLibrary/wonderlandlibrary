package me.aidanmees.trivia.client.modules.Player;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Haste extends Module {

	public Haste() {
		super("Haste", Keyboard.KEY_NONE, Category.EXPLOITS, "Makes you mine faster.");
	}

	@Override
	public void onUpdate() {
		if (mc.thePlayer.onGround) {
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 0, 1));
		}
		super.onUpdate();
	}

}
