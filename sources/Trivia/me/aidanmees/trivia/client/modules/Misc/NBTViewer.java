package me.aidanmees.trivia.client.modules.Misc;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S02PacketChat;

public class NBTViewer extends Module {


	public NBTViewer() {
		super("NBTViewer", Keyboard.KEY_NONE, Category.HIDDEN,
				"Shows you all the NBT data on an item/book compound by hovering over it.");
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