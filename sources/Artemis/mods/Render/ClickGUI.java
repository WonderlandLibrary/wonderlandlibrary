package artemis.mods.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.Artemis;
import artemis.module;
import artemis.clickgui.ClickGui;
import artemis.settings.Setting;
import artemis.settings.SettingsManager;
import net.minecraft.client.gui.FontRenderer;


public class ClickGUI extends module {
	
	  public ClickGUI() {
	    super("ClickGui", "The clickgui", Keyboard.KEY_RSHIFT, 5, Category.RENDER);
	    this.setKey(Keyboard.KEY_RSHIFT);
	  }
	    public void onDisable() {
	        super.onDisable();
	      }
	    
		@Override
		public void onEnable(){
			 if (isToggled()) {
				 this.toggle();
				 
				 mc.displayGuiScreen(new ClickGui());
				 ArrayList<String> options = new ArrayList<String>();
			        options.add("New");
			        options.add("JellyLike");
			        Artemis.instance.settingsManager.rSetting(new Setting("Design", this, "New", options));
			        //artemis_client.instance.settingsManager.rSetting(new Setting("Sound", this, false));
			        //artemis_client.instance.settingsManager.rSetting(new Setting("GuiRed", this, 255, 0, 255, true));
			        //artemis_client.instance.settingsManager.rSetting(new Setting("GuiGreen", this, 26, 0, 255, true));
			        //artemis_client.instance.settingsManager.rSetting(new Setting("GuiBlue", this, 42, 0, 255, true));
				 super.onUpdate();
		     }
		}
	}

	
	  

	
	  
	  
  
  