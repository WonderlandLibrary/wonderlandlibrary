package me.aidanmees.trivia.client.modules.Render;

import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.custom.clickgui.DisplayClickGui;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;

public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER, "Its a clickgui what do u expect.");
	}

	@Override
	public void onDisable() {
		onClose();
		super.onDisable();
	}
	
	public void onClose() {
		
	}

	public void onOpen() {
		
		
	}
	@Override
	public void onEnable() {

		if(!(mc.currentScreen instanceof DisplayClickGui)) {
			
			mc.displayGuiScreen(new DisplayClickGui(true));
		}
		setToggled(false, true);
		super.onEnable();
	}

	@Override
	public void onRender() {
		
	}
}
