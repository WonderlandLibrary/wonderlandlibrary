/*    */ package org.yaml.snakeyaml.error;
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
/*    */ public class MarkedYAMLException
/*    */   extends YAMLException
/*    */ {
/*    */   private static final long serialVersionUID = -9119388488683035101L;
/*    */   private String context;
/*    */   private Mark contextMark;
/*    */   private String problem;
/*    */   private Mark problemMark;
/*    */   private String note;
/*    */   
/*    */   protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, String note) {
/* 29 */     this(context, contextMark, problem, problemMark, note, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, String note, Throwable cause) {
/* 34 */     super(context + "; " + problem + "; " + problemMark, cause);
/* 35 */     this.context = context;
/* 36 */     this.contextMark = contextMark;
/* 37 */     this.problem = problem;
/* 38 */     this.problemMark = problemMark;
/* 39 */     this.note = note;
/*    */   }
/*    */   
/*    */   protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark) {
/* 43 */     this(context, contextMark, problem, problemMark, null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, Throwable cause) {
/* 48 */     this(context, contextMark, problem, problemMark, null, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 53 */     return toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     StringBuilder lines = new StringBuilder();
/* 59 */     if (this.context != null) {
/* 60 */       lines.append(this.context);
/* 61 */       lines.append("\n");
/*    */     } 
/* 63 */     if (this.contextMark != null && (this.problem == null || this.problemMark == null || this.contextMark.getName().equals(this.problemMark.getName()) || this.contextMark.getLine() != this.problemMark.getLine() || this.contextMark.getColumn() != this.problemMark.getColumn())) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 68 */       lines.append(this.contextMark.toString());
/* 69 */       lines.append("\n");
/*    */     } 
/* 71 */     if (this.problem != null) {
/* 72 */       lines.append(this.problem);
/* 73 */       lines.append("\n");
/*    */     } 
/* 75 */     if (this.problemMark != null) {
/* 76 */       lines.append(this.problemMark.toString());
/* 77 */       lines.append("\n");
/*    */     } 
/* 79 */     if (this.note != null) {
/* 80 */       lines.append(this.note);
/* 81 */       lines.append("\n");
/*    */     } 
/* 83 */     return lines.toString();
/*    */   }
/*    */   
/*    */   public String getContext() {
/* 87 */     return this.context;
/*    */   }
/*    */   
/*    */   public Mark getContextMark() {
/* 91 */     return this.contextMark;
/*    */   }
/*    */   
/*    */   public String getProblem() {
/* 95 */     return this.problem;
/*    */   }
/*    */   
/*    */   public Mark getProblemMark() {
/* 99 */     return this.problemMark;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\error\MarkedYAMLException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */