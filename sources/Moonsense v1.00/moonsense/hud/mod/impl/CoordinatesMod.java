package moonsense.hud.mod.impl;

import java.awt.Color;

import moonsense.hud.mod.HudMod;
import net.minecraft.client.gui.Gui;

public class CoordinatesMod extends HudMod {
	
	public CoordinatesMod() {
		super("Coordinates", 5, 20);
	}
	
	@Override
	public void draw() {
		Gui.drawRect(getX() - 2, getY() - 2, getX() + fr.getStringWidth("§f[§bCoordinates§f: " + Math.round(mc.thePlayer.posX * 1000.0) / 1000L + " / " + Math.round(mc.thePlayer.posY * 1000.0) / 1000L + " / " + Math.round(mc.thePlayer.posZ * 1000.0) / 1000L + "§f]") + 1, getY() + getHeight(), new Color(0, 0, 0, 170).getRGB());
		fr.drawStringWithShadow("§f[§bCoordinates§f: " + Math.round(mc.thePlayer.posX * 1000.0) / 1000L + " / " + Math.round(mc.thePlayer.posY * 1000.0) / 1000L + " / " + Math.round(mc.thePlayer.posZ * 1000.0) / 1000L + "§f]", getX(), getY(), -1);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		fr.drawStringWithShadow("§f[§bCoordinates§f: " + Math.round(mc.thePlayer.posX * 1000.0) / 1000L + " " + Math.round(mc.thePlayer.posY * 1000.0) / 1000L + " " + Math.round(mc.thePlayer.posZ * 1000.0) / 1000L + "§f]", getX(), getY(), -1);
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
