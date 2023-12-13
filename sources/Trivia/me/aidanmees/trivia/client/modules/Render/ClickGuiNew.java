package me.aidanmees.trivia.client.modules.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.Clickgui.clickgui.ClickGui;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S02PacketChat;

public class ClickGuiNew extends Module {

	public ClickGui clickgui;

	public ClickGuiNew() {
		super("ClickGuiNew", Keyboard.KEY_NONE, Category.RENDER,
				"New clickgui bby!");
	}

	public void onClose() {
		
	}

	public void onOpen() {
		
		
	}

	@Override
	public void onDisable() {
onClose();
		super.onDisable();
	}


    @Override
    public void onEnable()
    {
    	onOpen();
    	if(this.clickgui == null)
    		this.clickgui = new ClickGui();
    	
    	mc.displayGuiScreen(this.clickgui);
    	toggle();
    	super.onEnable();
    }


	
}
