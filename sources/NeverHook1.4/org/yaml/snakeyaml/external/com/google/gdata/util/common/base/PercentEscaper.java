/*     */ package org.yaml.snakeyaml.external.com.google.gdata.util.common.base;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PercentEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*     */   public static final String SAFECHARS_URLENCODER = "-_.*";
/*     */   public static final String SAFEPATHCHARS_URLENCODER = "-_.!~*'()@:$&,;=";
/*     */   public static final String SAFEQUERYSTRINGCHARS_URLENCODER = "-_.!~*'()@:$,;/?:";
/*  90 */   private static final char[] URI_ESCAPED_SPACE = new char[] { '+' };
/*     */   
/*  92 */   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean plusForSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean[] safeOctets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PercentEscaper(String safeChars, boolean plusForSpace) {
/* 122 */     if (safeChars.matches(".*[0-9A-Za-z].*")) {
/* 123 */       throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     if (plusForSpace && safeChars.contains(" ")) {
/* 130 */       throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
/*     */     }
/*     */     
/* 133 */     if (safeChars.contains("%")) {
/* 134 */       throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
/*     */     }
/* 136 */     this.plusForSpace = plusForSpace;
/* 137 */     this.safeOctets = createSafeOctets(safeChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean[] createSafeOctets(String safeChars) {
/* 146 */     int maxChar = 122;
/* 147 */     char[] safeCharArray = safeChars.toCharArray();
/* 148 */     for (char c1 : safeCharArray) {
/* 149 */       maxChar = Math.max(c1, maxChar);
/*     */     }
/* 151 */     boolean[] octets = new boolean[maxChar + 1]; int c;
/* 152 */     for (c = 48; c <= 57; c++) {
/* 153 */       octets[c] = true;
/*     */     }
/* 155 */     for (c = 65; c <= 90; c++) {
/* 156 */       octets[c] = true;
/*     */     }
/* 158 */     for (c = 97; c <= 122; c++) {
/* 159 */       octets[c] = true;
/*     */     }
/* 161 */     for (char c1 : safeCharArray) {
/* 162 */       octets[c1] = true;
/*     */     }
/* 164 */     return octets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 174 */     for (; index < end; index++) {
/* 175 */       char c = csq.charAt(index);
/* 176 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/*     */         break;
/*     */       }
/*     */     } 
/* 180 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String escape(String s) {
/* 190 */     int slen = s.length();
/* 191 */     for (int index = 0; index < slen; index++) {
/* 192 */       char c = s.charAt(index);
/* 193 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/* 194 */         return escapeSlow(s, index);
/*     */       }
/*     */     } 
/* 197 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char[] escape(int cp) {
/* 208 */     if (cp < this.safeOctets.length && this.safeOctets[cp])
/* 209 */       return null; 
/* 210 */     if (cp == 32 && this.plusForSpace)
/* 211 */       return URI_ESCAPED_SPACE; 
/* 212 */     if (cp <= 127) {
/*     */ 
/*     */       
/* 215 */       char[] dest = new char[3];
/* 216 */       dest[0] = '%';
/* 217 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 218 */       dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
/* 219 */       return dest;
/* 220 */     }  if (cp <= 2047) {
/*     */ 
/*     */       
/* 223 */       char[] dest = new char[6];
/* 224 */       dest[0] = '%';
/* 225 */       dest[3] = '%';
/* 226 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 227 */       cp >>>= 4;
/* 228 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 229 */       cp >>>= 2;
/* 230 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 231 */       cp >>>= 4;
/* 232 */       dest[1] = UPPER_HEX_DIGITS[0xC | cp];
/* 233 */       return dest;
/* 234 */     }  if (cp <= 65535) {
/*     */ 
/*     */       
/* 237 */       char[] dest = new char[9];
/* 238 */       dest[0] = '%';
/* 239 */       dest[1] = 'E';
/* 240 */       dest[3] = '%';
/* 241 */       dest[6] = '%';
/* 242 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 243 */       cp >>>= 4;
/* 244 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 245 */       cp >>>= 2;
/* 246 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 247 */       cp >>>= 4;
/* 248 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 249 */       cp >>>= 2;
/* 250 */       dest[2] = UPPER_HEX_DIGITS[cp];
/* 251 */       return dest;
/* 252 */     }  if (cp <= 1114111) {
/* 253 */       char[] dest = new char[12];
/*     */ 
/*     */       
/* 256 */       dest[0] = '%';
/* 257 */       dest[1] = 'F';
/* 258 */       dest[3] = '%';
/* 259 */       dest[6] = '%';
/* 260 */       dest[9] = '%';
/* 261 */       dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
/* 262 */       cp >>>= 4;
/* 263 */       dest[10] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 264 */       cp >>>= 2;
/* 265 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 266 */       cp >>>= 4;
/* 267 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 268 */       cp >>>= 2;
/* 269 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 270 */       cp >>>= 4;
/* 271 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 272 */       cp >>>= 2;
/* 273 */       dest[2] = UPPER_HEX_DIGITS[cp & 0x7];
/* 274 */       return dest;
/*     */     } 
/*     */ 
/*     */     
/* 278 */     throw new IllegalArgumentException("Invalid unicode character value " + cp);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\external\com\google\gdat\\util\common\base\PercentEscaper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */