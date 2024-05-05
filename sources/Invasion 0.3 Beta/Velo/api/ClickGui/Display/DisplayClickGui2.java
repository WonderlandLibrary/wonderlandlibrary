package Velo.api.ClickGui.Display;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Render.ScaledRes;
import Velo.api.Util.fontRenderer.Utils.DrawFontUtil;
import Velo.api.setting.Setting;
import Velo.impl.Event.EventRender;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.KeybindSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class DisplayClickGui2 extends GuiScreen {
	
	
	ArrayList<Module> modulesArray;
	ArrayList<Setting> modulesSetting;
	
	public boolean dragging = false;
	public boolean keybindFocused = false;
	
	public DisplayClickGui2() {
		modulesArray = new ArrayList<>(); 
		modulesSetting = new ArrayList<>(); 
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 220, ScaledRes.getScaledHeight()/2f + 140, ScaledRes.getScaledWidth()/2f - 285, ScaledRes.getScaledHeight()/2f - 140, 0xff3f3f3f);
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 220, ScaledRes.getScaledHeight()/2f + 140, ScaledRes.getScaledWidth()/2f - 180, ScaledRes.getScaledHeight()/2f - 140, 0xff1f1f1f);
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 220, ScaledRes.getScaledHeight()/2f + 140, ScaledRes.getScaledWidth()/2f - 60, ScaledRes.getScaledHeight()/2f - 140, 0xff3f3f3f);
		
		float count = 0;
		
		for(Category c : Module.Category.values()) {
			boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 135 && mouseY <= (ScaledRes.getScaledHeight()/2f + count*49 - 135) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 275 && mouseX <= ScaledRes.getScaledWidth()/2f - 275 + DrawFontUtil.renderer2.getStringWidth("" + c.name);
			
			DrawFontUtil.renderer2.drawString("" + c.name, ScaledRes.getScaledWidth()/2f - 275, ScaledRes.getScaledHeight()/2f + count*49 - 135, hoveringOver ? -1 : Color.GRAY.hashCode(), true);
			
			count+= 1;
		}
		
		if(!modulesArray.isEmpty()) {
			
			float count2 = 0;
			for(int i = 0; i < modulesArray.toArray().length; i++) {
				DrawFontUtil.renderer1.drawString("" + modulesArray.get(i).name + " ", ScaledRes.getScaledWidth()/2f - 170, ScaledRes.getScaledHeight()/2f + count2*13 - 135, modulesArray.get(i).isEnabled() ? -1 : Color.GRAY.hashCode(), true);
				
				count2+= 1.3;
			}
		}
		
		
		if(!modulesArray.isEmpty()) {
			
			float count3 = 0;
			for(int i = 0; i < modulesArray.toArray().length; i++) {
				DrawFontUtil.renderer3.drawString("                         ..." , ScaledRes.getScaledWidth()/2f - 170, ScaledRes.getScaledHeight()/2f + count3*13 - 135, modulesArray.get(i).isEnabled() ? -1 : 0xff888888, true);
				
				count3+= 1.3;
			}
		}
		
		if(!modulesSetting.isEmpty()) {
			int count3 = 0;
			for(int i = 0; i < modulesSetting.toArray().length; i++) {
				Setting setting = modulesSetting.get(i);
				if(setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;
					DrawFontUtil.renderer1.drawString(modulesSetting.get(i).name + ": " + mode.modes.get(mode.index), ScaledRes.getScaledWidth()/2f - 40, ScaledRes.getScaledHeight()/2f + count3*16 - 135, -1, true);
				}
				if(setting instanceof BooleanSetting) {
					BooleanSetting bool = (BooleanSetting) setting;
					DrawFontUtil.renderer1.drawString(modulesSetting.get(i).name, ScaledRes.getScaledWidth()/2f - 40, ScaledRes.getScaledHeight()/2f + count3*16 - 135, bool.isEnabled() ? -1 : Color.lightGray.hashCode(), true);
				}
				if(setting instanceof NumberSetting) {				
					NumberSetting number = (NumberSetting) setting;
					double numberbar = (number.getValue() - number.getMinimum())/(number.getMaximum() - number.getMinimum());
					
					boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 122 && mouseX >= ScaledRes.getScaledWidth()/2f - 40 && mouseX <= ScaledRes.getScaledWidth()/2f + 220;
					
					Gui.drawRect(ScaledRes.getScaledWidth()/2f - 40, ScaledRes.getScaledHeight()/2f + count3*16 - 135, ScaledRes.getScaledWidth()/2f + 220, ScaledRes.getScaledHeight()/2f + count3*16 - 122, 0x901f1f1f);
					Gui.drawRect(ScaledRes.getScaledWidth()/2f - 40, ScaledRes.getScaledHeight()/2f + count3*16 - 135, (float) (ScaledRes.getScaledWidth()/2f - 40 + (numberbar * 262)), ScaledRes.getScaledHeight()/2f + count3*16 - 122, 0xff0090ff);
					DrawFontUtil.renderer1.drawString(modulesSetting.get(i).name + ": " + number.getValue(), ScaledRes.getScaledWidth()/2f - 40, ScaledRes.getScaledHeight()/2f + count3*16 - 135, -1, true);
					
					if(isHoveringOver2) {
						if(dragging) {
							double diff = number.getMaximum() - number.getMinimum();
							double val = number.getMinimum() + (MathHelper.clamp_double((mouseX - ScaledRes.getScaledWidth()/2f + 39) / (ScaledRes.getScaledWidth()/2f - 219), 0, 1)) * diff;
							number.setValue(val);
						}
					}
				}
				count3++;
			}
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		int count = 0;
		
		for(Category c : Module.Category.values()) {
			boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count*49 - 135 + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 275 && mouseX <= ScaledRes.getScaledWidth()/2f - 275 + DrawFontUtil.renderer2.getStringWidth("" + c.name);
			
			if(hoveringOver) {
				modulesArray.clear();
				for(Module m : ModuleManager.getModulesByCategory(c)) {
					modulesArray.add(m);
				}
			}
			count++;
		}
		
		if(!modulesArray.isEmpty()) {
			float count2 = 0;
			for(int i = 0; i < modulesArray.toArray().length; i++) {
				boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count2*13 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count2*13 - 135 + DrawFontUtil.renderer1.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 170 && mouseX <= ScaledRes.getScaledWidth()/2f - 170 + 
				DrawFontUtil.renderer1.getStringWidth("" + modulesArray.get(i).name);
				
				if(isHoveringOver) {
					if(mouseButton == 0) {
						modulesArray.get(i).toggle();
					} else if(mouseButton == 1) {
						modulesSetting.clear();
						modulesSetting.addAll(modulesArray.get(i).settings);
					}
				}
				count2+= 1.3;
			}
		}
		
		if(!modulesSetting.isEmpty()) {
			int count3 = 0;
			for(int i = 0; i < modulesSetting.toArray().length; i++) {
				Setting setting = modulesSetting.get(i);
				if(setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;
					boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 135 + DrawFontUtil.renderer1.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 40 && mouseX <= ScaledRes.getScaledWidth()/2f - 40 +
					DrawFontUtil.renderer1.getStringWidth("" + modulesSetting.get(i).name + ": " + mode.modes.get(mode.index));
					
					if((isHoveringOver)) {
						mode.cycle();
					}
				}
				if(setting instanceof BooleanSetting) {
					BooleanSetting bool = (BooleanSetting) setting;
					boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 135 + DrawFontUtil.renderer1.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 40 && mouseX <= ScaledRes.getScaledWidth()/2f - 40 +
					DrawFontUtil.renderer1.getStringWidth("" + modulesSetting.get(i).name + ": " + (bool.isEnabled() ? "On" : "Off"));
					
					if(isHoveringOver) {
						bool.toggle();
					}
				}
				if(setting instanceof NumberSetting) {
					NumberSetting number = (NumberSetting) setting;
					boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 135 + DrawFontUtil.renderer1.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 40 && mouseX <= ScaledRes.getScaledWidth()/2f - 40 +
					DrawFontUtil.renderer1.getStringWidth("" + modulesSetting.get(i).name + ": " + number.getValue());
					
					double numberbar = (number.getValue() - number.getMinimum())/(number.getMaximum() - number.getMinimum());
					
					boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 135 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 122 && mouseX >= ScaledRes.getScaledWidth()/2f - 40 && mouseX <= ScaledRes.getScaledWidth()/2f + 220;
					
					if(isHoveringOver2) {
						if(mouseButton == 0) {
							dragging = true;
						}
					}
				}
				count3++;
			}
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
	}
}
