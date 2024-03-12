package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class NumberButton extends SettingsButton{

	private NumberSettings setting;
	
	private boolean dragging;
	
	private PositionUtils slider;
	
	public NumberButton(NumberSettings setting, PositionUtils modesPosition) {
		this.setting = setting;
		this.position = modesPosition;
		this.settingMain =setting;
		
		slider = new PositionUtils(0,0,120, 10, 1);
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		Fonts.getFont("rb-r").drawString(setting.getName(), (float) (this.position.getX() + 8), (float) (this.position.getY() + 3), -1);
		
		slider.setX(this.position.getX2()-10-slider.getWidth());
		slider.setY(this.position.getY()+2.5);
		double diff = Math.min(slider.getWidth(), Math.max(0, mouseX - (slider.getX())));
		if (dragging) {
			double newValue = roundToPlace(((diff / slider.getWidth()) * (setting.getMax() - setting.getMin()) + setting.getMin()),
					(int) slider.getWidth());
			setting.setValue((float) newValue);

		}
		
		RenderUtils.drawRect(slider.getX(), slider.getY(), slider.getX2(), slider.getY2(), 0x30000000);
		
		RenderUtils.drawOutlinedRect(slider.getX(), slider.getY(), slider.getX2(), slider.getY2(), 0x904269f5, 1);
		
		double x= (slider.getX()+2) + (slider.getWidth()-4) * (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
		
		RenderUtils.drawRect(x-2, slider.getY(), x+2, slider.getY2(),  0x404269f5);
		
		Fonts.getFont("rb-r").drawString(setting.getValue()+"", (float)(slider.getX()+slider.getWidth()/2-Fonts.getFont("rb-r").getWidth(setting.getValue()+"")/2), (float)slider.getY()+2.5F, 0xff4269f5);
		
	}

	@Override
	public void click(int mouseX, int mouseY, int button) {
		if(this.position.isInside(mouseX, mouseY) && slider.isInside(mouseX, mouseY) && button == 0) {
			dragging = true;
		}
	}

	@Override
	public void release(int mouseX, int mouseY, int button) {
		dragging = false;
	}
	
	private double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
 
}
