package de.Hero.clickgui.util;

import astronaut.Duckware;


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
		return new Color((int) Duckware.setmgr.getSettingByName("GuiRed").getValDouble(), (int)Duckware.setmgr.getSettingByName("GuiGreen").getValDouble(), (int)Duckware.setmgr.getSettingByName("GuiBlue").getValDouble());
	}
}
