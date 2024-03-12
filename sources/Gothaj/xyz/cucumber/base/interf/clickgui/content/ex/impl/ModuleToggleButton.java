package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class ModuleToggleButton extends SettingsButton{
	private Mod module;
	
	private PositionUtils toggleButton;
	
	private double toggleAnimation;
	
	public ModuleToggleButton(Mod mod, PositionUtils position) {
		module = mod;
		this.position = position;
		
		toggleButton = new PositionUtils(0,0, 20, 10,1);
	}
	

	@Override
	public void draw(int mouseX, int mouseY) {
		int color =  -1;
		if(module.isEnabled()) {
			toggleAnimation = (toggleAnimation*9 + toggleButton.getWidth()-10)/10;
			
		}else {
			toggleAnimation = (toggleAnimation*9)/10;
		}
		color = ColorUtils.mix(-1, 0xff4269f5, toggleAnimation, toggleButton.getWidth()-10);
		
		RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), 0xff1a1a1a, 1);
		Fonts.getFont("rb-r").drawString(module.getName(), position.getX()+5, position.getY()+5, color);
		
		Fonts.getFont("rb-r").drawString(module.getDescription(), position.getX()+position.getWidth()/2-Fonts.getFont("rb-r").getWidth(module.getDescription())/2, position.getY()+4, 0xffaaaaaa);
		
		toggleButton.setX(position.getX2()-toggleButton.getWidth()-2.5);
		toggleButton.setY(position.getY()+2.5);
		
		RenderUtils.drawRoundedRect(toggleButton.getX(), toggleButton.getY(), toggleButton.getX2(), toggleButton.getY2(), ColorUtils.mix(0xff333333, 0xff4269f5, toggleAnimation, toggleButton.getWidth()-10), 4f);
		
		RenderUtils.drawCircle(toggleButton.getX()+4+1+toggleAnimation, toggleButton.getY()+toggleButton.getHeight()/2, 4, 0xffffffff, 1);
	}

	@Override
	public void click(int mouseX, int mouseY, int button) {
		if(toggleButton.isInside(mouseX, mouseY) && button == 0) {
			module.toggle();
		}
	}
}
