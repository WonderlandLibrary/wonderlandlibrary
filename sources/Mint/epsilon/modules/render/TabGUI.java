package epsilon.modules.render;

import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Color;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventKey;
import epsilon.events.listeners.EventRenderGUI;
import epsilon.modules.Module;
import epsilon.settings.Setting;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.KeybindSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.ColorUtil;
import epsilon.util.ShapeUtils;
import epsilon.util.Font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class TabGUI extends Module{
	
	public int currentTab;
	public boolean expanded;
	
	
	
	
	public TabGUI(){
		super("TabGUI", Keyboard.KEY_NONE, Category.RENDER, "Allows you to change settings and binds of a module with arrow and enter keys, without opening ClickGUI(Coming soonTM)");
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventRenderGUI) {
			
			FontRenderer fr = mc.fontRendererObj;
			
			int primaryColor = 0xff00fa6e,
			secondaryColor = 0xff00a649;
			//Gui.drawRect(5, 30.5, 70, 30 + Module.Category.values().length * 16 + 1.5, 0x90000000);
			ShapeUtils shapeUtils = new ShapeUtils();
			shapeUtils.roundedRect(8, 32, 63, 31.9f * (Category.values().length - 3) + 1, 8, new Color(0, 0, 0, 140));
			shapeUtils.roundedRect(8, 32 + currentTab * 16, 3, 6  + 10,2, new Color(0, 250, 110, 250));

			int count = 0;
			for(Category c : Module.Category.values()) {
				mc.fontRendererObj.drawString(c.name, 15, 35 + count*16, -1);
				
				count++;
			}
			
			if(expanded){
				Category category = Module.Category.values()[currentTab];
				List<Module> modules = Epsilon.getModulesbyCategory(category);
				
				
				if(modules.size() == 0)
					return;
				
				//Gui.drawRect(70, 30.5, 70 + 68, 30 + modules.size() * 16 + 1.5, 0x90000000);
				shapeUtils.roundedRect(71, 32, 65,  modules.size() * 16 + 1.5, 8, new Color(0, 0, 0, 140));
				shapeUtils.roundedRect(71, 32 + category.moduleIndex * 16, 65, 15 + 2.5f, 6, new Color(0, 250, 110, 250));
				
				//Gui.drawRect(70, 30.5f + category.moduleIndex * 16, 7 + 61 + 70, 33 + category.moduleIndex * 16 + 12 + 2.5f, primaryColor);
				
				count = 0;

				
				for(Module m : modules) {
					
					
					mc.fontRendererObj.drawString(m.name, 77, 35 + count*16, -1);
					
					if(count == category.moduleIndex && m.expanded) {
						
						int index = 0, maxLength = 0;
						for(Setting setting : m.settings) {
							if(setting instanceof BooleanSetting) {
								BooleanSetting bool = (BooleanSetting) setting;
								if(maxLength < fr.getStringWidth(setting.name + ": " + (bool.enabled ? "Enabled" : "Disabled"))) {
									maxLength = fr.getStringWidth(setting.name + ": " + (bool.enabled ? "Enabled" : "Disabled"));
								}
								Fonts.Segation18.drawString(setting.name + ": " + (bool.enabled ? "Enabled" : "Disabled"), 73 + 68, 35 + index*16, -1);
							}
							
							if(setting instanceof NumberSetting) {
								NumberSetting number = (NumberSetting) setting;
								if(maxLength < fr.getStringWidth(setting.name + ": " + number.getValue())) {
									maxLength = fr.getStringWidth(setting.name + ": " + number.getValue());
								}
							}
							
							if(setting instanceof ModeSetting) {
								ModeSetting mode = (ModeSetting) setting;
								if(maxLength < fr.getStringWidth(setting.name + ": " + mode.getMode())) {
									maxLength = fr.getStringWidth(setting.name + ": " + mode.getMode());
								}
							}	
							
							if(setting instanceof KeybindSetting) {
								KeybindSetting keyBind = (KeybindSetting) setting;
								if(maxLength < fr.getStringWidth(setting.name + ": " + Keyboard.getKeyName(keyBind.code))) {
									maxLength = fr.getStringWidth(setting.name + ": " + Keyboard.getKeyName(keyBind.code));
								}
							}	
							
							Fonts.Segation18.drawString(setting.name, 73 + 68, 35 + index*16, -1);
							index++;
						}
						
						if(!m.settings.isEmpty()) {
							//Gui.drawRect(70 + 68, 30.5, 70 + 68 + maxLength + 8, 30 + m.settings.size() * 16 + 1.5, 0x90000000);
							shapeUtils.roundedRect(70 + 66, 32,   maxLength + 8, m.settings.size() * 15.8, 4, new Color(0,0,0,140));
							Gui.drawRect(70 +66, 32 + m.index * 16, 7 + 61 + maxLength + 8 + 70, 33 + m.index * 16 + 12 + 2.5f, m.settings.get(m.index).focused ? secondaryColor : primaryColor);
							
							index = 0;
							for(Setting setting : m.settings) {
								if(setting instanceof BooleanSetting) {
									BooleanSetting bool = (BooleanSetting) setting;
									Fonts.Segation18.drawString(setting.name + ": " + (bool.enabled ? "Enabled" : "Disabled"), 73 + 68, 35 + index*16, -1);
								}
								
								if(setting instanceof NumberSetting) {
									NumberSetting number = (NumberSetting) setting;
									Fonts.Segation18.drawString(setting.name + ": " + number.getValue(), 73 + 68, 35 + index*16, -1);
								}
								
								if(setting instanceof ModeSetting) {
									ModeSetting mode = (ModeSetting) setting;
									Fonts.Segation18.drawString(setting.name + ": " + mode.getMode(), 73 + 68, 35 + index*16, -1);
								}	
								
								if(setting instanceof KeybindSetting) {
									KeybindSetting keyBind = (KeybindSetting) setting;
									Fonts.Segation18.drawString(setting.name + ": " + Keyboard.getKeyName(keyBind.code), 73 + 68, 35 + index*16, -1);
								}
								
								Fonts.Segation18.drawString(setting.name, 73 + 68, 35 + index*16, -1);
								index++;
							}	
						}
					}
					
					count++;
				}
				
			}
		}
		if (e instanceof EventKey) {
			int code = ((EventKey)e).code;
			
			Category category = Module.Category.values()[currentTab];
			List<Module> modules = Epsilon.getModulesbyCategory(category);
			
			if(expanded && !modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
				Module module = modules.get(category.moduleIndex);
				
				if(!module.settings.isEmpty() && module.settings.get(module.index).focused && module.settings.get(module.index) instanceof KeybindSetting) {
					if(code != Keyboard.KEY_RETURN && code != Keyboard.KEY_UP && code != Keyboard.KEY_DOWN && code != Keyboard.KEY_LEFT && code != Keyboard.KEY_RIGHT && code != Keyboard.KEY_ESCAPE) {
						KeybindSetting keyBind = (KeybindSetting)module.settings.get(module.index);
						
						keyBind.code = code;
						keyBind.focused = false;
						
						return;
					}
				}
				
			}
			
			if(code == Keyboard.KEY_UP) {
				if(expanded) {
					if(expanded && !modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
						Module module = modules.get(category.moduleIndex);
						
						if(!module.settings.isEmpty()) {
							if(module.settings.get(module.index).focused) {
								Setting setting = module.settings.get(module.index);
								
								if(setting instanceof NumberSetting) {
									((NumberSetting)setting).increment(true);
								}
							}else {
								if(module.index <= 0) {
									module.index = module.settings.size() - 1;
								}else
									module.index--;
							}
						}
					}else {	
						if(category.moduleIndex <= 0) {
							category.moduleIndex = modules.size() - 1;
						}else
							category.moduleIndex--;
					}
				}else {
					if(currentTab <= 0) {
						currentTab = Module.Category.values().length - 1;
					}else
						currentTab--;
				}	
			}
			
			if(code == Keyboard.KEY_DOWN) {
				if(expanded) {
					if(expanded && !modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
						Module module = modules.get(category.moduleIndex);
						
						if(!module.settings.isEmpty()) {
							if(module.settings.get(module.index).focused) {
								Setting setting = module.settings.get(module.index);
								
								if(setting instanceof NumberSetting) {
									((NumberSetting)setting).increment(false);
								}
							}else {
								if(module.index >= module.settings.size() - 1) {
									module.index = 0;
								}else
									module.index++;
							}
						}
					}else {
						if(category.moduleIndex >= modules.size() - 1) {
							category.moduleIndex = 0;
						}else
							category.moduleIndex++;
					}
					
				}else {
					if(currentTab >= Module.Category.values().length - 1) {
						currentTab = 0;
					}else
						currentTab++;
				}	
			}
			
			if(code == Keyboard.KEY_RETURN) {
				if(expanded && modules.size() != 0) {
					Module module = modules.get(category.moduleIndex);
					
					if(!module.expanded && !module.settings.isEmpty())
						module.expanded = true;
					else if(module.expanded && !module.settings.isEmpty()) {
						module.settings.get(module.index).focused = !module.settings.get(module.index).focused;
					}
				}
			}
			
			
			if(code == Keyboard.KEY_RIGHT) {
				if(expanded && modules.size() != 0) {
					Module module = modules.get(category.moduleIndex);
					
					if(!module.settings.isEmpty()) {
						if(expanded && !modules.isEmpty() && module.expanded) {
							Setting setting = module.settings.get(module.index);
							
							if(setting instanceof BooleanSetting) {
								((BooleanSetting)setting).toggle();
							}
							if(setting instanceof ModeSetting) {
								((ModeSetting)setting).cycle(true);
							}
						}
					}else {
						if(!module.name.equals("TabGUI"))
							module.toggle();
					}
					
				}else {
					expanded = true;
				}
			}
			
			if(code == Keyboard.KEY_LEFT) {
				if(expanded && !modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
					Module module = modules.get(category.moduleIndex);
					
					if(!module.settings.isEmpty()) {
						if(module.settings.get(module.index).focused) {
							
						}else {
							modules.get(category.moduleIndex).expanded = false;
						}
					}
				}else
					expanded = false;
			}
		}
	}
}