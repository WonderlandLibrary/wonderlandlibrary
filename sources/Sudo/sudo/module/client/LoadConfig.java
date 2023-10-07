package sudo.module.client;

import sudo.core.managers.ConfigManager;
import sudo.module.Mod;

public class LoadConfig extends Mod {
	
    public LoadConfig() {
        super("LoadConfig", "Load config", Category.CLIENT, 0);
    }
    
    @Override
    public void onEnable() {
    	ConfigManager.loadConfig();
    	this.setEnabled(false);
    	super.onEnable();
    }
    
    @Override
    public void onDisable() {

    	super.onDisable();
    }
}
