package me.aidanmees.trivia.client.modules.Render;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BlockHitAnimation extends Module {

	public BlockHitAnimation() {
		super("BlockHitAnimation", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes the blockhit look dank!");
	}

	@Override
	public void onUpdate() {
		
		super.onUpdate();
	}

}
