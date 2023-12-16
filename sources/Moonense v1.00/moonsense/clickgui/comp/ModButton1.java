package moonsense.clickgui.comp;

import java.awt.Color;

import moonsense.hud.mod.HudMod;
import moonsense.utils.RoundedUtils;
import moonsense.utils.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ModButton1 {
	
	public int x, y, w, h;
	public HudMod mod;
	
	public ModButton1(int x, int y, int w, int h, HudMod m) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.mod = m;
	}
	
	public void draw() {
		RoundedUtils.drawSmoothRoundedRect(x, y + 35, x + w, y + h + 9 + 35, 5, new Color(0, 0, 0, 170).getRGB());
		RoundedUtils.drawSmoothRoundedRect(x, y + 35, x + w, y + h + 9 + 35, 5, new Color(0, 0, 0, 170).getRGB());
		RoundedUtils.drawRoundedRect(x + 1, 78 + 35, x + w - 1, y + 17 + 35, 4, getColor());
		Minecraft.getMinecraft().fontRendererObj.drawString(mod.name, x + 2, y + 3 + 35, getColor());
	}
	
	private int getColor() {
		if(mod.isEnabled()) {
			return new Color (0, 255, 0, 255).getRGB();
		} else {
			return new Color (255, 0, 0, 255).getRGB();
		}

	}
	
	public void onClick(int mouseX, int mouseY, int button) {
		if(mouseX >= x && mouseX <= x + w && mouseY >= y + 35 && mouseY <= y + h + 35) {
			if(mod.isEnabled()) {
				mod.setEnabled(false);
			} else {
				mod.setEnabled(true);
				System.out.println(mod.name);
			}
		}

	}

}
