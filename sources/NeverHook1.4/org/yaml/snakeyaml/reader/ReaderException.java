/*    */ package org.yaml.snakeyaml.reader;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.YAMLException;
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
/*    */ public class ReaderException
/*    */   extends YAMLException
/*    */ {
/*    */   private static final long serialVersionUID = 8710781187529689083L;
/*    */   private final String name;
/*    */   private final int codePoint;
/*    */   private final int position;
/*    */   
/*    */   public ReaderException(String name, int position, int codePoint, String message) {
/* 27 */     super(message);
/* 28 */     this.name = name;
/* 29 */     this.codePoint = codePoint;
/* 30 */     this.position = position;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getCodePoint() {
/* 38 */     return this.codePoint;
/*    */   }
/*    */   
/*    */   public int getPosition() {
/* 42 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     String s = new String(Character.toChars(this.codePoint));
/* 48 */     return "unacceptable code point '" + s + "' (0x" + Integer.toHexString(this.codePoint).toUpperCase() + ") " + getMessage() + "\nin \"" + this.name + "\", position " + this.position;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\reader\ReaderException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */