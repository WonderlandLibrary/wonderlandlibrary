package de.Hero.clickgui.util;

import de.theBest.MythicX.MythicX;


import java.awt.Color;

//Deine Imports


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ColorUtil {
	
	public static Color getClickGUIColor(){
		return new Color((int) MythicX.setmgr.getSettingByName("GuiRed").getValDouble(), (int) MythicX.setmgr.getSettingByName("GuiGreen").getValDouble(), (int) MythicX.setmgr.getSettingByName("GuiBlue").getValDouble());
	}
}
