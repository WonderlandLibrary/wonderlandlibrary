/*    */ package org.neverhook.client.ui.shader.shaders;
/*    */ 
/*    */ import org.lwjgl.opengl.GL20;
/*    */ import org.neverhook.client.ui.shader.FramebufferShader;
/*    */ 
/*    */ public class GlowShader
/*    */   extends FramebufferShader {
/*  8 */   public static final GlowShader GLOW_SHADER = new GlowShader();
/*    */   
/*    */   public GlowShader() {
/* 11 */     super("glow.frag");
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupUniforms() {
/* 16 */     setupUniform("texture");
/* 17 */     setupUniform("texelSize");
/* 18 */     setupUniform("color");
/* 19 */     setupUniform("radius");
/* 20 */     setupUniform("direction");
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateUniforms() {
/* 25 */     GL20.glUniform1i(getUniform("texture"), 0);
/* 26 */     GL20.glUniform2f(getUniform("texelSize"), 1.0F / mc.displayWidth * this.radius * this.quality, 1.0F / mc.displayHeight * this.radius * this.quality);
/* 27 */     GL20.glUniform3f(getUniform("color"), this.red, this.green, this.blue);
/* 28 */     GL20.glUniform1f(getUniform("radius"), this.radius);
/* 29 */     GL20.glUniform2f(getUniform("direction"), 1.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\shader\shaders\GlowShader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */