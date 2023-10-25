package me.rhys.client.shader.impl;

import java.awt.Color;
import me.rhys.client.shader.Shader;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class RoundOutlineShader extends Shader {
  public static RoundOutlineShader getInstance() {
    return instance;
  }
  
  private static final RoundOutlineShader instance = new RoundOutlineShader();
  
  private final long startTime = System.currentTimeMillis();
  
  public RoundOutlineShader() {
    super("vertex.vert", "round_outline.frag");
  }
  
  protected void createUniforms() {
    setupUniform("width");
    setupUniform("height");
    setupUniform("radius");
    setupUniform("time");
    setupUniform("shadeSpeed");
    setupUniform("shadeSize");
    setupUniform("shadeColor");
    setupUniform("rainbow");
    setupUniform("verticalShade");
    setupUniform("firstColor");
    setupUniform("secondColor");
    setupUniform("rainbowSaturation");
    setupUniform("rainbowBrightness");
    setupUniform("alphaColor");
    setupUniform("thickness");
  }
  
  public void drawSolid(double x, double y, double width, double height, double radius, double thickness, Color color) {
    draw(x, y, width, height, radius, 0.0D, 0.0D, 0.0D, 0.0D, thickness, (color
        .getAlpha() / 255.0F), color, (Color)null, false, false, false);
  }
  
  public void drawShade(double x, double y, double width, double height, double radius, double thickness, Color firstColor, Color secondColor, double shadeSpeed, double shadeSize, double alphaColor, boolean verticalShade) {
    draw(x, y, width, height, radius, shadeSpeed, shadeSize, 0.0D, 0.0D, thickness, alphaColor / 255.0D, firstColor, secondColor, true, false, verticalShade);
  }
  
  public void drawRainbow(double x, double y, double width, double height, double radius, double thickness, double shadeSpeed, double shadeSize, double rainbowSaturation, double rainbowBrightness, double alphaColor, boolean verticalShade) {
    draw(x, y, width, height, radius, shadeSpeed, shadeSize, rainbowSaturation, rainbowBrightness, thickness, alphaColor / 255.0D, (Color)null, (Color)null, false, true, verticalShade);
  }
  
  public void drawRainbow(double x, double y, double width, double height, double radius, double thickness, double shadeSpeed, double shadeSize, double alphaColor, boolean verticalShade) {
    draw(x, y, width, height, radius, shadeSpeed, shadeSize, 1.0D, 1.0D, thickness, alphaColor / 255.0D, (Color)null, (Color)null, false, true, verticalShade);
  }
  
  void draw(double x, double y, double width, double height, double radius, double shadeSpeed, double shadeSize, double rainbowSaturation, double rainbowBrightness, double alphaColor, double thickness, Color firstColor, Color secondColor, boolean shade, boolean rainbow, boolean verticalShade) {
    x -= 0.5D;
    y -= 0.5D;
    width++;
    height++;
    GlStateManager.enableBlend();
    boolean alphaTest = GL11.glIsEnabled(3008);
    if (alphaTest)
      GL11.glDisable(3008); 
    enableShader();
    GL20.glUniform1f(getUniform("width"), (float)width);
    GL20.glUniform1f(getUniform("height"), (float)height);
    GL20.glUniform1f(getUniform("radius"), (float)radius);
    GL20.glUniform1f(getUniform("time"), (float)(System.currentTimeMillis() - this.startTime) / 1000.0F);
    GL20.glUniform1f(getUniform("shadeSpeed"), (float)shadeSpeed);
    GL20.glUniform1f(getUniform("shadeSize"), (float)(shadeSize / (verticalShade ? height : width) / 50.0D));
    GL20.glUniform1i(getUniform("shadeColor"), shade ? 1 : 0);
    GL20.glUniform1i(getUniform("rainbow"), rainbow ? 1 : 0);
    GL20.glUniform1i(getUniform("verticalShade"), verticalShade ? 1 : 0);
    if (firstColor != null) {
      GL20.glUniform3f(getUniform("firstColor"), firstColor.getRed() / 255.0F, firstColor
          .getGreen() / 255.0F, firstColor.getBlue() / 255.0F);
    } else {
      GL20.glUniform3f(getUniform("firstColor"), 1.0F, 1.0F, 1.0F);
    } 
    if (secondColor != null) {
      GL20.glUniform3f(getUniform("secondColor"), secondColor.getRed() / 255.0F, secondColor
          .getGreen() / 255.0F, secondColor.getBlue() / 255.0F);
    } else {
      GL20.glUniform3f(getUniform("secondColor"), 1.0F, 1.0F, 1.0F);
    } 
    GL20.glUniform1f(getUniform("rainbowSaturation"), (float)rainbowSaturation);
    GL20.glUniform1f(getUniform("rainbowBrightness"), (float)rainbowBrightness);
    GL20.glUniform1f(getUniform("alphaColor"), (float)alphaColor);
    GL20.glUniform1f(getUniform("thickness"), (float)thickness);
    GL11.glDisable(3553);
    GL11.glBegin(7);
    GL11.glTexCoord2f(0.0F, 0.0F);
    GL11.glVertex2d(x, y);
    GL11.glTexCoord2f(0.0F, 1.0F);
    GL11.glVertex2d(x, y + height);
    GL11.glTexCoord2f(1.0F, 1.0F);
    GL11.glVertex2d(x + width, y + height);
    GL11.glTexCoord2f(1.0F, 0.0F);
    GL11.glVertex2d(x + width, y);
    GL11.glEnd();
    disableShader();
    GL11.glEnable(3553);
    if (alphaTest)
      GL11.glEnable(3008); 
    GlStateManager.disableBlend();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\shader\impl\RoundOutlineShader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */