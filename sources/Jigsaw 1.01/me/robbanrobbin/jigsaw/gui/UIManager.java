package me.robbanrobbin.jigsaw.gui;

import java.awt.Font;
import java.util.ArrayList;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ComponentWindow;
import me.robbanrobbin.jigsaw.hackerdetect.gui.GuiJigsawHackerDetect;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class UIManager extends GuiScreen {
	
	ArrayList<String> objectqueue = new ArrayList<String>();
	ArrayList<ScreenPos> enumqueue = new ArrayList<ScreenPos>();
	public FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 19));
	boolean enabled = true;
	private Minecraft mc = Minecraft.getMinecraft();
	String toDraw = Jigsaw.getClientName() + " v" + Jigsaw.getClientVersion();
	
	private ModuleList moduleList;

	public UIManager() {
		this.mc = Minecraft.getMinecraft();
		moduleList = new ModuleList();
	}

	public void addToQueue(String s, ScreenPos pos) {
		objectqueue.add(s);
		enumqueue.add(pos);
	}
	
	public void update() {
		
		ScaledResolution resolution = new ScaledResolution(mc);
		this.width = resolution.getScaledWidth();
		this.height = resolution.getScaledHeight();
		
		ComponentWindow firstWindow = Jigsaw.getClickGuiManager().windows.get(0);
		if(!firstWindow.getForeground().equals(ClientSettings.getForeGroundGuiColor())) {
			Jigsaw.getClickGuiManager().updateColorsForAllComponents();
		}
		if(!firstWindow.getBackground().equals(ClientSettings.getBackGroundGuiColor())) {
			Jigsaw.getClickGuiManager().updateColorsForAllComponents();
		}
		
	}

	public void tickQueues() {
		int i = 0;
		int offset = Jigsaw.getTabGui().rootContainer.y + Jigsaw.getTabGui().rootContainer.height;
		if (!ClientSettings.tabGui) {
			offset = ClientSettings.bigWaterMark ? 28 : 14;
		}
		for (ScreenPos p : enumqueue) {
			addToScreen(objectqueue.get(i), p, offset);
			offset += 10;
			i++;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void render(boolean renderModules) {
		if (!enabled || mc.gameSettings.showDebugInfo || mc.currentScreen instanceof GuiJigsawHackerDetect) {
			enumqueue.clear();
			return;
		}
		GlStateManager.enableBlend();
		if (ClientSettings.bigWaterMark) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.resetColor();
			GlStateManager.enableTexture2D();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.scale(0.285, 0.285, 1);
			GlStateManager.translate(4, -97, 0);
			mc.getTextureManager().bindTexture(Jigsaw.jigsawTexture512);
			GlStateManager.color(0.8f, 0.3f, 0.3f);
			drawTexturedModalRect(2, 2, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.color(1f, 1f, 1f);
			drawTexturedModalRect(0, 0, 0, 0, 512 / 2, 512 / 2);
			GlStateManager.popMatrix();
		} else {
			double xOffset = 1;
			Fonts.font55.drawString("Jigsaw", 2.5 + xOffset, 1.5, 0xaa000000);
			
			Fonts.font55.drawString("Jig", 2 + xOffset, 1, ClientSettings.getForeGroundGuiColor().brighter());
			Fonts.font55.drawString("§8saw", 2 + xOffset + Fonts.font55.getStringWidth("Jig"), 1, 0xffffffff);

			Fonts.font18.drawString("1.12.2", 45.5 + xOffset, 2.5, 0xaa000000);
			Fonts.font18.drawString("§71.12.2", 45 + xOffset, 2, 0xffffffff);
		}
		
		if (mc.currentScreen == null) {
			GlStateManager.translate(0, 0, 1000);
			Fonts.font18.drawStringWithShadow(Jigsaw.getClientVersion(), 1, height - 8, 0xff9a9a9a);
			GlStateManager.translate(0, 0, -1000);
		}

		GlStateManager.translate(0, 5, 0);
		tickQueues();
		GlStateManager.translate(0, -5, 0);
		int y = 1;
		

		if (Jigsaw.getTabGui() != null) {
			Jigsaw.getTabGui().render();
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(Jigsaw.jigsawTexture512);
			GlStateManager.popMatrix();
		}
		
		
		if (renderModules) {
			moduleList.render(width, height);
		}
		objectqueue.clear();
		enumqueue.clear();
		
		if(mc.currentScreen == null || mc.currentScreen instanceof GuiChat) {
			GlStateManager.pushMatrix();
			ScaledResolution resolution = new ScaledResolution(mc);
			GlStateManager.scale(2d / resolution.getScaleFactor(), 2d / resolution.getScaleFactor(), 1);
			for(ComponentWindow window : Jigsaw.getClickGuiManager().windows) {
				if(window.isPinned() && window.isEnabled()) {
					window.draw();
				}
			}
			GlStateManager.popMatrix();
		}
		
		if (!Jigsaw.java8 && mc.currentScreen != null) {
			GlStateManager.pushMatrix();
			String s = "You may have an old version of java: " + System.getProperty("java.version")
					+ ", please update to " + "Java 8!";
			mc.fontRenderer.drawStringWithShadow(s, (width) / 2 - mc.fontRenderer.getStringWidth(s) / 2, 2,
					0xffbbbbbb);
			GlStateManager.popMatrix();
		}
	}

	public void addToScreen(String text, ScreenPos pos, int offset) {
		GlStateManager.translate(0, 0, -1);
		if (pos == ScreenPos.LEFTUP) {
			Fonts.font19.drawStringWithShadow(text, 6, offset, 0xffffffff);
		}
		if (pos == ScreenPos.RIGHTUP) {
			Fonts.font19.drawStringWithShadow(text, 6, offset, 0xffffffff);
		}
		GlStateManager.translate(0, 0, 1);
	}

	public void setEnabled(boolean b) {

		enabled = b;

	}

	public boolean getUsePanicButton() {
		return false;
	}
	
	public void addModuleToList(Module module) {
		moduleList.addModule(module);
	}
	
	public void removeModuleFromList(Module module) {
		moduleList.removeModule(module);
	}
	
	public ModuleList getModuleList() {
		return moduleList;
	}
	
}
