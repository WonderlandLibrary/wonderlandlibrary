package me.aidanmees.trivia.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.custom.clickgui.utils.GuiUtils;

public class ModuleWindow extends Container {
	
	private Category category;
	private String title;
	private static UnicodeFontRenderer titleRenderer = new UnicodeFontRenderer(new Font("Comfortaa", Font.PLAIN, 23));
	private boolean dragging = false;
	private double preX;
	private double preY;

	@Override
	public void update() {
		
		super.update();
	}

	@Override
	public void draw() {
		
		if(dragging) {
			this.setX(getX() - (preX + (getX() - RenderUtil.calculateMouseLocation().x)));
			this.setY(getY() - (preY + (getY() - RenderUtil.calculateMouseLocation().y - 1)));
		}
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		Color DarkCol = new Color(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glColor4f(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2, 255f);
		int headHeight = titleRenderer.FONT_HEIGHT + 2;
		glBegin(GL_QUADS);
		{
			glVertex2d(2, -2);
			glVertex2d(2, 0 + headHeight);
			glVertex2d(getWidth() - 2, 0 + headHeight);
			glVertex2d(getWidth() - 2, -2);
		}
		glEnd();
		if(!extended) {
		glBegin(GL_QUADS);
		{
			glVertex2d(-1, 0 + headHeight - headHeight - 2);
			glVertex2d(-1, getHeight());
			glVertex2d(2, getHeight());
			glVertex2d(2, 0 + headHeight - headHeight - 2);
		}
		glEnd();
		}
		
		GL11.glLineWidth(1);
		if(extended) {
		
			
			GL11.glColor4f(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2, 255f);
			glBegin(GL_QUADS);
			{
				glVertex2d(-1, 0 + headHeight - headHeight - 2);
				glVertex2d(-1, getHeight());
				glVertex2d(2, getHeight());
				glVertex2d(2, 0 + headHeight - headHeight - 2);
			}
			glEnd();
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		if(title != null) {
			titleRenderer.drawString(title, 2, 0, 0xffffffff);
		}
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
		super.draw();
	}


	@Override
	public double getPreferedHeight() {
		double y = titleRenderer.FONT_HEIGHT + 2;
		if(!extended) {
			return y;
		}
		for(Component comp : children) {
			y += getBorderY() / 2.0;
			comp.setY(y);
			if(comp instanceof Container) {
				//comp.setHeight(comp.getPreferedHeight());
			}
			y += comp.getHeight();
			y += getBorderY() / 2.0;
		}
		return y;
	}
	

	@Override
	public double getPreferedWidth() {
		double maxWidth = 0;
		for(Component child : children) {
			if(child instanceof Container) {
				//child.setWidth(child.getPreferedWidth());
			}
			maxWidth = Math.max(child.getPreferedWidth() + getBorderX() * 2, maxWidth);
		}
		return maxWidth;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		trivia.getClickGuiManager().windows.sort(new Comparator<ModuleWindow>() {
			
			public int compare(ModuleWindow o1, ModuleWindow o2) {
				if(o1.title.equals(ModuleWindow.this.title)) {
					return 1;
				}
				return -1;
			};
			
		});
		super.onClicked(x, y, button);
		if(button == 0 && y <= titleRenderer.FONT_HEIGHT + 2) {
			preX = x;
			preY = y;
			this.dragging = true;
		}
	}
	
	@Override
	public void onDragged(int x, int y, double mx, double my, int button) {
		super.onDragged(x, y, mx, my, button);
		if(button == 1) {
			
		}
		if(button == 0) {
//			if(my <= titleRenderer.FONT_HEIGHT + 2) {
//				this.setX(this.getX() + x);
//				this.setY(this.getY() + y);
//			}
//			if(mx > this.getWidth() - 10) {
//				this.setWidth(this.getWidth() + (x));
//				this.justLayoutChildren();
//			}
//			else {
//				
//			}
		}
	}
	
	@Override
	public void onReleased(int button) {
		super.onReleased(button);
		if(button == 0) {
			this.dragging = false;
		}
	}
	
	@Override
	public void onReleased(double x, double y, int button) {
		super.onReleased(x, y, button);
		if(button == 1 && y < fontRenderer.FONT_HEIGHT + 3) {
			this.setExtended(!isExtended());
		}
	}

	public void onHover(double x, double y) {
		for(Component comp : children) {
			if(comp instanceof ModuleButton) {
				if(x > comp.getX() && x < comp.getX() + comp.getWidth()
					&& y > comp.getY() && y < comp.getY() + comp.getHeight()) {
					((ModuleButton) comp).hovered = true;
				}
			}
		}
	}

}
