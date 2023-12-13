package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Point;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.Minecraft;

public class ComponentButton extends Component {
	
	protected String title;
	
	protected String tooltip;
	
	public ComponentButton() {
		super();
		
	}
	
	public ComponentButton(String title, String tooltip) {
		super();
		this.title = title;
		this.tooltip = tooltip;
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw() {
		
		drawBody();
		
		drawShadow();
		
		drawTitle();
		
		if(hoverTime > 15) {
			drawTooltip();
		}
		
	}
	
	protected void drawShadow() {
		GuiUtils.enableDefaults();
		GuiUtils.translate(this, false);
		
		if(isHovered() && getParent().focus.equals(this)) {
			if(!firstChild) {
				GuiUtils.renderShadowHorizontal(0.1, 80, 0, 0, getWidth(), true, false);
			}
			if(!lastChild) {
				GuiUtils.renderShadowHorizontal(0.1, 80, getHeight(), 0, getWidth(), false, false);
			}
		}
		
		GuiUtils.translate(this, true);
		GuiUtils.disableDefaults();
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
		if(isHovered()) {
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
	
	protected void drawTitle() {
		GuiUtils.translate(this, false);
		float y = ((float) this.getHeight() - fontRenderer.FONT_HEIGHT) / 2f;
		fontRenderer.drawStringWithShadow(title, (float) (getWidth() / 2 - fontRenderer.getStringWidth(title) / 2), y, GuiUtils.getPreferredFontARGBColor());
		GuiUtils.translate(this, true);
	}
	
	protected void drawTooltip() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		Point mouse = RenderUtil.calculateMouseLocation();
		mouse.translate(0, 1);
		glDisable(GL_TEXTURE_2D);
		// TODO Jigsaw tooltips
		if (this.tooltip != null && this.tooltip != "") {
			GuiUtils.setColor(background, 1f);
			GL11.glTranslated(0, 0, 2);
			GuiUtils.enableDefaults();
			int mousey = (int) (mouse.y - 10);
			int mousex = mouse.x;
			int realMouseX = RenderUtil.calculateMouseLocation().x;
			int testWidth = (fontRenderer.getStringWidth(this.tooltip) + 28);
			

			int width = mousex + 3
					+ fontRenderer.getStringWidth(this.tooltip) + 2;
			int height = mousey + fontRenderer.FONT_HEIGHT + 2;
			
			if(realMouseX > Minecraft.getMinecraft().displayWidth / 2 - testWidth) {
				mousex = mousex - realMouseX + (Minecraft.getMinecraft().displayWidth / 2) - testWidth;
			}
			
			glBegin(GL_QUADS);
			{
				glVertex2d(mousex + 20, mousey - 1);
				glVertex2d(width + 20, mousey - 1);
				glVertex2d(width + 20, height);
				glVertex2d(mousex + 20, height);
			}
			glEnd();
			GuiUtils.setColor(foreground, 1f);
			GL11.glLineWidth(2);
			glBegin(GL11.GL_LINE_LOOP);
			{
				glVertex2d(mousex + 20, mousey - 1);
				glVertex2d(width + 20, mousey - 1);
				glVertex2d(width + 20, height);
				glVertex2d(mousex + 20, height);
			}
			glEnd();
			GL11.glLineWidth(1);
			fontRenderer.drawStringWithShadow(this.tooltip, mousex + 22, mousey,
					RenderUtil.toRGBA(Color.WHITE));
			GL11.glTranslated(0, 0, -2);
		}
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		GuiUtils.disableDefaults();
	}
	
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 8;
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
