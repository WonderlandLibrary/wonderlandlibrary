package me.aidanmees.trivia.client.csgogui.csgogui.util;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.client.csgogui.csgogui.CSGOGui;
import me.aidanmees.trivia.module.Module;

public class CSGuiMod extends Module {

	public CSGuiMod() {
		super("CSGuiMod", Keyboard.KEY_NONE, Category.RENDER, "Dank CSGO GUI!.");
		

	}
	@Override
	public ModSetting[] getModSettings() {
		
		CheckBtnSetting box1 = new CheckBtnSetting("Outline", "CSGuiOutline");
		
		return new ModSetting[] {  };
	}
	
	

	@Override
	public void onTick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		mc.displayGuiScreen(new CSGOGui());
		super.setToggled(false, false);

	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

}
