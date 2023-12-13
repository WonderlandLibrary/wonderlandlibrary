package me.aidanmees.trivia.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.RenderUtils;
import me.aidanmees.trivia.gui.Dash.ModuleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.CustomButton.GuiButtonDark;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class GuiDashboard extends GuiScreen {
	private int offset = -40;

	private GuiScreen before;
	private List<Entity> toAttack = new ArrayList<Entity>();
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();
	FontRenderer fontRenderer2 = new UnicodeFontRenderer(new Font("Charcoal", Font.BOLD, 30));
	FontRenderer fontRenderer3 = new UnicodeFontRenderer(new Font("Charcoal", Font.BOLD, 13));
	List Players;
	private int y = 50;
    

	public GuiDashboard(GuiScreen before) {
		
		
	}

	public void initGui() {
		this.buttonList.clear();
		
		
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			
			closed = true;
			mc.displayGuiScreen(before);
		}
		if (button.id == 1) {
			
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
		String ThePlayers = "";
		ModuleButton btn = new ModuleButton();
		
		GlStateManager.color(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1, 1, 1);
		 ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		 RenderUtils.drawVerticalLine(0, mc.displayHeight , 0,  100000);
		 RenderUtils.drawVerticalLine(100, mc.displayHeight , 50,  100000);
		 
		 RenderUtils.drawVerticalLine(sr.getScaledWidth() - 2, mc.displayHeight , 0,  100000);
		
		 RenderUtils.drawHLine(0, mc.displayHeight , 0,  100000);
		RenderUtils.drawHLine(0, mc.displayHeight , height - 2,  100000);
		
		
		fontRenderer2.drawCenteredString(this.fontRenderer2, "Dashboard", width / 2 ,
				15, TabGuiContainer.col);
		fontRenderer3.drawCenteredString(this.fontRenderer3, ThePlayers, width / 2 ,
				  50, TabGuiContainer.col);
		
		
		RenderUtils.drawHLine(0, mc.displayHeight , 0 + 50,  100000);
		
		
		for (int i = 1; i < mc.theWorld.loadedEntityList.size(); i ++) {
			GlStateManager.color(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2);
			if (((Entity)mc.theWorld.loadedEntityList.get(i) instanceof EntityPlayer) && !toAttack.contains(((Entity)mc.theWorld.loadedEntityList.get(i)))) {
				toAttack.add(((Entity)mc.theWorld.loadedEntityList.get(i)));
				
				ThePlayers = ThePlayers + "\n" + ((Entity)mc.theWorld.loadedEntityList.get(i)).getName();
				GlStateManager.color(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2);
				
				btn.setY(y);
				btn.setX(50);;
				btn.setTitle(((Entity)mc.theWorld.loadedEntityList.get(i)).getName());
				btn.draw();
				y += 10;
				
		}
			if (i >= mc.theWorld.loadedEntityList.size()) {
				
			}
		}
		y = 50;
		toAttack.clear();
		
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			
			closed = true;
			mc.displayGuiScreen(before);
		}
		
	}
}
