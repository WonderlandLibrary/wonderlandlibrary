package slavikcodd3r.rainbow;

import slavikcodd3r.rainbow.command.CommandManager;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.gui.hacktools.utils.HackPack;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.module.modules.misc.StaffDetector;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MCStencil;
import slavikcodd3r.rainbow.utils.PushUtils;
import slavikcodd3r.rainbow.module.Module;

import java.awt.AWTException;
import java.awt.Color;

import org.lwjgl.opengl.Display;

public final class Rainbow
{
    public static String name;
    public static String version;
    public static String developer;
    public static String vk;
    public static String youtube;
    public static String protectedname;
    public static String rainbowprotection;
    public static String rainbowprotectiondialog;
    public static boolean purchased = true;
    public static Rainbow instance;
    public final ModuleManager moduleManager;
    public final Module module;
    private static HackPack hackpack;

    public Rainbow() {
        Rainbow.instance = this;
        this.moduleManager = new ModuleManager();
        this.module = new Module();
    }
    
	static {
    	Rainbow.name = "Rainbow";
    	Rainbow.version = "2.0";
    	Rainbow.developer = "SlavikCodd3r";
    	Rainbow.vk = "https://vk.com/slavikskidd3r";
    	Rainbow.youtube = "https://youtube.com/slavikcodd3r";
    	Rainbow.protectedname = "Rainbow 2.0 User";
    	Rainbow.rainbowprotection = "[" + Rainbow.name + " Protection]";
    	Rainbow.rainbowprotectiondialog = Rainbow.name + " Protection";
    }
    
    public static void start() {
        ClientUtils.loadClientFont();
        ModuleManager.start();
        CommandManager.start();
        OptionManager.start();
        FriendManager.start();
        MCStencil.checkSetupFBO();
        hackpack = new HackPack();
		try {
			PushUtils.sendPush(Rainbow.name + " " + Rainbow.version, "Приятной игры :)");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static int getRainbow(final int speed, final int offset) {
        float hue = (float)((System.currentTimeMillis() + offset) % speed);
        hue /= speed;
        return Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
    }
    
    public static ModuleManager getModuleManager() {
        return (ModuleManager)Rainbow.instance.moduleManager;
    }
    
    public static Module getModule() {
        return (Module)Rainbow.instance.module;
    }
    
    public static HackPack getHackPack() {
        return hackpack;
    }
}