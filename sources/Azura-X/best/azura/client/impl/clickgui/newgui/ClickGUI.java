package best.azura.client.impl.clickgui.newgui;

import best.azura.client.impl.clickgui.newgui.impl.selectors.ModeSelector;
import best.azura.client.api.module.Category;
import best.azura.client.impl.clickgui.newgui.impl.panel.Panel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

	public static boolean cancel = false;
	private final ArrayList<Panel> panels = new ArrayList<>();
	public double animation = 0;
	public long start;
	public boolean close = false;
	public ModeSelector modeSelector;

	public ClickGUI() {
		int count = 0;
		start = System.currentTimeMillis();
		for (Category category : Category.values()) {
			panels.add(new Panel(20 + count * 350, 50, 260, 40, category));
			count++;
		}
	}

	@Override
	public void initGui() {
		close = false;
		start = System.currentTimeMillis();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		int scaleFactor = new ScaledResolution(this.mc).getScaleFactor();
		mouseX *= scaleFactor * 1.0;
		mouseY *= scaleFactor * 1.0;

		for (Panel panel : panels) {
			panel.mouseClicked(mouseX, mouseY, mouseButton);
		}

	}

	@Override
	public void onTick() {
		for (Panel panel : panels) {
			panel.onTick();
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {

		int scaleFactor = new ScaledResolution(this.mc).getScaleFactor();
		mouseX *= scaleFactor * 1.0;
		mouseY *= scaleFactor * 1.0;

		for (Panel panel : panels) {
			panel.mouseReleased(mouseX, mouseY);
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		if (close) return;
		super.handleMouseInput();

		for (Panel panel : panels) {
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
