package sudo.ui.screens.clickgui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;
import sudo.module.Mod.Category;
import sudo.module.client.ClickGuiMod;
import sudo.module.ModuleManager;
import sudo.ui.screens.clickgui.setting.ColorBox;
import sudo.ui.screens.clickgui.setting.Component;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class Frame {
	
	public int x, y, width, height, dragX, dragY;
	public Category category;
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	public boolean dragging, extended;
	
	private List<ModuleButton> buttons;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();
	private static ClickGuiMod ClickGuiMod = ModuleManager.INSTANCE.getModule(ClickGuiMod.class);
	
	public Frame(Category category, int x, int y, int width, int height) {
		this.category = category;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dragging = false;
		this.extended = true;
		
		buttons = new ArrayList<>();
		
		int offset = height;
		for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(category)) {
			buttons.add(new ModuleButton(mod, this, offset));
			offset += height;
		}
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, x, y+4, x + width, y + height, ClickGuiMod.primaryColor.getColor().getRGB());
		textRend.drawString(matrices, category.name, x + (width/2) - (mc.textRenderer.getWidth(category.name)/2), y + (height/2) - ((mc.textRenderer.fontHeight-4)/2)-2.5, -1, 1);
		if (extended) {
			for (ModuleButton button : buttons) {
				button.render(matrices, mouseX, mouseY, delta);
			}
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY)) {
			if (button == 0) {
				dragging = true;
				dragX = (int) (mouseX - x);
				dragY = (int) (mouseY - y);
			} else if  (button == 1) {
				extended = !extended;
			}
		}
		if (extended) {
			for (ModuleButton mb : buttons) {
				mb.mouseClicked(mouseX, mouseY, button);
			}
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0 && dragging == true) dragging = false;
		
		for (ModuleButton mb : buttons) {
			mb.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}

	public void updatePosition(double mouseX, double mouseY) {
		if (dragging) {
			x = (int) (mouseX - dragX);
			y = (int) (mouseY - dragY);
			
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void updateButton() {
		int offset = height;
		for (ModuleButton button : buttons) {
			button.offset = offset;
			offset += height;
			
			if (button.extended) {
				for (Component component : button.components) {
					if (component.setting.isVisible()) {
						if (component instanceof ColorBox) {
							if (((ColorBox)component).open) offset += height*6.2f;
							else offset+=height;
						} else offset += height;
					}
				}
			}
		}
	}
	
	public void keyPressed(int key) {
        for (ModuleButton mb : buttons) {
            mb.keyPressed(key);
        }
	}
}
