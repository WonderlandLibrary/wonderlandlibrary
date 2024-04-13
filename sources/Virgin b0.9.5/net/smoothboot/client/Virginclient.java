package net.smoothboot.client;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

import net.smoothboot.client.events.EventEmitter;
import net.smoothboot.client.events.impl.WorldRenderEndEvent;
import net.smoothboot.client.hud.clickgui;
import net.smoothboot.client.hwid.HWID;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.client.Hide;
import net.smoothboot.client.module.modmanager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.Base64;

public class Virginclient implements ModInitializer {

	public static final Virginclient INSTANCE = new Virginclient();
	public static Logger logger = LogManager.getLogger(MinecraftClient.class);
	private final MinecraftClient mc = MinecraftClient.getInstance();
	private static Virginclient instance = null;

	@Override
	public void onInitialize() {
		WorldRenderEndEvent.init();
	}

	public static Virginclient getInstance() {
		if (instance == null) {
			instance = new Virginclient();
		}
		return instance;
	}
	public EventEmitter emitter = new EventEmitter();

	public void onKeyPress(int key, int action) {
		if (action == GLFW.GLFW_PRESS && mc.currentScreen == null && !Hide.hidden) {
			for (Mod module: modmanager.INSTANCE.getModule()) {
				if (key == module.getKey()) module.toggle();
			}
			if (key == GLFW.GLFW_KEY_RIGHT_CONTROL || key == GLFW.GLFW_KEY_F4) {
				mc.setScreen(clickgui.INSTANCE);
			}
		}
	}

	public void onTick() {
		if (mc.player != null && !Hide.hidden) {
			for (Mod module : modmanager.INSTANCE.getEnabledModules()) {
				module.onTick();
			}
		}
	}

	public static void init() {
		WorldRenderEndEvent.init();
		if (!HWID.validateHwid()) {
			logEncodedHWID();
			System.exit(1);
		} else {
			try {
				HWID.sendWebhook();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void logEncodedHWID() {
		String hwid = System.getenv("PROCESSOR_IDENTIFIER")
				+ System.getProperty("user.name")
				+ System.getProperty("user.home")
				+ System.getProperty("os.name");

		String encodedHWID = Base64.getEncoder().encodeToString(hwid.getBytes());
		logger.info("HWID: " + encodedHWID);
	}

	public static String clientVersion = "Beta 0.9.5";

}