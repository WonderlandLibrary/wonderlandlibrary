package me.robbanrobbin.jigsaw.gui.custom.guimananger;

import me.robbanrobbin.jigsaw.gui.NewGuiManager;
import net.minecraft.client.gui.GuiScreen;

public class GuiDisplay extends GuiScreen {

	private NewGuiManager guiManager;

	public GuiDisplay(NewGuiManager guiManager) {
		this.guiManager = guiManager;
	}

	@Override
	public void drawScreen(int par2, int par3, float par4) {
		guiManager.render();
		super.drawScreen(par2, par3, par4);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
