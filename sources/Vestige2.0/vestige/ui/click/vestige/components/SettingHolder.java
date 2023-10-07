package vestige.ui.click.vestige.components;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import vestige.Vestige;
import vestige.api.setting.Setting;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.KeybindSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.impl.module.render.ClickGuiModule;
import vestige.ui.click.vestige.VestigeClickGUI;
import vestige.util.base.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;
import vestige.util.render.ColorUtil;

public class SettingHolder implements IMinecraft {
	
	private VestigeClickGUI parent;
	private Setting setting;
	private TimerUtil timer;
	
	private final double offsetY = 14;
	
	public SettingHolder(VestigeClickGUI parent, Setting setting, TimerUtil timer) {
		this.parent = parent;
		this.setting = setting;
		this.timer = timer;
	}
	
	public double drawScreen(double x, double y, int mouseX, int mouseY, float partialTicks, boolean holdingMouseButton) {
		if (!setting.isShown()) return 0;
		
		MinecraftFontRenderer fr = FontUtil.product_sans;
		
		Gui.drawRect(x + 2, y, x + 98, y + offsetY, new Color(50, 50, 50).getRGB());
		
		ClickGuiModule clickGui = (ClickGuiModule) Vestige.getInstance().getModuleManager().getModule(ClickGuiModule.class);
		
		int textColor = new Color(220, 220, 220).getRGB();
		
		if (setting instanceof BooleanSetting) {
			BooleanSetting boolSetting = (BooleanSetting) setting;
			
			int color = ColorUtil.getGradient(new Color((int) clickGui.red1.getCurrentValue(), (int) clickGui.green1.getCurrentValue(), (int) clickGui.blue1.getCurrentValue()), new Color((int) clickGui.red2.getCurrentValue(), (int) clickGui.green2.getCurrentValue(), (int) clickGui.blue2.getCurrentValue()), 0.5).getRGB();
			
			fr.drawStringWithShadow(setting.getDisplayName(), (float) x + 5, (float) y + 3, boolSetting.isEnabled() ? clickGui.rainbow.isEnabled() ? ColorUtil.getRainbow(3F, 0.75F, 0.85F, (int) (-x * 0.75)).getRGB() : color : textColor);
		} else if (setting instanceof ModeSetting) {
			ModeSetting modeSetting = (ModeSetting) setting;
			
			fr.drawStringWithShadow(modeSetting.getDisplayName() + " : " + modeSetting.getMode(), (float) x + 5, (float) y + 3, textColor);
		} else if (setting instanceof NumberSetting) {
			NumberSetting numberSetting = (NumberSetting) setting;
			
			double startX = x + 2;
			
			double length = 96;
			
			if (holdingMouseButton && mouseX >= x && mouseX <= x + 100 && mouseY > y && mouseY < y + offsetY) {
				
				double mousePos = mouseX - x - 2;
				double thing = (mousePos / length);
				
				numberSetting.setCurrentValue(thing * (numberSetting.getMax() - numberSetting.getMin()) + numberSetting.getMin());
			}
			
			double numberX = startX + ((numberSetting.getCurrentValue() - numberSetting.getMin()) * length / (numberSetting.getMax() - numberSetting.getMin()));
			
			Gui.drawRect(startX, y, numberX, y + offsetY, 0x75000000);
			
			if (numberSetting.shouldConvertToInteger()) {
				fr.drawStringWithShadow(numberSetting.getDisplayName() + " : " + (int) numberSetting.getCurrentValue(), (float) x + 5, (float) y + 3, textColor);
			} else {
				fr.drawStringWithShadow(numberSetting.getDisplayName() + " : " + numberSetting.getCurrentValue(), (float) x + 5, (float) y + 3, textColor);
			}
		} else if (setting instanceof KeybindSetting) {
			KeybindSetting keybind = (KeybindSetting) setting;
			
			fr.drawStringWithShadow(keybind.getDisplayName() + " : " + Keyboard.getKeyName(keybind.getKey()), (float) x + 5, (float) y + 3, textColor);
		}
		
		return offsetY;
	}
	
	public double mouseClicked(double x, double y, int mouseX, int mouseY, int button) {
		if (!setting.isShown()) return 0;
		
		if (setting instanceof BooleanSetting) {
			BooleanSetting boolSetting = (BooleanSetting) setting;
			
			if (mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + offsetY) {
				boolSetting.setEnabled(!boolSetting.isEnabled());
			}
		} else if (setting instanceof ModeSetting) {
			ModeSetting modeSetting = (ModeSetting) setting;
			
			if (mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + offsetY) {
				if (button == 0) {
					modeSetting.increment();
				} else if (button == 1) {
					modeSetting.decrement();
				}
			}
		} else if (setting instanceof KeybindSetting) {
			KeybindSetting keybind = (KeybindSetting) setting;
			
			if (mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + offsetY) {
				if (button == 0) {
					if (!keybind.isFocused()) {
						keybind.setFocused(true);
					}
				}
			}
		}
		return offsetY;
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		if (!setting.isShown()) return;
		
    	if (setting instanceof KeybindSetting) {
    		KeybindSetting keybind = (KeybindSetting) setting;
    		if (keybind.isFocused()) {
    			if (keyCode == Keyboard.KEY_BACK) {
    				keybind.setKey(0);
    			} else {
    				keybind.setKey(keyCode);
    			}
    			keybind.setFocused(false);
    		}
    	}
    }
	
}