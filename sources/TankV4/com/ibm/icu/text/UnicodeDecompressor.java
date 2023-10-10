package com.ibm.icu.text;

public final class UnicodeDecompressor implements SCSU {
   private int fCurrentWindow = 0;
   private int[] fOffsets = new int[8];
   private int fMode = 0;
   private static final int BUFSIZE = 3;
   private byte[] fBuffer = new byte[3];
   private int fBufferLength = 0;

   public UnicodeDecompressor() {
      this.reset();
   }

   public static String decompress(byte[] var0) {
      char[] var1 = decompress(var0, 0, var0.length);
      return new String(var1);
   }

   public static char[] decompress(byte[] var0, int var1, int var2) {
      UnicodeDecompressor var3 = new UnicodeDecompressor();
      int var4 = Math.max(2, 2 * (var2 - var1));
      char[] var5 = new char[var4];
      int var6 = var3.decompress(var0, var1, var2, (int[])null, var5, 0, var4);
      char[] var7 = new char[var6];
      System.arraycopy(var5, 0, var7, 0, var6);
      return var7;
   }

   public int decompress(byte[] var1, int var2, int var3, int[] var4, char[] var5, int var6, int var7) {
      int var8 = var2;
      int var9 = var6;
      boolean var10 = false;
      if (var5.length >= 2 && var7 - var6 >= 2) {
         int var11;
         if (this.fBufferLength > 0) {
            var11 = 0;
            if (this.fBufferLength != 3) {
               var11 = this.fBuffer.length - this.fBufferLength;
               if (var3 - var2 < var11) {
                  var11 = var3 - var2;
               }

               System.arraycopy(var1, var2, this.fBuffer, this.fBufferLength, var11);
            }

            this.fBufferLength = 0;
            int var12 = this.decompress(this.fBuffer, 0, this.fBuffer.length, (int[])null, var5, var6, var7);
            var9 = var6 + var12;
            var8 = var2 + var11;
         }

         label124:
         while(var8 < var3 && var9 < var7) {
            int var13;
            byte var14;
            switch(this.fMode) {
            case 0:
               while(true) {
                  if (var8 >= var3 || var9 >= var7) {
                     continue label124;
                  }

                  var13 = var1[var8++] & 255;
                  switch(var13) {
                  case 0:
                  case 9:
                  case 10:
                  case 13:
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 61:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 80:
                  case 81:
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 101:
                  case 102:
                  case 103:
                  case 104:
                  case 105:
                  case 106:
                  case 107:
                  case 108:
                  case 109:
                  case 110:
                  case 111:
                  case 112:
                  case 113:
                  case 114:
                  case 115:
                  case 116:
                  case 117:
                  case 118:
                  case 119:
                  case 120:
                  case 121:
                  case 122:
                  case 123:
                  case 124:
                  case 125:
                  case 126:
                  case 127:
                     var5[var9++] = (char)var13;
                     break;
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                     if (var8 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     var11 = var1[var8++] & 255;
                     var5[var9++] = (char)(var11 + (var11 >= 0 && var11 < 128 ? sOffsets[var13 - 1] : this.fOffsets[var13 - 1] - 128));
                     break;
                  case 11:
                     if (var8 + 1 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     var13 = var1[var8++] & 255;
                     this.fCurrentWindow = (var13 & 224) >> 5;
                     this.fOffsets[this.fCurrentWindow] = 65536 + 128 * ((var13 & 31) << 8 | var1[var8++] & 255);
                  case 12:
                  default:
                     break;
                  case 14:
                     if (var8 + 1 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     var14 = var1[var8++];
                     var5[var9++] = (char)(var14 << 8 | var1[var8++] & 255);
                     break;
                  case 15:
                     this.fMode = 1;
                     continue label124;
                  case 16:
                  case 17:
                  case 18:
                  case 19:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                     this.fCurrentWindow = var13 - 16;
                     break;
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                  case 28:
                  case 29:
                  case 30:
                  case 31:
                     if (var8 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     this.fCurrentWindow = var13 - 24;
                     this.fOffsets[this.fCurrentWindow] = sOffsetTable[var1[var8++] & 255];
                     break;
                  case 128:
                  case 129:
                  case 130:
                  case 131:
                  case 132:
                  case 133:
                  case 134:
                  case 135:
                  case 136:
                  case 137:
                  case 138:
                  case 139:
                  case 140:
                  case 141:
                  case 142:
                  case 143:
                  case 144:
                  case 145:
                  case 146:
                  case 147:
                  case 148:
                  case 149:
                  case 150:
                  case 151:
                  case 152:
                  case 153:
                  case 154:
                  case 155:
                  case 156:
                  case 157:
                  case 158:
                  case 159:
                  case 160:
                  case 161:
                  case 162:
                  case 163:
                  case 164:
                  case 165:
                  case 166:
                  case 167:
                  case 168:
                  case 169:
                  case 170:
                  case 171:
                  case 172:
                  case 173:
                  case 174:
                  case 175:
                  case 176:
                  case 177:
                  case 178:
                  case 179:
                  case 180:
                  case 181:
                  case 182:
                  case 183:
                  case 184:
                  case 185:
                  case 186:
                  case 187:
                  case 188:
                  case 189:
                  case 190:
                  case 191:
                  case 192:
                  case 193:
                  case 194:
                  case 195:
                  case 196:
                  case 197:
                  case 198:
                  case 199:
                  case 200:
                  case 201:
                  case 202:
                  case 203:
                  case 204:
                  case 205:
                  case 206:
                  case 207:
                  case 208:
                  case 209:
                  case 210:
                  case 211:
                  case 212:
                  case 213:
                  case 214:
                  case 215:
                  case 216:
                  case 217:
                  case 218:
                  case 219:
                  case 220:
                  case 221:
                  case 222:
                  case 223:
                  case 224:
                  case 225:
                  case 226:
                  case 227:
                  case 228:
                  case 229:
                  case 230:
                  case 231:
                  case 232:
                  case 233:
                  case 234:
                  case 235:
                  case 236:
                  case 237:
                  case 238:
                  case 239:
                  case 240:
                  case 241:
                  case 242:
                  case 243:
                  case 244:
                  case 245:
                  case 246:
                  case 247:
                  case 248:
                  case 249:
                  case 250:
                  case 251:
                  case 252:
                  case 253:
                  case 254:
                  case 255:
                     if (this.fOffsets[this.fCurrentWindow] <= 65535) {
                        var5[var9++] = (char)(var13 + this.fOffsets[this.fCurrentWindow] - 128);
                     } else {
                        if (var9 + 1 >= var7) {
                           --var8;
                           System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                           this.fBufferLength = var3 - var8;
                           var8 += this.fBufferLength;
                           break label124;
                        }

                        var11 = this.fOffsets[this.fCurrentWindow] - 65536;
                        var5[var9++] = (char)('\ud800' + (var11 >> 10));
                        var5[var9++] = (char)('\udc00' + (var11 & 1023) + (var13 & 127));
                     }
                  }
               }
            case 1:
               while(var8 < var3 && var9 < var7) {
                  var13 = var1[var8++] & 255;
                  switch(var13) {
                  case 224:
                  case 225:
                  case 226:
                  case 227:
                  case 228:
                  case 229:
                  case 230:
                  case 231:
                     this.fCurrentWindow = var13 - 224;
                     this.fMode = 0;
                     continue label124;
                  case 232:
                  case 233:
                  case 234:
                  case 235:
                  case 236:
                  case 237:
                  case 238:
                  case 239:
                     if (var8 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     this.fCurrentWindow = var13 - 232;
                     this.fOffsets[this.fCurrentWindow] = sOffsetTable[var1[var8++] & 255];
                     this.fMode = 0;
                     continue label124;
                  case 240:
                     if (var8 >= var3 - 1) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     var14 = var1[var8++];
                     var5[var9++] = (char)(var14 << 8 | var1[var8++] & 255);
                     break;
                  case 241:
                     if (var8 + 1 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     var13 = var1[var8++] & 255;
                     this.fCurrentWindow = (var13 & 224) >> 5;
                     this.fOffsets[this.fCurrentWindow] = 65536 + 128 * ((var13 & 31) << 8 | var1[var8++] & 255);
                     this.fMode = 0;
                     continue label124;
                  default:
                     if (var8 >= var3) {
                        --var8;
                        System.arraycopy(var1, var8, this.fBuffer, 0, var3 - var8);
                        this.fBufferLength = var3 - var8;
                        var8 += this.fBufferLength;
                        break label124;
                     }

                     var5[var9++] = (char)(var13 << 8 | var1[var8++] & 255);
                  }
               }
            }
         }

         if (var4 != null) {
            var4[0] = var8 - var2;
         }

         return var9 - var6;
      } else {
         throw new IllegalArgumentException("charBuffer.length < 2");
      }
   }

   public void reset() {
      this.fOffsets[0] = 128;
      this.fOffsets[1] = 192;
      this.fOffsets[2] = 1024;
      this.fOffsets[3] = 1536;
      this.fOffsets[4] = 2304;
      this.fOffsets[5] = 12352;
      this.fOffsets[6] = 12448;
      this.fOffsets[7] = 65280;
      this.fCurrentWindow = 0;
      this.fMode = 0;
      this.fBufferLength = 0;
   }
}
