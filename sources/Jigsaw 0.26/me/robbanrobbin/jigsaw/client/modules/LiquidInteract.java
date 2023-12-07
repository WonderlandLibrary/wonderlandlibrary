package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class LiquidInteract extends Module {

	public LiquidInteract() {
		super("LiquidInteract", Keyboard.KEY_NONE, Category.WORLD, "Allows you to place blocks on liquids!");
	}

}
