package me.robbanrobbin.jigsaw.gui.custom.component;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.opengl.GL11;

public class ComponentFrame extends AbstractContainer implements ComponentLook, ComponentDraggable {

	private boolean dragging;

	@Override
	public void render(Component component) {
		GL11.glColor3f(0.5f, 0.5f, 0.5f);

		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		{
			// glVertex2d(this.rectangle.x, this.rectangle.y);
			// glVertex2d(this.rectangle.width, this.rectangle.y);
			// glVertex2d(this.rectangle.x, this.rectangle.height);
			// glVertex2d(this.rectangle.width, this.rectangle.height);
			glVertex2d(0, 0);
			glVertex2d(10, 0);
			glVertex2d(10, 10);
			glVertex2d(0, 10);
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		for (Component child : children) {
			child.render();
		}
	}

	@Override
	public void update(Component component) {
		this.positionChildren();
		for (Component child : children) {
			child.update();
		}
	}

	@Override
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	@Override
	public boolean isDragging() {
		return this.dragging;
	}

}
