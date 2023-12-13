/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
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
/*    */ public abstract class Token
/*    */ {
/*    */   private final Mark startMark;
/*    */   private final Mark endMark;
/*    */   
/*    */   public enum ID
/*    */   {
/* 23 */     Alias("<alias>"),
/* 24 */     Anchor("<anchor>"),
/* 25 */     BlockEnd("<block end>"),
/* 26 */     BlockEntry("-"),
/* 27 */     BlockMappingStart("<block mapping start>"),
/* 28 */     BlockSequenceStart("<block sequence start>"),
/* 29 */     Directive("<directive>"),
/* 30 */     DocumentEnd("<document end>"),
/* 31 */     DocumentStart("<document start>"),
/* 32 */     FlowEntry(","),
/* 33 */     FlowMappingEnd("}"),
/* 34 */     FlowMappingStart("{"),
/* 35 */     FlowSequenceEnd("]"),
/* 36 */     FlowSequenceStart("["),
/* 37 */     Key("?"),
/* 38 */     Scalar("<scalar>"),
/* 39 */     StreamEnd("<stream end>"),
/* 40 */     StreamStart("<stream start>"),
/* 41 */     Tag("<tag>"),
/* 42 */     Value(":"),
/* 43 */     Whitespace("<whitespace>"),
/* 44 */     Comment("#"),
/* 45 */     Error("<error>");
/*    */     
/*    */     private final String description;
/*    */     
/*    */     ID(String s) {
/* 50 */       this.description = s;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 55 */       return this.description;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Token(Mark startMark, Mark endMark) {
/* 63 */     if (startMark == null || endMark == null) {
/* 64 */       throw new YAMLException("Token requires marks.");
/*    */     }
/* 66 */     this.startMark = startMark;
/* 67 */     this.endMark = endMark;
/*    */   }
/*    */   
/*    */   public Mark getStartMark() {
/* 71 */     return this.startMark;
/*    */   }
/*    */   
/*    */   public Mark getEndMark() {
/* 75 */     return this.endMark;
/*    */   }
/*    */   
/*    */   public abstract ID getTokenId();
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\tokens\Token.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */