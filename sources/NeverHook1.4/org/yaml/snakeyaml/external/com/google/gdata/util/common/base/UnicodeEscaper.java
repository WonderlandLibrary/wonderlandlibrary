/*     */ package org.yaml.snakeyaml.external.com.google.gdata.util.common.base;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public abstract class UnicodeEscaper
/*     */   implements Escaper
/*     */ {
/*     */   private static final int DEST_PAD = 32;
/*     */   
/*     */   protected abstract char[] escape(int paramInt);
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int start, int end) {
/* 122 */     int index = start;
/* 123 */     while (index < end) {
/* 124 */       int cp = codePointAt(csq, index, end);
/* 125 */       if (cp < 0 || escape(cp) != null) {
/*     */         break;
/*     */       }
/* 128 */       index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/*     */     } 
/* 130 */     return index;
/*     */   }
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
/*     */   public String escape(String string) {
/* 161 */     int end = string.length();
/* 162 */     int index = nextEscapeIndex(string, 0, end);
/* 163 */     return (index == end) ? string : escapeSlow(string, index);
/*     */   }
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
/*     */   protected final String escapeSlow(String s, int index) {
/* 188 */     int end = s.length();
/*     */ 
/*     */     
/* 191 */     char[] dest = DEST_TL.get();
/* 192 */     int destIndex = 0;
/* 193 */     int unescapedChunkStart = 0;
/*     */     
/* 195 */     while (index < end) {
/* 196 */       int cp = codePointAt(s, index, end);
/* 197 */       if (cp < 0) {
/* 198 */         throw new IllegalArgumentException("Trailing high surrogate at end of input");
/*     */       }
/* 200 */       char[] escaped = escape(cp);
/* 201 */       if (escaped != null) {
/* 202 */         int i = index - unescapedChunkStart;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 207 */         int sizeNeeded = destIndex + i + escaped.length;
/* 208 */         if (dest.length < sizeNeeded) {
/* 209 */           int destLength = sizeNeeded + end - index + 32;
/* 210 */           dest = growBuffer(dest, destIndex, destLength);
/*     */         } 
/*     */         
/* 213 */         if (i > 0) {
/* 214 */           s.getChars(unescapedChunkStart, index, dest, destIndex);
/* 215 */           destIndex += i;
/*     */         } 
/* 217 */         if (escaped.length > 0) {
/* 218 */           System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
/* 219 */           destIndex += escaped.length;
/*     */         } 
/*     */       } 
/* 222 */       unescapedChunkStart = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
/* 223 */       index = nextEscapeIndex(s, unescapedChunkStart, end);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     int charsSkipped = end - unescapedChunkStart;
/* 230 */     if (charsSkipped > 0) {
/* 231 */       int endIndex = destIndex + charsSkipped;
/* 232 */       if (dest.length < endIndex) {
/* 233 */         dest = growBuffer(dest, destIndex, endIndex);
/*     */       }
/* 235 */       s.getChars(unescapedChunkStart, end, dest, destIndex);
/* 236 */       destIndex = endIndex;
/*     */     } 
/* 238 */     return new String(dest, 0, destIndex);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appendable escape(final Appendable out) {
/* 286 */     assert out != null;
/*     */     
/* 288 */     return new Appendable() {
/* 289 */         int pendingHighSurrogate = -1;
/* 290 */         char[] decodedChars = new char[2];
/*     */         
/*     */         public Appendable append(CharSequence csq) throws IOException {
/* 293 */           return append(csq, 0, csq.length());
/*     */         }
/*     */         
/*     */         public Appendable append(CharSequence csq, int start, int end) throws IOException {
/* 297 */           int index = start;
/* 298 */           if (index < end) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 305 */             int unescapedChunkStart = index;
/* 306 */             if (this.pendingHighSurrogate != -1) {
/*     */ 
/*     */ 
/*     */               
/* 310 */               char c = csq.charAt(index++);
/* 311 */               if (!Character.isLowSurrogate(c)) {
/* 312 */                 throw new IllegalArgumentException("Expected low surrogate character but got " + c);
/*     */               }
/*     */               
/* 315 */               char[] escaped = UnicodeEscaper.this.escape(Character.toCodePoint((char)this.pendingHighSurrogate, c));
/*     */               
/* 317 */               if (escaped != null) {
/*     */ 
/*     */ 
/*     */                 
/* 321 */                 outputChars(escaped, escaped.length);
/* 322 */                 unescapedChunkStart++;
/*     */               
/*     */               }
/*     */               else {
/*     */ 
/*     */                 
/* 328 */                 out.append((char)this.pendingHighSurrogate);
/*     */               } 
/* 330 */               this.pendingHighSurrogate = -1;
/*     */             } 
/*     */ 
/*     */             
/*     */             while (true) {
/* 335 */               index = UnicodeEscaper.this.nextEscapeIndex(csq, index, end);
/* 336 */               if (index > unescapedChunkStart) {
/* 337 */                 out.append(csq, unescapedChunkStart, index);
/*     */               }
/* 339 */               if (index == end) {
/*     */                 break;
/*     */               }
/*     */ 
/*     */               
/* 344 */               int cp = UnicodeEscaper.codePointAt(csq, index, end);
/* 345 */               if (cp < 0) {
/*     */ 
/*     */ 
/*     */                 
/* 349 */                 this.pendingHighSurrogate = -cp;
/*     */                 
/*     */                 break;
/*     */               } 
/* 353 */               char[] escaped = UnicodeEscaper.this.escape(cp);
/* 354 */               if (escaped != null) {
/* 355 */                 outputChars(escaped, escaped.length);
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/* 360 */                 int len = Character.toChars(cp, this.decodedChars, 0);
/* 361 */                 outputChars(this.decodedChars, len);
/*     */               } 
/*     */ 
/*     */               
/* 365 */               index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/* 366 */               unescapedChunkStart = index;
/*     */             } 
/*     */           } 
/* 369 */           return this;
/*     */         }
/*     */         
/*     */         public Appendable append(char c) throws IOException {
/* 373 */           if (this.pendingHighSurrogate != -1) {
/*     */ 
/*     */ 
/*     */             
/* 377 */             if (!Character.isLowSurrogate(c)) {
/* 378 */               throw new IllegalArgumentException("Expected low surrogate character but got '" + c + "' with value " + c);
/*     */             }
/*     */ 
/*     */             
/* 382 */             char[] escaped = UnicodeEscaper.this.escape(Character.toCodePoint((char)this.pendingHighSurrogate, c));
/* 383 */             if (escaped != null) {
/* 384 */               outputChars(escaped, escaped.length);
/*     */             } else {
/* 386 */               out.append((char)this.pendingHighSurrogate);
/* 387 */               out.append(c);
/*     */             } 
/* 389 */             this.pendingHighSurrogate = -1;
/* 390 */           } else if (Character.isHighSurrogate(c)) {
/*     */             
/* 392 */             this.pendingHighSurrogate = c;
/*     */           } else {
/* 394 */             if (Character.isLowSurrogate(c)) {
/* 395 */               throw new IllegalArgumentException("Unexpected low surrogate character '" + c + "' with value " + c);
/*     */             }
/*     */ 
/*     */             
/* 399 */             char[] escaped = UnicodeEscaper.this.escape(c);
/* 400 */             if (escaped != null) {
/* 401 */               outputChars(escaped, escaped.length);
/*     */             } else {
/* 403 */               out.append(c);
/*     */             } 
/*     */           } 
/* 406 */           return this;
/*     */         }
/*     */         
/*     */         private void outputChars(char[] chars, int len) throws IOException {
/* 410 */           for (int n = 0; n < len; n++) {
/* 411 */             out.append(chars[n]);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
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
/*     */   protected static final int codePointAt(CharSequence seq, int index, int end) {
/* 456 */     if (index < end) {
/* 457 */       char c1 = seq.charAt(index++);
/* 458 */       if (c1 < '?' || c1 > '?')
/*     */       {
/* 460 */         return c1; } 
/* 461 */       if (c1 <= '?') {
/*     */ 
/*     */         
/* 464 */         if (index == end) {
/* 465 */           return -c1;
/*     */         }
/*     */         
/* 468 */         char c2 = seq.charAt(index);
/* 469 */         if (Character.isLowSurrogate(c2)) {
/* 470 */           return Character.toCodePoint(c1, c2);
/*     */         }
/* 472 */         throw new IllegalArgumentException("Expected low surrogate but got char '" + c2 + "' with value " + c2 + " at index " + index);
/*     */       } 
/*     */       
/* 475 */       throw new IllegalArgumentException("Unexpected low surrogate character '" + c1 + "' with value " + c1 + " at index " + (index - 1));
/*     */     } 
/*     */ 
/*     */     
/* 479 */     throw new IndexOutOfBoundsException("Index exceeds specified range");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char[] growBuffer(char[] dest, int index, int size) {
/* 488 */     char[] copy = new char[size];
/* 489 */     if (index > 0) {
/* 490 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 492 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 500 */   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>()
/*     */     {
/*     */       protected char[] initialValue() {
/* 503 */         return new char[1024];
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\external\com\google\gdat\\util\common\base\UnicodeEscaper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */