/*    */ package org.yaml.snakeyaml.tokens;
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
/*    */ public final class TagTuple
/*    */ {
/*    */   private final String handle;
/*    */   private final String suffix;
/*    */   
/*    */   public TagTuple(String handle, String suffix) {
/* 23 */     if (suffix == null) {
/* 24 */       throw new NullPointerException("Suffix must be provided.");
/*    */     }
/* 26 */     this.handle = handle;
/* 27 */     this.suffix = suffix;
/*    */   }
/*    */   
/*    */   public String getHandle() {
/* 31 */     return this.handle;
/*    */   }
/*    */   
/*    */   public String getSuffix() {
/* 35 */     return this.suffix;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\tokens\TagTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */