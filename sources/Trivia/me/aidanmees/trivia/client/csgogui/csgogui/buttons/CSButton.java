package me.aidanmees.trivia.client.csgogui.csgogui.buttons;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class CSButton extends Gui {

	public int x, y, width, height;
	public int color;
	public String displayString;

	public Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fr = mc.fontRendererObj;

	public CSButton(int x, int y, int width, int height, int color, String displayString) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.displayString = displayString;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	}

	public void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
	}

	public void initButton() {
	}

}
