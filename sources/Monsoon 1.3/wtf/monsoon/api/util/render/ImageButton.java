package wtf.monsoon.api.util.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageButton {

	protected ResourceLocation image;
	protected int x, y, width, height, ani = 0, target;
	protected String description;
	protected Minecraft mc;
	protected GuiScreen oldScreen;

	public ImageButton(ResourceLocation image, int x, int y, int width, int height, String description, int target, GuiScreen oldScreen) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.description = description;
		this.target = target;
		this.mc = Minecraft.getMinecraft();
		this.oldScreen = oldScreen;
	}

	public void draw(int mouseX, int mouseY, Color c) {
		GlStateManager.disableBlend();
		this.hoverAnimation(mouseX, mouseY);
		if (this.ani > 0) {
			DrawUtil.draw2DImage(this.image, this.x - this.ani, this.y - this.ani, this.width + this.ani * 2,
					this.height + this.ani * 2, c);
			/*Gui.drawRect(this.x - (int) descWidth / 2, this.y + this.height + 10,
					this.x + this.width + (int) descWidth / 2,
					this.y + this.height + 10 + Blank.INSTANCE.getCurrentFont().getHeight() + 1, Integer.MIN_VALUE);
			Blank.INSTANCE.getCurrentFont().drawString(this.description,
					this.x + this.width / 2 - Blank.INSTANCE.getCurrentFont().getStringWidth(this.description) / 2,
					this.y + this.height + 11, Color.WHITE.getRGB());*/
		} else {
			DrawUtil.draw2DImage(this.image, this.x, this.y, this.width, this.height, c);
		}
	}

	public void onClick(int mouseX, int mouseY) {
		if (this.isHovered(mouseX, mouseY)) {
			switch (this.target) {
			case 0:
				this.mc.shutdown();
				break;
			case 2:
				mc.displayGuiScreen(new GuiSelectWorld(oldScreen));
				break;
			case 3:
				mc.displayGuiScreen(new GuiMultiplayer(oldScreen));
				break;
			case 4:
				mc.displayGuiScreen(new GuiOptions(oldScreen, mc.gameSettings));
				break;
			default:
				break;
			}
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
