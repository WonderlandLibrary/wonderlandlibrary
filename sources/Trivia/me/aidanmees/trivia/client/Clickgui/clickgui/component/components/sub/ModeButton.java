package me.aidanmees.trivia.client.Clickgui.clickgui.component.components.sub;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.Clickgui.clickgui.component.Component;
import me.aidanmees.trivia.client.Clickgui.clickgui.component.components.Button;
import me.aidanmees.trivia.client.Clickgui.settings.Setting;
import me.aidanmees.trivia.client.tools.FontUtils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

//Your Imports


public class ModeButton extends Component {

	private boolean hovered;
	private Button parent;
	private Setting set;
	private int offset;
	private int x;
	private int y;
	private Module mod;
	FontUtils fu_default = new FontUtils("Audiowide", Font.PLAIN, 12);
	
	public ModeButton(Button button, Module mod, int offset) {
		
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		
		Minecraft.getMinecraft().fontRendererObj.drawString("Mode: " + mod.getCurrentMode(), (parent.parent.getX() + 7) * 2, (parent.parent.getY() + offset + 2) * 2 + 5, -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			if(button == 0) {
				mod.setMode(mod.getModes()[getModeIndexForward()]);
			}
			if(button == 1) {
				mod.setMode(mod.getModes()[getModeIndexBackward()]);
			}
		}
	}
	public int getModeIndexForward() {
		for(int i = 0; i < mod.getModes().length; i++) {
			if(mod.getModes()[i].equals(mod.getCurrentMode())) {
				if(i + 1 >= mod.getModes().length) {
					return 0;
				}
				return i + 1;
			}
		}
		return -1;
	}
	public int getModeIndexBackward() {
		for(int i = 0; i < mod.getModes().length; i++) {
			if(mod.getModes()[i].equals(mod.getCurrentMode())) {
				if(i - 1 < 0) {
					return mod.getModes().length - 1;
				}
				return i - 1;
			}
		}
		return -1;
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
