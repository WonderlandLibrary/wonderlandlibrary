package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;

public abstract class CheckBtn extends Component {
	
	private String title;
	private boolean toggled = false;

	@Override
	public void update() {
//		if (mod.getKeyboardKey() == Keyboard.KEY_NONE) {
//			this.setTitle(mod.getName());
//		} else {
//			this.setTitle(mod.getName() + "[" + Keyboard.getKeyName(mod.getKeyboardKey()) + "]");
//		}
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		
		GuiUtils.setColorsBasedOnSettingContainer(this);
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		
		if(isToggled()) {
			GuiUtils.setColor(foreground, 0.8f);
		}
		else {
			GuiUtils.setColor(background, 0.8f);
		}
		
		RenderTools.drawFilledCircle(6, getHeight() / 2d - 0.5, 5);
		
		if(isToggled()) {

			GL11.glColor4f(0f, 0f, 0f, 1f);
			
		}
		else {
			
			GuiUtils.setColor(foreground, 0.8f);
			
		}
		
		GL11.glLineWidth(0.5f);
		
		RenderTools.drawCircle(6, getHeight() / 2d - 0.5, 5);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontRenderer.drawStringWithShadow(title, 13, 1, RenderUtil.toRGBA(new Color(1f, 1f, 1f, 1f)));
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	public boolean isToggled() {
		return toggled;
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 16;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 3;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
