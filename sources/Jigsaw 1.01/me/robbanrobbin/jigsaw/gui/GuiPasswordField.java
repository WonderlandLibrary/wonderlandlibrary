package me.robbanrobbin.jigsaw.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField {
	
	public GuiPasswordField(int componentId, FontRenderer fontRenderer, int x, int y, int par5Width,
			int par6Height) {
		super(componentId, fontRenderer, x, y, par5Width, par6Height);
	}
	
	@Override
	public void drawTextBox() {
		if (this.getVisible()) {
			if (this.getEnableBackgroundDrawing()) {
				drawRect(this.x - 1, this.y - 1, this.x + this.width + 1,
						this.y + this.height + 1, -6250336);
				drawRect(this.x, this.y, this.x + this.width, this.y + this.height,
						-16777216);
			}

			int i = this.isEnabled ? this.enabledColor : this.disabledColor;
			int j = this.cursorPosition - this.lineScrollOffset;
			int k = this.selectionEnd - this.lineScrollOffset;
			String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset),
					this.getWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
			int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
			int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
			int j1 = l;

			if (k > s.length()) {
				k = s.length();
			}
			int iii = 0;
			if (s.length() > 0) {
				String s1 = flag ? s.substring(0, j) : s;
				
				j1 = this.fontRenderer.drawStringWithShadow(s1, (float) l, (float) i1, 0x00000000);
				for(Character c : s1.toCharArray()) {
					this.fontRenderer.drawStringWithShadow("§7*", (float) l + iii, (float) i1, i);
					iii += 6;
				}
				
			}

			boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
			int k1 = iii + x + 4;

			if (!flag) {
				k1 = j > 0 ? l + this.width : l;
			} else if (flag2) {
				k1 = j1 - 1;
				--j1;
			}

			if (s.length() > 0 && flag && j < s.length()) {
				j1 = this.fontRenderer.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
			}

			if (flag1) {
				if (flag2) {
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
				} else {
					this.fontRenderer.drawStringWithShadow("_", (float) k1, (float) i1, i);
				}
			}

			if (k != j) {
				int l1 = l + this.fontRenderer.getStringWidth(s.substring(0, k));
				this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT);
			}
		}
	}

}
