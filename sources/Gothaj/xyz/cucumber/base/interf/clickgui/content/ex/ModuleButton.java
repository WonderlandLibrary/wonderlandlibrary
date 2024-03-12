package xyz.cucumber.base.interf.clickgui.content.ex;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.BooleanButton;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.ColorButton;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.ModeButton;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.ModuleToggleButton;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.NumberButton;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.SettingsButton;
import xyz.cucumber.base.interf.clickgui.content.ex.impl.StringButton;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;

public class ModuleButton {
	
	private Mod module;
	
	private PositionUtils position;
	
	private List<SettingsButton> settings = new ArrayList();
	private boolean open = false;
	private double rollAnimation = getSettingsHeight();
	
	public ModuleButton(Mod module, PositionUtils position) {
		this.module = module;
		this.position = position;
		settings.add(new ModuleToggleButton(module, new PositionUtils(0,0,position.getWidth(),15,1)));
		for(ModuleSettings setting : module.getSettings()) {
			if(setting instanceof ModeSettings) settings.add(new ModeButton((ModeSettings) setting, new PositionUtils(0,0,position.getWidth(),15,1)));
			if(setting instanceof BooleanSettings) settings.add(new BooleanButton((BooleanSettings) setting, new PositionUtils(0,0,position.getWidth(),15,1)));
			if(setting instanceof StringSettings) settings.add(new StringButton((StringSettings) setting, new PositionUtils(0,0,position.getWidth(),15,1)));
			if(setting instanceof ColorSettings) settings.add(new ColorButton((ColorSettings) setting, new PositionUtils(0,0,position.getWidth(),15,1), 
					new PositionUtils(0,0,65,40,1),
					new PositionUtils(0,0,65,40,1),
					new PositionUtils(0,0,135,4,1),
					new PositionUtils(0,0,80,15,1)
					));
			if(setting instanceof NumberSettings) settings.add(new NumberButton((NumberSettings) setting, new PositionUtils(0,0,position.getWidth(),15,1)));
		}
	}
	
	public void draw(int mouseX, int mouseY) {
		rollAnimation = (rollAnimation * 12 + getSettingsHeight()) /13;
		position.setHeight(rollAnimation);
		RenderUtils.enableScisor();
		RenderUtils.scissor(new ScaledResolution(Minecraft.getMinecraft()),position.getX(), position.getY(), position.getWidth(), position.getHeight());
		RenderUtils.drawRoundedRectWithCorners(position.getX(), position.getY(), position.getX2(), position.getY2(), 0x90202020, 4, true, true, true, true);
		
		double h = 0;
		for(SettingsButton s : getVisibleSettings()) {
			if(s.getSettingMain() != null && !s.getSettingMain().getVisibility().get()) continue;
			s.getPosition().setX(position.getX());
			s.getPosition().setY(position.getY()+h);
			
			s.draw(mouseX, mouseY);
			h+= s.getPosition().getHeight();
		}
		RenderUtils.disableScisor();
		
	}
	
	public double getSettingsHeight() {
		double h = 0;
		for(SettingsButton s : settings) {
			if(!open && !(s instanceof ModuleToggleButton)) continue;
			if(s.getSettingMain() != null && !s.getSettingMain().getVisibility().get()) continue;
			
			
			h+= s.getPosition().getHeight();
		}
		return h;
	}
	public List<SettingsButton> getVisibleSettings() {
		List<SettingsButton> set = new ArrayList();
		for(SettingsButton s : settings) {
			if(!open && !(s instanceof ModuleToggleButton)) continue;
			if(s.getSettingMain() != null && !s.getSettingMain().getVisibility().get()) continue;
			if(position.getY() <= s.getPosition().getY2() || position.getY2() >= s.getPosition().getY()) {
				set.add(s);
			}
			
		}
		return set;
	}
	
	public void clicked(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY)) {
			
			for(SettingsButton s : getVisibleSettings()) {
				if(!open && !(s instanceof ModuleToggleButton)) continue;
				if(s instanceof ModuleToggleButton && s.getPosition().isInside(mouseX, mouseY) && button == 1) {
					this.open = !open;
				}
				if(s.getSettingMain() != null && !s.getSettingMain().getVisibility().get()) continue;
				s.click(mouseX, mouseY, button);
				
			}
		}
	}
	public void released(int mouseX, int mouseY, int button) {
		for(SettingsButton s : getVisibleSettings()) {
			if(!open && !(s instanceof ModuleToggleButton)) continue;
			if(s.getSettingMain() != null && !s.getSettingMain().getVisibility().get()) continue;
			s.release(mouseX, mouseY, button);
			
		}
	}
	public void key(char typedChar, int keyCode) {
		for(SettingsButton s : getVisibleSettings()) {
			if(!open && !(s instanceof ModuleToggleButton)) continue;
			if(s.getSettingMain() != null && !s.getSettingMain().getVisibility().get()) continue;
			s.key(typedChar, keyCode);
			
		}
	}

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	
}
