package me.aidanmees.trivia.client.csgogui.csgogui.buttons.setting.settings;

import java.io.IOException;
import java.util.ArrayList;

import me.aidanmees.trivia.client.csgogui.csgogui.setting.Setting;
import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class CSSettingComboValue {
	public int x, y, width, height;
	public String displayString;

	public Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fr = mc.fontRendererObj;

	public ArrayList<String> values;
	public Setting set;

	public CSSettingComboValue(int x, int y, int width, int height, Setting s, String displayString) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.displayString = displayString;
		this.set = s;
		this.values = s.getOptions();
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(this.isHovered(mouseX, mouseY)) {
			this.set.setValString(this.displayString);
		}
	}

	public void drawScreen(int mouseX, int mouseY) {
		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
		int color = this.set.getValString().equalsIgnoreCase(this.displayString) ? TabGuiContainer.col : Integer.MAX_VALUE;
		fr.drawString(this.displayString, this.x + width / 2 - fr.getStringWidth(this.displayString) / 2, this.y + 1, color);
	}	

	public boolean isHovered(int mouseX, int mouseY) {
		boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
		boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
		return hoveredx && hoveredy;
	}

}
