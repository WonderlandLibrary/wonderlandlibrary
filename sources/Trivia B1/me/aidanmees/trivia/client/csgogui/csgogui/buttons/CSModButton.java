package me.aidanmees.trivia.client.csgogui.csgogui.buttons;

import java.io.IOException;
import java.util.ArrayList;

import me.aidanmees.trivia.client.csgogui.csgogui.CSGOGui;
import me.aidanmees.trivia.client.csgogui.csgogui.buttons.setting.CSSetting;
import me.aidanmees.trivia.client.csgogui.csgogui.buttons.setting.settings.CSSettingCheck;
import me.aidanmees.trivia.client.csgogui.csgogui.buttons.setting.settings.CSSettingCombo;
import me.aidanmees.trivia.client.csgogui.csgogui.buttons.setting.settings.CSSettingDouble;
import me.aidanmees.trivia.client.csgogui.csgogui.setting.Setting;
import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.gui.GuiScreen;

public class CSModButton extends CSButton {

	public Module mod;

	public CSModButton(int x, int y, int width, int height, int color, String displayString, Module mod) {
		super(x, y, width, height, color, displayString);
		this.mod = mod;
		initSettings();
	}

	private void initSettings() {
		int y = 120;
		int x = this.x + 120;
		if(mod.getModSettings() != null) {
		}
	}

	public ArrayList<CSSetting> settings = new ArrayList<CSSetting>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		int color = this.isHovered(mouseX, mouseY) ? TabGuiContainer.col : 0xFFFFFFFF;

		if (this.mod.isToggled()) {
			color = 1;
		}

		if (this.isCurrentMod()) {
			color = TabGuiContainer.col;
		}

		fr.drawString(displayString, x, y, color);

		for (CSSetting cs : settings) {
			if (isCurrentMod()) {
				cs.drawScreen(mouseX, mouseY, partialTicks);
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		if (this.isHovered(mouseX, mouseY)) {

			if (mouseButton == 0 && isHovered(mouseX, mouseY) && CSGOGui.currentCategory != null
					&& CSGOGui.currentCategory.category == this.mod.getCategory()) {
				this.mod.toggle();

			} else if (mouseButton == 1) {
				try {

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
		for (CSSetting cs : settings) {
			if (isCurrentMod()) {
				cs.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean isHovered(int mouseX, int mouseY) {
		boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
		boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
		return hoveredx && hoveredy;
	}

	private boolean isCurrentMod() {
		return CSGOGui.currentCategory != null && CSGOGui.currentCategory.currentMod != null
				&& CSGOGui.currentCategory.currentMod == this;
	}

	@Override
	public void initButton() {
		initSettings();

		super.initButton();
	}

}
