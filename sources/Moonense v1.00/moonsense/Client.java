package moonsense;

import org.lwjgl.opengl.Display;

import moonsense.alt.AltManager;
import moonsense.clickgui.ClickGUI;
import moonsense.event.EventManager;
import moonsense.event.EventTarget;
import moonsense.event.impl.ClientTick;
import moonsense.gui.SplashProgress;
import moonsense.hud.HUDConfigScreen;
import moonsense.hud.mod.HudManager;
import moonsense.mod.ModManager;
import moonsense.utils.DiscordRP;
import moonsense.utils.SessionChanger;
import moonsense.utils.font.FontUtil;
import moonsense.utils.notification.Notification;
import moonsense.utils.notification.NotificationManager;
import moonsense.utils.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;

public class Client {
	
	private DiscordRP discordRP = new DiscordRP();
	
	public String NAME = "Moonsense", VERSION = "v1.00", AUTHOR = "Moonsense LLC", NAMEVER = NAME + " " + VERSION;
	public static Client INSTANCE = new Client();
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	public EventManager eventManager;
	public static AltManager altManager;
	public ModManager modManager;
	public HudManager hudManager;
	
	public void startup() {
		eventManager = new EventManager();
		altManager = new AltManager();
		modManager = new ModManager();
		hudManager = new HudManager();
		
		System.out.println("Starting... | " + NAME + " Client " + VERSION + " By: " + AUTHOR);
		discordRP.start();
		Display.setTitle(NAME + " Client " + VERSION);
		
		FontUtil.bootstrap();
		
		eventManager.register(this);
		

	}
	
	public void shutdown() {
		System.out.println("Shutting down... | " + NAME + " Client " + VERSION);
		discordRP.shutdown();
		
		eventManager.unregister(this);

	}
	
	@EventTarget
	public void onTick(ClientTick event) {
		
		if(mc.gameSettings.TEST_MOD.isPressed()) {
			modManager.toggleSprint.toggle();
		}
		
//		if(mc.gameSettings.HUD_CONFIG.isPressed()) {
//			mc.displayGuiScreen(new HUDConfigScreen());
//		}
		
		if(mc.gameSettings.CLICK_GUI.isPressed()) {
//			mc.displayGuiScreen(new ClickGUI());
			mc.displayGuiScreen(new HUDConfigScreen());
			NotificationManager.show(new Notification(NotificationType.INFO, "HUD Manager", "Opened HUD Manager", 3));
		}
		
		if(Client.INSTANCE.hudManager.fullBright.isEnabled()) {
//			mc.gameSettings.gammaSetting = 10.0F;
			mc.thePlayer.addPotionEffect(new PotionEffect(16, 0));
		} else if(!Client.INSTANCE.hudManager.fullBright.isEnabled()) {
			mc.gameSettings.gammaSetting = 0.0F;
		}

	}
	
}
