package de.Hero.example;


import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class clickgui extends Module {

    public clickgui()
    {
		super("clickgui", Type.Visual, Keyboard.KEY_RSHIFT, Category.VISUAL, Color.green, "Render the GUI");
    }

    //Setup is called in the Module con
    @Override
    public void setup(){
    	ArrayList<String> options = new ArrayList<>();
		MythicX.setmgr.rSetting(new Setting("Sound", this, false));
		MythicX.setmgr.rSetting(new Setting("GuiRed", this, 0, 0, 255, true));
		MythicX.setmgr.rSetting(new Setting("GuiGreen", this, 0, 0, 255, true));
		MythicX.setmgr.rSetting(new Setting("GuiBlue", this, 255, 0, 255, true));
    }
    
    @Override
    public void onEnable()
    {
    	/**
    	 * Einfach in der StartMethode
    	 * clickgui = new ClickGUI(); ;)
    	 */
    	mc.displayGuiScreen(MythicX.clickgui);
    	toggle();
    	super.onEnable();
    }
}
