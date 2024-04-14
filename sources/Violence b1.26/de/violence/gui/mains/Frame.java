package de.violence.gui.mains;

import de.violence.font.FontManager;
import de.violence.gui.VSetting;
import de.violence.gui.mains.Button;
import de.violence.gui.mains.Component;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Colours;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Frame {
	private Category category;
	public boolean hovered;
	public int x;
	public int y;
	public int dragX;
	public int dragY;
	public boolean dragging;
	public int width;
	public int heigth;
	public int defaultWidth;
	public Minecraft mc = Minecraft.getMinecraft();
	private List buttonList = new ArrayList();

	public Frame(Category category, int x, int y, int width) {
		this.category = category;
		this.x = x;
		this.y = y;
		this.width = width;
		this.defaultWidth = width;
		this.heigth = FontManager.clickGUI.getStringHeight(this.category.name());
		this.initButtons();
		this.sortButtons();
		this.rescaleButtons();
	}

	private void sortButtons() {
		this.buttonList.sort(new Comparator<Button>() {
			@Override
			public int compare(final Button o1, final Button o2) {
				return FontManager.clickGUI.getStringWidth(o2.getModule().getName())
						- FontManager.clickGUI.getStringWidth(o1.getModule().getName());
			}
		});
	}

	private void initButtons() {
		Iterator var2 = Module.getModuleList().iterator();

		while (var2.hasNext()) {
			Module module = (Module) var2.next();
			if (module.getCategory() == this.category && !module.getName().equalsIgnoreCase("GUI")) {
				this.buttonList.add(new Button(module, this.width));
			}
		}

	}

	private int getY(Button button) {
		int y = 10;
		if (button.isExtend()) {
			Iterator var4 = button.getComponentList().iterator();

			while (var4.hasNext()) {
				Component components = (Component) var4.next();
				VSetting.SettingType settingType = components.getVSetting().getSettingType();
				if (components.getVSetting().show) {
					if (settingType == VSetting.SettingType.BUTTON) {
						y += 8;
					} else if (settingType == VSetting.SettingType.SLIDER) {
						y += 11;
					} else if (settingType == VSetting.SettingType.MODE) {
						if (components.getVSetting().extend) {
							y += components.getVSetting().getModeList().size() * 9 + 9;
						} else {
							y += 9;
						}
					}
				}
			}

			y += 3;
		}

		return y;
	}

	private int getComponentWidth(Button button) {
		int w = 0;
		Iterator var4 = button.getComponentList().iterator();

		while (true) {
			Component components;
			do {
				if (!var4.hasNext()) {
					return w;
				}

				components = (Component) var4.next();
				w = Math.max(w, FontManager.clickGUI.getStringWidth(components.getVSetting().getName()));
			} while (components.getVSetting().getSettingType() != VSetting.SettingType.MODE);

			String modes;
			for (Iterator var6 = components.getVSetting().getModeList().iterator(); var6
					.hasNext(); w = Math.max(w, FontManager.clickGUI.getStringWidth(modes)) + 1) {
				modes = (String) var6.next();
			}
		}
	}

	private void rescaleButtons() {
		int y = 10;
		boolean isOneExtend = false;

		Button buttons;
		for (Iterator var4 = this.buttonList.iterator(); var4.hasNext(); y += this.getY(buttons)) {
			buttons = (Button) var4.next();
			if (buttons.isExtend()) {
				GuiScreen var10000 = this.mc.currentScreen;
				GuiScreen.drawRect(this.x, this.y + y + this.getY(buttons) - 3, this.x + buttons.width,
						this.y + y + this.getY(buttons), Colours.getColor(64, 64, 64, 255));
				isOneExtend = true;
				if (this.width < this.getComponentWidth(buttons) + 20) {
					++this.width;
				}
			}

			buttons.x = this.x;
			buttons.y = this.y + y;
			buttons.width = this.width;
			if (buttons.isExtend()) {
				if (buttons.componentWidth + 10 < this.width) {
					++buttons.componentWidth;
				}
			} else if (buttons.componentWidth + 10 > buttons.defaultWidth) {
				--buttons.componentWidth;
			}
		}

		if (!isOneExtend && this.width > this.defaultWidth) {
			--this.width;
		}

	}

	public void onRender(int mouseX, int mouseY) {
		this.rescaleButtons();
		Iterator var4 = this.buttonList.iterator();

		while (var4.hasNext()) {
			Button buttons = (Button) var4.next();
			buttons.onRender(mouseX, mouseY);
		}

		GL11.glPushMatrix();
		GlStateManager.disableTexture2D();
		GuiScreen var10000 = this.mc.currentScreen;
		GuiScreen.drawRect(this.x, this.y, this.x + this.width, this.y + this.heigth + 3,
				Colours.getColor(0, 0, 0, 255));
		var10000 = this.mc.currentScreen;
		GuiScreen.drawRect(this.x, this.y, this.x + this.width, this.y + this.heigth + 3, Colours.getMain(255));
		GlStateManager.enableTexture2D();
		GL11.glPopMatrix();
		FontManager.clickGUI.drawCenteredString(this.category.name(), this.x + this.width / 2, this.y + 1,
				Colours.getColor(0, 0, 0, 255));
		FontManager.clickGUI.drawCenteredString(this.category.name(), this.x + this.width / 2, this.y, -1);
	}

	public void onClick(int mouseX, int mouseY, int mouseButton) {
		Iterator var5 = this.buttonList.iterator();

		while (var5.hasNext()) {
			Button buttons = (Button) var5.next();
			buttons.onClick(mouseX, mouseY, mouseButton);
		}

	}

	public int getWidth() {
		return this.width;
	}

	public int getHeigth() {
		return this.heigth;
	}

	public Category getCategory() {
		return this.category;
	}
}
