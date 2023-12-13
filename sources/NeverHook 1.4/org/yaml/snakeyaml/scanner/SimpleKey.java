/*    */ package org.yaml.snakeyaml.scanner;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SimpleKey
/*    */ {
/*    */   private int tokenNumber;
/*    */   private boolean required;
/*    */   private int index;
/*    */   private int line;
/*    */   private int column;
/*    */   private Mark mark;
/*    */   
/*    */   public SimpleKey(int tokenNumber, boolean required, int index, int line, int column, Mark mark) {
/* 37 */     this.tokenNumber = tokenNumber;
/* 38 */     this.required = required;
/* 39 */     this.index = index;
/* 40 */     this.line = line;
/* 41 */     this.column = column;
/* 42 */     this.mark = mark;
/*    */   }
/*    */   
/*    */   public int getTokenNumber() {
/* 46 */     return this.tokenNumber;
/*    */   }
/*    */   
/*    */   public int getColumn() {
/* 50 */     return this.column;
/*    */   }
/*    */   
/*    */   public Mark getMark() {
/* 54 */     return this.mark;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 58 */     return this.index;
/*    */   }
/*    */   
/*    */   public int getLine() {
/* 62 */     return this.line;
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 66 */     return this.required;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "SimpleKey - tokenNumber=" + this.tokenNumber + " required=" + this.required + " index=" + this.index + " line=" + this.line + " column=" + this.column;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\scanner\SimpleKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */