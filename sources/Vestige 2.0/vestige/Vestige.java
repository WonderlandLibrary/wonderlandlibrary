package vestige;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import vestige.api.event.EventManager;
import vestige.command.CommandManager;
import vestige.config.SaveLoad;
import vestige.font.FontUtil;
import vestige.impl.module.ModuleManager;
import vestige.impl.processor.BalanceCalculatorProcessor;
import vestige.impl.processor.KeybindProcessor;
import vestige.impl.processor.ModuleDragProcessor;
import vestige.impl.processor.SessionInfoProcessor;
import vestige.ui.altmanager.AltManager;
import vestige.ui.menu.VestigeMainMenu;
import vestige.util.auth.Encryption;
import vestige.util.base.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.lwjgl.opengl.Display;

import com.mojang.realmsclient.gui.ChatFormatting;

@Getter
public class Vestige implements IMinecraft {

    static final Vestige instance = new Vestige();

    final String name = "Vestige",
                         version = "2.0",
                         versionUnchanged = "1.9.6",
                         fullname = name + " " + version;

    EventManager eventManager;
    ModuleManager moduleManager;
    CommandManager commandManager;

    KeybindProcessor keybindProcessor;
    ModuleDragProcessor moduleDragProcessor;
    SessionInfoProcessor sessionInfoProcessor;
    BalanceCalculatorProcessor balanceProcessor;
    
    AltManager altManager;
    
    SaveLoad config;
    
    @Setter
    boolean destructed;
    
    final Color clientColor = new Color(58, 192, 109);
    
    private Vestige() { /* So other classes can't make an instance of it */ }
    
    public boolean authStep1;
    public boolean authStep2;
    public boolean authStep3;
    public boolean authStep4;
    public boolean authStep5;
    
    String username;
    
    TimerUtil shaderTimer = new TimerUtil();
    
    @Setter
    boolean shaderEnabled;

    public static Vestige getInstance() { // because lombok don't make getters for static fields
        return instance;
    }

    public void addChatMessage(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText(ChatFormatting.BLUE + this.name + ChatFormatting.WHITE + " : " + message));
    }
    
    public GuiScreen getMainMenu() {
    	if (this.isDestructed()) {
    		return new GuiMainMenu();
    	} else {
    		return new VestigeMainMenu();
    	}
    }
    
    public long getShaderTimeElapsed() {
    	return shaderTimer.getTimeElapsed();
    }
    
    public void shutdown() {
    	config.save();
    	
    	try {
			File dir = new File(mc.mcDataDir, "Vestige 2.0");
			File configDir = new File(dir, "alts");

			if(!dir.exists()) {
				dir.mkdir();
			}

			if(!configDir.exists()) {
				configDir.mkdir();
			}
			
			File dataFile = new File(configDir, "alts.list");

			FileOutputStream fos = new FileOutputStream(dataFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(Vestige.getInstance().getAltManager().alts);

			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}