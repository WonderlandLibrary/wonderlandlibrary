package me.aidanmees.trivia.client.csgogui.csgogui;

import java.io.IOException;
import java.util.ArrayList;

import me.aidanmees.trivia.client.csgogui.csgogui.buttons.CSCategoryButton;
import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.module.state.Category;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class CSGOGui extends GuiScreen {

	public ArrayList<CSCategoryButton> buttons = new ArrayList<CSCategoryButton>();

	public static CSCategoryButton currentCategory;

	public int x, y, width, height;

	public CSGOGui() {
		this.x = 100;
		this.y = 100;
		this.width = GuiScreen.width - 100;
		this.height = GuiScreen.height - 100;

	}

	private void initButtons() {
		this.buttons.clear();
		int x = 110;
		int y = 106;

		for (Category c : Category.values()) {

			String categoryname = c.name().substring(0, 1).toUpperCase()
					+ c.name().substring(1, c.name().length()).toLowerCase();
			CSCategoryButton cscb = new CSCategoryButton(x, y, mc.fontRendererObj.getStringWidth(categoryname),
					mc.fontRendererObj.FONT_HEIGHT , 0xFFFFFFFF, categoryname, c);

			this.buttons.add(cscb);

			y += 10;
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(x, y, width, height, Integer.MIN_VALUE);

		for (CSCategoryButton cb : buttons) {
			cb.drawScreen(mouseX, mouseY, partialTicks);
		}
		Gui.drawRect(this.x, this.y, GuiScreen.width - 100 , 83, TabGuiContainer.col);
		Gui.drawRect(this.x + 65, this.y, this.x + 67, this.height, TabGuiContainer.col);
		Gui.drawRect(this.x + 165, this.y, this.x + 167, this.height, TabGuiContainer.col);

	
				mc.fontRendererObj.drawString("By Mees",
				GuiScreen.width - mc.fontRendererObj.getStringWidth("By Mees"),
				GuiScreen.height - mc.fontRendererObj.FONT_HEIGHT,
				true ? TabGuiContainer.col : Integer.MAX_VALUE);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (CSCategoryButton cb : buttons) {
			cb.keyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		boolean anyhovered = true;
		for (CSCategoryButton cb : buttons) {

			if (cb.isHovered(mouseX, mouseY) && mouseButton == 0) {
				anyhovered = true;
				currentCategory = cb;
			}

			cb.mouseClicked(mouseX, mouseY, mouseButton);
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (CSCategoryButton cb : buttons) {
			cb.mouseReleased(mouseX, mouseY, state);
		}

		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void initGui() {
		initButtons();

		this.x = 100;
		this.y = 100;
		this.width = GuiScreen.width - 100;
		this.height = GuiScreen.height / 4 + 200;
		for (CSCategoryButton cb : buttons) {
			cb.initButton();
		}

		super.initGui();
	}

}
