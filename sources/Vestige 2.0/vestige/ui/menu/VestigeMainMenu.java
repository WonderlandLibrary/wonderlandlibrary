package vestige.ui.menu;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import vestige.Vestige;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.util.misc.TimerUtil;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;
import vestige.util.render.GLSLSandboxShader;
import vestige.util.sound.AudioUtil;

import java.awt.*;
import java.io.IOException;

public class VestigeMainMenu extends GuiScreen {

	private GLSLSandboxShader backgroundShader;
	private boolean shaderBound;
	
	public void initGui() {
		shaderBound = false;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		MinecraftFontRenderer fr = FontUtil.product_sans;
		ScaledResolution sr = new ScaledResolution(mc);

		this.a();
		
		boolean shaderEnabled = Vestige.getInstance().isShaderEnabled();

		if(shaderEnabled) {
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
		
		if(shaderEnabled) {
			DrawUtil.drawRoundedRect(sr.getScaledWidth() / 2 - 60, 120, sr.getScaledWidth() / 2 + 60, 155, 3, 0x19000000);
			DrawUtil.drawImage(new ResourceLocation("vestige/VestigeLogo.png"), sr.getScaledWidth() / 2 - 96, 100, 187, 75);
			DrawUtil.drawRoundedRect(sr.getScaledWidth() / 2 - 60, 120, sr.getScaledWidth() / 2 + 60, 155, 3, 0x10000000);
		} else {
			DrawUtil.drawImage(new ResourceLocation("vestige/VestigeLogo.png"), sr.getScaledWidth() / 2 - 96, 100, 187, 75);
		}
		
		Gui.drawRect(0, 0, 0, 0, 0);
		
		String[] buttons = {"Singleplayer", "Multiplayer", "Alt Manager", "Settings", "Credits", "Quit"};
		
		float offsetY = 200;
		for(String button : buttons) {
			boolean buttonHovered = mouseX >= sr.getScaledWidth() / 2 - 40 && mouseY >= offsetY - 6 && mouseX < sr.getScaledWidth() / 2 + 40 && mouseY < offsetY + 11;
			if(buttonHovered) {
				Gui.drawRect(sr.getScaledWidth() / 2 - 40, offsetY - 4, sr.getScaledWidth() / 2 + 40, offsetY + 15, 0x80000000);
			} else {
				Gui.drawRect(sr.getScaledWidth() / 2 - 40, offsetY - 4, sr.getScaledWidth() / 2 + 40, offsetY + 15, 0x80000000);
			}
			
			fr.drawStringWithShadow(button, sr.getScaledWidth() / 2 - (fr.getStringWidth(button) / 2), offsetY + 1.5F, new Color(215, 215, 215).getRGB());
			offsetY += 19;
		}
		
		if(!Vestige.getInstance().isShaderEnabled()) {
			Gui.drawRect(sr.getScaledWidth() / 2 - 72, sr.getScaledHeight() - 25, sr.getScaledWidth() / 2 + 72, sr.getScaledHeight() - 6, 0x95000000);
			String text = "Toggle animated background";
			fr.drawStringWithShadow(text, sr.getScaledWidth() / 2 - (fr.getStringWidth(text) / 2), sr.getScaledHeight() - 20, new Color(215, 215, 215).getRGB());
		}
	}
	
	private void a() {
		while(!Vestige.getInstance().authStep1 || !Vestige.getInstance().authStep2 || !Vestige.getInstance().authStep3 || !Vestige.getInstance().authStep4 || Vestige.getInstance().authStep5) {
			
		}
		
		if(Vestige.getInstance().getUsername().equals("test")) {
			while(396 != 286) {
				
			}
		}
		
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
	}
	
	private String[] b() {
		while(!Vestige.getInstance().authStep1 || !Vestige.getInstance().authStep2 || !Vestige.getInstance().authStep3 || !Vestige.getInstance().authStep4 || Vestige.getInstance().authStep5) {
			
		}
		
		String buttons[] = {"Singleplayer", "Multiplayer", "Alt Manager", "Settings", "Credits", "Quit"};
		return buttons;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		ScaledResolution sr = new ScaledResolution(mc);
		
		String[] buttons = this.b();
		
		float offsetY = 200;
		
		for(String buttonClicked : buttons) {
			boolean moduleHovered = mouseX >= sr.getScaledWidth() / 2 - 40 && mouseY >= offsetY - 6 && mouseX < sr.getScaledWidth() / 2 + 40 && mouseY < offsetY + 11;
			if(moduleHovered) {
				AudioUtil.buttonClick();
				switch(buttonClicked) {
					case "Singleplayer":
						mc.displayGuiScreen(new GuiSelectWorld(this));
						break;
					case "Multiplayer":
						mc.displayGuiScreen(new GuiMultiplayer(this));
						break;
					case "Alt Manager":
						mc.displayGuiScreen(Vestige.getInstance().getAltManager());
						break;
					case "Settings":
						mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
						break;
					case "Credits":
						mc.displayGuiScreen(new CreditsMenu());
						break;
					case "Quit":
						mc.shutdown();
						break;
				}
			}
			offsetY += 19;
		}
		
		if(!Vestige.getInstance().isShaderEnabled()) {
			Gui.drawRect(sr.getScaledWidth() / 2 - 72, sr.getScaledHeight() - 25, sr.getScaledWidth() / 2 + 72, sr.getScaledHeight() - 6, 0x95000000);
			
			if(mouseX > sr.getScaledWidth() / 2 - 72 && mouseX < sr.getScaledWidth() / 2 + 72 && mouseY > sr.getScaledHeight() - 25 && mouseY < sr.getScaledHeight() - 6) {
				Vestige.getInstance().setShaderEnabled(true);
				AudioUtil.buttonClick();
			}
		}
	}
	
	public void onGuiClosed() {
		
	}
	
}
