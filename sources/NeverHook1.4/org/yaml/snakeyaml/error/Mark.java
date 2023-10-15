/*     */ package org.yaml.snakeyaml.error;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.yaml.snakeyaml.scanner.Constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Mark
/*     */   implements Serializable
/*     */ {
/*     */   private String name;
/*     */   private int index;
/*     */   private int line;
/*     */   private int column;
/*     */   private int[] buffer;
/*     */   private int pointer;
/*     */   
/*     */   private static int[] toCodePoints(char[] str) {
/*  35 */     int[] codePoints = new int[Character.codePointCount(str, 0, str.length)];
/*  36 */     for (int i = 0, c = 0; i < str.length; c++) {
/*  37 */       int cp = Character.codePointAt(str, i);
/*  38 */       codePoints[c] = cp;
/*  39 */       i += Character.charCount(cp);
/*     */     } 
/*  41 */     return codePoints;
/*     */   }
/*     */   
/*     */   public Mark(String name, int index, int line, int column, char[] str, int pointer) {
/*  45 */     this(name, index, line, column, toCodePoints(str), pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Mark(String name, int index, int line, int column, String buffer, int pointer) {
/*  55 */     this(name, index, line, column, buffer.toCharArray(), pointer);
/*     */   }
/*     */ 
/*     */   
/*     */   public Mark(String name, int index, int line, int column, int[] buffer, int pointer) {
/*  60 */     this.name = name;
/*  61 */     this.index = index;
/*  62 */     this.line = line;
/*  63 */     this.column = column;
/*  64 */     this.buffer = buffer;
/*  65 */     this.pointer = pointer;
/*     */   }
/*     */   
/*     */   private boolean isLineBreak(int c) {
/*  69 */     return Constant.NULL_OR_LINEBR.has(c);
/*     */   }
/*     */   
/*     */   public String get_snippet(int indent, int max_length) {
/*  73 */     float half = max_length / 2.0F - 1.0F;
/*  74 */     int start = this.pointer;
/*  75 */     String head = "";
/*  76 */     while (start > 0 && !isLineBreak(this.buffer[start - 1])) {
/*  77 */       start--;
/*  78 */       if ((this.pointer - start) > half) {
/*  79 */         head = " ... ";
/*  80 */         start += 5;
/*     */         break;
/*     */       } 
/*     */     } 
/*  84 */     String tail = "";
/*  85 */     int end = this.pointer;
/*  86 */     while (end < this.buffer.length && !isLineBreak(this.buffer[end])) {
/*  87 */       end++;
/*  88 */       if ((end - this.pointer) > half) {
/*  89 */         tail = " ... ";
/*  90 */         end -= 5;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  95 */     StringBuilder result = new StringBuilder(); int i;
/*  96 */     for (i = 0; i < indent; i++) {
/*  97 */       result.append(" ");
/*     */     }
/*  99 */     result.append(head);
/* 100 */     for (i = start; i < end; i++) {
/* 101 */       result.appendCodePoint(this.buffer[i]);
/*     */     }
/* 103 */     result.append(tail);
/* 104 */     result.append("\n");
/* 105 */     for (i = 0; i < indent + this.pointer - start + head.length(); i++) {
/* 106 */       result.append(" ");
/*     */     }
/* 108 */     result.append("^");
/* 109 */     return result.toString();
/*     */   }
/*     */   
/*     */   public String get_snippet() {
/* 113 */     return get_snippet(4, 75);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     String snippet = get_snippet();
/* 119 */     StringBuilder builder = new StringBuilder(" in ");
/* 120 */     builder.append(this.name);
/* 121 */     builder.append(", line ");
/* 122 */     builder.append(this.line + 1);
/* 123 */     builder.append(", column ");
/* 124 */     builder.append(this.column + 1);
/* 125 */     builder.append(":\n");
/* 126 */     builder.append(snippet);
/* 127 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 131 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 139 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 147 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 155 */     return this.index;
/*     */   }
/*     */   
/*     */   public int[] getBuffer() {
/* 159 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public int getPointer() {
/* 163 */     return this.pointer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\error\Mark.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */