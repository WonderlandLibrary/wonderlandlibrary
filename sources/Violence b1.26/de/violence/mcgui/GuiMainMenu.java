package de.violence.mcgui;

import de.violence.Violence;
import de.violence.font.FontManager;
import de.violence.mcgui.AccountManager;
import de.violence.mcgui.utils.Ball;
import de.violence.mcgui.utils.GuiMains;
import de.violence.ui.Colours;
import java.awt.Desktop;
import java.net.URI;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiMainMenu extends GuiScreen {
	static int x = 0;
	static int a = 0;
	static boolean b = false;

	public void initGui() {
		this.buttonList.clear();
		x = 0;
		a = 0;
		if (!b) {
			Ball.instances.clear();

			for (int i = 0; i < 40; ++i) {
				Ball.instances.add(new Ball((double) (new Random()).nextInt(ScaledResolution.getScaledWidth()),
						(double) (new Random()).nextInt(ScaledResolution.getScaledHeight())));
			}
		}

		b = true;
		super.initGui();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(),
				Colours.getColor(0, 0, 0, 255));
		GlStateManager.disableTexture2D();
		Iterator a = Ball.instances.iterator();

		while (a.hasNext()) {
			Ball line = (Ball) a.next();
			line.render();
		}

		String line1 = Violence.NAME;
		if (GuiMainMenu.x < FontManager.mainMenu.getStringWidth(line1)) {
			GuiMainMenu.x += 2;
		}
		if (GuiMainMenu.a < 255) {
			GuiMainMenu.a += 5;
		}

		this.drawString(this.mc.fontRendererObj, "x", -11111, -11111, -1);
		FontManager.mainMenu.drawCenteredString(line1, ScaledResolution.getScaledWidth() / 2 - 2,
				ScaledResolution.getScaledHeight() / 2 - 10, -1);
		byte a1 = 5;
		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		int x = 1;
		if (GuiMains.isHovered(mouseX, mouseY, 1, 10, FontManager.mainMenu.getStringWidth("Singleplayer"), 6)) {
			x += a1;
			if (Mouse.isButtonDown(0)) {
				this.mc.displayGuiScreen(new GuiSelectWorld(this));
			}
		}

		FontManager.mainMenu.drawString("Singleplayer", x, 10, -1);
		x = 1;
		if (GuiMains.isHovered(mouseX, mouseY, 0, 20, FontManager.mainMenu.getStringWidth("Multiplayer"), 6)) {
			x += a1;
			if (Mouse.isButtonDown(0)) {
				this.mc.displayGuiScreen(new GuiMultiplayer(this));
			}
		}

		FontManager.mainMenu.drawString("Multiplayer", x, 30, -1);
		x = 1;
		if (GuiMains.isHovered(mouseX, mouseY, 0, 30, FontManager.mainMenu.getStringWidth("Alt Manager"), 6)) {
			x += a1;
			if (Mouse.isButtonDown(0)) {
				this.mc.displayGuiScreen(new AccountManager(this));
			}
		}

		FontManager.mainMenu.drawString("Alt Manager", x, 50, -1);
		x = ScaledResolution.getScaledWidth() * 2 - FontManager.mainMenu.getStringWidth("Connect to Discord") - 10;
		if (GuiMains.isHovered(mouseX, mouseY,
				ScaledResolution.getScaledWidth() - FontManager.mainMenu.getStringWidth("Connect to Discord"), 0,
				FontManager.mainMenu.getStringWidth("Connect to Discord"), 6)) {
			x += a1;
			if (Mouse.isButtonDown(0)) {
				try {
					Desktop.getDesktop().browse(new URI("https://discord.gg/ZGWm6hg"));
					Mouse.destroy();
					Mouse.create();
				} catch (Exception var8) {
					;
				}
			}
		}

		FontManager.mainMenu.drawString("Connect to Discord", x, 0, -1);
		FontManager.mainMenu.drawShadowString("StaticCode & LiquidDev", ScaledResolution.getScaledWidth() * 2 / 2 - 95,
				ScaledResolution.getScaledHeight() * 2 / 2 + 20, -1);
		FontManager.mainMenu.drawString(Violence.VERSION,
				ScaledResolution.getScaledWidth() * 2 - FontManager.mainMenu.getStringWidth(Violence.VERSION) * 2 + 15,
				ScaledResolution.getScaledHeight() * 2 - 25, -1);
		GlStateManager.disableTexture2D();
		GL11.glPopMatrix();
		FontManager.clickGUI.drawShadowString("Designed by kureji", 1, ScaledResolution.getScaledHeight() - 10, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
