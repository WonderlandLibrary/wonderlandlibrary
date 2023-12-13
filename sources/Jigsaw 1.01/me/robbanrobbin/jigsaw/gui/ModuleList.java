package me.robbanrobbin.jigsaw.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ModuleList {
	
	private ArrayList<ModuleListEntry> modules = new ArrayList<ModuleListEntry>();
	private ArrayList<ModuleListEntry> otherModules = new ArrayList<ModuleListEntry>();
	
	private int maxWidth;
	
	public ModuleList() {
		
	}
	
	public void addModule(Module module) {
		if(module.getCategory() == Category.TARGET && module.getName() != "AntiBot") {
			otherModules.add(new ModuleListEntry(module, 1, new Color(1f, 1f, 1f)));
		}
		else {
			modules.add(new ModuleListEntry(module, 0));
		}
	}
	
	public void removeModule(Module module) {
		Iterator<ModuleListEntry> iter = modules.iterator();
		
		while(iter.hasNext()) {
			if(iter.next().module.equals(module)) {
				iter.remove();
			}
		}
		
		iter = otherModules.iterator();
		
		while(iter.hasNext()) {
			if(iter.next().module.equals(module)) {
				iter.remove();
			}
		}
	}
	
	public void render(int width, int height) {
		
		try {
			if (Jigsaw.java8) {
				modules.sort(new Comparator<ModuleListEntry>() {
					@Override
					public int compare(ModuleListEntry o1, ModuleListEntry o2) {
						FontRenderer fr = Fonts.font18;
						Module o1m = o1.module;
						Module o2m = o2.module;
						int o1w = 0;
						int o2w = 0;
						if (o1m.getAddonText() == null) {
							o1w = fr.getStringWidth(o1m.getName());
						} else {
							o1w = fr.getStringWidth(o1m.getName() + o1m.getAddonText() + " ");
						}
						if (o2m.getAddonText() == null) {
							o2w = fr.getStringWidth(o2m.getName());
						} else {
							o2w = fr.getStringWidth(o2m.getName() + o2m.getAddonText() + " ");
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
			Jigsaw.java8 = false;
		}
		
		try {
			if (Jigsaw.java8) {
				otherModules.sort(new Comparator<ModuleListEntry>() {
					@Override
					public int compare(ModuleListEntry o1, ModuleListEntry o2) {
						FontRenderer fr = Fonts.font18;
						Module o1m = o1.module;
						Module o2m = o2.module;
						int o1w = 0;
						int o2w = 0;
						if (o1m.getAddonText() == null) {
							o1w = fr.getStringWidth(o1m.getName());
						} else {
							o1w = fr.getStringWidth(o1m.getName() + o1m.getAddonText() + " ");
						}
						if (o2m.getAddonText() == null) {
							o2w = fr.getStringWidth(o2m.getName());
						} else {
							o2w = fr.getStringWidth(o2m.getName() + o2m.getAddonText() + " ");
						}
						if (o1w > o2w) {
							return 1;
						}
						if (o1w < o2w) {
							return -1;
						}
						return 0;
					}
				});
			}
		} catch (NoSuchMethodError e) {
			Jigsaw.java8 = false;
		}
		
		int y = 1;
		int lastY = y;
		int count = 0;
		for (ModuleListEntry entry : modules) {
			String name = entry.module.getName();
			Module module = Jigsaw.getModuleByName(name);
			String toRender;
			String addonText = " " + module.getAddonText() + "";
			int nameWidth = Fonts.font18.getStringWidth(name);
			int toRenderWidth;
			if (!(module.getAddonText() == null)) {
				toRender = name + addonText;
			} else {
				toRender = name;
			}
			
			toRenderWidth = Fonts.font18.getStringWidth(toRender) + 1;
			
			GuiUtils.drawBlurBuffer(width - 2 - toRenderWidth - 2, y - 1, width, y + 8, true);
			GuiUtils.enableDefaults();
			
			GuiUtils.setColor(ClientSettings.getBackGroundGuiColor(), 0.7f);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(width - 2 - toRenderWidth - 2, y - 1);
				GL11.glVertex2d(width, y - 1);
				GL11.glVertex2d(width, y + 8);
				GL11.glVertex2d(width - 2 - toRenderWidth - 2, y + 8);
			}
			GL11.glEnd();
			
//			drawRect(width - 2 - toRenderWidth - 2, y - 1, width, y + 8, 0xc0000000);
			
			if(entry.color != null) {
				Fonts.font18.drawString(name, width - 1 - toRenderWidth, y - 0.5, entry.color);
			}
			else {
				Fonts.font18.drawString(name, width - 1 - toRenderWidth, y - 0.5, ClientSettings.getForeGroundGuiColor().brighter());
			}
			
			if (module.getAddonText() != null) {
				Fonts.font18.drawString(addonText, width - 1 - toRenderWidth + nameWidth, y - 0.5, 0xffcccccc);
			}
				
			count++;
			y += 9;
		}
		
		if(y > 1) {
			GuiUtils.enableDefaults();
			
			GuiUtils.setColor(ClientSettings.getForeGroundGuiColor(), 1f);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(width - 1, 0);
				GL11.glVertex2d(width, 0);
				GL11.glVertex2d(width, y - 1);
				GL11.glVertex2d(width - 1, y - 1);
			}
			GL11.glEnd();
		}
		
		if(!ClientSettings.showTargetsInSeperateWindow || otherModules.isEmpty()) {
			maxWidth = 2;
			return;
		}
		maxWidth = 0;
		y = height + 1;
		for (ModuleListEntry entry1 : otherModules) {
			String name = entry1.module.getName();
			Module module = Jigsaw.getModuleByName(name);
			String toRender;
			String addonText = " " + module.getAddonText() + "";
			int nameWidth = Fonts.font18.getStringWidth(name);
			int toRenderWidth;
			if (!(module.getAddonText() == null)) {
				toRender = name + addonText;
			} else {
				toRender = name;
			}
			toRenderWidth = Fonts.font18.getStringWidth(toRender);
			maxWidth = Math.max(maxWidth, toRenderWidth);
			y-=10;
		}
		for (ModuleListEntry entry : otherModules) {
			String name = entry.module.getName();
			Module module = Jigsaw.getModuleByName(name);
			String toRender;
			String addonText = " " + module.getAddonText() + "";
			int nameWidth = Fonts.font18.getStringWidth(name);
			int toRenderWidth;
			if (!(module.getAddonText() == null)) {
				toRender = name + addonText;
			} else {
				toRender = name;
			}
			
			toRenderWidth = Fonts.font18.getStringWidth(toRender);
			
			GuiUtils.drawBlurBuffer(width - 2 - toRenderWidth - 2, y - 1, width, y + 9, true);
			GuiUtils.enableDefaults();
			
			GuiUtils.setColor(ClientSettings.getBackGroundGuiColor(), 0.7f);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(width - 2 - toRenderWidth - 2, y - 1);
				GL11.glVertex2d(width, y - 1);
				GL11.glVertex2d(width, y + 9);
				GL11.glVertex2d(width - 2 - toRenderWidth - 2, y + 9);
			}
			GL11.glEnd();
			
//			drawRect(width - 2 - toRenderWidth - 2, y - 1, width, y + 9, 0xc0000000);
			
			if(entry.color != null) {
				Fonts.font18.drawString(name, width - 1 - toRenderWidth, y + 0.5, entry.color);
			}
			else {
				Fonts.font18.drawString(name, width - 1 - toRenderWidth, y + 0.5, ClientSettings.getForeGroundGuiColor().brighter());
			}
			
			if (module.getAddonText() != null) {
				Fonts.font18.drawString(addonText, width - 1 - toRenderWidth + nameWidth, y + 0.5, 0xffcccccc);
			}
				
			count++;
			y += 10;
		}
		maxWidth += 6;
		
	}
	
	/**
     * Draws a solid color rectangle with the specified coordinates and color.
     */
    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
	
	public int getTargetWindowWidth() {
		if(!ClientSettings.showTargetsInSeperateWindow) {
			return 3;
		}
		return maxWidth + 1;
	}
	
}
