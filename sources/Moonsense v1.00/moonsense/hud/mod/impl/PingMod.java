package moonsense.hud.mod.impl;

import java.awt.Color;

import moonsense.hud.mod.HudMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMultiplayer;

public class PingMod extends HudMod {

	public PingMod() {
		super("Display Ping", 5, 35);
	}
	
	@Override
	public void draw() {
		if(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null) {
			Gui.drawRect(getX() - 2, getY() - 2, getX() + fr.getStringWidth("§f[§bPing§f: " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "§f]") + 1, getY() + getHeight(), new Color(0, 0, 0, 170).getRGB());
			fr.drawStringWithShadow("§f[§bPing§f: " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "§f]", getX(), getY(), -1);
		} else {
			Gui.drawRect(getX() - 2, getY() - 2, getX() + fr.getStringWidth("§f[§bPing§f: " + "Ping Not Found" + "§f]"), getY() + getHeight(), new Color(0, 0, 0, 170).getRGB());
			fr.drawStringWithShadow("§f[§bPing§f: " + "Ping Not Found" + "§f]", getX(), getY(), -1);
		}
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		fr.drawStringWithShadow("§f[§bPing§f: " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "§f]", getX(), getY(), -1);
		
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
