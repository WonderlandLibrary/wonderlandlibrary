package dev.monsoon.module.implementation.misc;


import dev.monsoon.module.enums.Category;
import dev.monsoon.Monsoon;
import dev.monsoon.module.base.Module;
import dev.monsoon.util.misc.Timer;
import org.lwjgl.input.Keyboard;

public class DiscordRPC extends Module {


	public DiscordRPC() {
		super("DiscordRPC", Keyboard.KEY_NONE, Category.MISC);
	}
	
	Timer timer = new Timer();

	@Override
	public void onEnable() {
		Monsoon.discordRPC.start();
		Monsoon.discordRPC.update("Utilizing Monsoon " + Monsoon.version + " (" + Monsoon.type + ")", Monsoon.monsoonUsername + " is a sexy beast");
	}

	@Override
	public void onDisable() {
		Monsoon.discordRPC.shutdown();
	}
}