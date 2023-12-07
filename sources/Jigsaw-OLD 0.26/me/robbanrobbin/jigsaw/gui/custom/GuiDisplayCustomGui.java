package me.robbanrobbin.jigsaw.gui.custom;

import java.awt.Rectangle;
import java.io.IOException;

import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiDisplayCustomGui extends GuiScreen {
	private final GuiManager guiManager;
	public final SearchBar searchBar;
	public boolean bg = false;

	public GuiDisplayCustomGui(GuiManager guiManager) {
		this.guiManager = guiManager;
		this.searchBar = new SearchBar();
		bg = false;
	}

	public GuiDisplayCustomGui(GuiManager guiManager, boolean b) {
		this.guiManager = guiManager;
		this.searchBar = new SearchBar();
		bg = b;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		if (!bg) {
			searchBar.init();
		}
		super.initGui();
	}

	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button); // This line throws IOException which
											// is why this method has to have
											// the 'throws' declaration
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && !frame.getArea().contains(x, y)) {
				for (Component component : frame.getChildren()) {
					for (Rectangle area : component.getTheme().getUIForComponent(component)
							.getInteractableRegions(component)) {
						if (area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
							frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
							guiManager.bringForward(frame);
							return;
						}
					}
				}
			}
		}
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && frame.getArea().contains(x, y)) {
				frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
				guiManager.bringForward(frame);
				break;
			} else if (frame.isMinimized()) {
				for (Rectangle area : frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)) {
					if (area.contains(x - frame.getX(), y - frame.getY())) {
						frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
						guiManager.bringForward(frame);
						return;
					}
				}
			}
		}
		searchBar.mouseClicked(x, y, button);
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		super.mouseReleased(x, y, button);
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && !frame.getArea().contains(x, y)) {
				for (Component component : frame.getChildren()) {
					for (Rectangle area : component.getTheme().getUIForComponent(component)
							.getInteractableRegions(component)) {
						if (area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
							frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
							guiManager.bringForward(frame);
							return;
						}
					}
				}
			}
		}
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && frame.getArea().contains(x, y)) {
				frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
				guiManager.bringForward(frame);
				break;
			} else if (frame.isMinimized()) {
				for (Rectangle area : frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)) {
					if (area.contains(x - frame.getX(), y - frame.getY())) {
						frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
						guiManager.bringForward(frame);
						return;
					}
				}
			}
		}
		searchBar.mouseReleased(x, y, button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (!bg) {
			searchBar.keyTyped(typedChar, keyCode);
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (bg) {
			drawGradientRect(0, 0, width, height, 0xff061006, 0xff061006);
			GlStateManager.disableTexture2D();
		}
		guiManager.render();
		if (!bg) {
			searchBar.render(width, height, mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		if (bg) {
			guiManager.update();
		} else {
			searchBar.update();
		}
		super.updateScreen();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}