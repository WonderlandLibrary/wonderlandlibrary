package me.aidanmees.trivia.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Point;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

//import me.aidanmees.trivia.client.bot.account.MC;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.GuitriviaKeyBind;
import me.aidanmees.trivia.gui.custom.clickgui.utils.GuiUtils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;

public class KeybindButton extends Component {
	
	public Module mod;
	private String title;
	public boolean KeyBinding = false;
	
	public KeybindButton(Module mod) {
		this.setTitle("Key: ");
		this.setMod(mod);
		setHeight(12);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		Point mouse = GuiUtils.calculateMouseLocation();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.05f, 0.05f, 0.05f, 0.8f - trivia.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		
		GL11.glLineWidth(1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(mod != null && trivia.getClickGuiManager().getAlpha() <= 0.5f) {
			fontRenderer.drawString(title + Keyboard.getKeyName(this.mod.getKeyboardKey()), (int) (getWidth() / 2 - fontRenderer.getStringWidth("Key: "+Keyboard.getKeyName(this.mod.getKeyboardKey())) / 2 ), 0, 
					0xffffffff);
		}
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	public Module getMod() {
		return mod;
	}
	
	public void setMod(Module mod) {
		this.mod = mod;
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			
			Minecraft.getMinecraft()
			.displayGuiScreen(new GuitriviaKeyBind(mod, Minecraft.getMinecraft().currentScreen));
			
		}
	
		
	
	
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		trivia.chatMessage("keytype");
		if(this.KeyBinding) {
			if (!(key == Keyboard.KEY_ESCAPE)) {
				mod.setKeyCode(key);
				this.KeyBinding = !this.KeyBinding;
				
			}
		}
	}
	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 8;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 2;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
