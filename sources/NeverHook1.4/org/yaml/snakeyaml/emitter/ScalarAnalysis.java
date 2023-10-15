/*    */ package org.yaml.snakeyaml.emitter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ScalarAnalysis
/*    */ {
/*    */   private String scalar;
/*    */   private boolean empty;
/*    */   private boolean multiline;
/*    */   private boolean allowFlowPlain;
/*    */   private boolean allowBlockPlain;
/*    */   private boolean allowSingleQuoted;
/*    */   private boolean allowBlock;
/*    */   
/*    */   public ScalarAnalysis(String scalar, boolean empty, boolean multiline, boolean allowFlowPlain, boolean allowBlockPlain, boolean allowSingleQuoted, boolean allowBlock) {
/* 33 */     this.scalar = scalar;
/* 34 */     this.empty = empty;
/* 35 */     this.multiline = multiline;
/* 36 */     this.allowFlowPlain = allowFlowPlain;
/* 37 */     this.allowBlockPlain = allowBlockPlain;
/* 38 */     this.allowSingleQuoted = allowSingleQuoted;
/* 39 */     this.allowBlock = allowBlock;
/*    */   }
/*    */   
/*    */   public String getScalar() {
/* 43 */     return this.scalar;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 47 */     return this.empty;
/*    */   }
/*    */   
/*    */   public boolean isMultiline() {
/* 51 */     return this.multiline;
/*    */   }
/*    */   
/*    */   public boolean isAllowFlowPlain() {
/* 55 */     return this.allowFlowPlain;
/*    */   }
/*    */   
/*    */   public boolean isAllowBlockPlain() {
/* 59 */     return this.allowBlockPlain;
/*    */   }
/*    */   
/*    */   public boolean isAllowSingleQuoted() {
/* 63 */     return this.allowSingleQuoted;
/*    */   }
/*    */   
/*    */   public boolean isAllowBlock() {
/* 67 */     return this.allowBlock;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\emitter\ScalarAnalysis.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */