package Velo.api.ClickGui.Display;

import java.awt.Color;
import java.util.ArrayList;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.setting.Setting;
import Velo.impl.Modules.visuals.hud.HUD;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.KeybindSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class DropdownClickgui extends GuiScreen {
	
	ArrayList<Module> modulesArray;
	ArrayList<Setting> modulesSetting;
	
	public boolean dragging = false;
	public boolean dragging2 = false;
	
	public int defaultCount = 0;
	
	public Fonts font1 = new Fonts();
	
	public DropdownClickgui() {
		modulesArray = new ArrayList<>(); 
		modulesSetting = new ArrayList<>();
		
		for(Category c : Module.Category.values()) {
			c.y = this.height/2;
			c.x = this.width/2 + (defaultCount*125);
			defaultCount++;
		}
		
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Category c : Module.Category.values()) {
			boolean hoveringOver = mouseY >= c.y && mouseY <= c.y + font1.clickguitest.FONT_HEIGHT && mouseX >= c.x - 5 && mouseX <= c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25;
			if(hoveringOver) {
				if(dragging) {
					c.x = mouseX - 25;
					c.y = mouseY - 10;
				}
			}
			
			Gui.drawRect(0, 0, 0, 0, 0xff);
			Gui.drawRect(c.x - 6, c.y, c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25, c.y + font1.clickguitest.FONT_HEIGHT, 0x70000000);
			font1.clickguitest.drawString(c.name, c.x, c.y, -1, true);
			
			int count = 0;
			int count2 = 0;
			int count3 = 0;
			
			if(c.isExpanded) {
				for(Module m : ModuleManager.getModulesByCategory(c)) {
					Gui.drawRect(0, 0, 0, 0, 0xff);
					Gui.drawRect(c.x - 6, c.y + 21 + count*16, c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25, c.y + 37 + count*16, 0x30000000);
					if(c.maxLength < font1.targethudNameMoon.getStringWidth(m.name)) {
						c.maxLength = font1.targethudNameMoon.getStringWidth(m.name);
					}
					if(c.name.equalsIgnoreCase("Visuals")) {
						//c.maxLength -= 0.001;
					}
					if(m.isToggled()) {
						Gui.drawRect(c.x - 6, c.y + 21 + count*16, c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25, c.y + 37 + count*16, 0x40000000);
						count3++;
					}
					if(HUD.colormode.equalsIgnorecase("Astolfo")) {
						font1.targethudNameMoon.drawString(m.name + (m.clickguisettingsexpanded ? "    >" : "    ^"), c.x, c.y + 23 + count*16, m.isEnabled() ? ColorUtil.astolfoColors((int) count*10, 1000) : Color.GRAY.getRGB());
					}
					if(HUD.colormode.equalsIgnorecase("Rainbow")) {
						font1.targethudNameMoon.drawString(m.name + (m.clickguisettingsexpanded ? "    >" : "    ^"), c.x, c.y + 23 + count*16, m.isEnabled() ? ColorUtil.getRainbow(6, 0.5F, 1, (long) -count*200) : Color.GRAY.getRGB());
					}
					if(HUD.colormode.equalsIgnorecase("Fade")) {
						font1.targethudNameMoon.drawString(m.name + (m.clickguisettingsexpanded ? "    >" : "    ^"), c.x, c.y + 23 + count*16, m.isEnabled() ? ColorUtil.getGradientOffset(new Color((int) HUD.red2.getValue(), (int) HUD.green2.getValue(), (int) HUD.blue2.getValue(), 255), new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7D - count3*25)) / 120D)).getRGB() : Color.GRAY.getRGB());
					}
					if(HUD.colormode.equalsIgnorecase("Custom")) {
						font1.targethudNameMoon.drawString(m.name + (m.clickguisettingsexpanded ? "    >" : "    ^"), c.x, c.y + 23 + count*16, m.isEnabled() ? new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255).getRGB() : Color.GRAY.getRGB());
					}
					count++;
					
					if(m.clickguisettingsexpanded) {
						for(Setting s : m.settings) {
							if(s instanceof KeybindSetting)
								continue;
							
							Gui.drawRect(0, 0, 0, 0, 0xff);
							Gui.drawRect(c.x - 6, c.y + 21 + count*16, c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25, c.y + 37 + count*16, 0x30000000);
							if(m.isToggled()) {
								Gui.drawRect(c.x - 6, c.y + 21 + count*16, c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25, c.y + 37 + count*16, 0x40000000);
							}
							if(s instanceof NumberSetting) {
								NumberSetting val = (NumberSetting) s;
								font1.Hud.drawString(s.name + ": " + val.getValue(), c.x, c.y + 23 + count*16, -1);
								
								if(c.maxLength < font1.Hud.getStringWidth(s.name + ": " + val.getValue())) {
									c.maxLength = font1.Hud.getStringWidth(s.name + ": " + val.getValue()); 
								}
								
								count++;
							}
							if(s instanceof BooleanSetting) {
								BooleanSetting val = (BooleanSetting) s;
								font1.Hud.drawString(s.name + ": " + (val.isEnabled() ? "On" : "Off"), c.x, c.y + 23 + count*16, -1);
								
								if(c.maxLength < font1.Hud.getStringWidth(s.name + ": " + (val.isEnabled() ? "On" : "Off"))) {
									c.maxLength = font1.Hud.getStringWidth(s.name + ": " + (val.isEnabled() ? "On" : "Off")); 
								}
								
								count++;
							}
							if(s instanceof ModeSetting) {
								ModeSetting val = (ModeSetting) s;
								font1.Hud.drawString(s.name + ": " + val.modes.get(val.index), c.x, c.y + 23 + count*16, -1);
								
								if(c.maxLength < font1.Hud.getStringWidth(s.name + ": " + val.modes.get(val.index))) {
									c.maxLength = font1.Hud.getStringWidth(s.name + ": " + val.modes.get(val.index)); 
								}
								
								count++;
							}
						}
						
						//Gui.drawRect(0, 0, 0, 0, 0xff);
						//Gui.drawRect(c.x - 6, c.y + 21, c.x + font1.clickguitest.getStringWidth("" + c.name) + c.maxLength - 25, c.y + 42, 0xff202020);
							
						
						//font1.targethudNameMoon.drawString(m.name, c.x, c.y + 25 + count*16, m.isEnabled() ? -1 : Color.LIGHT_GRAY.getRGB());
					}
				}
			}
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for(Category c : Module.Category.values()) {
			boolean hoveringOver = mouseY >= c.y && mouseY <= c.y + font1.clickguitest.FONT_HEIGHT && mouseX >= c.x && mouseX <= c.x + font1.clickguitest.getStringWidth("" + c.name);
			
			if(mouseButton == 0 && hoveringOver) {
				dragging = true;
			}
			if(mouseButton == 1 && hoveringOver) {
				c.isExpanded = !c.isExpanded;
				c.settingsExpand = false;
				for(Module m : ModuleManager.getModulesByCategory(c)) {
					m.clickguisettingsexpanded = false;
				}
			}
			
			int count = 0;
			
			if(c.isExpanded) {
				for(Module m : ModuleManager.getModulesByCategory(c)) {
					boolean hoveringOver2 = mouseY >= c.y + 21 + count*16 && mouseY <= c.y + 37 + count*16 && mouseX >= c.x - 6 && mouseX <= c.x + font1.targethudNameMoon.getStringWidth("" + c.name) + c.maxLength - 25;
					
					if(hoveringOver2 && mouseButton == 0) {
						m.toggle();
					}
					if(mouseButton == 1 && hoveringOver2) {
						c.maxLength = 0;
						m.clickguisettingsexpanded = !m.clickguisettingsexpanded;
					}
					
					int count3 = 0;
					
					if(m.clickguisettingsexpanded) {
						for(Setting s : m.settings) {
							if(s instanceof KeybindSetting)
								continue;
							
							if(s instanceof BooleanSetting) {
								if(hoveringOver2 && mouseButton == 0) {
									
								}
							}
							if(s instanceof NumberSetting) {
								
							}
							if(s instanceof ModeSetting) {
								
							}
							count3++;
							count++;
						}
					}
					
					count++;
				}
			}
			
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
	}
}
