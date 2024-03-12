package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import java.math.BigDecimal;
import java.math.RoundingMode;

import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownClickGuiNumber extends DropdownClickGuiSettings{
	private NumberSettings setting;
	
	private boolean dragging;
	
	private PositionUtils slider = new PositionUtils(0,0,90, 3, 1);
	private PositionUtils collideCircle = new PositionUtils(0,0,6, 6, 1);
	
	public DropdownClickGuiNumber(ModuleSettings setting, PositionUtils modesPosition) {
		this.setting = (NumberSettings) setting;
		this.position = modesPosition;
		this.mainSetting =setting;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		
		double diff = Math.min(slider.getWidth(), Math.max(0, mouseX - (slider.getX())));
		if (dragging) {
			double newValue = roundToPlace(((diff / slider.getWidth()) * (setting.getMax() - setting.getMin()) + setting.getMin()),
					(int) slider.getWidth());
			setting.setValue((float) newValue);

		}
		
		Fonts.getFont("rb-m-13").drawString(setting.getName(), this.position.getX()+3, this.position.getY()+1, -1);
		Fonts.getFont("rb-m-13").drawString(setting.getValue()+"", this.position.getX2()-5-Fonts.getFont("rb-m-13").getWidth(setting.getValue()+""), this.position.getY()+1, 0xffaaaaaa);
		slider.setX(this.position.getX()+5);
		slider.setY(this.position.getY()+2+8);
		
		double x= (slider.getX()) + (slider.getWidth()) * (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
		RenderUtils.drawRoundedRect(slider.getX(), slider.getY(), slider.getX2(), slider.getY2(), 0xff000000, 1);
		RenderUtils.drawRoundedRect(slider.getX(), slider.getY(), x, slider.getY2(), 0xff8a66ff, 1);
		collideCircle.setX(x-collideCircle.getWidth()/2);
		collideCircle.setY(slider.getY()+slider.getHeight()/2-collideCircle.getHeight()/2);
		
		RenderUtils.drawCircle(x, slider.getY()+slider.getHeight()/2, collideCircle.getHeight()/2, 0xff8a66ff, 1);
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		if((slider.isInside(mouseX, mouseY) || collideCircle.isInside(mouseX, mouseY)) && button == 0) {
			dragging = true;
		}
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
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
