package me.rhys.client.shader;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Shader {
  private final Minecraft mc = Minecraft.getMinecraft();
  
  private int lastFactorBuffer;
  
  private int lastWidthBuffer;
  
  private int lastHeightBuffer;
  
  private final ShaderUtils.ShaderProgram shaderProgram;
  
  private Map<String, Integer> uniformsMap;
  
  private Framebuffer framebuffer;
  
  private boolean entityShadows;
  
  public Minecraft getMc() {
    return this.mc;
  }
  
  public int getLastFactorBuffer() {
    return this.lastFactorBuffer;
  }
  
  public int getLastWidthBuffer() {
    return this.lastWidthBuffer;
  }
  
  public int getLastHeightBuffer() {
    return this.lastHeightBuffer;
  }
  
  public ShaderUtils.ShaderProgram getShaderProgram() {
    return this.shaderProgram;
  }
  
  public void setUniformsMap(Map<String, Integer> uniformsMap) {
    this.uniformsMap = uniformsMap;
  }
  
  public Map<String, Integer> getUniformsMap() {
    return this.uniformsMap;
  }
  
  public Framebuffer getFramebuffer() {
    return this.framebuffer;
  }
  
  public boolean isEntityShadows() {
    return this.entityShadows;
  }
  
  public Shader(String vertexName, String fragmentName) {
    this.shaderProgram = ShaderUtils.loadShader("/assets/minecraft/Lite/" + vertexName, "/assets/minecraft/Lite/" + fragmentName);
    createUniformMap();
  }
  
  protected void updateUniforms() {}
  
  boolean sizeHasChangedBuffer(int scaleFactor, int width, int height) {
    return (this.lastFactorBuffer != scaleFactor || this.lastWidthBuffer != width || this.lastHeightBuffer != height);
  }
  
  public void useShader() {
    GlStateManager.enableAlpha();
    GlStateManager.pushMatrix();
    GlStateManager.pushAttrib();
    ScaledResolution scaledResolution = new ScaledResolution(this.mc);
    int scaleFactor = scaledResolution.getScaleFactor();
    int width = scaledResolution.getScaledWidth();
    int height = scaledResolution.getScaledHeight();
    if (this.framebuffer == null) {
      this.framebuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
      this.framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    } 
    if (sizeHasChangedBuffer(scaleFactor, width, height)) {
      this.framebuffer.createBindFramebuffer(this.mc.displayWidth, this.mc.displayHeight);
      this.framebuffer.framebufferRender(this.mc.displayWidth, this.mc.displayHeight);
    } 
    this.lastFactorBuffer = scaleFactor;
    this.lastWidthBuffer = width;
    this.lastHeightBuffer = height;
    this.framebuffer.framebufferClear();
    this.framebuffer.bindFramebuffer(true);
    this.entityShadows = this.mc.gameSettings.field_181151_V;
    this.mc.gameSettings.field_181151_V = false;
    if (this.mc.theWorld != null)
      this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 0); 
  }
  
  public void disposeShader() {
    this.mc.gameSettings.field_181151_V = this.entityShadows;
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    this.mc.getFramebuffer().bindFramebuffer(true);
    this.mc.entityRenderer.disableLightmap();
    RenderHelper.disableStandardItemLighting();
    GL11.glPushMatrix();
    useProgramAndUpdate();
    this.mc.entityRenderer.setupOverlayRendering();
    drawFramebuffer(this.framebuffer);
    disableShader();
    GL11.glPopMatrix();
    this.mc.entityRenderer.disableLightmap();
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }
  
  public void drawFramebuffer(Framebuffer framebuffer) {
    ScaledResolution scaledResolution = new ScaledResolution(this.mc);
    GL11.glBindTexture(3553, framebuffer.framebufferTexture);
    GL11.glBegin(7);
    GL11.glTexCoord2d(0.0D, 1.0D);
    GL11.glVertex2d(0.0D, 0.0D);
    GL11.glTexCoord2d(0.0D, 0.0D);
    GL11.glVertex2d(0.0D, scaledResolution.getScaledHeight());
    GL11.glTexCoord2d(1.0D, 0.0D);
    GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
    GL11.glTexCoord2d(1.0D, 1.0D);
    GL11.glVertex2d(scaledResolution.getScaledWidth(), 0.0D);
    GL11.glEnd();
    disableShader();
  }
  
  public void useProgramAndUpdate() {
    enableShader();
    updateUniforms();
  }
  
  public void enableShader() {
    GL20.glUseProgram(this.shaderProgram.getProgramID());
  }
  
  public void disableShader() {
    GL20.glUseProgram(0);
  }
  
  public void createUniformMap() {
    if (this.uniformsMap == null) {
      this.uniformsMap = new HashMap<>();
      createUniforms();
    } 
  }
  
  protected void setupUniform(String uniformName) {
    this.uniformsMap.put(uniformName, Integer.valueOf(GL20.glGetUniformLocation(this.shaderProgram.getProgramID(), uniformName)));
  }
  
  protected int getUniform(String uniformName) {
    return ((Integer)this.uniformsMap.get(uniformName)).intValue();
  }
  
  protected abstract void createUniforms();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\shader\Shader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */