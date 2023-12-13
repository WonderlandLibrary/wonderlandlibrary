package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MetadataParserImpl;
import java.io.IOException;
import java.nio.charset.Charset;

final class FLVMetadataParser extends MetadataParserImpl {
   private int dataSize = 0;
   private static final String CHARSET_UTF_8 = "UTF-8";

   public FLVMetadataParser(Locator var1) {
      super(var1);
   }

   protected void parse() {
      try {
         if (this.getNextByte() == 70 && this.getNextByte() == 76 && this.getNextByte() == 86) {
            this.skipBytes(2);
            int var1 = this.getInteger();
            this.skipBytes(var1 - 9);
            boolean var2 = false;

            for(int var6 = 0; var6 < 10; ++var6) {
               this.skipBytes(4);
               byte var3 = this.getNextByte();
               this.dataSize = this.getU24();
               this.skipBytes(7);
               if (var3 == 18) {
                  int var4 = this.getStreamPosition() + this.dataSize;
                  if (this.parseDataTag()) {
                     break;
                  }

                  if (this.getStreamPosition() < var4) {
                     this.skipBytes(var4 - this.getStreamPosition());
                  }
               } else {
                  this.skipBytes(this.dataSize);
               }
            }
         }
      } catch (IOException var5) {
      }

   }

   private boolean parseDataTag() throws IOException {
      if (this.dataSize < 14) {
         return false;
      } else {
         byte[] var1 = new byte[14];

         int var2;
         for(var2 = 0; var2 < 14; ++var2) {
            var1[var2] = this.getNextByte();
         }

         if (var1[0] != 2) {
            return false;
         } else {
            var2 = (var1[1] & 255) << 8 | var1[2] & 255;
            if (var2 != 10) {
               return false;
            } else if (!Charset.isSupported("UTF-8")) {
               return false;
            } else {
               String var3 = new String(var1, 3, var2, Charset.forName("UTF-8"));
               if (!var3.equals("onMetaData")) {
                  return false;
               } else if (var1[13] != 8) {
                  if (Logger.canLog(3)) {
                     Logger.logMsg(3, "FLV metadata must be in an ECMA array");
                  }

                  return false;
               } else {
                  this.startRawMetadata(this.dataSize);
                  if (null == this.rawMetaBlob) {
                     if (Logger.canLog(1)) {
                        Logger.logMsg(1, "Unable to allocate buffer for FLV metadata");
                     }

                     return false;
                  } else {
                     this.stuffRawMetadata(var1, 0, 14);
                     this.readRawMetadata(this.dataSize - 14);
                     this.setParseRawMetadata(true);
                     this.skipBytes(14);

                     try {
                        int var5 = this.getInteger();
                        int var6 = 0;
                        boolean var7 = false;
                        boolean var8 = false;

                        do {
                           String var9 = this.getString(this.getShort(), Charset.forName("UTF-8"));
                           FlvDataValue var4 = this.readDataValue(false);
                           ++var6;
                           String var10 = this.convertTag(var9);
                           if (Logger.canLog(1) && !var9.equals("")) {
                              Logger.logMsg(1, var6 + ": \"" + var9 + "\" -> " + (null == var10 ? "(unsupported)" : "\"" + var10 + "\""));
                           }

                           if (var10 != null) {
                              Object var11 = this.convertValue(var9, var4.obj);
                              if (var11 != null) {
                                 this.addMetadataItem(var10, var11);
                              }
                           }

                           if (var6 >= var5) {
                              if (this.getStreamPosition() < this.dataSize) {
                                 if (!var8 && Logger.canLog(3)) {
                                    Logger.logMsg(3, "FLV Source has malformed metadata, invalid ECMA element count");
                                    var8 = true;
                                 }
                              } else {
                                 var7 = true;
                              }
                           }
                        } while(!var7);
                     } catch (Exception var15) {
                        if (Logger.canLog(3)) {
                           Logger.logMsg(3, "Exception while processing FLV metadata: " + var15);
                        }
                     } finally {
                        if (null != this.rawMetaBlob) {
                           this.setParseRawMetadata(false);
                           this.addRawMetadata("FLV");
                           this.disposeRawMetadata();
                        }

                        this.done();
                     }

                     return true;
                  }
               }
            }
         }
      }
   }

   private FlvDataValue readDataValue(boolean var1) throws IOException {
      FlvDataValue var2 = new FlvDataValue();
      if (var1) {
         this.skipBytes(this.getShort());
      }

      var2.type = this.getNextByte();
      switch (var2.type) {
         case 0:
            var2.obj = this.getDouble();
            break;
         case 1:
            boolean var3 = this.getNextByte() != 0;
            var2.obj = var3;
            break;
         case 2:
            var2.obj = this.getString(this.getShort(), Charset.forName("UTF-8"));
            break;
         case 3:
            this.skipObject();
            break;
         case 4:
            this.getString(this.getShort(), Charset.forName("UTF-8"));
         case 5:
         case 6:
         default:
            break;
         case 7:
            this.skipBytes(2);
            break;
         case 8:
            this.skipArray();
            break;
         case 9:
            var2.scriptDataObjectEnd = true;
            break;
         case 10:
            this.skipStrictArray();
            break;
         case 11:
            var2.obj = this.getDouble();
            this.skipBytes(2);
            break;
         case 12:
            var2.obj = this.getString(this.getInteger(), Charset.forName("UTF-8"));
      }

      return var2;
   }

   private void skipObject() throws IOException {
      FlvDataValue var1;
      do {
         var1 = this.readDataValue(true);
      } while(!var1.scriptDataObjectEnd);

   }

   private void skipArray() throws IOException {
      int var1 = this.getInteger();

      for(int var2 = 0; var2 < var1; ++var2) {
         this.readDataValue(true);
      }

   }

   private void skipStrictArray() throws IOException {
      long var1 = (long)this.getInteger();

      for(int var3 = 0; (long)var3 < var1; ++var3) {
         this.readDataValue(false);
      }

   }

   private String convertTag(String var1) {
      if (var1.equals("duration")) {
         return "duration";
      } else if (var1.equals("width")) {
         return "width";
      } else if (var1.equals("height")) {
         return "height";
      } else if (var1.equals("framerate")) {
         return "framerate";
      } else if (var1.equals("videocodecid")) {
         return "video codec";
      } else if (var1.equals("audiocodecid")) {
         return "audio codec";
      } else {
         return var1.equals("creationdate") ? "creationdate" : null;
      }
   }

   private static class FlvDataValue {
      static final byte NUMBER = 0;
      static final byte BOOLEAN = 1;
      static final byte STRING = 2;
      static final byte OBJECT = 3;
      static final byte MOVIE_CLIP = 4;
      static final byte NULL = 5;
      static final byte UNDEFINED = 6;
      static final byte REFERENCE = 7;
      static final byte ECMA_ARRAY = 8;
      static final byte END_OF_DATA = 9;
      static final byte STRICT_ARRAY = 10;
      static final byte DATE = 11;
      static final byte LONG_STRING = 12;
      boolean scriptDataObjectEnd;
      Object obj;
      byte type;

      private FlvDataValue() {
         this.scriptDataObjectEnd = false;
      }

      // $FF: synthetic method
      FlvDataValue(Object var1) {
         this();
      }
   }
}
