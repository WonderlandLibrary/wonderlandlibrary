package me.aidanmees.trivia.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.gui.tab.TabGuiItem;
import me.aidanmees.trivia.client.gui.tab.TabGuiTitle;
import me.aidanmees.trivia.client.main.ReturnType;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.FontUtils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class UIRenderer extends GuiScreen {
	ArrayList<String> objectqueue = new ArrayList<String>();
	
	UnicodeFontRenderer fontRenderer2 = new UnicodeFontRenderer(new Font("Comfortaa", Font.PLAIN, 30));
	UnicodeFontRenderer fontRenderer3 = new UnicodeFontRenderer(new Font("Comfortaa", Font.PLAIN, 20));
	
	ArrayList<ScreenPos> enumqueue = new ArrayList<ScreenPos>();
	public FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Consolas", Font.PLAIN, 19));
	FontUtils fu_default = new FontUtils("Consolas", Font.PLAIN, 35);
	FontUtils fu_default2 = new FontUtils("Arial", Font.PLAIN, 12);
	
	boolean enabled = true;
	private Minecraft mc = Minecraft.getMinecraft();
	String toDraw = trivia.getClientName() + " B" + trivia.getClientVersion();

	public UIRenderer() {
		this.mc = Minecraft.getMinecraft();
	}

	public void addToQueue(String s, ScreenPos pos) {
		objectqueue.add(s);
		enumqueue.add(pos);
	}

	public void tickQueues() {
		int i = 0;
		int offset = trivia.getTabGui().rootContainer.y + trivia.getTabGui().rootContainer.height;
		if(!ClientSettings.tabGui) {
			offset = ClientSettings.bigWaterMark ? 28 : 15;
		}
		for (ScreenPos p : enumqueue) {
			addToScreen(objectqueue.get(i), p, offset);
			offset += 10;
			i++;
		}
	}
	
	public int getWidth() {
		return mc.displayWidth / mc.gameSettings.guiScale;
	}
	
	public int getHeight() {
		return mc.displayHeight / mc.gameSettings.guiScale;
	}

	public void render(boolean renderModules) {
		if (!enabled) {
			return;
		}
		if(mc.currentScreen != null && mc.currentScreen instanceof GuiManagerDisplayScreen && ClientSettings.clickGuiTint) {
			drawRect(0, 0, width, height, 0x6a000000);
		}
		if(trivia.getTabGui() != null) {
			trivia.getTabGui().render();
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(trivia.triviaTexture512);
			GlStateManager.popMatrix();
		}
		
		GlStateManager.enableBlend();
		
	
		fontRenderer2.drawString("Remix", 3, 0, TabGuiContainer.col);
		
		
			
			
		
		
		//mc.fontRendererObj.drawString(trivia.getClientVersion(), 113, -14 + 20, 0xffdddddd, true);


		
//		mc.fontRendererObj.drawString("by: " + trivia.getClientAuthor(), 4, 33, 0xffffffff);
		if(mc.currentScreen == null) {
			if(trivia.devVersion) {
				GlStateManager.translate(0, 0, 1000);
				
				GlStateManager.translate(0, 0, -1000);
			}
			else {
				GlStateManager.translate(0, 0, 1000);
				
				GlStateManager.translate(0, 0, -1000);
			}
		}
		
		GlStateManager.translate(0, 5, 0);
		tickQueues();
		GlStateManager.translate(0, -5, 0);
		int y = 1;
		ArrayList<Object> modules = trivia.filterModulesByCategory(trivia.getToggledModules(),
				trivia.defaultCategories,
				ReturnType.NAME);
		try {
			if (trivia.java8) {
				modules.sort(new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						FontRenderer fr = mc.fontRendererObj;
						Module o1m = trivia.getModuleByName((String) o1);
						Module o2m = trivia.getModuleByName((String) o2);
						int o1w = 0;
						int o2w = 0;
						if (o1m.getAddonText() == null) {
							o1w = fr.getStringWidth(o1m.getName());
						} else {
							o1w = fr.getStringWidth(o1m.getName() + o1m.getAddonText() + " - []");
						}
						if (o2m.getAddonText() == null) {
							o2w = fr.getStringWidth(o2m.getName());
						} else {
							o2w = fr.getStringWidth(o2m.getName() + o2m.getAddonText() + " - []");
						}
						if (o1w > o2w) {
							return -1;
						}
						if (o1w < o2w) {
							return 1;
						}
						return 0;
					}
				});
			}
		} catch (NoSuchMethodError e) {
			trivia.java8 = false;
		}
		
		if (renderModules) {
			for (Object m : modules) {
				String name = (String) m;
				Module module = trivia.getModuleByName(name);
				String toRender;
				String addonText = " (" + module.getAddonText() + ")";
				int nameWidth = fontRenderer3.getStringWidth(name);
				
				if (!(module.getAddonText() == null)) {
					toRender = name + addonText;
				} else {
					toRender = name;
				}
			
				
				int toRenderWidth = fontRenderer3.getStringWidth(toRender);
				
				
				
				
				
                fontRenderer3.drawString(name,
						width  - 2 - toRenderWidth, y, TabGuiContainer.col);
				if (module.getAddonText() != null) {
					fontRenderer3.drawString(addonText,
							width - 2 - toRenderWidth + nameWidth, y, 0xffffff);
				}
				
				y += 9;
			}
			
		}
		objectqueue.clear();
		enumqueue.clear();
		if (!trivia.java8 && mc.currentScreen != null) {
			GlStateManager.pushMatrix();
			String s = "You may have an old version of java: " + System.getProperty("java.version")
					+ ", please update to " + "Java 8!";
			mc.fontRendererObj.drawStringWithShadow("",
					(width) / 2 - mc.fontRendererObj.getStringWidth(s) / 2, 2,
					0xffbbbbbb);
			GlStateManager.popMatrix();
		}
	}

	public void addToScreen(String text, ScreenPos pos, int offset) {

		if (pos == ScreenPos.LEFTUP) {
			mc.fontRendererObj.drawString(text, 4, offset, 0xffffffff);
		}
		if (pos == ScreenPos.RIGHTUP) {
			mc.fontRendererObj.drawString(text, 4, offset, 0xffffffff);
		}
	}

	public void setEnabled(boolean b) {

		enabled = b;

	}

	public boolean getUsePanicButton() {
		return false;
	}
}
