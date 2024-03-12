package de.Hero.clickgui.elements.menu;

import java.awt.Color;


import de.theBest.MythicX.MythicX;

import net.minecraft.client.Minecraft;
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
public class ElementComboBox extends Element {
	/*
	 * Konstrukor
	 */
	public ElementComboBox(ModuleButton iparent, Setting iset) {
		parent = iparent;
		set = iset;
		super.setup();
	}

	/*
	 * Rendern des Elements
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Color temp = ColorUtil.getClickGUIColor();
		int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
		
		/*
		 * Die Box und Umrandung rendern
		 */
		Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), new Color(0, 0, 0, 170).getRGB());

		FontUtil.drawTotalCenteredString(setstrg, x + width / 2, y + 15 / 2f, 0xffffffff);
		int clr1 = color;
		int clr2 = temp.getRGB();

		Gui.drawRect((int) x, (int) (y + 14), (int) (x + width), (int) (y + 15), 0x77000000);
		if (comboextended) {
			Gui.drawRect((int) x, (int) (y + 15), (int) (x + width), (int) (y + height), new Color(0, 0, 0, 50).getRGB());
			double ay = y + 15;
			for (String sld : set.getOptions()) {
				String elementtitle = sld.substring(0, 1).toUpperCase() + sld.substring(1, sld.length());
				if (sld.equalsIgnoreCase(set.getValString())) {
					Gui.drawRect((int) x, (int) ay, (int) (x + width), (int) (ay + FontUtil.getFontHeight() + 2), ColorUtil.getClickGUIColor().getRGB());
				}
				FontUtil.drawCenteredString(elementtitle, x + width / 2, ay, 0xffffffff);
				
				/*
				 * Ist das Element ausgew�hlt, wenn ja dann markiere
				 * das Element in der ComboBox
				 */
				/*
				 * Wie bei mouseClicked 'is hovered', wenn ja dann markiere
				 * das Element in der ComboBox
				 */
				ay += FontUtil.getFontHeight() + 2;
			}
		}
	}

	/*
	 * 'true' oder 'false' bedeutet hat der Nutzer damit interagiert und
	 * sollen alle anderen Versuche der Interaktion abgebrochen werden?
	 */
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			if (isButtonHovered(mouseX, mouseY)) {
				comboextended = !comboextended;
				return true;
			}
			
			/*
			 * Also wenn die Box ausgefahren ist, dann wird f�r jede m�gliche Options
			 * �berpr�ft, ob die Maus auf diese zeigt, wenn ja dann global jeder weitere 
			 * call an mouseClicked gestoppt und die Values werden aktualisiert
			 */
			if (!comboextended)return false;
			double ay = y + 15;
			for (String slcd : set.getOptions()) {
				if (mouseX >= x && mouseX <= x + width && mouseY >= ay && mouseY <= ay + FontUtil.getFontHeight() + 2) {
					if(MythicX.setmgr.getSettingByName("Sound").getValBoolean())
					Minecraft.getMinecraft().thePlayer.playSound("tile.piston.in", 20.0F, 20.0F);
					
					if(clickgui != null && clickgui.setmgr != null)
					clickgui.setmgr.getSettingByName(set.getName()).setValString(slcd.toLowerCase());
					return true;
				}
				ay += FontUtil.getFontHeight() + 2;
			}
		}

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/*
	 * Einfacher HoverCheck, ben�tigt damit die Combobox ge�ffnet und geschlossen werden kann
	 */
	public boolean isButtonHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 15;
	}
}
