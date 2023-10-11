package markgg.modules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import markgg.RazeClient;
import markgg.settings.Setting;
import net.minecraft.client.Minecraft;

public class Module {
	public String name = this.getClass().getAnnotation(ModuleInfo.class).name();
	public Category category = this.getClass().getAnnotation(ModuleInfo.class).category();
	public int keyCode = this.getClass().getAnnotation(ModuleInfo.class).bind();
	public boolean listening, expanded, toggled;

	public List<Setting> settings = new ArrayList<Setting>();

	public Minecraft mc = Minecraft.getMinecraft();

	public void addSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
	}
	
	public List<Setting> getSettings() {
        return settings;
    }

	public boolean isEnabled() {
		return toggled;
	}

	public int getKey() {
		return keyCode;
	}

	public void toggle() {
		toggled = !toggled;
		if (toggled) {
			RazeClient.getEventBus().register(this);
			onEnable();
		} else {
			onDisable();
			RazeClient.getEventBus().unregister(this);
		} 
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		if (this.toggled) {
			RazeClient.getEventBus().register(this);
		} else {
			RazeClient.getEventBus().unregister(this);
		}
	}

	public void onEnable() { }

	public void onDisable() { }

	public String getName() {
		return name;
	}

	public enum Category {
		COMBAT("Combat", new int[] {212, 77, 68}),
		MOVEMENT("Movement", new int[] {66, 194, 121}),
		PLAYER("Player", new int[] {165, 96, 198}),
		MISC("Exploit", new int[] {74, 153, 200}),
		RENDER("Visual", new int[] {55, 0, 208}),
		GHOST("Ghost", new int[] {215, 156, 45});

		public String name;

		public boolean expanded = true;
		
		private int[] rgb;

		Category(String name, int[] rgb) {
			this.name = name;
			this.rgb = rgb;
		}
		
		public int[] getCatRGB() {
	        return rgb;
	    }
	}
	
	public Category getCategory() {
		return category;
	}
	
	public static Color getCategoryColor(Category c) {
		int[] rgb = c.getCatRGB();
	    return new Color(rgb[0], rgb[1], rgb[2]);
	}

}
