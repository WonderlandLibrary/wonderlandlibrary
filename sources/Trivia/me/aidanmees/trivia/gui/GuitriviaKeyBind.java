package me.aidanmees.trivia.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.alts.New.GuiAltManager;
import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.RenderUtils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class GuitriviaKeyBind extends GuiScreen {
	private int offset = -40;
	private Module module;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();
	FontRenderer fontRenderer2 = new UnicodeFontRenderer(new Font("Charcoal", Font.BOLD, 30));
	FontRenderer fontRenderer3 = new UnicodeFontRenderer(new Font("Charcoal", Font.BOLD, 20));
    

	public GuitriviaKeyBind(Module module, GuiScreen before) {
		this.module = module;
		this.before = before;
	}

	public void initGui() {
		this.buttonList.clear();
		
		
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			module.setKeyCode(module.getDefaultKeyboardKey());
			closed = true;
			mc.displayGuiScreen(before);
		}
		if (button.id == 1) {
			module.setKeyCode(Keyboard.KEY_NONE);
			closed = true;
			mc.displayGuiScreen(before);
		}
		if (button.id == 3) {
			mc.displayGuiScreen(before);
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		Color colll = new Color(10,10,10 , 200);
	    
		Gui.drawRect(0, height, 10000, 0, colll.getRGB());
		 
		
		fontRenderer3.drawString("Press a key to bind it to that key \nor press Escape to remove the keybind!", width / 50, 60, TabGuiContainer.col);
		fontRenderer2.drawCenteredString(this.fontRenderer2, "Information: ", width / 2, height / 2 + offset + 15, TabGuiContainer.col);
		fontRenderer3.drawCenteredString(this.fontRenderer3, "Name: " +module.getName() + "\n" , width / 2, height / 2 + offset + 40, TabGuiContainer.col);
		fontRenderer3.drawCenteredString(this.fontRenderer3, "Action: " +module.description , width / 2, height / 2 + offset + 55, TabGuiContainer.col);
		
		fontRenderer3.drawCenteredString(this.fontRenderer3, "Category: "+module.getCategory(), width / 2, height / 2 + offset + 70, TabGuiContainer.col);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(1, 1, 1);
		 ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		 RenderUtils.drawVerticalLine(0, mc.displayHeight , 0,  100000);
		 RenderUtils.drawVerticalLine(sr.getScaledWidth() - 2, mc.displayHeight , 0,  100000);
		 
		 
		 RenderUtils.drawHLine(0, mc.displayHeight , 0,  100000);
		RenderUtils.drawHLine(0, mc.displayHeight , height - 2,  100000);
		
		fontRenderer2.drawCenteredString(this.fontRenderer2, "You are currently keybinding " + module.getName() + "", width / 2 ,
				15, TabGuiContainer.col);
		
		
		
		RenderUtils.drawHLine(0, mc.displayHeight , 0 + 50,  100000);
		
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!(keyCode == Keyboard.KEY_ESCAPE)) {
			module.setKeyCode(keyCode);
			closed = true;
			mc.displayGuiScreen(before);
		}
		else {
			module.setKeyCode(Keyboard.KEY_NONE);
			mc.displayGuiScreen(before);
		}
	}
}
