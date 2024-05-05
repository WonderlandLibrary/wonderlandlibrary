package xyz.cucumber.base.utils.math;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.renderer.GLAllocation;
import xyz.cucumber.base.Client;

public class Convertors {
	public static float[] convert2D(float x, float y, float z, int scaleFactor) {
		FloatBuffer windowPosition = GLAllocation.createDirectFloatBuffer(4);
	    IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
	    FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
	    FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
	
		if (GLU.gluProject(x, y, z, modelMatrix, projectionMatrix, viewport, windowPosition)) {
			return new float [] {windowPosition.get(0) / scaleFactor, (Display.getHeight() - windowPosition.get(1)) / scaleFactor, windowPosition.get(2)};
		}
		return null;
	}
	
	public static double calculateCPS(double min, double max, double last) {
		double diff = max-min;
		double ult = min + diff*Math.random()*Math.random();
		return 1000/ ult;
	}
}
