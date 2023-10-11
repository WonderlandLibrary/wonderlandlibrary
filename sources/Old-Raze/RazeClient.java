package markgg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.viaversion.viaversion.api.Via;
import markgg.event.core.EventBus;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.ChatEvent;
import markgg.modules.ModuleManager;
import org.lwjgl.opengl.Display;

import markgg.alts.AltManager;
import markgg.command.CommandManager;
import markgg.config.ConfigSystem;
import markgg.config.KeybindSystem;
import markgg.event.Event;
import markgg.modules.Module;
import markgg.ui.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import viamcp.ViaMCP;

public enum RazeClient {
	INSTANCE("Raze", "DEV");

	public String name, version;

	RazeClient(String name, String version) {
		this.name = name;
		this.version = version;
	}

	private final static HUD hud = new HUD();

	private static AltManager altManager;
	private static ModuleManager moduleManager;

	public static EventBus eventBus = new EventBus();

	private final static CommandManager cmdManager = new CommandManager();
	private final static ConfigSystem cfgSystem = new ConfigSystem(new File("Raze/configs/default.json"));
	private final static KeybindSystem kbSystem = new KeybindSystem(new File("Raze/keybinds.json"));

	public void startup() {
		try{
			ViaMCP.getInstance().start();
			ViaMCP.getInstance().initAsyncSlider();
		}catch (Exception e){
			e.printStackTrace();
		}

		setAltManager(new AltManager());
		Minecraft mc = Minecraft.getMinecraft();

		String icon16x16 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAADVSURBVDhPY6AUMEJpMHjqkyYCZWKAr3/YP6vtmPwTyoUDJigNBv///nuNC3Myfm+BKkMBKAbAwP//DB+wYPGHHok2tzxy2aHKwIAFSqMA2R1zBKFMOHjsmbKAiYH5MPf/7ylA7lyIKFoYPPFM+Q+iGf8zgBShgH+MjN6MDP8kGf8zzpHeMQe/AdgAUKJHdvucUigXDrAaACQWggWQwP///zgYGZke///PuEVux+yDUGEcYbB9TgKUCQegMADaVsLE8P8GkAs3AGsskAJGDaCCARQCBgYASiFjh7ORcJoAAAAASUVORK5CYII=";
		String icon32x32 = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAACsSURBVFhHYxgFIx4wQmmc4KlHSjmUSTSQ3jGnE8okCAg64Ilnyn8okyjw/z/DB9kdcwShXIKACUoPGCApBEC+gzLxgZ//GP6GQNkM8jvmH4EysQKSHPD3/19bQgY+9Ei0YWZkPgzlMshsn4PXjgGPgqGVBogBQy4XDK0o+Pf/X/Z/hv+XoFysgJGBUY+RgakVymUgFB2j2XDUAaMOGHXAqANGHTAKRsEAAwYGAD4YQCi//tmdAAAAAElFTkSuQmCC";
		Minecraft.getMinecraft().setWindowIcon(icon16x16, icon32x32);
		Display.setTitle(name + " " + version);

		File directory = new File("Raze");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File directory1 = new File("Raze/configs");
		if (!directory1.exists()) {
			directory1.mkdirs();
		}

		moduleManager = new ModuleManager();

		//load configs
		loadConfigs();

		getEventBus().register(this);
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

	public static ModuleManager getModuleManager() {
		return moduleManager;
	}

	@EventHandler
	public final Listener<ChatEvent> chatEventListener = e -> {
		getCmdmanager().handlechat(e);
	};

	public String getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public static void addChatMessage(String message) {
		message = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.RED + "Raze" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE + message;
		(Minecraft.getMinecraft()).thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
	}

	public static void addChatMessage() {
		(Minecraft.getMinecraft()).thePlayer.addChatMessage((IChatComponent)new ChatComponentText(""));
	}

	public static void print(String message) {
		System.out.println(message);
	}
	
	public static void loadConfigs() {
		File configFile = new File("Raze/configs/default.json");
		if (!configFile.exists()) {
			System.out.println("[RAZE] - Error! Default config not found! Recreating!");
			getCfgsystem().saveConfig("default.json");
		}else {
			if(getCfgsystem().error == "Incompatible config version") {
				System.out.println("[RAZE] - Retrying to load default.json config.");
				getCfgsystem().saveConfig("default.json");
				getCfgsystem().loadConfig("default.json");
			}else
				getCfgsystem().loadConfig("default.json");
		}

		File keybindFile = new File("Raze/keybinds.json");
		if (!keybindFile.exists()) {
			kbSystem.saveKeybinds("keybinds.json");
		}else {
			kbSystem.loadKeybinds("keybinds.json");
		}
		
		File cfgFile = new File("Raze/config.properties");
		try {
			if (!cfgFile.exists()) {
				cfgFile.createNewFile();
			}
			Properties properties = new Properties();
			OutputStream outputStream = new FileOutputStream("Raze/config.properties");
			properties.setProperty("prefix", ".");
			properties.store(outputStream, "Raze Properties");
			outputStream.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public static AltManager getAltManager() {
		return altManager;
	}

	public static void setAltManager(AltManager altManager) {
		RazeClient.altManager = altManager;
	}

	public static HUD getHud() {
		return hud;
	}

	public static ConfigSystem getCfgsystem() {
		return cfgSystem;
	}

	public static CommandManager getCmdmanager() {
		return cmdManager;
	}
}
