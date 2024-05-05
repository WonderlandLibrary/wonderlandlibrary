package net.minecraft.client.gui.CustomButton;

import java.awt.Color;


import me.aidanmees.trivia.client.tools.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.tools.SpecialCircle;

public class MMButton extends GuiButton {
	private int x;
	private int y;
	private int x1;
	private int y1;
	private String text;
	int alphaInc = 100;
	int alpha = 0;
	int size = 0;
	int size2 = 0;

	public MMButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.x = par2;
		this.y = par3;
		this.x1 = par4;
		this.y1 = par5;
		this.text = par6Str;
	}

	public MMButton(int i, int j, int k, String stringParams) {
		this(i, j, k, 200, 20, stringParams);
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.x1) && (mouseY >= this.y)
				&& (mouseY <= this.y + this.y1);
		if ((isOverButton) && (this.alphaInc <= 150)) {
			this.alphaInc += 5;
			this.alpha = (this.alphaInc << 24);
		} else if ((!isOverButton) && (this.alphaInc >= 100)) {
			this.alphaInc -= 6;
			this.alpha = (this.alphaInc << 24);
		}
		if (this.alphaInc > 150) {
			this.alphaInc = 150;
		} else if (this.alphaInc < 100) {
			this.alphaInc = 100;
		}
		if ((isOverButton) && (this.size <= 1)) {
			this.size += 1;
		} else if ((!isOverButton) && (this.size >= 0)) {
			this.size -= 1;
		}
		if ((isOverButton) && (this.size2 <= 10)) {
			this.size2 += 0;
		} else if ((!isOverButton) && (this.size2 >= 0)) {
			this.size2 -= 0;
		}
		
		RenderUtils.drawBorderedRect(this.x + 1 - this.size, this.y + size2 + 0, this.x + this.x1 - 58, this.y + this.y1 - 10, 2,
				-1, -1);
		
		RenderUtils.drawBorderedRect(this.x + 58, this.y + size2 + 0, this.x + this.x1 - 1 + this.size, this.y + this.y1 - 10, 2,
				-1, -1);
		
		RenderUtils.drawBorderedRect(this.x - this.size, this.y, this.x + this.x1 + this.size, this.y + this.y1 - 11, 2,
				-1, -1);
		RenderUtils.drawBorderedRect(this.x - this.size, this.y + 12, this.x + this.x1 + this.size, this.y + this.y1 + 3, 2,
				-1, -1);
		SpecialCircle.circleOutline(this.x + this.x1 + this.size2, this.y + 11, 10, 0x00FFFFFF);

		Minecraft.getMinecraft().fontRendererObj.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.text, this.x + this.x1 / 2, this.y + this.y1 / 2 - 2,
				-1);
	}
}
