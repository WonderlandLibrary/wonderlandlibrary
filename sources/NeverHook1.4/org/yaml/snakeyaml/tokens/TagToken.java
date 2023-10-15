/*    */ package org.yaml.snakeyaml.tokens;
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
/*    */ public final class TagToken
/*    */   extends Token
/*    */ {
/*    */   private final TagTuple value;
/*    */   
/*    */   public TagToken(TagTuple value, Mark startMark, Mark endMark) {
/* 24 */     super(startMark, endMark);
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public TagTuple getValue() {
/* 29 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 34 */     return Token.ID.Tag;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\tokens\TagToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */