/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ 
/*    */ public final class DirectiveToken<T>
/*    */   extends Token
/*    */ {
/*    */   private final String name;
/*    */   private final List<T> value;
/*    */   
/*    */   public DirectiveToken(String name, List<T> value, Mark startMark, Mark endMark) {
/* 28 */     super(startMark, endMark);
/* 29 */     this.name = name;
/* 30 */     if (value != null && value.size() != 2) {
/* 31 */       throw new YAMLException("Two strings must be provided instead of " + String.valueOf(value.size()));
/*    */     }
/*    */     
/* 34 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public List<T> getValue() {
/* 42 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 47 */     return Token.ID.Directive;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\tokens\DirectiveToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */