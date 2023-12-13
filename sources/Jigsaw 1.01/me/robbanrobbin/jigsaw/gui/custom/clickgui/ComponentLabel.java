package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;

public class ComponentLabel extends Component {
	
	protected String text;
	
	public ComponentLabel(String text) {
		super();
		this.text = text;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		
		drawBody();
		
		drawTitle();
		
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
		
		GuiUtils.translate(this, true);
		GuiUtils.disableDefaults();
	}
	
	protected void drawTitle() {
		GuiUtils.translate(this, false);
		float y = ((float) this.getHeight() - fontRenderer.FONT_HEIGHT) / 2f;
		fontRenderer.drawStringWithShadow(text, (float)(this.getHeight() - fontRenderer.FONT_HEIGHT), y, RenderUtil.toRGBA(new Color(1f, 1f, 1f, 1f)));
		GuiUtils.translate(this, true);
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(text) + 8;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 3;
	}
	
	public void setTitle(String title) {
		this.text = title;
	}
	
	public String getTitle() {
		return text;
	}
	
}
