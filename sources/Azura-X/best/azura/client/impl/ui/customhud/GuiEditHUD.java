package best.azura.client.impl.ui.customhud;

import best.azura.client.api.ui.buttons.Button;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.impl.Client;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.impl.Window;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiEditHUD extends GuiScreen {

	public final GuiScreen parent;
	public final ArrayList<Element> elementTypes = new ArrayList<>();
	private final long start;
	private Element currentElement, draggingElement;
	private int dragX, dragY;

	private best.azura.client.impl.ui.gui.impl.Window contextMenu, currentWindow;
	private SettingsWindow settingsWindow;

	public GuiEditHUD(GuiScreen parent) {
		this.parent = parent;
		this.start = System.currentTimeMillis();
	}

	@Override
	public void initGui() {
		elementTypes.clear();
		elementTypes.addAll(Client.INSTANCE.getElementManager().getAvailableElementTypes());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		ScaledResolution sr = new ScaledResolution(this.mc);
		mouseX *= sr.getScaleFactor();
		mouseY *= sr.getScaleFactor();

		if (currentWindow != null) {
			if (currentWindow.animation <= 0.01 && currentWindow.hidden) {
				currentWindow = null;
			}
		}

		if (draggingElement != null) {
			draggingElement.setX(dragX + mouseX);
			draggingElement.setY(dragY + mouseY);
		}

		mouseX /= sr.getScaleFactor();
		mouseY /= sr.getScaleFactor();

		GlStateManager.pushMatrix();
		RenderUtil.INSTANCE.scaleFix(1.0);

		float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
		double animation = -1 * Math.pow(anim - 1, 6) + 1;

		RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, (int) (100 + 30 * animation)));

		for (Element e : Client.INSTANCE.getElementManager().getElements()) {
			if (!e.isEnabled()) continue;
			GlStateManager.scale(e.getScale(), e.getScale(), 1);
			e.render();
			GlStateManager.scale(1. / e.getScale(), 1. / e.getScale(), 1);
			if (RenderUtil.INSTANCE.isHoveredScaled(e.getX() * e.getScale(), e.getY() * e.getScale(), e.getWidth() * e.getScale(), e.getHeight() * e.getScale(), mouseX, mouseY, 1) && currentWindow == null)
				RenderUtil.INSTANCE.drawHollowRect(e.getX() * e.getScale(), e.getY() * e.getScale(), e.getX() * e.getScale() + e.getWidth() * e.getScale(), e.getY() * e.getScale() + e.getHeight() * e.getScale(), -1);
		}
		GlStateManager.resetColor();

		if (contextMenu != null) {
			contextMenu.draw(mouseX, mouseY);
		}

		if (currentWindow != null) {
			currentWindow.draw(mouseX, mouseY);
		}

		if (settingsWindow != null) {
			settingsWindow.draw(mouseX, mouseY);
			if (settingsWindow.animation <= 0.01) {
				settingsWindow = null;
			}
		}

		GlStateManager.popMatrix();
	}

	public void openContextMenu(int x, int y) {
		for (Element element : Client.INSTANCE.getElementManager().getElements()) {
			if (RenderUtil.INSTANCE.isHovered(element.getX() * element.getScale(), element.getY() * element.getScale(), element.getWidth() * element.getScale(), element.getHeight() * element.getScale(), x, y)) {
				currentElement = element;
				break;
			}
		}
		if (x > mc.displayWidth - 400) {
			x = x - 300;
		}
		String[] buttons = new String[]{"New"};
		if (currentElement != null) {
			if (currentElement.getValues() == null || currentElement.getValues().isEmpty())
				buttons = new String[]{"New", "Delete"};
			else buttons = new String[]{"New", "Properties", "Delete"};
		}
		contextMenu = new best.azura.client.impl.ui.gui.impl.Window(x, y, 300, buttons.length * 40 + 20);
		contextMenu.backgroundColor = new Color(0, 0, 0, 100);
		int calcHeight = 0;
		for (String s : buttons) {
			ButtonImpl button;
			contextMenu.buttons.add(button = new ButtonImpl(s, x, y + 10 + calcHeight, 300, 40, 0));
			button.normalColor = new Color(50, 50, 50, 0);
			button.hoverColor = new Color(50, 50, 50, 100);
			calcHeight += 40;
		}
	}

	@Override
	public void onTick() {
		if (settingsWindow != null) {
			settingsWindow.onTick();
		}
	}

	@Override
	public void handleMouseInput() {
		try {
			super.handleMouseInput();
		} catch (IOException ignored) {
		}
		if (settingsWindow != null) {
			settingsWindow.onMouse();
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (settingsWindow != null) {
			settingsWindow.mouseReleased(mouseX, mouseY);
			return;
		}
		super.mouseReleased(mouseX, mouseY, state);
		draggingElement = null;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		ScaledResolution sr = new ScaledResolution(this.mc);
		mouseX *= sr.getScaleFactor();
		mouseY *= sr.getScaleFactor();

		if (settingsWindow != null) {
			settingsWindow.mouseClicked(mouseX, mouseY, mouseButton);
			if (!settingsWindow.hovered) {
				settingsWindow.hide();
			}
			return;
		}

		if (currentWindow != null) {
			if (currentWindow.text.contains("Create")) {
				Button clicked = currentWindow.clickedButton(mouseX, mouseY);
				if (clicked == null) {
					currentWindow.hide();
					return;
				}
				final Element type = clicked instanceof ButtonImpl ? Client.INSTANCE.getElementManager().getElement(((ButtonImpl)clicked).text) : null;
				if (type != null) Client.INSTANCE.getElementManager().addElement(type);
			}
			currentWindow.hide();
			return;
		}

		if (mouseButton == 1) {
			openContextMenu(mouseX, mouseY);
		} else {

			if (mouseButton == 0) {
				if (contextMenu == null || contextMenu.hidden) {
					for (Element element : Client.INSTANCE.getElementManager().getElements()) {
						if (RenderUtil.INSTANCE.isHovered(element.getX() * element.getScale(), element.getY() * element.getScale(), element.getWidth() * element.getScale(), element.getHeight() * element.getScale(), mouseX, mouseY)) {
							draggingElement = element;
							dragX = (int) (draggingElement.getX() - mouseX);
							dragY = (int) (draggingElement.getY() - mouseY);
							return;
						}
					}
				}
			}

			if (contextMenu != null && !contextMenu.hidden) {
				Button button = contextMenu.clickedButton(mouseX, mouseY);
				if (button != null) {
					if (button instanceof ButtonImpl) {
						ButtonImpl button1 = (ButtonImpl) button;
						if (button1.text.equals("New")) {
							currentWindow = new Window("Create new element", 300, 400);
							int calcHeight = 0;
							currentWindow.backgroundColor = new Color(0, 0, 0, 100);
							for (Element elementType : elementTypes) {
								ButtonImpl design;
								currentWindow.buttons.add(design = new ButtonImpl(elementType.getName(), mc.displayWidth / 2 - 150, mc.displayHeight / 2 - 140 + calcHeight, 300, 40));
								design.normalColor = new Color(50, 50, 50, 0);
								calcHeight += 40;
							}
						} else if (button1.text.equals("Delete") && currentElement != null) {
							currentElement.onRemove();
							Client.INSTANCE.getElementManager().getElements().remove(currentElement);
						} else if (button1.text.equals("Properties") && currentElement != null && !currentElement.getValues().isEmpty()) {
							openProperties(currentElement);
						}
					}
				}
				contextMenu.hide();
				currentElement = null;
			}
		}
	}

	public void openProperties(Element element) {
		if (element.getValues() == null || element.getValues().isEmpty()) return;
		settingsWindow = new SettingsWindow("Properties for " + element.getName(), 300, element.getValues().size() * 35 + 55, element);
		settingsWindow.backgroundColor = new Color(0, 0, 0, 100);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (settingsWindow != null) {
			settingsWindow.keyTyped(typedChar, keyCode);
		}
	}
}
