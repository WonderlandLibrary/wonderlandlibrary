package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.bypasses.ModPreset;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.layout.HorizontalLayout;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;

public class ClickGuiManager {
	
	public ArrayList<ComponentWindow> windows = new ArrayList<ComponentWindow>();
	final Map<Category, ComponentWindow> catWindows = new HashMap<Category, ComponentWindow>();
	
	public OverlayWindow lastOverlayWindow;
	public boolean canRemoveOverlayWindow;
	
	public int mouseX;
	
	public int mouseY;
	
	public Component focusedComponent = null;
	
	public ComponentTextField searchComponent;
	
	public int MAX_CONTAINER_HEIGHT = -1;
	
	public void setup() {
		ComponentWindow minimapWindow = new ComponentWindow(new HorizontalLayout(1));
		minimapWindow.setTitle("Minimap");
		minimapWindow.setExtended(false);
		
		ComponentMinimap minimap = new ComponentMinimap();
		minimapWindow.addChild(minimap);
		
		ComponentWindow windowManagerWindow = new ComponentWindow(new HorizontalLayout(2));
		windowManagerWindow.setTitle("Window Manager");
		
		ComponentWindow searchWindow = new ComponentWindow(new HorizontalLayout(1));
		searchWindow.setTitle("Search Modules");
		searchWindow.setExtended(true);
		searchWindow.addChild(searchComponent = new ComponentTextField());
		
		for(Category category : Category.values()) {
			if (category == Category.HIDDEN) {
				continue;
			}
			ModuleWindow wind = new ModuleWindow(new HorizontalLayout(2));
			wind.setCategory(category);
			String name = null;
			if(category.getDisplayName() != null) {
				name = category.getDisplayName();
			}
			else {
				name = category.name().toLowerCase();
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			}
			wind.setTitle(name);
			wind.setExtended(false);
			wind.setEnabled(false);
			catWindows.put(category, wind);
		}
		for(Module module : Jigsaw.getModules()) {
			if (module.getCategory() == Category.HIDDEN) {
				continue;
			}
			if(module.isCheckbox()) {
				ModCheckBtn btn = new ModCheckBtn();
				btn.setMod(module);
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
			SliderSetting setting = new SliderSetting("Minimap Smoothness", "minimapTerrainSmoothness", 0.3, 1.0, 0.0, ValueFormat.PERCENT);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			CheckBtnSetting setting = new CheckBtnSetting("Glass Mode", "glassMode");
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			CheckBtnSetting setting = new CheckBtnSetting("Enable Blur Effects", "enableBlur");
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Blur Buffer FPS", "blurBufferFPS", 1, 144, ValueFormat.INT);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			CheckBtnSetting setting = new CheckBtnSetting("Smooth Aiming (Client Side)", "smoothAim");
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Smooth Aim Factor", "smoothAimSpeed", 1.5, 5.0, 0.0, ValueFormat.PERCENT);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			CheckBtnSetting setting = new CheckBtnSetting("Show Targets List", "showTargetsInSeperateWindow");
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Pathfinder Max Computations", "pathfinderMaxComputations", 0, 2500, ValueFormat.INT);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Theme Red", "guiColorForegroundR", 0.2, 1.0, 0.0, ValueFormat.DECIMAL);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Theme Green", "guiColorForegroundG", 0.2, 1.0, 0.0, ValueFormat.DECIMAL);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting("Theme Blue", "guiColorForegroundB", 0.2, 1.0, 0.0, ValueFormat.DECIMAL);
			catWindows.get(Category.GLOBAL).addChild(setting.createComponent());
		}
		
		for(ModPreset modPreset : Jigsaw.getPresetManager().getBypasses()) {
			addButtonAndSettingsContainerForMod(modPreset.createModule());
		}
		
		windows.add(minimapWindow);
		
		for(ComponentWindow wind : windows) {
			windowManagerWindow.addChild(new WindowButton(wind));
		}

		windows.add(windowManagerWindow);
		
		for(ComponentWindow wind : catWindows.values()) { //Remove empty windows
			if(wind.getChildren().isEmpty()) {
				continue;
			}
			windows.add(wind);
			windowManagerWindow.addChild(new WindowButton(wind));
		}
		
		windows.add(searchWindow);
		windowManagerWindow.addChild(new WindowButton(searchWindow));
		
		windowManagerWindow.setExtended(true);
		
		int x = 0 + 10;
		int y = 0 + 10;
		for(ComponentWindow wind : windows) {
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
	
	public void updateWindowManager() {
		ComponentWindow windowManager = this.getWindowByTitle("Window Manager");
		
		windowManager.layoutChildren();
	}
	
	public ComponentWindow getWindowByTitle(String title) {
		for(ComponentWindow window : this.windows) {
			if(window.getTitle().equals(title)) {
				return window;
			}
		}
		return null;
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
		btn.setTooltip(module.description);
		catWindows.get(module.getCategory()).addChild(btn);
		catWindows.get(module.getCategory()).addChild(con);
	}
	
	public OverlayWindow createOverlayWindow() {
		removeOverlayWindow();
		OverlayWindow overlayWindow = new OverlayWindow(new HorizontalLayout(1));
		lastOverlayWindow = overlayWindow;
		windows.add(overlayWindow);
		canRemoveOverlayWindow = false;
		return lastOverlayWindow;
	}
	
	public void removeOverlayWindow() {
		if(lastOverlayWindow != null) {
			windows.remove(Jigsaw.getClickGuiManager().lastOverlayWindow);
			lastOverlayWindow = null;
		}
	}
	
	public void draw() {
		if(Jigsaw.isFrameBufferRendering()) {
			return;
		}
		int i = 0;
		for(ComponentWindow wind : windows) {
			if(!wind.enabled) {
				i++;
				continue;
			}
			wind.preDraw();
			wind.draw();
			wind.postDraw();
			i++;
		}
	}
	
	public void update() {
		
		boolean updateLayout = MAX_CONTAINER_HEIGHT == -1;
		
		MAX_CONTAINER_HEIGHT = Minecraft.getMinecraft().displayHeight;
		
		if(updateLayout) {
			this.updateLayoutForAllComponents();
		}
		
		for(ComponentWindow wind : windows) {
			if(!wind.enabled) {
				continue;
			}
			wind.update();
			wind.preUpdate();
		}
	}
	
	public void reload() {
		windows.clear();
		catWindows.clear();
		setup();
	}
	
	public void resetTimes() {
		for(ComponentWindow window : windows) {
			if(!window.enabled) {
				continue;
			}
			window.resetTimeForChildren(true);
		}
	}
	
	public void updateColorsForAllComponents() {
		
		for(ComponentWindow window : windows) {
			window.updateColors();
		}
		
	}
	
	public void updateLayoutForAllComponents() {
		
		MAX_CONTAINER_HEIGHT = Minecraft.getMinecraft().displayHeight;
		
		for(ComponentWindow window : windows) {
			window.layoutChildren();
		}
		
	}
	
}
