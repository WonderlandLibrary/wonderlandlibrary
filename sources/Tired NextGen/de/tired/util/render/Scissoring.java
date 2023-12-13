package de.tired.util.render;

import de.tired.base.interfaces.IHook;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;

public enum Scissoring implements IHook {

	SCISSORING;

	public void startScissor() {
		glEnable(GL11.GL_SCISSOR_TEST);
	}

	public void doScissor(float x, float y, float width, float height) {
		width -= x;
		height -= y;
		ScaledResolution resolution = new ScaledResolution(MC);
		GL11.glScissor((int)x * resolution.getScaleFactor(), (int) MC.displayHeight - (int) y * resolution.getScaleFactor() - (int) height * resolution.getScaleFactor(), (int)width * resolution.getScaleFactor(), (int)height * resolution.getScaleFactor());
	}

	public void disableScissor() {
		glDisable(GL11.GL_SCISSOR_TEST);
	}

}
