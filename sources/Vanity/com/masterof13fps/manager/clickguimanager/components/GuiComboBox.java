/**
 * 
 */
package com.masterof13fps.manager.clickguimanager.components;

import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.clickguimanager.Panel;
import com.masterof13fps.manager.clickguimanager.listeners.ComboListener;
import com.masterof13fps.manager.clickguimanager.util.RenderUtil;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at
 *         11.11.2020. Use is only authorized if given credit!
 * 
 */
public class GuiComboBox implements GuiComponent {
	private Setting setting;
	private boolean extended;
	private int height, posX, posY, width;

	private ArrayList<ComboListener> comboListeners = new ArrayList<ComboListener>();

	public GuiComboBox(Setting setting) {
		this.setting = setting;
	}

	public void addComboListener(ComboListener comboListener) {
		comboListeners.add(comboListener);
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		switch (Panel.theme) {
		case "Caesium":
			renderCaesium();
			break;

		default:
			break;
		}
	}

	public void renderCaesium() {
		if (extended) {
			RenderUtil.drawRect(posX, posY, posX + width, posY + Panel.fR.FONT_HEIGHT + 2, Panel.grey40_240);
			RenderUtil.drawHorizontalLine(posX, posX + width, posY, Panel.black195);
			RenderUtil.drawHorizontalLine(posX, posX + width, posY + Panel.fR.FONT_HEIGHT + 2,
					new Color(0, 0, 0, 150).getRGB());
			Panel.fR.drawStringWithShadow(setting.getName() + "  -",
					posX + width / 2 - Panel.fR.getStringWidth(setting.getName() + "  -") / 2, posY + 2,
					Panel.fontColor);

			int innerHeight = (Panel.fR.FONT_HEIGHT + 5);

			for (String combo : setting.getOptions()) {
				if (setting.getCurrentMode().equalsIgnoreCase(combo)) {
					Panel.fR.drawStringWithShadow(combo, posX + 10, posY + innerHeight, Panel.color);
				} else {
					Panel.fR.drawStringWithShadow(combo, posX + 5, posY + innerHeight, Panel.fontColor);
				}
				innerHeight += (Panel.fR.FONT_HEIGHT + 2);
			}

			height = innerHeight + 2;
		} else {
			RenderUtil.drawRect(posX, posY, posX + width, posY + Panel.fR.FONT_HEIGHT + 2, Panel.grey40_240);

			RenderUtil.drawHorizontalLine(posX, posX + width, posY, Panel.black195);
			RenderUtil.drawHorizontalLine(posX, posX + width, posY + Panel.fR.FONT_HEIGHT + 2, Panel.black195);
			Panel.fR.drawStringWithShadow(setting.getName() + "  +",
					posX + width / 2 - Panel.fR.getStringWidth(setting.getName() + "  +") / 2, posY + 2,
					Panel.fontColor);
			height = Panel.fR.FONT_HEIGHT + 4;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (RenderUtil.isHovered(posX, posY, width, Panel.fR.FONT_HEIGHT + 2, mouseX, mouseY)) {
			extended = !extended;
		}
		if (extended) {

			if (RenderUtil.isHovered(posX, posY + Panel.fR.FONT_HEIGHT + 2, width,
					(Panel.fR.FONT_HEIGHT + 2) * setting.getOptions().length, mouseX, mouseY) && mouseButton == 0) {
				int h = Panel.fR.FONT_HEIGHT + 2;
				for (int i = 1; i <= setting.getOptions().length + 1; i++) {
					if (RenderUtil.isHovered(posX, posY + h * i, width, h * i, mouseX, mouseY)) {
						setting.setMode(setting.getOptions()[i - 1]);
					}
				}
				for (ComboListener comboListener : comboListeners) {
					comboListener.comboChanged(setting.getCurrentMode());
				}
			}

		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {

	}

	@Override
	public int getWidth() {

		return 0;
	}

	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * @return the setting
	 */
	public Setting getSetting() {
		return setting;
	}

	/**
	 * @param setting the setting to set
	 */
	public void setSetting(Setting setting) {
		this.setting = setting;
	}

}
