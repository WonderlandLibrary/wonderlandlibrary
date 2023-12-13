package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawCredits extends GuiScreen {

	private GuiScreen before;

	public GuiJigsawCredits(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 20, "Okay!"));
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
		this.drawDefaultBackground();
		this.drawRect(20, 20, width - 20, height - 20, 0x50000000);
		GlStateManager.scale(2, 2, 0);
		this.drawString(fontRenderer, "Coder - Robofån", 25 / 2, 25 / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 0);
		this.drawString(fontRenderer, "People who Helped / Are Really Nice:", 25, 60, 0xffffffff);
		this.drawString(fontRenderer, "IsakTheKing", 30, 75, 0xffffffff);
		this.drawString(fontRenderer, "AdvancedAce", 30, 85, 0xffffffff);
		this.drawString(fontRenderer, "PandaAnimations", 30, 95, 0xffffffff);
		this.drawString(fontRenderer, "TheRainbowKoalas", 30, 105, 0xffffffff);
		this.drawString(fontRenderer, "ProMcHacks - Helped me with integrating MCLeaks a bit. Thank you! §c<3", 30,
				115, 0xffffffff);
		this.drawString(fontRenderer,
				"§6iDuckYT / WellThatsCute§r - Super nice, has a youtube channel if you want to check it out (§cYT§r:WellThatsCute) §6QUACK",
				30, 125, 0xffffffff);
		this.drawString(fontRenderer,
				"§bYario§r - Helped me with NCP AirJump, Flight Kick Bypass, NCP AntiWeb, SafeWalk Thanks man!! :D", 30, 135,
				0xffffffff);
		this.drawString(fontRenderer, "§6Novitex§r - Test subject for old TpAura!", 30, 145, 0xffffffff);
		this.drawString(fontRenderer, "§6AckroyJR§r - Nicest guy on planet earth (Also tested reach and stuff)!", 30,
				155, 0xffffffff);
		this.drawString(fontRenderer, "§6mcresolver.pw§r - IP Resolving (doesnt work anymore but I'll leave it here as a thanks)", 30,
				165, 0xffffffff);
		this.drawString(fontRenderer, "§bMGames§r - AAC Flight, Gave me the idea to try infinite reach on Cubecraft when it worked", 30,
				175, 0xffffffff);
		this.drawString(fontRenderer, "§bCaleb Anderson§r - Tons of suggestions and helps a lot with the Discord server", 30,
				185, 0xffffffff);
		this.drawString(fontRenderer, "§7Meldoux§cOfficial§r - Gave me a free premium altdispenser account! Thanks <3 §bTwitter: §r@MeldouxOfficial", 30,
				195, 0xffffffff);
		this.drawString(fontRenderer, "§cSenk Ju§r - Helped my with the new website, git and more. He is a helper for the LiquidBounce client. Really nice :)", 30,
				205, 0xffffffff);
		this.drawString(fontRenderer,
				"§cmoneycat is bae§r", 30, 215, 0xffffffff);
		this.drawString(fontRenderer,
				"§9XPross §3Center§r - A really nice guy with a dank yt channel called XPross Center!", 30, 225, 0xffffffff);
		this.drawString(fontRenderer,
				"§rHe was one of the first youtubers to make a video with Jigsaw", 30, 235, 0xffffffff);
		this.drawString(fontRenderer, "§cHerrKanelBulle§r - NewNCP-1 speed, Tick aura", 30,
				245, 0xffffffff);
		this.drawString(fontRenderer, "§8xDark§r - ToTheDumpster ServerCrasher mode", 30,
				255, 0xffffffff);
		this.drawString(fontRenderer, "Tomygames§r - Blurring screen with shaders because I was stupid enough to not think about using shaders", 30,
				265, 0xffffffff);
		this.drawString(fontRenderer, "§cGingerGoat69§r - StrongholdFinder triangulation", 30,
				275, 0xffffffff);
		this.drawString(fontRenderer, "§cDepressino§r - Idea how to made ElytraHack bypass ncp", 30,
				285, 0xffffffff);
		this.drawString(fontRenderer, "§lYago§r - Awesome tester for 1.12.2, reported like 20 bugs which was fixed before full release", 30,
				295, 0xffffffff);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
