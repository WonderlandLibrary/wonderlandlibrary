package moonsense.hud.mod.impl;

import java.awt.Color;

import moonsense.hud.mod.HudMod;
import net.minecraft.client.gui.Gui;

public class TestMod extends HudMod {

	public TestMod() {
		super("TestMod", 5, 5);
	}
	
	@Override
	public void draw() {
		Gui.drawRect(getX() - 2, getY() - 2, getX() + getWidth() + 2, getY() + getHeight(), new Color(0, 0, 0, 170).getRGB());
		fr.drawStringWithShadow(name, getX(), getY(), -1);
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		fr.drawStringWithShadow(name, getX(), getY(), -1);
		
		super.renderDummy(mouseX, mouseY);
	}
	
	@Override
	public int getWidth() {
		return fr.getStringWidth(name);
	}
	
	@Override
	public int getHeight() {
		return fr.FONT_HEIGHT;
	}

}
