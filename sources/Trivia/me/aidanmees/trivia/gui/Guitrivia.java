package me.aidanmees.trivia.gui;

import java.io.IOException;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class Guitrivia extends GuiScreen {
	private GuiScreen before;
	private Minecraft mc = Minecraft.getMinecraft();

	public Guitrivia(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2, 98, 20, "Client Settings"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 22, 98, 20, "Client Tools"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 44, 98, 20, "Advanced"));
		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, this.height / 2 + 44, 98, 20, "Credits"));
		this.buttonList.add(new GuiButton(5, this.width / 2 + 2, this.height / 2 + 22, 98, 20, "YouTube"));
		this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 2, 98, 20, "Changelog"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + 66, 200, 20, "Done"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if(id == 0) {
			mc.displayGuiScreen(new GuitriviaSettings(this));
		}
		if(id == 1) {
			mc.displayGuiScreen(new GuitriviaTools(this));
		}
		if(id == 2) {
			mc.displayGuiScreen(new GuitriviaAdvanced(this));
		}
		if(id == 3) {
			mc.displayGuiScreen(before);
		}
		if(id == 4) {
			mc.displayGuiScreen(new GuiMusic(this));
		}
		if(id == 5) {
			mc.displayGuiScreen(new GuiDashboard(this));
		}
		if(id == 6) {
			mc.displayGuiScreen(new GuitriviaChangelog(this));
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, trivia.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Menu", this.width / 2 / 2, (this.height / 2 - 25) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5) / 1, 0xffaaaaaa);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
