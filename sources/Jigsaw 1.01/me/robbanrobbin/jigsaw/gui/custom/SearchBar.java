package me.robbanrobbin.jigsaw.gui.custom;

import me.robbanrobbin.jigsaw.font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class SearchBar extends Gui {

	public boolean hovered;
	public String typed = "";
	public FontRenderer fontRenderer = Fonts.font19;
	private int width;
	private int height;
	public boolean typing;

	public void render(int w, int h, int mouseX, int mouseY) {
		
		int x = 0;
		int y = 0;
		
		drawRect(x, y, y + 100, x + 18, 0x6a000000);
		
		if (typing) {
			fontRenderer.drawString(typed + "_", x + 5, y + 5, 0xffcfcfcf);
		} else {
			fontRenderer.drawString("Search...", x + 5, y + 5, 0xffcccccc);
		}
		
	}

	public void update() {
		if (typed == null) {
			typed = "";
		}
		if(typed.length() > 15) {
			typed = typed.substring(0, 15);
		}
	}

	public void keyTyped(char typedChar, int keyCode) {
		// Jigsaw.chatMessage(Character.toString(typedChar));
		// Jigsaw.chatMessage(keyCode);
		if (keyCode == 1) { // Escape
			typing = false;
			return;
		}
		if (keyCode == 14) { // Backspace
			if (!typed.isEmpty()) {
				typed = typed.substring(0, typed.length() - 1);
			}
			if(typed.length() == 0) {
				typing = false;
			}
			return;
		}
		if (keyCode == 28 || keyCode == 42 || keyCode == 29) {
			return;
		}
		if ((keyCode > 1 && keyCode < 12) || (keyCode > 15 && keyCode < 26) || (keyCode > 29 && keyCode < 39)
				|| (keyCode > 43 && keyCode < 51)) {
			typed += Character.toString(typedChar);
		}
		typing = true;
	}

	public void mouseClicked(int x, int y, int button) {

	}

	public void mouseReleased(int x, int y, int button) {

	}

	public void init() {
		typing = false;
		typed = "";
	}

}
