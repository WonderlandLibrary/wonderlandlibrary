package me.robbanrobbin.jigsaw.gui.custom.component;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class ComponentButton extends AbstractComponent implements ComponentLook {

	@Override
	public void render(Component component) {
		glColor4f(0.5f, 0.5f, 0.5f, 0.5f);

		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		{
			glVertex2d(this.rectangle.x, this.rectangle.y);
			glVertex2d(this.rectangle.width, this.rectangle.y);
			glVertex2d(this.rectangle.x, this.rectangle.height);
			glVertex2d(this.rectangle.width, this.rectangle.height);
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);

	}

	@Override
	public void update(Component component) {

	}

}
