package xyz.cucumber.base.interf.DropdownClickGui.ext;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiBoolean;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiColor;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiKeybind;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiMode;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiNumber;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiSettings;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiString;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownModule implements DropdownButton{
	private Mod module;
	
	private double lastX,lastY;
	
	public PositionUtils position = new PositionUtils(0,0,100,15,1);
	private boolean open;
	private double defaultHeight = 15;
	
	private double Coloranimation, openAnimationArrow,openAnimation;
	
	
	private ArrayList<DropdownClickGuiSettings> settings = new ArrayList();
	
	public DropdownModule(Mod module) {
		this.module = module;
		settings.add(new DropdownClickGuiKeybind(module, new PositionUtils(0,0,100,12,1)));
		for(ModuleSettings s : module.getSettings()) {
			if(s instanceof ModeSettings) {
				settings.add(new DropdownClickGuiMode(s, new PositionUtils(0,0,100,12,1)));
			}
			if(s instanceof BooleanSettings) {
				settings.add(new DropdownClickGuiBoolean(s, new PositionUtils(0,0,100,12,1)));
			}
			if(s instanceof StringSettings) {
				settings.add(new DropdownClickGuiString(s, new PositionUtils(0,0,100,12,1)));
			}
			if(s instanceof ColorSettings) {
				settings.add(new DropdownClickGuiColor(s, new PositionUtils(0,0,100,12,1)));
			}
			if(s instanceof NumberSettings) {
				settings.add(new DropdownClickGuiNumber(s, new PositionUtils(0,0,100,15,1)));
			}
		}
	}

	public Mod getModule() {
		return module;
	}

	public void setModule(Mod module) {
		this.module = module;
	}

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		int color = 0xee4734ed;
		if(module.isEnabled()) {
			Coloranimation = (Coloranimation * 10 +100)/11;
		}else {
			Coloranimation = (Coloranimation < 10 ? 0 : Coloranimation-10);
		}
		RenderUtils.drawRoundedRectWithCorners(position.getX(), position.getY(), position.getX2(), position.getY2(),0xff131313, 4f, true,true,true,true);
		color = ColorUtils.getAlphaColor(0xff6143cc, (int) Coloranimation);
		
		Fonts.getFont("rb-m").drawString(module.getName(),position.getX()+3, position.getY()+5, ColorUtils.mix(0xffaaaaaa, color, Coloranimation, 100));
		
		renderArrow(position.getX2()-7, position.getY()+7.5, 4, 0xffaaaaaa,openAnimationArrow);
		this.position.setHeight(defaultHeight);
		if(open) {
			openAnimationArrow =  (openAnimationArrow * 10 + 180)/11;
			this.position.setHeight(defaultHeight+getSettingsHeight());
			double s = 0;
			for(DropdownClickGuiSettings set : settings) {
				if(set.getMainSetting() != null && !set.getMainSetting().getVisibility().get()) continue;
				set.getPosition().setX(this.position.getX());
				set.getPosition().setY(this.position.getY()+defaultHeight+s);
				set.draw(mouseX, mouseY);
				s+=set.getPosition().getHeight();
			}
		}else {
			openAnimationArrow =  (openAnimationArrow * 10)/11;
		}
		
	}
	
	public double getSettingsHeight() {
		double s = 0;
		for(DropdownClickGuiSettings set : settings) {
			if(set.getMainSetting() != null && !set.getMainSetting().getVisibility().get()) continue;
			s+=set.getPosition().getHeight();
		}
		return s;
	}
	
	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY)) {
			for(DropdownClickGuiSettings set : settings) {
				if(set.getMainSetting() != null && !set.getMainSetting().getVisibility().get()) continue;
				if(set.getPosition().isInside(mouseX, mouseY)) {
					set.onClick(mouseX, mouseY, button);
					return;
				}
			}
			if(button == 0) {
				module.toggle();
			}
			if(button == 1) {
				open = !open;
			}
		}
		
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		for(DropdownClickGuiSettings set : settings) {
			if(set.getMainSetting() != null && !set.getMainSetting().getVisibility().get()) continue;
			set.onRelease(mouseX, mouseY, button);
		}
	}

	@Override
	public void onKey(char chr, int key) {
		for(DropdownClickGuiSettings set : settings) {
			if(set.getMainSetting() != null && !set.getMainSetting().getVisibility().get()) continue;
			set.onKey(chr, key);
		}
	}
	public void renderArrow(double x, double y, double size, int color, double angle) {
		GL11.glPushMatrix();
		
		GL11.glTranslated(x, y, 0.0f);
		GL11.glRotated(angle, 0.0f, 0.0f, 1.0f);
		GL11.glTranslated(-x, -y, 0.0f);
		
		RenderUtils.start2D();
		RenderUtils.color(color);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x-size/3, y-size/3);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x+size/3, y-size/3);
			GL11.glVertex2d(x, y+size/3);
			
		}
		GL11.glEnd();
		RenderUtils.stop2D();
		
		GL11.glRotated(0, 0.0f, 0.0f, 1.0f);
		
		GL11.glPopMatrix();
	}
}
