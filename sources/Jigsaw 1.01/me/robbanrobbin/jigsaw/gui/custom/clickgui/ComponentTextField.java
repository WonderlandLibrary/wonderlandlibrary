package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.util.ChatAllowedCharacters;

public class ComponentTextField extends Component {
	
	protected String typed = "";

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		
		drawBody();
		
		drawShadow();
		
		drawTyped();
		
	}
	
	protected void drawShadow() {
		GuiUtils.enableDefaults();
		GuiUtils.translate(this, false);
		
		if(hovered && getParent().focus.equals(this)) {
			if(!firstChild) {
				GuiUtils.renderShadowHorizontal(0.3, 50, 0, 0, getWidth(), true, false);
			}
			if(!lastChild) {
				GuiUtils.renderShadowHorizontal(0.3, 50, getHeight(), 0, getWidth(), false, false);
			}
		}
		
		GuiUtils.translate(this, true);
		GuiUtils.disableDefaults();
	}
	
	protected void drawTyped() {
		GuiUtils.translate(this, false);
		float y = ((float) this.getHeight() - fontRenderer.FONT_HEIGHT) / 2f;
		fontRenderer.drawStringWithShadow(typed + (isFocused() ? "_" : ""), (float)(this.getHeight() - fontRenderer.FONT_HEIGHT), y, RenderUtil.toRGBA(new Color(1f, 1f, 1f, 1f)));
		GuiUtils.translate(this, true);
	}
	
	protected void drawBody() {
		GuiUtils.enableDefaults();
		GuiUtils.translate(this, false);
		
		GL11.glLineWidth(1f);
		
		GuiUtils.setColor(this.getBackground());
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		
//		if(hovered && !mod.isToggled() && time == 0f) {
//			GL11.glColor4f(0.8f, 0.1f, 0.1f, 0.91f - Jigsaw.getClickGuiManager().getAlpha());
//			glBegin(GL11.GL_QUADS);
//			{
//				glVertex2d(0, 0);
//				glVertex2d(0, getHeight());
//				glVertex2d(2, getHeight());
//				glVertex2d(2, 0);
//			}
//			glEnd();
//		}
		if(hovered) {
			GL11.glColor4f(1f, 1f, 1f, 0.05f);
			glBegin(GL11.GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(0, getHeight());
				glVertex2d(getWidth(), getHeight());
				glVertex2d(getWidth(), 0);
			}
			glEnd();
		}
		
		GuiUtils.translate(this, true);
		GuiUtils.disableDefaults();
	}

	@Override
	public double getPreferedWidth() {
		return 50;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 3;
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
		
		switch(keyCode) {
		case 14:
			backspace();
			return;
		}
		
		if(ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
			typed += typedChar;
		}
	}
	
	private void backspace() {
		if(typed.isEmpty()) {
			return;
		}
		typed = typed.substring(0, typed.length() - 1);
	}
	
	private boolean repeat;
	
	@Override
	public void onGainedFocus() {
		super.onGainedFocus();
		repeat = Keyboard.isRepeatEvent();
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void onLostFocus() {
		super.onLostFocus();
		Keyboard.enableRepeatEvents(repeat);
	}
	
	public String getTyped() {
		return typed;
	}
	
	public void resetTyped() {
		typed = "";
	}
	
}
