/*     */ package org.yaml.snakeyaml.reader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CodingErrorAction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeReader
/*     */   extends Reader
/*     */ {
/*  54 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*  55 */   private static final Charset UTF16BE = Charset.forName("UTF-16BE");
/*  56 */   private static final Charset UTF16LE = Charset.forName("UTF-16LE");
/*     */   
/*     */   PushbackInputStream internalIn;
/*  59 */   InputStreamReader internalIn2 = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BOM_SIZE = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   public UnicodeReader(InputStream in) {
/*  68 */     this.internalIn = new PushbackInputStream(in, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/*  77 */     return this.internalIn2.getEncoding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() throws IOException {
/*     */     Charset encoding;
/*     */     int unread;
/*  86 */     if (this.internalIn2 != null) {
/*     */       return;
/*     */     }
/*     */     
/*  90 */     byte[] bom = new byte[3];
/*     */     
/*  92 */     int n = this.internalIn.read(bom, 0, bom.length);
/*     */     
/*  94 */     if (bom[0] == -17 && bom[1] == -69 && bom[2] == -65) {
/*  95 */       encoding = UTF8;
/*  96 */       unread = n - 3;
/*  97 */     } else if (bom[0] == -2 && bom[1] == -1) {
/*  98 */       encoding = UTF16BE;
/*  99 */       unread = n - 2;
/* 100 */     } else if (bom[0] == -1 && bom[1] == -2) {
/* 101 */       encoding = UTF16LE;
/* 102 */       unread = n - 2;
/*     */     } else {
/*     */       
/* 105 */       encoding = UTF8;
/* 106 */       unread = n;
/*     */     } 
/*     */     
/* 109 */     if (unread > 0) {
/* 110 */       this.internalIn.unread(bom, n - unread, unread);
/*     */     }
/*     */     
/* 113 */     CharsetDecoder decoder = encoding.newDecoder().onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */     
/* 115 */     this.internalIn2 = new InputStreamReader(this.internalIn, decoder);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 119 */     init();
/* 120 */     this.internalIn2.close();
/*     */   }
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 124 */     init();
/* 125 */     return this.internalIn2.read(cbuf, off, len);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\reader\UnicodeReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */