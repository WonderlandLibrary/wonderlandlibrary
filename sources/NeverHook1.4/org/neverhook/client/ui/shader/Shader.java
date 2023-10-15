/*    */ package org.neverhook.client.ui.shader;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.lwjgl.opengl.ARBShaderObjects;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.lwjgl.opengl.GL20;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public abstract class Shader
/*    */   implements Helper
/*    */ {
/*    */   private int program;
/*    */   private Map<String, Integer> uniformsMap;
/*    */   
/*    */   public Shader(String fragmentShader) {
/*    */     int vertexShaderID, fragmentShaderID;
/*    */     try {
/* 21 */       InputStream vertexStream = getClass().getResourceAsStream("/assets/minecraft/neverhook/shaders/vertex.vert");
/* 22 */       vertexShaderID = createShader(IOUtils.toString(vertexStream), 35633);
/* 23 */       IOUtils.closeQuietly(vertexStream);
/*    */       
/* 25 */       InputStream fragmentStream = getClass().getResourceAsStream("/assets/minecraft/neverhook/shaders/fragment/" + fragmentShader);
/* 26 */       fragmentShaderID = createShader(IOUtils.toString(fragmentStream), 35632);
/* 27 */       IOUtils.closeQuietly(fragmentStream);
/* 28 */     } catch (Exception e) {
/*    */       return;
/*    */     } 
/*    */     
/* 32 */     if (vertexShaderID == 0 || fragmentShaderID == 0) {
/*    */       return;
/*    */     }
/* 35 */     this.program = ARBShaderObjects.glCreateProgramObjectARB();
/*    */     
/* 37 */     if (this.program == 0) {
/*    */       return;
/*    */     }
/* 40 */     ARBShaderObjects.glAttachObjectARB(this.program, vertexShaderID);
/* 41 */     ARBShaderObjects.glAttachObjectARB(this.program, fragmentShaderID);
/*    */     
/* 43 */     ARBShaderObjects.glLinkProgramARB(this.program);
/* 44 */     ARBShaderObjects.glValidateProgramARB(this.program);
/*    */   }
/*    */   
/*    */   public void startShader() {
/* 48 */     GL11.glPushMatrix();
/* 49 */     GL20.glUseProgram(this.program);
/*    */     
/* 51 */     if (this.uniformsMap == null) {
/* 52 */       this.uniformsMap = new HashMap<>();
/* 53 */       setupUniforms();
/*    */     } 
/*    */     
/* 56 */     updateUniforms();
/*    */   }
/*    */   
/*    */   public void stopShader() {
/* 60 */     GL20.glUseProgram(0);
/* 61 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */   public abstract void setupUniforms();
/*    */   
/*    */   public abstract void updateUniforms();
/*    */   
/*    */   private int createShader(String shaderSource, int shaderType) {
/* 69 */     int shader = 0;
/*    */     
/*    */     try {
/* 72 */       shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
/*    */       
/* 74 */       if (shader == 0) {
/* 75 */         return 0;
/*    */       }
/* 77 */       ARBShaderObjects.glShaderSourceARB(shader, shaderSource);
/* 78 */       ARBShaderObjects.glCompileShaderARB(shader);
/*    */       
/* 80 */       return shader;
/* 81 */     } catch (Exception e) {
/* 82 */       ARBShaderObjects.glDeleteObjectARB(shader);
/* 83 */       throw e;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUniform(String uniformName, int location) {
/* 89 */     this.uniformsMap.put(uniformName, Integer.valueOf(location));
/*    */   }
/*    */   
/*    */   public void setupUniform(String uniformName) {
/* 93 */     setUniform(uniformName, GL20.glGetUniformLocation(this.program, uniformName));
/*    */   }
/*    */   
/*    */   public int getUniform(String uniformName) {
/* 97 */     return ((Integer)this.uniformsMap.get(uniformName)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\shader\Shader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */