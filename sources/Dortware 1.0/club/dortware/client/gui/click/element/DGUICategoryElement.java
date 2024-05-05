package club.dortware.client.gui.click.element;

import club.dortware.client.Client;
import club.dortware.client.gui.click.DortGUI;
import club.dortware.client.gui.click.Rect;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.util.Util;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class DGUICategoryElement implements Util {

	private ModuleCategory category;
	private String name;
	private Collection<DGUIModuleButton> elements = Lists.newArrayList();
	private int index;
	public ArrayList<Rect> rects = new ArrayList<>();
	private int scrollY;

	public DGUICategoryElement(ModuleCategory category, String name) {
		this.name = name;
		this.category = category;
		Client.INSTANCE.getModuleManager().getAllInCategory(this.category)
				.forEach(module -> this.elements.add(new DGUIModuleButton(module)));
	}

	public void kt(int kt) {
		for (DGUIModuleButton el : this.elements) {
			if (el.listening) {
				el.keyPress(kt);
			}
		}
	}

	public void drawElement(int w, int h, ScaledResolution scaled, float x, float y, int index, int mouseX,
			int mouseY) {
		if(this.getCategory() != ModuleCategory.HIDE_ME) {
			Color BACKGROUND_COLOR = new Color(48, 44, 44);
			Color d2 = new Color(0, 127, 172);
			Color d3 = new Color(117, 50, 168, 1);
			DortGUI.drawRect(new Rect(x - 5, y + index - 13, x + 70, y + index - 11), BACKGROUND_COLOR.brighter().getRGB());
			this.rects.add(DortGUI.drawRect(new Rect(x - 1, y + index - 9, x + 66, y + index + 16),
					this.category == DortGUI.instance().selCat ? d2.getRGB() : d3.getRGB()));
			for (Rect rect : this.rects) {
				if (rect.isMouseHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
					DortGUI.instance().selCat = this.category;
					DortGUI.instance().selMod = null;
					Mouse.destroy();
					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
			this.rects.clear();
			mc.fontRendererObj.drawStringWithShadow(this.name, x + 1, y + index, -1);
			if (DortGUI.instance().selCat == this.category) {
				for (DGUIModuleButton el : this.elements) {
					if (this.index > DortGUI.instance().height) {
						continue;
					}
					this.index += 24;
					el.render(mouseX, mouseY, scaled, x + 75, y, this.index);
				}
			} else {
				for (DGUIModuleButton el : this.elements) {
					if (el.listening && el.module.getModuleData().category() != this.category) {
						el.listening = false;
					}
				}
			}
			this.index = -8;
		}
	}

	public String getName() {
		return name;
	}

	public ModuleCategory getCategory() {
		return category;
	}

}
