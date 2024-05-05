package me.aidanmees.trivia.gui;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.cracker.gui.GuitriviaAccHacker;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

public class GuitriviaSettings extends GuiScreen {
	private int offset = 44;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuitriviaSettings(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.height / 2 - offset, 160, 20, "Reset all Keybinds"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 80, this.height / 2 + 22 - offset, 160, 20, "Reset all Friends"));

		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, height - 50, 200, 20, "Done"));

		this.buttonList.add(new GuiButton(4, this.width / 2 - 80, this.height / 2 + 44 - offset, 160, 20, "Change ClickGUI Keybind"));
		
		this.buttonList.add(new GuiButton(5, this.width / 2 - 80, this.height / 2 + 66 - offset, 160, 20, ClientSettings.bigWaterMark ? "Watermark: Big" : "Watermark: Small"));
		
		this.buttonList.add(new GuiButton(6, this.width / 2 - 80, this.height / 2 + 88 - offset, 160, 20, ClientSettings.tabGui ? "TabGui: Enabled" : "TabGui: Disabled"));

		// this.buttonList.add(new GuiButton(6, this.width / 2 + 2,
		// 94, 198, 20, "Account Hacker"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			for (Module module : trivia.getModules()) {
				module.setKeyCode(module.getDefaultKeyboardKey());
			}
		}
		if (button.id == 1) {
			trivia.getFriendsMananger().getFriends().clear();
			trivia.getFileMananger().friendsDir.delete();
			this.onGuiClosed();
		}
		if (button.id == 2) {
			for (EntityPlayer en : mc.theWorld.playerEntities) {
				if (mc.thePlayer.getDistanceToEntity(en) <= 24) {
					trivia.getFriendsMananger().getFriends().add(en.getName());
				}
			}
		}
		if (button.id == 3) {
			mc.displayGuiScreen(before);
		}
		if (button.id == 4) {
			mc.displayGuiScreen(new GuitriviaKeyBind(trivia.getModuleByName("ClickGUI"), this));
		}
		if (button.id == 6) {
			mc.displayGuiScreen(new GuitriviaAccHacker(this));
		}
		if (button.id == 5) {
			ClientSettings.bigWaterMark = !ClientSettings.bigWaterMark;
			mc.displayGuiScreen(new GuitriviaSettings(before));
		}
		if (button.id == 6) {
			ClientSettings.tabGui = !ClientSettings.tabGui;
			mc.displayGuiScreen(new GuitriviaSettings(before));
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void onGuiClosed() {
		if (closed = false) {
			mc.displayGuiScreen(before);
			closed = true;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, trivia.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - offset) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Settings", this.width / 2 / 2, (this.height / 2 - 25 - offset) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - offset) / 1, 0xffaaaaaa);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
