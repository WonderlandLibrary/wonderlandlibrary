/*    */ package org.neverhook.client.ui.shader.shaders;
/*    */ 
/*    */ import org.lwjgl.opengl.GL20;
/*    */ import org.neverhook.client.ui.shader.FramebufferShader;
/*    */ 
/*    */ public class EntityGlowShader
/*    */   extends FramebufferShader {
/*  8 */   public static EntityGlowShader GLOW_SHADER = new EntityGlowShader();
/*    */   
/*    */   public EntityGlowShader() {
/* 11 */     super("entityGlow.frag");
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupUniforms() {
/* 16 */     setupUniform("texture");
/* 17 */     setupUniform("texelSize");
/* 18 */     setupUniform("color");
/* 19 */     setupUniform("divider");
/* 20 */     setupUniform("radius");
/* 21 */     setupUniform("maxSample");
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateUniforms() {
/* 26 */     GL20.glUniform1i(getUniform("texture"), 0);
/* 27 */     GL20.glUniform2f(getUniform("texelSize"), 1.0F / mc.displayWidth * this.radius * this.quality, 1.0F / mc.displayHeight * this.radius * this.quality);
/* 28 */     GL20.glUniform3f(getUniform("color"), this.red, this.green, this.blue);
/* 29 */     GL20.glUniform1f(getUniform("divider"), 140.0F);
/* 30 */     GL20.glUniform1f(getUniform("radius"), this.radius);
/* 31 */     GL20.glUniform1f(getUniform("maxSample"), 10.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\shader\shaders\EntityGlowShader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */