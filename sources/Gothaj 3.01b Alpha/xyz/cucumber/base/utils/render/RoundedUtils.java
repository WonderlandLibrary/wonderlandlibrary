package xyz.cucumber.base.utils.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.ShaderUtils;

public class RoundedUtils {

	public static Shader roundedRect = new Shader(
			"#version 120\n" +
                    "\n" +
                    "uniform vec2 size;\n" +
                    "uniform vec4 color;\n" +
                    "uniform float radius;\n" +
                    "\n" +
                    "float roundSDF(vec2 p, vec2 b, float r) {\n" +
                    "    return length(max(abs(p) - b, 0.0)) - r;\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 rectHalf = size * .5;\n" +
                    "    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * size), rectHalf - radius - 1., radius))) * color.a;\n" +
                    "    gl_FragColor = vec4(color.rgb, smoothedAlpha);\n" +
                    "\n" +
                    "}"
			);
	public static Shader outlinedRoundedRect = new Shader(
			"#version 120\r\n"
			+ "\r\n"
			+ "uniform vec2 location, rectSize;\r\n"
			+ "uniform vec4 color, outlineColor;\r\n"
			+ "uniform float radius, outlineThickness;\r\n"
			+ "\r\n"
			+ "float roundedSDF(vec2 centerPos, vec2 size, float radius) {\r\n"
			+ "    return length(max(abs(centerPos) - size + radius, 0.0)) - radius;\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "void main() {\r\n"
			+ "    float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness *.5) - 1.0, radius);\r\n"
			+ "\r\n"
			+ "    float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * .5));\r\n"
			+ "\r\n"
			+ "    vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);\r\n"
			+ "    gl_FragColor = mix(outlineColor, insideColor, blendAmount);\r\n"
			+ "\r\n"
			+ "}"
			);

	
	
	public static void drawRoundedRect(double x, double y, double width, double height, int color, float rounding) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GlStateManager.enableBlend();
		roundedRect.startProgram();

		GL20.glUniform1f(roundedRect.getUniformLoc("radius"), rounding);

		GL20.glUniform2f(roundedRect.getUniformLoc("size"),(float)width, (float)height);
		GL20.glUniform4f(roundedRect.getUniformLoc("color"), f, f1, f2, f3);

		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		roundedRect.renderShader(x, y, (float)width, (float)height);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		roundedRect.stopProgram();
		GlStateManager.disableBlend();
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	
	public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color outlineColor) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        outlinedRoundedRect.startProgram();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        setupRoundedRectUniforms(x, y, width, height, radius);
        outlinedRoundedRect.uniform1f(outlinedRoundedRect.getProgramID(), "outlineThickness", outlineThickness);
        outlinedRoundedRect.uniform4f(outlinedRoundedRect.getProgramID(), "color", 0 / 255f, 0 / 255f, 0 / 255f, 0 / 255f);
        outlinedRoundedRect.uniform4f(outlinedRoundedRect.getProgramID(),"outlineColor", outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);

        roundedRect.renderShader(x - (2 + outlineThickness), y - (2 + outlineThickness), width + (4 + outlineThickness), height + (4 + outlineThickness));
        outlinedRoundedRect.stopProgram();
        GlStateManager.disableBlend();
    }
	
	private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        outlinedRoundedRect.uniform2f(outlinedRoundedRect.getProgramID(),"location", x * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        outlinedRoundedRect.uniform2f(outlinedRoundedRect.getProgramID(),"rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        outlinedRoundedRect.uniform1f(outlinedRoundedRect.getProgramID(),"radius", radius * 2);
    }

	
}
