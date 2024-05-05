package me.aidanmees.trivia.client.csgogui.csgogui.buttons.setting;

import java.io.IOException;

import me.aidanmees.trivia.client.csgogui.csgogui.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class CSSetting {

	public int x, y, width, height;

	public Setting set;

	public String displayString;

	public Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fr = mc.fontRendererObj;

	public CSSetting(int x, int y, int width, int height, Setting s) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.set = s;
		this.displayString = s.displayName;
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
