package de.violence.tabgui.componenents;

import de.violence.font.FontManager;
import de.violence.ingame.IngameRenderer;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.tabgui.TabGui;
import de.violence.tabgui.componenents.ModuleTab;
import de.violence.ui.Colours;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class CategoryTab {
	private Category category;
	private int x;
	private int y;
	private int width;
	private int heigth;
	private int listId;
	private Minecraft mc = Minecraft.getMinecraft();
	private int fadeX;
	private boolean finished;
	private int currentTab;
	private boolean extend;
	private List moduleTabList = new ArrayList();

	public CategoryTab(Category category, int x, int y, int width) {
		this.category = category;
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigth = 11;
		this.createTabs();
		this.sortTabs();
	}

	private void sortTabs() {
		this.moduleTabList.sort(new Comparator<ModuleTab>() {
			@Override
			public int compare(final ModuleTab o1, final ModuleTab o2) {
				return FontManager.arrayList_Bignoodletitling.getStringWidth(o2.getModule().getName())
						- FontManager.arrayList_Bignoodletitling.getStringWidth(o1.getModule().getName());
			}
		});
	}

	private void createTabs() {
		int y = 15;
		int x = this.x + this.width + 1;
		Iterator var4 = Module.getModuleList().iterator();

		while (var4.hasNext()) {
			Module modules = (Module) var4.next();
			if (modules.getCategory() == this.category) {
				this.moduleTabList.add(new ModuleTab(modules, x, y, this.getWidth()));
				y += 11;
			}
		}

	}

	public void draw(boolean selected) {
		int color = Integer.MIN_VALUE;
		if (selected) {
			color = Colours.getMain(255);
		}

		if (selected && !this.finished) {
			this.fadeX = 0;
		}

		if (this.fadeX < this.width) {
			++this.fadeX;
			this.finished = true;
		}

		if (!selected && this.finished) {
			this.finished = false;
		}

		String name = this.category.name().substring(0, 1) + this.category.name().substring(1).toLowerCase();
		GuiIngame var10000 = this.mc.ingameGUI;
		GuiIngame.drawRect(this.x, this.y, this.x + this.fadeX, this.y + this.heigth - 1,
				Colours.getColor(0, 0, 0, 150));
		var10000 = this.mc.ingameGUI;
		GuiIngame.drawRect(this.x, this.y, this.x + this.fadeX, this.y + this.heigth - 1, color);
		FontManager.arrayList_Bignoodletitling.drawCenteredString(name, this.x + this.fadeX / 2, this.y - 1, -1);
		if (this.extend) {
			int y = 5;

			for (Iterator var6 = this.moduleTabList.iterator(); var6.hasNext(); y += 11) {
				ModuleTab moduleTabs = (ModuleTab) var6.next();
				moduleTabs.setX(this.x + this.width + 1);
				moduleTabs.setY(TabGui.y + y);
				moduleTabs.draw(this.currentTab == this.moduleTabList.indexOf(moduleTabs));
			}
		}

	}

	private int getWidth() {
		int w = 0;
		Iterator var3 = Module.getModuleList().iterator();

		while (var3.hasNext()) {
			Module module = (Module) var3.next();
			if (module.getCategory() == this.category) {
				w = Math.max(w, FontManager.arrayList_Bignoodletitling.getStringWidth(module.getName())) + 1;
			}
		}

		return w;
	}

	public void onSelect(int key) {
		if (key == 208) {
			if (this.currentTab < this.moduleTabList.size() - 1) {
				++this.currentTab;
			} else {
				this.currentTab = 0;
			}
		} else if (key == 200) {
			if (this.currentTab > 0) {
				--this.currentTab;
			} else {
				this.currentTab = this.moduleTabList.size() - 1;
			}
		}

		if (key == 205) {
			this.resetAnothers();
			this.extend = true;
		}

		if (key == 203) {
			this.resetAnothers();
			this.extend = false;
		}

	}

	private void resetAnothers() {
		Iterator var2 = IngameRenderer.getTabGui().getCategoryTabList().iterator();

		while (var2.hasNext()) {
			CategoryTab categoryTabs = (CategoryTab) var2.next();
			categoryTabs.setExtend(false);
		}

	}

	public boolean isExtend() {
		return this.extend;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setExtend(boolean extend) {
		this.extend = extend;
	}
}
