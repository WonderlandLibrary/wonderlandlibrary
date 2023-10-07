package dev.monsoon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.monsoon.event.listeners.*;
import dev.monsoon.manager.CommandManager;
import dev.monsoon.event.EventManager;
import dev.monsoon.manager.ModuleManager;
import dev.monsoon.module.enums.Category;
import dev.monsoon.module.implementation.misc.*;
import dev.monsoon.module.implementation.render.*;
import dev.monsoon.ui.clickgui.dropdown1.api.ClickHandler;
import dev.monsoon.ui.clickgui.dropdown1.impl.ClickGUI;
import dev.monsoon.ui.clickgui.skeet.SkeetGUI;
import dev.monsoon.ui.hud.mod.HudManager;
import dev.monsoon.util.font.GloriFontRenderer;
import dev.monsoon.util.misc.MonsoonRPC;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import dev.monsoon.config.SaveLoad;
import dev.monsoon.event.Event;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.implementation.combat.Killaura;
import dev.monsoon.module.implementation.movement.Fly;
import dev.monsoon.module.implementation.movement.Speed;
import dev.monsoon.module.implementation.exploit.Blink;
import dev.monsoon.module.implementation.player.Cheststealer;
import dev.monsoon.module.implementation.player.Scaffold;
import dev.monsoon.module.implementation.player.Phase;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import dev.monsoon.ui.ingame.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;

public class Monsoon {
	
	public static String name = "Monsoon", version = "1.2", type = "Beta";
	public static String NameInConsole = "<monsoon> ";
	public static CopyOnWriteArrayList<Module> modules;
	public static HUD hud = new HUD();
	
	public static String monsoonUsername, monsoonUid;
	public static boolean authorized;
	public static SaveLoad saveLoad;
	public static ModuleManager manager;
	public static CommandManager commandManager = new CommandManager();
	public static HudManager hudManagaer;
	public static ClickGUI clickGui;
	public static SkeetGUI skeetGui;
	public static MonsoonRPC discordRPC = new MonsoonRPC();
	public static GloriFontRenderer monsoonFont, monsoonLargeFont;
	
	public static BlockAnimation animations;
	public static Killaura killAura;
	public static Scaffold scaffold;
	public static HUDOptions arraylist;
	public static Blink blink;
	public static Fly fly;
	public static Speed speed;
	public static Cheststealer cheststealer;
	public static TargetHUD targethud;
	public static ESP esp;
	public static ModToggleGUI clickGuiMod;
	
	public static void startup() {
		authorized = false;
		System.out.println(NameInConsole + "Starting Monsoon");
		Display.setTitle(name + " " + version);

		manager = new ModuleManager();
		assignOldVariables();
		
		saveLoad = new SaveLoad("default");
		hudManagaer = new HudManager();
		clickGui = (ClickGUI) new ClickGUI().withWidth(118).withHeight(15);
		skeetGui = new SkeetGUI(160, 84);
		if(manager.rpcModule.isEnabled()) {
			discordRPC.start();
			discordRPC.update("Utilizing Monsoon " + version + " (" + type + ")", "Sexy gaming chair");
		}

		manager.breakReminder.timer.reset();
		monsoonFont = new GloriFontRenderer(new ResourceLocation("monsoon/font/whitneysemibold.otf"), 18.0F);
		monsoonLargeFont = new GloriFontRenderer(new ResourceLocation("monsoon/font/whitneysemibold.otf"), 45.0F);
		blink.setEnabledSilent(false);
		killAura.setEnabledSilent(false);
		scaffold.setEnabledSilent(false);
	}
	
	public static void shutdown() {
		discordRPC.shutdown();
		if(saveLoad != null) {
			saveLoad.save();
		}
	}

	public static void event(Event e) {
		if (e instanceof EventPacket && e.isIncoming()) {
			if (((EventPacket) e).getPacket() instanceof S08PacketPlayerPosLook) {
				for (Module m : modules) {
					if (m.disableOnLagback) {
						if (m.isEnabled() && !(m instanceof Phase)) {
							m.toggleSilent();
							NotificationManager.show(new Notification(NotificationType.WARNING, "Lagback", m.getName() + " was disabled due to lagbacks.", 3));
						}
					}
				}
			}
		}
		if (e instanceof EventUpdate) {
			scaffold.timerAmount.shouldRender = scaffold.timerBoost.isEnabled();
			fly.pulseDelay.shouldRender = fly.blink.is("Pulse");
			fly.health.shouldRender = fly.damageMode.is("Two");
		}
		if (e instanceof EventChat) {
			commandManager.handleChat((EventChat) e);
		}
		if(e instanceof EventMotion) {
			//Monsoon.sendMessage(((EventMotion) e).yaw + " " + ((EventMotion) e).pitch);
		}
	}
	
	public static void keyPress(int key) {
		//if(authorized) {
			EventManager.call(new EventKey(key));

			for (Module m : manager.modules) {
				if (m.getKey() == key) {
					m.toggle();
				}
			}

			if (key == Keyboard.KEY_PERIOD) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiChat("."));
			}
		//}
	}
	
	public static Module getModule(String name) {
		for (Module m : manager.modules) {
			if (m.name.equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	
	public static List<Module> getModulesByCategory(Category c) {
		List<Module> modules = new ArrayList<Module>();

		for(Module m : manager.modules) {
			if(m.category == c)
				modules.add(m);
		}
		return modules;
	}

	public static void setAuthorized(boolean authorized) {
		Monsoon.authorized = authorized;
	}

	public static void sendMessage(String message)
    {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        StringBuilder messageBuilder = new StringBuilder();

        if (prefix) messageBuilder.append("&r<&bmonsoon&r>").append("&r ");

        messageBuilder.append(message);

        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(messageBuilder.toString().replace("&", "\247")));
    }

    public static void sendNotif(String title, String desc) {
		NotificationManager.show(new Notification(NotificationType.INFO, title, desc, 1));
	}
    
    public static void setMonsoonName(String newname) {
		monsoonUsername = newname;
	}

	public static ClickHandler getClickGUI() {
		return clickGui;
	}

	/*public static GloriFontRenderer getFont() {
		return monsoonFont;
	}*/

	public static FontRenderer getFont() {
		return Minecraft.getMinecraft().fontRendererObj;
	}

	public static GloriFontRenderer getLargeFont() {
		return monsoonLargeFont;
	}

	private static void assignOldVariables() {
		modules = manager.modules;

		animations = manager.animations;;
		killAura = manager.killAura;
		scaffold = manager.scaffold;
		arraylist = manager.arraylist;
		blink = manager.blink;
		fly = manager.fly;
		speed = manager.speed;
		cheststealer = manager.cheststealer;
		targethud = manager.targethud;
		esp = manager.esp;
		clickGuiMod = manager.clickGuiMod;
	}

	public static SkeetGUI getSkeetGui() {
		return skeetGui;
	}
}
