package me.aidanmees.trivia.cracker.gui;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.cracker.PortscanManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;

public class GuiScan extends GuiScreen {

	private GuiScreen before;
	private GuiTextField emailField;
	private String status = "idle";
	private String Scanning = "Scanning: Nothing";

	public GuiScan(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {

		Keyboard.enableRepeatEvents(true);
		GuiButton crackBtn = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 22, 100, 20, "Scan!");
		
		this.buttonList.add(crackBtn);
		GuiButton crackBtn2 = new GuiButton(2, this.width / 2, this.height / 2 + 22, 100, 20, "Stop!");
		if (trivia.getCrackManager() != null && trivia.getCrackManager().isChecking) {
			crackBtn.enabled = false;
			crackBtn2.enabled = true;
		}
		else {
			crackBtn.enabled = true;
			crackBtn2.enabled = false;
		}
		this.buttonList.add(crackBtn2);
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 44, 200, 20, "Back"));
		emailField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 2, 200, 20);
		emailField.setMaxStringLength(254);
		emailField.setFocused(true);
		emailField.setText("");
		
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if(button.enabled) {
			if (id == 0) {
				for (int i = 25500; i < 30000; i ++) {
					int B = i;
					Scanning = "Scanning: " + i;
					System.out.println(i);
				 new Thread(){

		                @Override
		                public void run() {
		                    try {
		                    	
		                      
		                    }
		                    catch (Exception ex) {
		                       
		                       
		                      
		                       
		                    }
		                }
		            }.start();
				}
				super.actionPerformed(button);
				return;
			}
			if (id == 1) {
				this.mc.displayGuiScreen(before);
			}
			if (id == 2) {
				trivia.getCrackManager().onStop();
				trivia.getCrackManager().isChecking = false;
				buttonList.get(0).enabled = true;
				buttonList.get(1).enabled = false;
				super.actionPerformed(button);
				return;
			}
		}
		
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		
		
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, trivia.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - 25) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7PortScan", this.width / 2 / 2, (this.height / 2 - 25 - 25) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - 25) / 1, 0xffaaaaaa);
		
		drawCenteredString(fontRendererObj, "§cTries every Port of the a Server IP.", this.width / 2 / 1, (this.height / 2 - 22) / 1, 0xffffffff);
		drawString(fontRendererObj, "§7Server IP:", this.width / 2 - 100, (this.height / 2 - 11) / 1, 0xffffffff);
		drawString(fontRendererObj, "§7"+Scanning, this.width - 100, (this.height / 50) / 1, 0xffffffff);
		emailField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		emailField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		emailField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		this.status = (trivia.getCrackManager() != null && trivia.getCrackManager().isChecking) ? "Checking every single port..."
				: "idle";
		emailField.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}
}
