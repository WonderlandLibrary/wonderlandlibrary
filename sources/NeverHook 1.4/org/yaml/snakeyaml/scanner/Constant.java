/*    */ package org.yaml.snakeyaml.scanner;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ public final class Constant
/*    */ {
/*    */   private static final String ALPHA_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
/*    */   private static final String LINEBR_S = "\n  ";
/*    */   private static final String FULL_LINEBR_S = "\r\n  ";
/*    */   private static final String NULL_OR_LINEBR_S = "\000\r\n  ";
/*    */   private static final String NULL_BL_LINEBR_S = " \000\r\n  ";
/*    */   private static final String NULL_BL_T_LINEBR_S = "\t \000\r\n  ";
/*    */   private static final String NULL_BL_T_S = "\000 \t";
/*    */   private static final String URI_CHARS_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%";
/* 31 */   public static final Constant LINEBR = new Constant("\n  ");
/* 32 */   public static final Constant FULL_LINEBR = new Constant("\r\n  ");
/* 33 */   public static final Constant NULL_OR_LINEBR = new Constant("\000\r\n  ");
/* 34 */   public static final Constant NULL_BL_LINEBR = new Constant(" \000\r\n  ");
/* 35 */   public static final Constant NULL_BL_T_LINEBR = new Constant("\t \000\r\n  ");
/* 36 */   public static final Constant NULL_BL_T = new Constant("\000 \t");
/* 37 */   public static final Constant URI_CHARS = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%");
/*    */   
/* 39 */   public static final Constant ALPHA = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_");
/*    */   
/*    */   private String content;
/* 42 */   boolean[] contains = new boolean[128];
/*    */   boolean noASCII = false;
/*    */   
/*    */   private Constant(String content) {
/* 46 */     Arrays.fill(this.contains, false);
/* 47 */     StringBuilder sb = new StringBuilder();
/* 48 */     for (int i = 0; i < content.length(); i++) {
/* 49 */       int c = content.codePointAt(i);
/* 50 */       if (c < 128) {
/* 51 */         this.contains[c] = true;
/*    */       } else {
/* 53 */         sb.appendCodePoint(c);
/*    */       } 
/* 55 */     }  if (sb.length() > 0) {
/* 56 */       this.noASCII = true;
/* 57 */       this.content = sb.toString();
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean has(int c) {
/* 62 */     return (c < 128) ? this.contains[c] : ((this.noASCII && this.content.indexOf(c, 0) != -1));
/*    */   }
/*    */   
/*    */   public boolean hasNo(int c) {
/* 66 */     return !has(c);
/*    */   }
/*    */   
/*    */   public boolean has(int c, String additional) {
/* 70 */     return (has(c) || additional.indexOf(c, 0) != -1);
/*    */   }
/*    */   
/*    */   public boolean hasNo(int c, String additional) {
/* 74 */     return !has(c, additional);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\scanner\Constant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */