package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MetadataParserImpl;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

final class ID3MetadataParser extends MetadataParserImpl {
   private static final int ID3_VERSION_MIN = 2;
   private static final int ID3_VERSION_MAX = 4;
   private static final String CHARSET_UTF_8 = "UTF-8";
   private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
   private static final String CHARSET_UTF_16 = "UTF-16";
   private static final String CHARSET_UTF_16BE = "UTF-16BE";
   private int COMMCount = 0;
   private int TXXXCount = 0;
   private int version = 3;
   private boolean unsynchronized = false;

   public ID3MetadataParser(Locator var1) {
      super(var1);
   }

   protected void parse() {
      try {
         if (!Charset.isSupported("ISO-8859-1")) {
            throw new UnsupportedCharsetException("ISO-8859-1");
         }

         byte[] var1 = this.getBytes(10);
         this.version = var1[3] & 255;
         if (var1[0] == 73 && var1[1] == 68 && var1[2] == 51 && this.version >= 2 && this.version <= 4) {
            int var2 = var1[5] & 255;
            if ((var2 & 128) == 128) {
               this.unsynchronized = true;
            }

            int var3 = 0;
            int var4 = 6;

            for(int var5 = 21; var4 < 10; ++var4) {
               var3 += (var1[var4] & 127) << var5;
               var5 -= 7;
            }

            this.startRawMetadata(var3 + 10);
            this.stuffRawMetadata(var1, 0, 10);
            this.readRawMetadata(var3);
            this.setParseRawMetadata(true);
            this.skipBytes(10);

            while(this.getStreamPosition() < var3) {
               byte[] var21;
               if (2 == this.version) {
                  var21 = this.getBytes(3);
                  var4 = this.getU24();
               } else {
                  var21 = this.getBytes(4);
                  var4 = this.getFrameSize();
                  this.skipBytes(2);
               }

               if (0 == var21[0]) {
                  if (Logger.canLog(1)) {
                     Logger.logMsg(1, "ID3MetadataParser", "parse", "ID3 parser: zero padding detected at " + this.getStreamPosition() + ", terminating");
                  }
                  break;
               }

               String var6 = new String(var21, Charset.forName("ISO-8859-1"));
               if (Logger.canLog(1)) {
                  Logger.logMsg(1, "ID3MetadataParser", "parse", this.getStreamPosition() + "\\" + var3 + ": frame ID " + var6 + ", size " + var4);
               }

               byte[] var8;
               if (!var6.equals("APIC") && !var6.equals("PIC")) {
                  String var9;
                  int var11;
                  String var22;
                  if (var6.startsWith("T") && !var6.equals("TXXX")) {
                     var22 = this.getEncoding();
                     var8 = this.getBytes(var4 - 1);
                     if (this.unsynchronized) {
                        var8 = this.unsynchronizeBuffer(var8);
                     }

                     var9 = new String(var8, var22);
                     String[] var24 = this.getTagFromFrameID(var6);
                     if (var24 != null) {
                        for(var11 = 0; var11 < var24.length; ++var11) {
                           Object var26 = this.convertValue(var24[var11], var9);
                           if (var26 != null) {
                              this.addMetadataItem(var24[var11], var26);
                           }
                        }
                     }
                  } else {
                     String var12;
                     if (!var6.equals("COMM") && !var6.equals("COM")) {
                        if (!var6.equals("TXX") && !var6.equals("TXXX")) {
                           this.skipBytes(var4);
                        } else {
                           var22 = this.getEncoding();
                           var8 = this.getBytes(var4 - 1);
                           if (this.unsynchronized) {
                              var8 = this.unsynchronizeBuffer(var8);
                           }

                           var9 = new String(var8, var22);
                           if (null != var9) {
                              int var23 = var9.indexOf(0);
                              String var25 = var23 != 0 ? var9.substring(0, var23) : "";
                              var12 = this.isTwoByteEncoding(var22) ? var9.substring(var23 + 2) : var9.substring(var23 + 1);
                              String[] var28 = this.getTagFromFrameID(var6);
                              if (var28 != null) {
                                 for(int var27 = 0; var27 < var28.length; ++var27) {
                                    if (var25.equals("")) {
                                       this.addMetadataItem(var28[var27] + "-" + this.TXXXCount, var12);
                                    } else {
                                       this.addMetadataItem(var28[var27] + "-" + this.TXXXCount, var25 + "=" + var12);
                                    }

                                    ++this.TXXXCount;
                                 }
                              }
                           }
                        }
                     } else {
                        var22 = this.getEncoding();
                        var8 = this.getBytes(3);
                        if (this.unsynchronized) {
                           var8 = this.unsynchronizeBuffer(var8);
                        }

                        var9 = new String(var8, Charset.forName("ISO-8859-1"));
                        var8 = this.getBytes(var4 - 4);
                        if (this.unsynchronized) {
                           var8 = this.unsynchronizeBuffer(var8);
                        }

                        String var10 = new String(var8, var22);
                        if (var10 != null) {
                           var11 = var10.indexOf(0);
                           var12 = "";
                           String var13;
                           if (var11 == 0) {
                              if (this.isTwoByteEncoding(var22)) {
                                 var13 = var10.substring(2);
                              } else {
                                 var13 = var10.substring(1);
                              }
                           } else {
                              var12 = var10.substring(0, var11);
                              if (this.isTwoByteEncoding(var22)) {
                                 var13 = var10.substring(var11 + 2);
                              } else {
                                 var13 = var10.substring(var11 + 1);
                              }
                           }

                           String[] var14 = this.getTagFromFrameID(var6);
                           if (var14 != null) {
                              for(int var15 = 0; var15 < var14.length; ++var15) {
                                 this.addMetadataItem(var14[var15] + "-" + this.COMMCount, var12 + "[" + var9 + "]=" + var13);
                                 ++this.COMMCount;
                              }
                           }
                        }
                     }
                  }
               } else {
                  byte[] var7 = this.getBytes(var4);
                  if (this.unsynchronized) {
                     var7 = this.unsynchronizeBuffer(var7);
                  }

                  var8 = var6.equals("PIC") ? this.getImageFromPIC(var7) : this.getImageFromAPIC(var7);
                  if (var8 != null) {
                     this.addMetadataItem("image", var8);
                  }
               }
            }
         }
      } catch (Exception var19) {
         if (Logger.canLog(3)) {
            Logger.logMsg(3, "ID3MetadataParser", "parse", "Exception while processing ID3v2 metadata: " + var19);
         }
      } finally {
         if (null != this.rawMetaBlob) {
            this.setParseRawMetadata(false);
            this.addRawMetadata("ID3");
            this.disposeRawMetadata();
         }

         this.done();
      }

   }

