package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.ScreenPos;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.AbstractPacket;

public class Ping extends Module {

	public static WaitTimer timer = new WaitTimer();

	public Ping() {
		super("Ping", Keyboard.KEY_NONE, Category.HIDDEN);
	}

	@Override
	public void onToggle() {
		timer.reset();
		super.onToggle();
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		timer.reset();
		super.onPacketRecieved(packetIn);
	}

	@Override
	public void onRender() {
		if (timer.getTime() > 1000) {
			trivia.getUIRenderer().addToQueue(String.valueOf("§cPing: §4§l" + timer.getTime()), ScreenPos.LEFTUP);
		}
		super.onRender();
	}

	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}

	@Override
	public boolean getEnableAtStartup() {
		return true;
	}

}
