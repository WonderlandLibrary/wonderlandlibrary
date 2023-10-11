
package markgg.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.opengl.GL11;

import markgg.RazeClient;
import markgg.alts.GuiAltManager;
import markgg.config.ConfigSystem;
import markgg.config.KeybindSystem;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.ui.click.astolfo.AstolfoGui;
import markgg.ui.gui.GuiChangelog;
import markgg.ui.gui.GuiCredits;
import markgg.util.others.RandomUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;

public class MainMenu extends GuiScreen{

	public MainMenu() {}

	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			mc.displayGuiScreen(new GuiSelectWorld(this));
			break;
		case 2:
			mc.displayGuiScreen(new GuiMultiplayer(this));
			break;
		case 3:
			mc.displayGuiScreen(new GuiAltManager());
			break;
		case 4:
			mc.displayGuiScreen(new GuiCredits());
			break;
		case 5:
			mc.displayGuiScreen(new GuiChangelog());
			
			break;
		case 6:
			mc.displayGuiScreen(new GuiOptions(this,mc.gameSettings));
			break;
		case 7:
			mc.shutdown();
			break;
		case 8:
			Desktop d = Desktop.getDesktop();
		    try {
				d.browse(new URI("https://discord.gg/qynVdyQaXe"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
			break;
		} 
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRenderer fr = mc.fontRendererObj;
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);

		this.drawDefaultBackground();

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.7, 0.7, 1);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(width/2f, height/2f, 0);
		GlStateManager.scale(2, 2, 1);
		GlStateManager.translate(-(width/2f), -(height/2f), 0);

		int stringWidth = mc.fontRendererObj.getStringWidth("Raze");
		int centerX = Math.round(width / 2f);
		int centerY = Math.round(height / 2f - mc.fontRendererObj.FONT_HEIGHT / 2f);

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(width/2f, height/2f, 0);
		GlStateManager.scale(3, 3, 1);
		GlStateManager.translate(-(width/2f), -(height/2f), 0);


		fr.drawStringWithShadow("R"+ EnumChatFormatting.WHITE + "aze", centerX - stringWidth / 2, centerY - 22, 0xFFE44964);

		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
	    buttonList.clear();
	    buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 - 40, "Singleplayer"));
	    buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 - 20, "Multiplayer"));
	    buttonList.add(new GuiButton(3, width / 2 - 100, height / 2, "Alt Manager"));
	    buttonList.add(new GuiButton(4, width / 2, height / 2 + 20, 100, 20, "Credits"));
	    buttonList.add(new GuiButton(5, width / 2 - 100, height / 2 + 20, 100, 20, "Changlelog"));
	    buttonList.add(new GuiButton(6, width / 2 - 100, height / 2 + 40, 100, 20, "Options"));
	    buttonList.add(new GuiButton(7, width / 2, height / 2 + 40, 100, 20, "Quit"));
	    buttonList.add(new GuiButton(8, width - 60, height - 20, 60, 20, "Discord"));
	}


}
