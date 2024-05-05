package sudo.module.client;

import sudo.core.managers.ConfigManager;
import sudo.module.Mod;

public class SaveConfig extends Mod {
	
    public SaveConfig() {
        super("SaveConfig", "Saves the current enabled modules & their settings", Category.CLIENT, 0);
    }
    
    @Override
    public void onEnable() {
    	ConfigManager.saveConfig();
    	this.setEnabled(false);
    	super.onEnable();
    }
    
    @Override
    public void onDisable() {

    	super.onDisable();
    }
}
