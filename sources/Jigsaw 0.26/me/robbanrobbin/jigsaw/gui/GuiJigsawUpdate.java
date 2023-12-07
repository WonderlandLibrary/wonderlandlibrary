package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.nodes.Element;
import org.lwjgl.input.Mouse;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawUpdate extends GuiScreen {

	private int scroll = 0;

	@Override
	public void initGui() {

		this.buttonList.add(new GuiButton(0, width / 2 - 100, 90 - scroll, 98, 20, "Update!", true));
		this.buttonList.add(new GuiButton(1, width / 2 + 2, 90 - scroll, 98, 20, "I want bugs...", true));

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		drawRect(0, 0, width, height, 0xff500030);
		drawDefaultBackground();

		drawCenteredString(Fonts.font55, "Your Jigsaw version is outdated!", width / 2, 40 - scroll, 0xffffff);

		drawCenteredString(Fonts.font19, "Your version is: §c" + Jigsaw.getClientVersion(), width / 2, 60 - scroll,
				0xffffff);

		drawCenteredString(Fonts.font19, "Recommended version is: §6" + Jigsaw.serverVersion, width / 2, 70 - scroll,
				0xffffff);
		
		drawRect(width / 2 - 300, 120 - scroll, width / 2 + 300, height, 0x40000000);
		
		drawHorizontalLine(width / 2 - 300, width / 2 + 300, 120 - scroll, 0xffffffff);
		
		drawCenteredString(Fonts.font55, "Changelog:", width / 2, 140 - scroll, 0xffffffff);
		
		if (!Jigsaw.changelogFailed) {
			if (Jigsaw.changeLineElmts != null) {
				
				int sub = 160;
				for (Element elmt : Jigsaw.changeLineElmts) {
					sub += 5;
					if (elmt.ownText().trim().equals(Jigsaw.getClientVersion())) {
						drawCenteredString(Fonts.font55, "§c§l" + elmt.ownText() + "§6 - Your version", width / 2, sub - scroll,
								0xffffff);
					} else if (elmt.ownText().trim().equals(Jigsaw.serverVersion)) {
						drawCenteredString(Fonts.font55, "§e§l" + elmt.ownText() + "§6 - Latest version", width / 2, sub - scroll,
								0xffffff);
					} else {
						drawCenteredString(Fonts.font55, "§l" + elmt.ownText(), width / 2, sub - scroll, 0xffffff);
					}

					sub += 20;
					for (Element elmtChild : elmt.child(0).children()) {
						drawCenteredString(Fonts.font18,
								elmtChild.text()
										.replaceAll("Removed", "§cRemoved§r")
										.replaceAll("removed", "§cremoved§r").replaceAll("Fixed", "§bFixed§r")
										.replaceAll("fixed", "§bfixed§r").replaceAll("Added", "§aAdded§r")
										.replaceAll("added", "§aadded§r").replaceAll("bug", "§7bug§r")
										.replaceAll("§7bug§rs", "§7bugs§r").replaceAll("Bug", "§7Bug§r")
										.replaceAll("§7Bug§rs", "§7Bugs§r").replaceAll("Buggy", "§7Buggy§r"),
										width / 2, sub - scroll, 0xcfcfcf);
						sub += 10;
					}
				}
				drawRect(width - 10, 0, width, height, 0xff000000);
				drawRect(width - 10, 0 + scroll, width, 145 + scroll, 0xffffffff);
				drawRect(width - 8, 0 + scroll, width, 145 + scroll, 0xffcccccc);
				drawRect(width - 8, 0 + scroll, width - 2, 145 + scroll, 0xffdddddd);
			} else {
				drawCenteredString(Fonts.font18, "§7Loading changelog...", 4, height / 2 - 30 - scroll, 0xffffff);
			}
		} else {
			drawCenteredString(Fonts.font18, "§cCould not get changelog!", 4, height / 2 - 30 - scroll, 0xffffff);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}
			i *= 20;
			if (scroll - i < 0) {
				return;
			}
			this.scroll -= i;
			for (int ii = 0; ii < this.buttonList.size(); ++ii) {
				((GuiButton) this.buttonList.get(ii)).yPosition += i;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if (id == 0) {
			try {
				this.openWebLink(new URI("https://www.jigsawclient.ml/#download"));
				mc.shutdown();
			} catch (URISyntaxException e) {
				mc.displayGuiScreen(new GuiJigsawOpenLinkFailed());

			}
		}
		if (id == 1) {
			mc.displayGuiScreen(new GuiMainMenu());
			Jigsaw.triedConnectToUpdate = true;
		}
		super.actionPerformed(button);
	}

	// No escape here
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

	}

}