   private int getFrameSize() throws IOException {
      if (this.version != 4) {
         return this.getInteger();
      } else {
         byte[] var1 = this.getBytes(4);
         int var2 = 0;
         int var3 = 0;

         for(int var4 = 21; var3 < 4; ++var3) {
            var2 += (var1[var3] & 127) << var4;
            var4 -= 7;
         }

         return var2;
      }
   }

   private String getEncoding() throws IOException {
      byte var1 = this.getNextByte();
      if (var1 == 0) {
         return "ISO-8859-1";
      } else if (var1 == 1) {
         return "UTF-16";
      } else if (var1 == 2) {
         return "UTF-16BE";
      } else if (var1 == 3) {
         return "UTF-8";
      } else {
         throw new IllegalArgumentException();
      }
   }

   private boolean isTwoByteEncoding(String var1) {
      if (!var1.equals("ISO-8859-1") && !var1.equals("UTF-8")) {
         if (!var1.equals("UTF-16") && !var1.equals("UTF-16BE")) {
            throw new IllegalArgumentException();
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private String[] getTagFromFrameID(String var1) {
      if (!var1.equals("TPE2") && !var1.equals("TP2")) {
         if (!var1.equals("TALB") && !var1.equals("TAL")) {
            if (!var1.equals("TPE1") && !var1.equals("TP1")) {
               if (!var1.equals("COMM") && !var1.equals("COM")) {
                  if (!var1.equals("TCOM") && !var1.equals("TCM")) {
                     if (!var1.equals("TLEN") && !var1.equals("TLE")) {
                        if (!var1.equals("TCON") && !var1.equals("TCO")) {
                           if (!var1.equals("TIT2") && !var1.equals("TT2")) {
                              if (!var1.equals("TRCK") && !var1.equals("TRK")) {
                                 if (!var1.equals("TPOS") && !var1.equals("TPA")) {
                                    if (!var1.equals("TYER") && !var1.equals("TDRC")) {
                                       return !var1.equals("TXX") && !var1.equals("TXXX") ? null : new String[]{"text"};
                                    } else {
                                       return new String[]{"year"};
                                    }
                                 } else {
                                    return new String[]{"disc number", "disc count"};
                                 }
                              } else {
                                 return new String[]{"track number", "track count"};
                              }
                           } else {
                              return new String[]{"title"};
                           }
                        } else {
                           return new String[]{"genre"};
                        }
                     } else {
                        return new String[]{"duration"};
                     }
                  } else {
                     return new String[]{"composer"};
                  }
               } else {
                  return new String[]{"comment"};
               }
            } else {
               return new String[]{"artist"};
            }
         } else {
            return new String[]{"album"};
         }
      } else {
         return new String[]{"album artist"};
      }
   }

   private byte[] getImageFromPIC(byte[] var1) {
      int var2;
      for(var2 = 5; 0 != var1[var2] && var2 < var1.length; ++var2) {
      }

      if (var2 == var1.length) {
         return null;
      } else {
         String var3 = new String(var1, 1, 3, Charset.forName("ISO-8859-1"));
         if (Logger.canLog(1)) {
            Logger.logMsg(1, "ID3MetadataParser", "getImageFromPIC", "PIC type: " + var3);
         }

         if (!var3.equalsIgnoreCase("PNG") && !var3.equalsIgnoreCase("JPG")) {
            if (Logger.canLog(3)) {
               Logger.logMsg(3, "ID3MetadataParser", "getImageFromPIC", "Unsupported picture type found \"" + var3 + "\"");
            }

            return null;
         } else {
            return Arrays.copyOfRange(var1, var2 + 1, var1.length);
         }
      }
   }

   private byte[] getImageFromAPIC(byte[] var1) {
      boolean var2 = false;
      boolean var3 = false;
      int var4 = var1.length - 10;
      int var5 = 0;

      for(int var6 = 0; var6 < var4; ++var6) {
         if (var1[var6] == 105 && var1[var6 + 1] == 109 && var1[var6 + 2] == 97 && var1[var6 + 3] == 103 && var1[var6 + 4] == 101 && var1[var6 + 5] == 47) {
            var6 += 6;
            if (var1[var6] == 106 && var1[var6 + 1] == 112 && var1[var6 + 2] == 101 && var1[var6 + 3] == 103) {
               var2 = true;
               var5 = var6 + 4;
               break;
            }

            if (var1[var6] == 112 && var1[var6 + 1] == 110 && var1[var6 + 2] == 103) {
               var3 = true;
               var5 = var6 + 3;
               break;
            }
         }
      }

      int var7;
      int var8;
      boolean var9;
      if (var2) {
         var9 = false;
         var7 = var1.length - 1;

         for(var8 = var5; var8 < var7; ++var8) {
            if (-1 == var1[var8] && -40 == var1[var8 + 1]) {
               var9 = true;
               var5 = var8;
               break;
            }
         }

         if (var9) {
            return Arrays.copyOfRange(var1, var5, var1.length);
         }
      }

      if (var3) {
         var9 = false;
         var7 = var1.length - 7;

         for(var8 = var5; var8 < var7; ++var8) {
            if (-119 == var1[var8] && 80 == var1[var8 + 1] && 78 == var1[var8 + 2] && 71 == var1[var8 + 3] && 13 == var1[var8 + 4] && 10 == var1[var8 + 5] && 26 == var1[var8 + 6] && 10 == var1[var8 + 7]) {
               var9 = true;
               var5 = var8;
               break;
            }
         }

         if (var9) {
            return Arrays.copyOfRange(var1, var5, var1.length);
         }
      }

      return null;
   }

   private byte[] unsynchronizeBuffer(byte[] var1) {
      byte[] var2 = new byte[var1.length];
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (((var1[var4] & 255) != 255 || var1[var4 + 1] != 0 || var1[var4 + 2] != 0) && ((var1[var4] & 255) != 255 || var1[var4 + 1] != 0 || (var1[var4 + 2] & 224) != 224)) {
            var2[var3] = var1[var4];
            ++var3;
         } else {
            var2[var3] = var1[var4];
            ++var3;
            var2[var3] = var1[var4 + 2];
            ++var3;
            var4 += 2;
         }
      }

      return Arrays.copyOf(var2, var3);
   }
}
