package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BloomUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class BooleanButton extends SettingsButton{
	
	private BooleanSettings setting;
	
	private PositionUtils checkBox;
	
	private double animation;
	
	private BloomUtils bloom = new BloomUtils();

	public BooleanButton(BooleanSettings setting, PositionUtils checkBox) {
		this.setting = setting;
		this.settingMain =setting;
		this.position = checkBox;
		this.checkBox = new PositionUtils(0,0,10,10,1);
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		if(setting.isEnabled()) {
			animation = (animation * 15 + 5)/16;
		}else {
			animation = (animation * 15)/16;
		}
		
		Fonts.getFont("rb-r").drawString(setting.getName(), position.getX()+8, position.getY()+3, -1);
		
		this.checkBox.setX(position.getX2()-55);
		this.checkBox.setY(position.getY()+2.5);
		

		RenderUtils.drawRoundedRect(this.checkBox.getX(), this.checkBox.getY(), this.checkBox.getX2(), this.checkBox.getY2(),0xff131417, 2);
		
		RenderUtils.drawRoundedRect(this.checkBox.getX()+this.checkBox.getWidth()/2-animation, this.checkBox.getY()+this.checkBox.getHeight()/2-animation, this.checkBox.getX()+this.checkBox.getWidth()/2+animation, this.checkBox.getY()+this.checkBox.getHeight()/2+animation, 0xff4269f5,2);
		if(setting.isEnabled())
			RenderUtils.drawImage(this.checkBox.getX()+this.checkBox.getWidth()/2-3, this.checkBox.getY()+this.checkBox.getHeight()/2-3+0.5, 6, 6, new ResourceLocation("client/images/check.png"), -1);
	}

	@Override
	public void click(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY)) {
			if(checkBox.isInside(mouseX, mouseY) && button == 0) {
				setting.setEnabled(!setting.isEnabled());
			}
		}
	}

	public BooleanSettings getSetting() {
		return setting;
	}

	public void setSetting(BooleanSettings setting) {
		this.setting = setting;
	}

	public PositionUtils getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(PositionUtils checkBox) {
		this.checkBox = checkBox;
	}
	
	
	
}
