package moonsense.hud.mod.impl;

import java.awt.Color;

import moonsense.hud.mod.HudMod;
import net.minecraft.client.gui.Gui;

public class FPSMod extends HudMod {

	public FPSMod() {
		super("FPS Mod", 5, 50);
	}
	
	@Override
	public void draw() {
		Gui.drawRect(getX() - 2, getY() - 2, getX() + fr.getStringWidth("§f[§bFPS§f: " + mc.getDebugFPS() + "§f]") + 1, getY() + getHeight(), new Color(0, 0, 0, 170).getRGB());
		fr.drawStringWithShadow("§f[§bFPS§f: " + mc.getDebugFPS() + "§f]", getX(), getY(), -1);
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		fr.drawStringWithShadow("§f[§bFPS§f: " + mc.getDebugFPS() + "§f]", getX(), getY(), -1);
		
		super.renderDummy(mouseX, mouseY);
	}
	
	@Override
	public int getWidth() {
		return fr.getStringWidth("§f[§6FPS§f: " + mc.getDebugFPS() + "§f]");
	}
	
	@Override
	public int getHeight() {
		return fr.FONT_HEIGHT;
	}

}
