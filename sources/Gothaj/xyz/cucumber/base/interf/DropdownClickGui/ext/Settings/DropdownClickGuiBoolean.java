package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownClickGuiBoolean extends DropdownClickGuiSettings{
	
	private BooleanSettings settings;
	
	private PositionUtils checkMark = new PositionUtils(0,0,8,8,1);
	
	private double animation;
	
	public DropdownClickGuiBoolean(ModuleSettings settings, PositionUtils position) {
		this.settings = (BooleanSettings) settings;
		this.position = position;
		this.mainSetting =settings;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		this.checkMark.setX(this.position.getX2()-12);
		this.checkMark.setY(this.position.getY()+this.position.getHeight()/2-this.checkMark.getHeight()/2);
		
		Fonts.getFont("rb-m-13").drawString(settings.getName(), this.position.getX()+3, this.position.getY()+3, -1);
		
		RenderUtils.drawRoundedRectWithCorners(checkMark.getX(), checkMark.getY(), checkMark.getX2(),checkMark.getY2(), 0x30000000, 4, true,true,true,true);
		RenderUtils.drawRoundedRectWithCorners(checkMark.getX()+checkMark.getWidth()/2-animation, checkMark.getY()+checkMark.getHeight()/2-animation, checkMark.getX()+checkMark.getWidth()/2+animation, checkMark.getY()+checkMark.getHeight()/2+animation, 0xff343a40, 4/(checkMark.getWidth()/2)*animation, true,true,true,true);
		if(settings.isEnabled()) {
			this.animation = (animation * 9 + checkMark.getWidth()/2)/10;
			RenderUtils.drawImage(checkMark.getX()+1, checkMark.getY()+1, 6, 6, new ResourceLocation("client/images/check.png"), -1);
		}else this.animation = (animation * 9)/10;
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		if(checkMark.isInside(mouseX, mouseY) && button == 0) {
			settings.setEnabled(!settings.isEnabled());
		}
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {

	}

	@Override
	public void onKey(char chr, int key) {

	}
	
	
	
}
