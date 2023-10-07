package sudo.module;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.settings.*;
import sudo.utils.misc.Notification;
import sudo.utils.misc.NotificationUtil;

public class Mod {
	protected MinecraftClient mc = MinecraftClient.getInstance();

	public String name;
	public String displayName;
	public String description;
	public Category category;
	public int key;
	public boolean enabled;
	
	private List<Setting> settings = new ArrayList<>();
	
	public Mod(String name, String description, Category category, int key) {
		this.name = name;
		this.displayName = name;
		this.description = description;
		this.category = category;
		this.key = key;
		
		addSetting(new KeybindSetting("Bind: ", key));
	}
	
	public List<Setting> getSetting() {
		return settings;
	}
	
	public void addSetting(Setting setting) {
		settings.add(setting);
	}
	
	public void addSettings(Setting...settings) {
		for (Setting setting : settings) addSetting(setting);
	}
	
	public List<Setting> getSettings() {
		return settings;
	}
	
	 public Setting getSettingByIndex(int i) {
        return (Setting)this.getSettings().get(i);
    }
	
	public void toggle() {
		this.enabled = !this.enabled;
		
		if (enabled) {
			onEnable(); 
			NotificationUtil.send_notification(new Notification(name +" was enabled", 202, 122, 248));
		}
		else {
			onDisable(); 
			NotificationUtil.send_notification(new Notification(name +" was disabled", 202, 122, 248));
		}
	}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        
        if (mc.player != null) {  
        	if (enabled) onEnable();
            else onDisable();
        }
    }
    
	public void nullCheck() {
		if(mc.world == null || mc.player == null || mc.getNetworkHandler() == null || mc.getBufferBuilders() == null) {
			return;
		}
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void onTick() {}
	public void onTickDisabled() {}
	public void onMotion() {};
	public String getDisplayName() {return displayName;}
	public void setDisplayName(String displayName) {this.displayName = displayName;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public int getKey() {return key;}
	public void setKey(int key) {this.key = key;}
	public boolean isEnabled() {return enabled;}
    public void onWorldRender(MatrixStack matrices) {}
    public void onRun() {}
	public Category getCategory() {return category;}

	public enum Category {
		MOVEMENT("Movement"),
		COMBAT("Combat"),
		RENDER("Render"),
		EXPLOIT("Exploit"),
		WORLD("World"),
		CLIENT("Client");
		
		public String name;
		public int moduleIndex;
		
		private Category(String name) {
			this.name = name;
		}
	}

	//If you use this on settings then you wont have to deal with adding addSettings because itll add two
	// You can also look at an example of this on AAAExample.java
	public BooleanSetting reg(String name, Boolean val){
		BooleanSetting setting = new BooleanSetting(name, val);
		return setting;
	}

	public NumberSetting reg(String name, double min, double max, double def, double inc){
		NumberSetting setting = new NumberSetting(name, min,max,def,inc);
		return setting;
	}

	public ModeSetting reg(String name, String mode, String... modes){
		ModeSetting setting = new ModeSetting(name,  mode, modes);
		return setting;
	}

	public ColorSetting reg(String name, Color val){
		ColorSetting setting = new ColorSetting(name,  val);
		return setting;
	}

}
