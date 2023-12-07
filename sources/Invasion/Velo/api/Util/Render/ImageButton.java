package Velo.api.Util.Render;

import java.awt.Color;

import Velo.api.Main.Main;
import Velo.api.Util.fontRenderer.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;

public class ImageButton {
	
	public Fonts font1 = new Fonts();
	protected ResourceLocation image;
	protected int x, y, width, height, ani = 0, target;
	protected String description;
	protected Minecraft mc;
	
	public ImageButton(ResourceLocation image, int x, int y, int width, int height, String description, int target) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.description = description;
		this.target = target;
	}
	
	public void draw(int mouseX, int mouseY, Color c) {
		Velo.api.Util.fontRenderer.Utils.DrawFontUtil.loadFonts();
		Fonts.loadFonts();
		this.hoverAnimation(mouseX, mouseY);
		if(this.ani > 0) {
			RenderUtil.draw2DImage(image, this.x - this.ani, this.y - ani, this.width + this.ani * 2, this.height + ani * 2, c);
			float descWidth = font1.Hud.getStringWidth(description);
			font1.Hud.drawString(description, this.x + width / 2 - descWidth/2, this.y + height + 11, -1, true);
		} else {
			RenderUtil.draw2DImage(image, this.x, this.y, this.width, this.height, c);
		}
	}
	
	public void onClicked(int mouseX, int mouseY) {
		if(this.isHovered(mouseX, mouseY)) {
			if(target == 1) {
				mc.shutdown();
			}
		}
	}
	
	protected void hoverAnimation(int mouseX, int mouseY) {
		if(this.isHovered(mouseX, mouseY)) {
			if(this.ani < 5) {
				this.ani++;
			}
		} else {
			if(this.ani > 0) {
				this.ani--;
			}
		}
	}
	
	protected boolean isHovered(int mouseX, int mouseY) {
		return RenderUtil.isHovered(this.x, this.y, this.x + width, this.y + height, mouseX, mouseY);
	}
}
