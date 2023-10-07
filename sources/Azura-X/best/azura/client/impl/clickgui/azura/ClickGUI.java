package best.azura.client.impl.clickgui.azura;

import best.azura.client.api.module.Category;
import best.azura.client.impl.ui.config.ConfigGUI;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class ClickGUI extends GuiScreen {

	public static boolean cancel = false;
	private final ArrayList<best.azura.client.impl.clickgui.azura.Panel> panels = new ArrayList<>();
	private final ArrayList<ButtonImpl> buttons = new ArrayList<>();
	public double animation = 0;
	public long start;
	public boolean close = false;

	public ClickGUI() {
		int count = 0;
		start = System.currentTimeMillis();
		for (Category category : Category.values()) {
			panels.add(new best.azura.client.impl.clickgui.azura.Panel(20 + count * 300, 50, 260, 40, category));
			count++;
		}
	}

	@Override
	public void initGui() {
		close = false;
		start = System.currentTimeMillis();
		buttons.clear();
		buttons.add(new ButtonImpl("Config", 3, mc.displayHeight - 43, 80, 40, 5));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		int scaleFactor = new ScaledResolution(this.mc).getScaleFactor();
		mouseX *= scaleFactor * 1.0;
		mouseY *= scaleFactor * 1.0;

		if (close) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
			animation = 1 - animation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		GlStateManager.pushMatrix();
		RenderUtil.INSTANCE.scaleFix(1.0);
		RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, (int) (160 * animation)));
		mouseX /= scaleFactor * 1.0;
		mouseY /= scaleFactor * 1.0;
		for (final ButtonImpl button : buttons) {
			button.animation = animation;
			button.normalColor = new Color(3, 3, 3, (int) (button.animation * 170));
			button.hoverColor = new Color(33, 33, 33, (int) (button.animation * 170));
			button.draw(mouseX, mouseY);
		}
		mouseX *= scaleFactor * 1.0;
		mouseY *= scaleFactor * 1.0;
		boolean blend = glIsEnabled(GL_BLEND);
		if (!blend) glEnable(GL_BLEND);

		for (best.azura.client.impl.clickgui.azura.Panel panel : panels) {
			panel.animation = animation;
			panel.render(mouseX, mouseY);
		}

		if (!blend) glDisable(GL_BLEND);
		GlStateManager.popMatrix();
		GlStateManager.enableBlend();

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int scaleFactor = new ScaledResolution(this.mc).getScaleFactor();
		mouseX *= scaleFactor * 1.0;
		mouseY *= scaleFactor * 1.0;

		String clicked = "null";
		for (final ButtonImpl button : buttons) {
			if (button.hovered) clicked = button.text;
			button.mouseClicked();
		}
		if ("Config".equals(clicked)) {
			mc.displayGuiScreen(new ConfigGUI());
		}
		for (best.azura.client.impl.clickgui.azura.Panel panel : panels) {
			panel.mouseClicked(mouseX, mouseY, mouseButton);
		}

	}

	@Override
	public void onTick() {
		for (best.azura.client.impl.clickgui.azura.Panel panel : panels) {
			panel.onTick();
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		int scaleFactor = new ScaledResolution(this.mc).getScaleFactor();
		mouseX *= scaleFactor * 1.0;
		mouseY *= scaleFactor * 1.0;

		for (best.azura.client.impl.clickgui.azura.Panel panel : panels) {
			panel.mouseReleased(mouseX, mouseY);
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		if (close) return;
		super.handleMouseInput();

		for (best.azura.client.impl.clickgui.azura.Panel panel : panels) {
			panel.handleInput();
		}

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (close) return;
		for (Panel panel : panels) {
			panel.keyTyped(typedChar, keyCode);
		}
		if (cancel) {
			cancel = false;
			return;
		}
		if (keyCode == 1 && !close) {
			close = true;
			start = System.currentTimeMillis();
			mc.displayGuiScreen(null);
		}
	}
}
