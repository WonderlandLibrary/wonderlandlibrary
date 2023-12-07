package me.robbanrobbin.jigsaw.client.bypasses;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.ImmutableMultiset;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.module.Module;

public abstract class ModPreset {
	
	public void onEnable() {
		
		Jigsaw.disableModules(Jigsaw.getModulesByCategories(Jigsaw.getModules(), Jigsaw.defaultCategoriesWithTarget));
		
		Jigsaw.getModuleByName("FPS").setToggled(true, true);
		Jigsaw.getModuleByName("Coords").setToggled(true, true);
		Jigsaw.getModuleByName("TPS").setToggled(true, true);
		
		
		//Jigsaw.getGUIMananger().reloadSettingScreens();
		
		Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "[" + getName() + "] - Disabled all bannable mods and changed all settings to bypass!"));
	}
	
	public String getName() {
		return null;
	}
	
	public Module createModule() {
		if(this.getName() == null) {
			return null;
		}
		return new Module(this.getName(), Keyboard.KEY_NONE, Category.PRESETS) {
			
			@Override
			public void onEnable() {
				ModPreset.this.onEnable();
				super.onEnable();
				this.setToggled(false, true);
			}
			
		};
	}
	
}
