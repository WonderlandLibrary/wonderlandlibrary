package com.enjoytheban.utils.proxy;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

/**
 * Created by Jutting on Oct 27, 2018
 */

public class GuiProxy extends GuiScreen {
	private GuiMultiplayer prevMenu;
	private GuiTextField proxyBox;
	private String error = "";

	public static boolean connected = false;

	public GuiProxy(GuiMultiplayer guiMultiplayer) {
		prevMenu = guiMultiplayer;
	}

	@Override
	public void updateScreen() {
		proxyBox.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, "Back"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 72 + 12, "Connect"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 12, "Disconnect"));
		proxyBox = new GuiTextField(0, fontRendererObj, width / 2 - 100, 60, 200, 20);
		proxyBox.setFocused(true);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton clickedButton) {
		if (clickedButton.enabled)
			if (clickedButton.id == 0) {
				mc.displayGuiScreen(prevMenu);
			} else if (clickedButton.id == 1) {
				if (!proxyBox.getText().contains(":") || proxyBox.getText().split(":").length != 2) {
					error = "Not a proxy!";
					return;
				}
				String[] parts = proxyBox.getText().split(":");

				if (!isInteger(parts[1]) || Integer.parseInt(parts[1]) > 65536 || Integer.parseInt(parts[1]) < 0) {
				}
				try {
					ProxyConfig.proxyAddr = new ConnectionInfo();
					ProxyConfig.proxyAddr.ip = parts[0];
					ProxyConfig.proxyAddr.port = Integer.parseInt(parts[1]);
					connected = true;
				} catch (Exception e) {
					error = e.toString();
					return;
				}
				if (error.isEmpty()) {
					//mc.displayGuiScreen(prevMenu);
				} else
					return;
			} else if (clickedButton.id == 2) {
				ProxyConfig.stop();
				connected = false;
				// mc.displayGuiScreen(prevMenu);
			}
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		proxyBox.textboxKeyTyped(par1, par2);

		if (par2 == 28 || par2 == 156)
			actionPerformed((GuiButton) buttonList.get(1));
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		super.mouseClicked(par1, par2, par3);
		proxyBox.mouseClicked(par1, par2, par3);
		if (proxyBox.isFocused())
			error = "";
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		mc.fontRendererObj.drawCenteredString("Proxies Reloaded", width / 2, 20, 0xFFFFFF);
		mc.fontRendererObj.drawCenteredString("(SOCKS5 Proxies only)", width / 2, 30, 0xFFFFFF);
		mc.fontRendererObj.drawStringWithShadow("IP:Port", width / 2 - 100, 47, 0xA0A0A0);
		mc.fontRendererObj.drawCenteredString(error, width / 2, 95, 0xFF0000);
		String currentProxy = "";
		if (connected) {
			currentProxy = "§a" + ProxyConfig.proxyAddr.ip + ":" + ProxyConfig.proxyAddr.port;
		}
		if (!connected) {
			currentProxy = "§cN/A";
		}
		mc.fontRendererObj.drawStringWithShadow("Current proxy: " + currentProxy, 1, 3, -1);
		proxyBox.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}