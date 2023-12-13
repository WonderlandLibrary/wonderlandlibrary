package wtf.monsoon;

import org.lwjgl.opengl.Display;

import wtf.monsoon.api.Logger;
import wtf.monsoon.api.Wrapper;
import wtf.monsoon.api.config.SaveLoad;
import wtf.monsoon.api.event.EventManager;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventChat;
import wtf.monsoon.api.event.impl.EventReceivePacket;
import wtf.monsoon.api.event.impl.EventRender2D;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.managers.CommandManager;
import wtf.monsoon.api.managers.ModuleManager;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.util.font.FontUtil;
import wtf.monsoon.api.util.font.MinecraftFontRenderer;
import wtf.monsoon.impl.ui.clickgui.dropdown1.api.ClickHandler;
import wtf.monsoon.impl.ui.clickgui.dropdown1.impl.ClickGUI;
import wtf.monsoon.impl.ui.clickgui.dropdown1.impl.modern.ModernClickGUI;
import wtf.monsoon.impl.ui.clickgui.skeet.SkeetGUI;
import wtf.monsoon.impl.ui.game.HUD;
import wtf.monsoon.impl.ui.notification.Notification;
import wtf.monsoon.impl.ui.notification.NotificationManager;
import wtf.monsoon.impl.ui.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

public class Monsoon implements Wrapper {
	
	public static Monsoon INSTANCE = new Monsoon();
	public static Logger LOGGER = new Logger();
	public static Core CORE = new Core();
		
	public String name = "Monsoon", 
			version = "1.3", 
			type = "Development", 
			consoleName = "<monsoon>",  
			nameVer = "Monsoon " + version;

	public MinecraftFontRenderer monsoonFont, monsoonLargeFont;
	public String monsoonUsername;
	public static boolean authorized;
	
	public EventManager eventManager;
	public ModuleManager manager;
	public CommandManager commandManager;
	
	public ClickGUI clickGui;
	public ModernClickGUI modernClickGui;
	public SkeetGUI skeetGui;
	public SaveLoad saveLoad;
	public HUD hud;
	
	// Called on Minecraft initiation
	public void init() {
		authorized = false;
		Display.setTitle(nameVer);
		LOGGER.info("Initializing " + nameVer);
		
		eventManager = new EventManager();
		manager = new ModuleManager();
		commandManager = new CommandManager();
		
		saveLoad = new SaveLoad("default");
		hud = new HUD();

		FontUtil.bootstrap();

		clickGui = (ClickGUI) new ClickGUI().withWidth(118).withHeight(15);
		modernClickGui = (ModernClickGUI) new ModernClickGUI().withWidth(118).withHeight(20);
		skeetGui = new SkeetGUI(160, 84);
		monsoonFont = FontUtil.monsoon_regular;
		monsoonLargeFont = FontUtil.large;
		
		eventManager.register(this);
	}
	
	public void shutdown() {
		
		if(saveLoad != null) {
			saveLoad.save();
		}
		eventManager.unregister(this);
	}
	
	@EventTarget
	public void onChat(EventChat e) {
		commandManager.handleChat(e);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D e) {
		hud.onRender2D(e);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		//Monsoon.sendMessage(mc.thePlayer.rotationPitch + " ");
		manager.scaffold.timerAmount.shouldRender = manager.scaffold.timerBoost.isEnabled();
		manager.fly.pulseDelay.shouldRender = manager.fly.blink.is("Pulse");
		manager.fly.health.shouldRender = manager.fly.damageMode.is("Two");
	}
	
	@EventTarget
	public void onGetPacket(EventReceivePacket e) {
		if (e.getPacket() instanceof S08PacketPlayerPosLook) {
			for (Module m : manager.modules) {
				if (m.disableOnLagback) {
					if (m.isEnabled() ) {
						m.toggle();
						NotificationManager.show(new Notification(NotificationType.WARNING, "Lagback", m.getName() + " was disabled due to lagbacks.", 3));
					}
				}
			}
		}
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public  void setMonsoonName(String newname) {
		this.monsoonUsername = newname;
	}
	
	public MinecraftFontRenderer getCustomFont() {
		return monsoonFont;
	}

	public FontRenderer getFont() {
		return Minecraft.getMinecraft().fontRendererObj;
	}

	public MinecraftFontRenderer getLargeFont() {
		return monsoonLargeFont;
	}
	
	public ClickHandler getClickGUI() {
		if(manager.clickGuiMod.theme.is("Modern")) {
			return modernClickGui;
		} else {
			return clickGui;
		}
	}
	
	public SkeetGUI getSkeetGui() {
		return skeetGui;
	}
	
	public static void sendMessage(String message) {
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("&r<&bmonsoon&r>").append("&r ");

        messageBuilder.append(message);

        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(messageBuilder.toString().replace("&", "\247")));
    }

    public static void sendNotif(String title, String desc) {
		NotificationManager.show(new Notification(NotificationType.INFO, title, desc, 1));
	}
	
}
