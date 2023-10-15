/*     */ package org.yaml.snakeyaml.external.biz.base64Coder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64Coder
/*     */ {
/*  35 */   private static final String systemLineSeparator = System.getProperty("line.separator");
/*     */ 
/*     */   
/*  38 */   private static char[] map1 = new char[64];
/*     */   static {
/*  40 */     int i = 0; char c;
/*  41 */     for (c = 'A'; c <= 'Z'; c = (char)(c + 1))
/*  42 */       map1[i++] = c; 
/*  43 */     for (c = 'a'; c <= 'z'; c = (char)(c + 1))
/*  44 */       map1[i++] = c; 
/*  45 */     for (c = '0'; c <= '9'; c = (char)(c + 1))
/*  46 */       map1[i++] = c; 
/*  47 */     map1[i++] = '+';
/*  48 */     map1[i++] = '/';
/*     */   }
/*     */ 
/*     */   
/*  52 */   private static byte[] map2 = new byte[128];
/*     */   static {
/*  54 */     for (i = 0; i < map2.length; i++)
/*  55 */       map2[i] = -1; 
/*  56 */     for (i = 0; i < 64; i++) {
/*  57 */       map2[map1[i]] = (byte)i;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeString(String s) {
/*  69 */     return new String(encode(s.getBytes()));
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
/*     */   public static String encodeLines(byte[] in) {
/*  82 */     return encodeLines(in, 0, in.length, 76, systemLineSeparator);
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
/*     */   public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
/* 104 */     int blockLen = lineLen * 3 / 4;
/* 105 */     if (blockLen <= 0)
/* 106 */       throw new IllegalArgumentException(); 
/* 107 */     int lines = (iLen + blockLen - 1) / blockLen;
/* 108 */     int bufLen = (iLen + 2) / 3 * 4 + lines * lineSeparator.length();
/* 109 */     StringBuilder buf = new StringBuilder(bufLen);
/* 110 */     int ip = 0;
/* 111 */     while (ip < iLen) {
/* 112 */       int l = Math.min(iLen - ip, blockLen);
/* 113 */       buf.append(encode(in, iOff + ip, l));
/* 114 */       buf.append(lineSeparator);
/* 115 */       ip += l;
/*     */     } 
/* 117 */     return buf.toString();
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
/*     */   public static char[] encode(byte[] in) {
/* 129 */     return encode(in, 0, in.length);
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
/*     */   public static char[] encode(byte[] in, int iLen) {
/* 143 */     return encode(in, 0, iLen);
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
/*     */   public static char[] encode(byte[] in, int iOff, int iLen) {
/* 160 */     int oDataLen = (iLen * 4 + 2) / 3;
/* 161 */     int oLen = (iLen + 2) / 3 * 4;
/* 162 */     char[] out = new char[oLen];
/* 163 */     int ip = iOff;
/* 164 */     int iEnd = iOff + iLen;
/* 165 */     int op = 0;
/* 166 */     while (ip < iEnd) {
/* 167 */       int i0 = in[ip++] & 0xFF;
/* 168 */       int i1 = (ip < iEnd) ? (in[ip++] & 0xFF) : 0;
/* 169 */       int i2 = (ip < iEnd) ? (in[ip++] & 0xFF) : 0;
/* 170 */       int o0 = i0 >>> 2;
/* 171 */       int o1 = (i0 & 0x3) << 4 | i1 >>> 4;
/* 172 */       int o2 = (i1 & 0xF) << 2 | i2 >>> 6;
/* 173 */       int o3 = i2 & 0x3F;
/* 174 */       out[op++] = map1[o0];
/* 175 */       out[op++] = map1[o1];
/* 176 */       out[op] = (op < oDataLen) ? map1[o2] : '=';
/* 177 */       op++;
/* 178 */       out[op] = (op < oDataLen) ? map1[o3] : '=';
/* 179 */       op++;
/*     */     } 
/* 181 */     return out;
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
/*     */   public static String decodeString(String s) {
/* 195 */     return new String(decode(s));
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
/*     */   public static byte[] decodeLines(String s) {
/* 211 */     char[] buf = new char[s.length()];
/* 212 */     int p = 0;
/* 213 */     for (int ip = 0; ip < s.length(); ip++) {
/* 214 */       char c = s.charAt(ip);
/* 215 */       if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
/* 216 */         buf[p++] = c; 
/*     */     } 
/* 218 */     return decode(buf, 0, p);
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
/*     */   public static byte[] decode(String s) {
/* 232 */     return decode(s.toCharArray());
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
/*     */   public static byte[] decode(char[] in) {
/* 246 */     return decode(in, 0, in.length);
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
/*     */   public static byte[] decode(char[] in, int iOff, int iLen) {
/* 266 */     if (iLen % 4 != 0) {
/* 267 */       throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
/*     */     }
/* 269 */     while (iLen > 0 && in[iOff + iLen - 1] == '=')
/* 270 */       iLen--; 
/* 271 */     int oLen = iLen * 3 / 4;
/* 272 */     byte[] out = new byte[oLen];
/* 273 */     int ip = iOff;
/* 274 */     int iEnd = iOff + iLen;
/* 275 */     int op = 0;
/* 276 */     while (ip < iEnd) {
/* 277 */       int i0 = in[ip++];
/* 278 */       int i1 = in[ip++];
/* 279 */       int i2 = (ip < iEnd) ? in[ip++] : 65;
/* 280 */       int i3 = (ip < iEnd) ? in[ip++] : 65;
/* 281 */       if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
/* 282 */         throw new IllegalArgumentException("Illegal character in Base64 encoded data."); 
/* 283 */       int b0 = map2[i0];
/* 284 */       int b1 = map2[i1];
/* 285 */       int b2 = map2[i2];
/* 286 */       int b3 = map2[i3];
/* 287 */       if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
/* 288 */         throw new IllegalArgumentException("Illegal character in Base64 encoded data."); 
/* 289 */       int o0 = b0 << 2 | b1 >>> 4;
/* 290 */       int o1 = (b1 & 0xF) << 4 | b2 >>> 2;
/* 291 */       int o2 = (b2 & 0x3) << 6 | b3;
/* 292 */       out[op++] = (byte)o0;
/* 293 */       if (op < oLen)
/* 294 */         out[op++] = (byte)o1; 
/* 295 */       if (op < oLen)
/* 296 */         out[op++] = (byte)o2; 
/*     */     } 
/* 298 */     return out;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\external\biz\base64Coder\Base64Coder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */