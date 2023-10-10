package de.Hero.clickgui.elements.menu;

import java.awt.Color;


import net.minecraft.client.gui.Gui;
import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ElementCheckBox extends Element {
	/*
	 * Konstrukor
	 */
	public ElementCheckBox(ModuleButton iparent, Setting iset) {
		parent = iparent;
		set = iset;
		super.setup();
	}

	/*
	 * Rendern des Elements 
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Color temp = ColorUtil.getClickGUIColor();
		int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
		
		/*
		 * Die Box und Umrandung rendern
		 */
		Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), new Color(0, 0, 0, 170).getRGB());

		/*
		 * Titel und Checkbox rendern.
		 */
		FontUtil.drawString(setstrg, x + width - FontUtil.getStringWidth(setstrg) - 4, y + FontUtil.getFontHeight() / 2f - 2, 0xffffffff);
		Gui.drawRect((int) (x + 1), (int) (y + 2), (int) (x + 12), (int) (y + 13), set.getValBoolean() ? new Color(200, 140, 244, 255).getRGB() : 0xff000000);
		if (isCheckHovered(mouseX, mouseY))
			Gui.drawRect((int) (x + 1), (int) (y + 2), (int) (x + 12), (int) (y + 13), 0x55111111);
	}

	/*
	 * 'true' oder 'false' bedeutet hat der Nutzer damit interagiert und
	 * sollen alle anderen Versuche der Interaktion abgebrochen werden?
	 */
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && isCheckHovered(mouseX, mouseY)) {
			set.setValBoolean(!set.getValBoolean());
			return true;
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/*
	 * Einfacher HoverCheck, ben�tigt damit die Value ge�ndert werden kann
	 */
	public boolean isCheckHovered(int mouseX, int mouseY) {
		return mouseX >= x + 1 && mouseX <= x + 12 && mouseY >= y + 2 && mouseY <= y + 13;
	}
}
