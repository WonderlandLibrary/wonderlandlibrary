package Velo.api.Main;

import java.io.File;

import org.lwjgl.opengl.Display;

import com.thealtening.AltService;




import Velo.api.Command.CommandManager;
import Velo.api.Module.ModuleManager;
import Velo.api.Module.Config.ConfigManager;
import Velo.api.Util.alt.system.AccountManager;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.setting.Setting;

public class Main {
	
	public static ModuleManager moduleManager = new ModuleManager();
	private static DiscordRP discordRP = new DiscordRP();
	public static String name = "Velo", version = "0.3 Beta", authors = "Plexter C or MC_exe And Tab2 or I P";
	
	public static CommandManager commandManager = new CommandManager();
public static ConfigManager cfgManager = new ConfigManager();
private static File dataDirectory;
public static ConfigManager configManager;
private AltService altService = new AltService();
public static Setting setting = new Setting();
public static Main instance = new Main();
public static Main INSTANCE = new Main();

public String apyKey = "";
private static AccountManager accountManager;




	public static void onClientStartup() {
		Display.setTitle("Invasion");

		System.out.println("Invasion loading all classes...");
		discordRP.start();
		Velo.api.Module.ModuleManager.registerModules();
		Velo.api.Util.fontRenderer.Utils.DrawFontUtil.loadFonts();
		Fonts.loadFonts();

		accountManager = new AccountManager(dataDirectory);
	
	}
	
	

	
	
	public static void onClientShutDown() {
		discordRP.shutdown();
        accountManager.save();
	}
	
	public static ModuleManager getModuleManager() {
		return moduleManager;
	}
	public static DiscordRP getDiscordRP() {
		return discordRP;
	}
	   public static File getDataDir() {
		      return dataDirectory;
		   }
	   
	   public AccountManager getAccountManager() {
	        return accountManager;
	    }

	    public void switchToMojang() {
	        try {
	            altService.switchService(AltService.EnumAltService.MOJANG);
	        } catch (NoSuchFieldException | IllegalAccessException e) {
	            System.out.println("Couldnt switch to modank altservice");
	        }
	    }

	    public void switchToTheAltening() {
	        try {
	            altService.switchService(AltService.EnumAltService.THEALTENING);
	        } catch (NoSuchFieldException | IllegalAccessException e) {
	            System.out.println("Couldnt switch to altening altservice");
	        }
	    }
}
