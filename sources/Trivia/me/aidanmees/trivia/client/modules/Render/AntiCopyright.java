package me.aidanmees.trivia.client.modules.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S02PacketChat;

public class AntiCopyright extends Module {

	
	public AntiCopyright() {
		super("AntiCopyright", Keyboard.KEY_NONE, Category.RENDER,
				"Creds to Tommer for the idea! Partnered on YT");
	}

	

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}