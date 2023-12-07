package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.bypasses.ModPreset;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ClickGuiManager {
	
	public ArrayList<ModuleWindow> windows = new ArrayList<ModuleWindow>();
	final Map<Category, ModuleWindow> catWindows = new HashMap<Category, ModuleWindow>();
	public float globalAlphaMinus = 0f;
	
	public int mouseX;
	
	public int mouseY;
	
	public float getAlphaDec() {
		return 0.5f;
	}
	
	public float getAlpha() {
		if(globalAlphaMinus == 0) {
			return 0;
		}
		return globalAlphaMinus - GuiUtils.partialTicks * getAlphaDec();
	}
	
	public void setup() {
		for(Category category : Category.values()) {
			if (category == Category.HIDDEN) {
				continue;
			}
			ModuleWindow wind = new ModuleWindow();
			wind.setExtended(false);
			wind.setX(10);
			wind.setY(10);
			wind.setCategory(category);
			String name = category.name().toLowerCase();
			name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			wind.setTitle(name);
			catWindows.put(category, wind);
		}
		for(Module module : Jigsaw.getModules()) {
			if (module.getCategory() == Category.HIDDEN) {
				continue;
			}
			if(module.isCheckbox()) {
				ModCheckBtn btn = new ModCheckBtn();
				btn.setMod(module);
				btn.setHeight(12);
				if (module.getKeyboardKey() == Keyboard.KEY_NONE) {
					btn.setTitle(module.getName());
				} else {
					btn.setTitle(module.getName() + "[" + Keyboard.getKeyName(module.getKeyboardKey()) + "]");
				}
				catWindows.get(module.getCategory()).addChild(btn);
			}
			else {
				addButtonAndSettingsContainerForMod(module);
			}
			
		}
		
		{
			CheckBtnSetting setting = new CheckBtnSetting("Smooth Aiming", "smoothAim");
			catWindows.get(Category.SETTINGS).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Smooth Aim Factor", "smoothAimSpeed", 1.5, 5.0, 0.0, ValueFormat.PERCENT);
			catWindows.get(Category.SETTINGS).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Pathfinder Max Computations", "pathfinderMaxComputations", 100, 11000, ValueFormat.INT);
			catWindows.get(Category.SETTINGS).addChild(setting.createComponent());
		}
		
		for(ModPreset modPreset : Jigsaw.getPresetManager().getBypasses()) {
			addButtonAndSettingsContainerForMod(modPreset.createModule());
		}
		
		for(ModuleWindow wind : catWindows.values()) { //Remove empty windows
			if(wind.getChildren().isEmpty()) {
				continue;
			}
			windows.add(wind);
		}
		
		int x = 0 + 10;
		int y = 0 + 10;
		for(ModuleWindow wind : windows) {
			wind.layoutChildren();
			wind.setX(x);
			wind.setY(y);
			x += wind.getWidth() + 10;
			if(x + wind.getWidth() > Minecraft.getMinecraft().displayWidth / 2) {
				y += 20;
				x = 10;
			}
		}
	}
	
	private void addButtonAndSettingsContainerForMod(Module module) {
		ModuleButton btn = new ModuleButton();
		btn.setMod(module);
		btn.setHeight(12);
		if (module.getKeyboardKey() == Keyboard.KEY_NONE) {
			btn.setTitle(module.getName());
		} else {
			btn.setTitle(module.getName() + "[" + Keyboard.getKeyName(module.getKeyboardKey()) + "]");
		}
		SettingContainer con = new SettingContainer();
		con.extended = false;
		con.addChild(new KeybindButton(module));
		btn.setSettingContainer(con);
		if(module.getModes().length > 1) {
			ModeButton modeBtn = new ModeButton(module);
			con.addChild(modeBtn);
		}
		if(module.getModSettings() != null) {
			for(ModSetting setting : module.getModSettings()) {
				if(setting == null) {
					continue;
				}
				con.addChild(setting.createComponent());
			}
		}
		catWindows.get(module.getCategory()).addChild(btn);
		catWindows.get(module.getCategory()).addChild(con);
	}
	
	public void draw() {
		GlStateManager.scale(2d / Minecraft.getMinecraft().gameSettings.guiScale, 2d / Minecraft.getMinecraft().gameSettings.guiScale, 1);
		for(ModuleWindow wind : windows) {
			wind.draw();
		}
	}
	
	public void update() {
		for(ModuleWindow wind : windows) {
			wind.update();
		}
	}
	
	public void reload() {
		windows.clear();
		catWindows.clear();
		setup();
	}
	
	public void resetTimes() {
		for(ModuleWindow window : windows) {
			window.resetTimeForChildren();
		}
	}
	
}
