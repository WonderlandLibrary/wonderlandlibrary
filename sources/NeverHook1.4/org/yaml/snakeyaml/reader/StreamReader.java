/*     */ package org.yaml.snakeyaml.reader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Arrays;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
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
/*     */ public class StreamReader
/*     */ {
/*     */   private String name;
/*     */   private final Reader stream;
/*     */   private int[] dataWindow;
/*     */   private int dataLength;
/*  47 */   private int pointer = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean eof;
/*     */ 
/*     */   
/*  54 */   private int index = 0;
/*  55 */   private int line = 0;
/*  56 */   private int column = 0;
/*     */   
/*     */   private char[] buffer;
/*     */   
/*     */   private static final int BUFFER_SIZE = 1025;
/*     */   
/*     */   public StreamReader(String stream) {
/*  63 */     this(new StringReader(stream));
/*  64 */     this.name = "'string'";
/*     */   }
/*     */   
/*     */   public StreamReader(Reader reader) {
/*  68 */     this.name = "'reader'";
/*  69 */     this.dataWindow = new int[0];
/*  70 */     this.dataLength = 0;
/*  71 */     this.stream = reader;
/*  72 */     this.eof = false;
/*  73 */     this.buffer = new char[1025];
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(String data) {
/*  77 */     int length = data.length();
/*  78 */     for (int offset = 0; offset < length; ) {
/*  79 */       int codePoint = data.codePointAt(offset);
/*     */       
/*  81 */       if (!isPrintable(codePoint)) {
/*  82 */         return false;
/*     */       }
/*     */       
/*  85 */       offset += Character.charCount(codePoint);
/*     */     } 
/*     */     
/*  88 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(int c) {
/*  92 */     return ((c >= 32 && c <= 126) || c == 9 || c == 10 || c == 13 || c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mark getMark() {
/*  98 */     return new Mark(this.name, this.index, this.line, this.column, this.dataWindow, this.pointer);
/*     */   }
/*     */   
/*     */   public void forward() {
/* 102 */     forward(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forward(int length) {
/* 112 */     for (int i = 0; i < length && ensureEnoughData(); i++) {
/* 113 */       int c = this.dataWindow[this.pointer++];
/* 114 */       this.index++;
/* 115 */       if (Constant.LINEBR.has(c) || (c == 13 && ensureEnoughData() && this.dataWindow[this.pointer] != 10)) {
/*     */         
/* 117 */         this.line++;
/* 118 */         this.column = 0;
/* 119 */       } else if (c != 65279) {
/* 120 */         this.column++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int peek() {
/* 126 */     return ensureEnoughData() ? this.dataWindow[this.pointer] : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int peek(int index) {
/* 136 */     return ensureEnoughData(index) ? this.dataWindow[this.pointer + index] : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefix(int length) {
/* 146 */     if (length == 0)
/* 147 */       return ""; 
/* 148 */     if (ensureEnoughData(length)) {
/* 149 */       return new String(this.dataWindow, this.pointer, length);
/*     */     }
/* 151 */     return new String(this.dataWindow, this.pointer, Math.min(length, this.dataLength - this.pointer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefixForward(int length) {
/* 162 */     String prefix = prefix(length);
/* 163 */     this.pointer += length;
/* 164 */     this.index += length;
/*     */     
/* 166 */     this.column += length;
/* 167 */     return prefix;
/*     */   }
/*     */   
/*     */   private boolean ensureEnoughData() {
/* 171 */     return ensureEnoughData(0);
/*     */   }
/*     */   
/*     */   private boolean ensureEnoughData(int size) {
/* 175 */     if (!this.eof && this.pointer + size >= this.dataLength) {
/* 176 */       update();
/*     */     }
/* 178 */     return (this.pointer + size < this.dataLength);
/*     */   }
/*     */   
/*     */   private void update() {
/*     */     try {
/* 183 */       int read = this.stream.read(this.buffer, 0, 1024);
/* 184 */       if (read > 0) {
/* 185 */         int cpIndex = this.dataLength - this.pointer;
/* 186 */         this.dataWindow = Arrays.copyOfRange(this.dataWindow, this.pointer, this.dataLength + read);
/*     */         
/* 188 */         if (Character.isHighSurrogate(this.buffer[read - 1])) {
/* 189 */           if (this.stream.read(this.buffer, read, 1) == -1) {
/* 190 */             this.eof = true;
/*     */           } else {
/* 192 */             read++;
/*     */           } 
/*     */         }
/*     */         
/* 196 */         int nonPrintable = 32;
/* 197 */         for (int i = 0; i < read; cpIndex++) {
/* 198 */           int codePoint = Character.codePointAt(this.buffer, i);
/* 199 */           this.dataWindow[cpIndex] = codePoint;
/* 200 */           if (isPrintable(codePoint)) {
/* 201 */             i += Character.charCount(codePoint);
/*     */           } else {
/* 203 */             nonPrintable = codePoint;
/* 204 */             i = read;
/*     */           } 
/*     */         } 
/*     */         
/* 208 */         this.dataLength = cpIndex;
/* 209 */         this.pointer = 0;
/* 210 */         if (nonPrintable != 32) {
/* 211 */           throw new ReaderException(this.name, cpIndex - 1, nonPrintable, "special characters are not allowed");
/*     */         }
/*     */       } else {
/*     */         
/* 215 */         this.eof = true;
/*     */       } 
/* 217 */     } catch (IOException ioe) {
/* 218 */       throw new YAMLException(ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 224 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 231 */     return this.index;
/*     */   }
/*     */   
/*     */   public int getLine() {
/* 235 */     return this.line;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\reader\StreamReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */