package dev.monsoon.ui.clickgui.skeet.comp;

import dev.monsoon.Monsoon;
import dev.monsoon.module.enums.Category;
import dev.monsoon.ui.clickgui.skeet.SkeetGUI;
import dev.monsoon.util.render.DrawUtil;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class SkeetCategoryButton {

	public ResourceLocation image;
	public int x, y, width, height, ani = 0, target;
	public String description;
	public Minecraft mc;
	public GuiScreen oldScreen;
	public Category category;

	public SkeetCategoryButton(ResourceLocation image, int x, int y, int width, int height, int target, Category category) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.target = target;
		this.category = category;
		this.mc = Minecraft.getMinecraft();
		DrawUtil.draw2DImage(this.image, this.x, this.y, this.width, this.height, Color.WHITE);
	}

	public void draw(int mouseX, int mouseY, Color c) {
		GlStateManager.disableBlend();
		DrawUtil.draw2DImage(this.image, this.x, this.y, this.width, this.height, c);
	}

	public void onClick(int mouseX, int mouseY) {
		if (this.isHovered(mouseX, mouseY)) {
			switch (this.target) {
			default:
				break;
			}
			Monsoon.getSkeetGui().setViewing(this.category);
		}
	}

	protected void hoverAnimation(int mouseX, int mouseY) {
		if (this.isHovered(mouseX, mouseY)) {
			if (this.ani < 5)
				this.ani++;
		} else {
			if (this.ani > 0)
				this.ani--;
		}
	}

	protected boolean isHovered(int mouseX, int mouseY) {
		return RenderUtil.isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY);
	}

}
