package de.Hero.example;


import astronaut.Duckware;
import astronaut.modules.Category;
import astronaut.modules.Module;
import de.Hero.settings.Setting;

import java.awt.*;
import java.util.ArrayList;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class GUI extends Module {

    public GUI()
    {
		super("GUI", Type.Visual, 0, Category.VISUAL, Color.green, "Render the GUI");
    }

    //Setup is called in the Module con
    @Override
    public void setup(){
    	ArrayList<String> options = new ArrayList<>();
		Duckware.setmgr.rSetting(new Setting("Sound", this, false));
		Duckware.setmgr.rSetting(new Setting("GuiRed", this, 255, 0, 255, true));
		Duckware.setmgr.rSetting(new Setting("GuiGreen", this, 26, 0, 255, true));
		Duckware.setmgr.rSetting(new Setting("GuiBlue", this, 42, 0, 255, true));
    }
    
    @Override
    public void onEnable()
    {
    	/**
    	 * Einfach in der StartMethode
    	 * clickgui = new ClickGUI(); ;)
    	 */
    	mc.displayGuiScreen(Duckware.clickgui);
    	toggle();
    	super.onEnable();
    }
}
