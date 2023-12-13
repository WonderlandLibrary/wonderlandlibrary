package vestige.ui.menu;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import vestige.Vestige;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.util.render.GLSLSandboxShader;
import vestige.util.sound.AudioUtil;

public class CreditsMenu extends GuiScreen {
	
	private GLSLSandboxShader backgroundShader;
	private boolean shaderBound;
	
	public void initGui() {
		shaderBound = false;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		MinecraftFontRenderer fr = FontUtil.product_sans;
		ScaledResolution sr = new ScaledResolution(mc);
		
		if(Vestige.getInstance().isShaderEnabled()) {
			if(!shaderBound) {
				GlStateManager.disableCull();

				try {
					this.backgroundShader = new GLSLSandboxShader("/background.fsh");
				} catch (IOException e) {
					e.printStackTrace();
				}
				shaderBound = true;
			}
		}
		
		if(Vestige.getInstance().isShaderEnabled()) {
			GlStateManager.enableAlpha();

			this.backgroundShader.useShader(this.width, this.height, mouseX, mouseY, Vestige.getInstance().getShaderTimeElapsed() / 1000F);

			GL11.glBegin(GL11.GL_QUADS);

			GL11.glVertex2f(-1F, -1F);
			GL11.glVertex2f(-1F, 1F);
			GL11.glVertex2f(1F, 1F);
			GL11.glVertex2f(1F, -1F);

			GL11.glEnd();

			GL20.glUseProgram(0);
		} else {
			mc.getTextureManager().bindTexture(new ResourceLocation("vestige/background.jpg"));
			this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		}
		
		int color = new Color(215, 215, 215).getRGB();
		
		String creditsMenu = "Credits menu";
		
		fr.drawStringWithShadow(creditsMenu, sr.getScaledWidth() / 2 - fr.getStringWidth(creditsMenu) / 2, 50, color);
		
		fr.drawStringWithShadow("YesCheatPlus : main developer of Vestige", 50, 100, color);
		fr.drawStringWithShadow("Quick : helped me with obfuscation and protection", 50, 125, color);
		fr.drawStringWithShadow("Flaily : made the redesky glide (a year ago, when that server was still a thing)", 50, 150, color);
		fr.drawStringWithShadow("Skizzme : helped me a lot when I started making my first client", 50, 175, color);
		fr.drawStringWithShadow("teqhs : gave me a strafe disabler back in february, and helped me a bit with hypixel bypasses recently", 50, 200, color);
		
		fr.drawStringWithShadow("Scaffold block pos and facing finder : Superblaubeere27", 50, 250, color);
		fr.drawStringWithShadow("Inventory Manager and AutoArmor : Sigma 4.11", 50, 275, color);
		
		String back = "Back";
		
		Gui.drawRect(sr.getScaledWidth() / 2 - 25, sr.getScaledHeight() - 37, sr.getScaledWidth() / 2 + 25, sr.getScaledHeight() - 14, 0x80000000);
		fr.drawStringWithShadow(back, sr.getScaledWidth() / 2 - fr.getStringWidth(back) / 2, sr.getScaledHeight() - 30, color);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		ScaledResolution sr = new ScaledResolution(mc);
		
		if(mouseX > sr.getScaledWidth() / 2 - 25 && mouseX < sr.getScaledWidth() / 2 + 25 && mouseY > sr.getScaledHeight() - 37 && mouseY < sr.getScaledHeight() - 14) {
			mc.displayGuiScreen(new VestigeMainMenu());
			AudioUtil.buttonClick();
		}
	}
	
}