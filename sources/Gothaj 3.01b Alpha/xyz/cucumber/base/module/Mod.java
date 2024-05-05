package xyz.cucumber.base.module;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.module.feat.other.NotificationsModule;
import xyz.cucumber.base.module.feat.other.NotificationsModule.Notification;
import xyz.cucumber.base.module.feat.other.NotificationsModule.Type;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.utils.position.PositionUtils;

public class Mod {

	private String name = this.getClass().getAnnotation(ModuleInfo.class).name(),
			description = this.getClass().getAnnotation(ModuleInfo.class).description();

	private int key = this.getClass().getAnnotation(ModuleInfo.class).key();

	private Category category = this.getClass().getAnnotation(ModuleInfo.class).category();

	private ArrayPriority priority = this.getClass().getAnnotation(ModuleInfo.class).priority();

	private boolean enabled;

	public Minecraft mc = Minecraft.getMinecraft();

	private ArrayList<ModuleSettings> settings = new ArrayList();

	public String info = "";
	
	public Mod() {
		
	}
	public Mod(String name, String description, Category category) {
		this.category = category;
		this.description = description;
		this.name = name;
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public void toggle() {
		NotificationsModule mod = (NotificationsModule) Client.INSTANCE.getModuleManager().getModule(NotificationsModule.class);
		enabled = !enabled;
		if (enabled) {
			
			if(mod.isEnabled()) {
				mod.notifications.add(new Notification(this.name, "Module was enabled!", Type.ENABLED,  new PositionUtils(0,0,0,0,1)));
			}
			
			Client.INSTANCE.getEventBus().register(this);
			try {
				this.onEnable();
			} catch (Exception e) {

			}

		} else {
			if(mod.isEnabled()) {
				mod.notifications.add(new Notification(this.name, "Module was disabled!", Type.DISABLED,  new PositionUtils(0,0,0,0,1)));
			}
			Client.INSTANCE.getEventBus().unregister(this);
			try {
				this.onDisable();
			} catch (Exception e) {

			}

		}
	}
	
	public void setInfo(String info) {
		this.info = info;
	}

	public ArrayList<ModuleSettings> getSettings() {
		return settings;
	}

	public void addSettings(ModuleSettings... moduleSettings) {
		settings.addAll(Arrays.asList(moduleSettings));
	}

	public String getName() {
		return name;
	}
	
	public String getName(boolean split) {
		if(split) {
			return name;
		}else {
			return name.replace(" ", "");
		}
	}
	public PositionUtils getPosition(){
		return null;
	}
	public void setXYPosition(double x, double y) {
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ArrayPriority getPriority() {
		return priority;
	}

	public void setPriority(ArrayPriority priority) {
		this.priority = priority;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
