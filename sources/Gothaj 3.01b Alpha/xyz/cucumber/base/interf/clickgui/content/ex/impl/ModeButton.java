package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class ModeButton extends SettingsButton{
	
	private ModeSettings setting;
	
	private PositionUtils modesPosition;
	
	private double defaultHeight;
	
	private List<mode> modes;
	
	private boolean open;
	
	private double rotateAnimation, rollAnimation;
	
	
	public ModeButton(ModeSettings setting, PositionUtils modesPosition) {
		this.settingMain =setting;
		this.setting = setting;
		this.position = modesPosition;
		defaultHeight = this.position.getHeight();
		modes = new ArrayList();
		for(String s : setting.getModes()) {
			modes.add(new mode(s, new PositionUtils(0,0,80,12,1)));
		}
		this.modesPosition = new PositionUtils(0,0,80,12,1);
	}


	@Override
	public void draw(int mouseX, int mouseY) {
		
		rotateAnimation = (rotateAnimation*12)/13;
		
		Fonts.getFont("rb-r").drawString(setting.getName(), this.position.getX()+8, this.position.getY()+4, -1);
		
		modesPosition.setX(this.position.getX2()-modesPosition.getWidth()-10);
		modesPosition.setY(this.position.getY()+1);
		
		RenderUtils.drawRect(modesPosition.getX(), modesPosition.getY(), modesPosition.getX2(), modesPosition.getY2(), 0x30000000);
		RenderUtils.drawArrowForClickGui(modesPosition.getX2()-4, modesPosition.getY()+modesPosition.getHeight()/2,3, 0xff4269f5, rotateAnimation);
		Fonts.getFont("rb-r").drawString(setting.getMode(), modesPosition.getX()+modesPosition.getWidth()/2-Fonts.getFont("rb-r").getWidth(setting.getMode())/2, modesPosition.getY()+modesPosition.getHeight()/2-Fonts.getFont("rb-r").getHeight(setting.getMode())/2,  0xff4269f5);
		
		if(open) {
			rollAnimation = getModesHeight();
			double p = 0;
			for(mode m : modes) {
				if(m.mode.toLowerCase().equals(this.setting.getMode().toLowerCase())) {
					m.pos.setX(-200);
					m.pos.setY(-200);
					continue;
				}
				m.pos.setX(modesPosition.getX());
				m.pos.setY(modesPosition.getY2()+p);
				
				m.draw(mouseX, mouseY);
				p+= m.pos.getHeight();
			}
			this.position.setHeight(this.defaultHeight+rollAnimation);
		}else {
			rollAnimation = (rollAnimation * 11+this.defaultHeight) /12;
			rotateAnimation = (rotateAnimation*12+180)/13;
			this.position.setHeight(rollAnimation);
		}
		RenderUtils.drawOutlinedRect(modesPosition.getX(), modesPosition.getY(), modesPosition.getX2(), modesPosition.getY2(), 0xff4269f5,1f);
	}
	
	public double getModesHeight() {
		double p = 0;
		for(mode m : modes) {
			if(m.mode.toLowerCase().equals(this.setting.getMode().toLowerCase())) continue;
			p+= m.pos.getHeight();
		}
		return p;
	}

	@Override
	public void click(int mouseX, int mouseY, int button) {
		if(this.position.isInside(mouseX, mouseY)) {
			if(button == 0) {
				if(open) {
					for(mode m : modes) {
						m.click(mouseX, mouseY, setting, button);
					}
				}
			} else if(button == 1) {
				if(modesPosition.isInside(mouseX, mouseY))
					open = !open;
			}
		}
	}


	public class mode{
		
		public String mode;
		
		public PositionUtils pos;

		public mode(String mode, PositionUtils pos) {
			this.mode = mode;
			this.pos = pos;
		}
		public void draw(int mouseX, int mouseY) {
			RenderUtils.drawRect(pos.getX(), pos.getY(), pos.getX2(), pos.getY2(), 0x30000000);
			Fonts.getFont("rb-r").drawString(mode, pos.getX()+pos.getWidth()/2-Fonts.getFont("rb-r").getWidth(mode)/2, pos.getY()+pos.getHeight()/2-Fonts.getFont("rb-r").getHeight(mode)/2,  -1);
		}
		public void click(int mouseX, int mouseY,ModeSettings setting, int button) {
			if(pos.isInside(mouseX, mouseY) && button == 0) {
				setting.setMode(mode);
			}
		}
		
	}
}
