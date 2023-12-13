package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.input.Mouse;

import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiJigsawWelcome extends GuiScreen {

	private GuiScreen before;

	public GuiJigsawWelcome(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 65, 200, 20, "Okay, let me play!", true));
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(before);
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		int xMid = width / 2;
		int yMid = height / 2;
		
		int top = yMid - 100;
		int left = xMid - 150;
		int right = xMid + 150;
		int bottom = yMid + 100;
		
		GuiUtils.drawBlurBuffer(left, top, right, bottom, true);
		
		drawRect(xMid - 150, yMid - 100, xMid + 150, yMid + 100, 0xca000000);
		
		float startAlpha = 0.4f;
		int size = 5;
		
		GuiUtils.enableDefaults();
		
		GuiUtils.renderShadowHorizontal(startAlpha, size, top, left, right, true, true);
		GuiUtils.renderShadowHorizontal(startAlpha, size, bottom, left, right, false, true);
		
		GuiUtils.renderShadowVertical(startAlpha, size, right, top, bottom, true, true);
		GuiUtils.renderShadowVertical(startAlpha, size, left, top + 0.5, bottom + 0.5, false, true);
		
		
		GuiUtils.disableDefaults();
		GuiUtils.enableTextureDefaults();
		
		drawCenteredString(Fonts.font55, "Thank you for downloading Jigsaw!", xMid, yMid - 88, 0xffffff);
		
		drawCenteredString(Fonts.font18, "Press §jleft ctrl§r to access the §jClick GUI", xMid, yMid - 60, 0xcfcfcf);
		drawCenteredString(Fonts.font18, "Press §jU§r to access the §jConsole", xMid, yMid - 45, 0xcfcfcf);
		drawCenteredString(Fonts.font18, "Press §jP§r to §jhide§r the client from §jMinecraft", xMid, yMid - 30, 0xcfcfcf);
		
		drawCenteredString(Fonts.font18, "If you encounter any §7bugs§r, please report them to my Twitter account", xMid, yMid - 5, 0xcfcfcf);
		drawCenteredString(Fonts.font18, "§7@JigsawClient", xMid, yMid + 5, 0xcfcfcf);
		
		drawCenteredString(Fonts.font18, "A lot of §chard work§r went into this project, so feel free making", xMid, yMid + 25, 0xcfcfcf);
		drawCenteredString(Fonts.font18, "§cYouTube§r videos. Just make sure to link to the website:", xMid, yMid + 35, 0xcfcfcf);
		
		boolean linkHovered = false;
		
		if(mouseX > xMid - Fonts.font18.getStringWidth("§bhttps://jigsawclient.net/#download") / 2
				&& mouseX < xMid + Fonts.font18.getStringWidth("§bhttps://jigsawclient.net/#download") / 2
				&& mouseY > yMid + 45
				&& mouseY < yMid + 45 + 8) {
			linkHovered = true;
		}
		
		drawCenteredString(Fonts.font18, ((linkHovered ? "§n" : "§b") + "https://jigsawclient.net/#download"), xMid, yMid + 45, 0xcfcfcf);
		
		if(linkHovered) {
			if(Mouse.isButtonDown(0)) {
				try {
					openWebLink(new URI("https://jigsawclient.net/#download"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
