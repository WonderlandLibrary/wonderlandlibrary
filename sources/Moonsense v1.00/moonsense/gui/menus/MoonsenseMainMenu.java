package moonsense.gui.menus;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import moonsense.Client;
import moonsense.alt.GuiAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;

public class MoonsenseMainMenu extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(new ResourceLocation("moonsense/images/bg.png"));
		this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		
		if(!mc.isFullScreen()) {
			Gui.drawRect(520, 290, 280, 140, new Color(0, 0, 0, 170).getRGB());
			GlStateManager.pushMatrix();
			GlStateManager.translate(-(width/1f), -(height/1.8f), 0);
			GlStateManager.scale(3, 3, 3);
			
			this.drawCenteredString(mc.fontRendererObj, Client.INSTANCE.NAME, this.width / 2, this.height / 2 - 100, -1);
			GlStateManager.popMatrix();
		} else {
			Gui.drawRect(520, 310, 280, 155, new Color(0, 0, 0, 170).getRGB());
			GlStateManager.pushMatrix();
			GlStateManager.translate(-(width/1f), -(height/1.8f), 0);
			GlStateManager.scale(3, 3, 3);
			
			this.drawCenteredString(mc.fontRendererObj, Client.INSTANCE.NAME, this.width / 2, this.height / 2 - 110, -1);
			GlStateManager.popMatrix();
		}
		
//		GlStateManager.pushMatrix();
//		GlStateManager.translate(-(width/1f), -(height/1.8f), 0);
//		GlStateManager.scale(3, 3, 3);
//		
//		this.drawCenteredString(mc.fontRendererObj, Client.INSTANCE.name, this.width / 2, this.height / 2 - 100, -1);
//		GlStateManager.popMatrix();
//		GlStateManager.pushMatrix();
//		GlStateManager.scale(1.3, 1.3, 1);
//		this.drawString(fontRendererObj, "Welcome Back " + mc.session.getUsername(), this.width / 2 - 155, this.height / 2 + 20, -1);
//		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 50, "Singleplayer"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 27, "Multiplayer"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 - 4, "Cosmetics"));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + 19, "Settings"));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 2 + 42, "Exit Game"));
		this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 2 + 100, "Credits"));
		this.buttonList.add(new GuiButton(7, this.width / 2 - 400, this.height / 2 + 100, "Test Alt"));
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 1) {
			mc.displayGuiScreen(new GuiSelectWorld(this));
		}
		if(button.id == 2) {
			mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		if(button.id == 3) {
			mc.displayGuiScreen(new CosmeticGuiScreen());
		}
		if(button.id == 4) {
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
		}
		if(button.id == 5) {
			mc.shutdown();
		}
		if(button.id == 6) {
			mc.displayGuiScreen(new CreditsGuiScreen());
		}
		if(button.id == 7) {
			mc.displayGuiScreen(new GuiAltManager());
		}
		super.actionPerformed(button);
	}
	
	 private void switchToRealms()
	    {
	        RealmsBridge realmsbridge = new RealmsBridge();
	        realmsbridge.switchToRealms(this);
	    }
	
}
