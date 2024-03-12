package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownClickGuiMode extends DropdownClickGuiSettings{

	private ModeSettings settings;

	public DropdownClickGuiMode(ModuleSettings settings, PositionUtils position) {
		this.settings = (ModeSettings) settings;
		this.position = position;
		this.mainSetting =settings;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		Fonts.getFont("rb-m-13").drawString(settings.getName(), this.position.getX()+3, this.position.getY()+4, -1);
		
		RenderUtils.drawRoundedRectWithCorners(this.position.getX()+Fonts.getFont("rb-m-13").getWidth(settings.getName())+5-1, this.position.getY()+this.position.getHeight()/2-4,  this.position.getX()+Fonts.getFont("rb-m-13").getWidth(settings.getName())+6+Fonts.getFont("rb-m-13").getWidth(settings.getMode()),this.position.getY()+this.position.getHeight()/2+4, 0xff343a40, 2, true,true,true,true);
		Fonts.getFont("rb-m-13").drawString(settings.getMode(), this.position.getX()+Fonts.getFont("rb-m-13").getWidth(settings.getName())+5.5, this.position.getY()+4, -1);
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		settings.cycleModes();
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		
	}

	@Override
	public void onKey(char chr, int key) {
		
	}
	
	
}
