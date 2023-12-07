package me.robbanrobbin.jigsaw.client.gui.tab;

import java.awt.Font;

import me.robbanrobbin.jigsaw.font.Fonts;
import net.minecraft.client.gui.FontRenderer;

public abstract class TabGuiItem {
	
	public boolean selected = false;
	
	public boolean maximised = false;
	
	public int x;
	
	public int y;
	
	public int width;
	
	public int height;
	
	public TabGuiItem parent;
	
	public int getWidth() {
		return 0;
	}
	
	public int getHeigth() {
		return 0;
	}
	
	public static FontRenderer fontRenderer = TabGui.novitex ? Fonts.font25 : Fonts.font18;
	
	public TabGuiItem() {
		
	}
	
	public void update() {
		
	}
	
	public void setValues() {
		this.width = getWidth();
		this.height = getHeigth();
	}
	
	public void render() {
		
	}

	public void keyPressed(int keyCode) {
		if(this.parent != null) {
			this.parent.keyPressed(keyCode);
		}
	}
	
}
