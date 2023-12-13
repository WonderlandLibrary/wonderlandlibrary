package com.sun.javafx.font;

import java.util.Arrays;
import java.util.zip.Inflater;

class WoffDecoder extends FontFileWriter {
   WoffHeader woffHeader;
   WoffDirectoryEntry[] woffTableDirectory;

   public WoffDecoder() {
   }

   public void decode(FontFileReader var1) throws Exception {
      var1.reset();
      this.initWoffTables(var1);
      if (this.woffHeader != null && this.woffTableDirectory != null) {
         int var2 = this.woffHeader.flavor;
         if (var2 != 65536 && var2 != 1953658213 && var2 != 1330926671) {
            throw new Exception("WoffDecoder: invalid flavor");
         } else {
            short var3 = this.woffHeader.numTables;
            this.setLength(this.woffHeader.totalSfntSize);
            this.writeHeader(var2, var3);
            Arrays.sort(this.woffTableDirectory, (var0, var1x) -> {
               return var0.offset - var1x.offset;
            });
            Inflater var4 = new Inflater();
            int var5 = 12 + var3 * 16;

            for(int var6 = 0; var6 < this.woffTableDirectory.length; ++var6) {
               WoffDirectoryEntry var7 = this.woffTableDirectory[var6];
               this.writeDirectoryEntry(var7.index, var7.tag, var7.origChecksum, var5, var7.origLength);
               FontFileReader.Buffer var8 = var1.readBlock(var7.offset, var7.comLength);
               byte[] var9 = new byte[var7.comLength];
               var8.get(0, var9, 0, var7.comLength);
               if (var7.comLength != var7.origLength) {
                  var4.setInput(var9);
                  byte[] var10 = new byte[var7.origLength];
                  int var11 = var4.inflate(var10);
                  if (var11 != var7.origLength) {
                     throw new Exception("WoffDecoder: failure expanding table");
                  }

                  var4.reset();
                  var9 = var10;
               }

               this.seek(var5);
               this.writeBytes(var9);
               var5 += var7.origLength + 3 & -4;
            }

            var4.end();
         }
      } else {
         throw new Exception("WoffDecoder: failure reading header");
      }
   }

   void initWoffTables(FontFileReader var1) throws Exception {
      long var2 = var1.getLength();
      if (var2 < 44L) {
         throw new Exception("WoffDecoder: invalid filesize");
      } else {
         FontFileReader.Buffer var4 = var1.readBlock(0, 44);
         WoffHeader var5 = new WoffHeader(var4);
         short var6 = var5.numTables;
         if (var5.signature != 2001684038) {
            throw new Exception("WoffDecoder: invalid signature");
         } else if (var5.reserved != 0) {
            throw new Exception("WoffDecoder: invalid reserved != 0");
         } else if (var2 < (long)(44 + var6 * 20)) {
            throw new Exception("WoffDecoder: invalid filesize");
         } else {
            WoffDirectoryEntry[] var8 = new WoffDirectoryEntry[var6];
            int var9 = 44 + var6 * 20;
            int var10 = 12 + var6 * 16;
            var4 = var1.readBlock(44, var6 * 20);
            byte var11 = 0;
            int var12 = 0;

            while(var12 < var6) {
               WoffDirectoryEntry var7;
               var8[var12] = var7 = new WoffDirectoryEntry(var4, var12);
               if (var7.tag <= var11) {
                  throw new Exception("WoffDecoder: table directory not ordered by tag");
               }

               int var13 = var7.offset;
               int var14 = var7.offset + var7.comLength;
               if (var9 <= var13 && (long)var13 <= var2) {
                  if (var13 <= var14 && (long)var14 <= var2) {
                     if (var7.comLength > var7.origLength) {
                        throw new Exception("WoffDecoder: invalid compressed length");
                     }

                     var10 += var7.origLength + 3 & -4;
                     if (var10 > var5.totalSfntSize) {
                        throw new Exception("WoffDecoder: invalid totalSfntSize");
                     }

                     ++var12;
                     continue;
                  }

                  throw new Exception("WoffDecoder: invalid table offset");
               }

               throw new Exception("WoffDecoder: invalid table offset");
            }

            if (var10 != var5.totalSfntSize) {
               throw new Exception("WoffDecoder: invalid totalSfntSize");
            } else {
               this.woffHeader = var5;
               this.woffTableDirectory = var8;
            }
         }
      }
   }

   static class WoffDirectoryEntry {
      int tag;
      int offset;
      int comLength;
      int origLength;
      int origChecksum;
      int index;

      WoffDirectoryEntry(FontFileReader.Buffer var1, int var2) {
         this.tag = var1.getInt();
         this.offset = var1.getInt();
         this.comLength = var1.getInt();
         this.origLength = var1.getInt();
         this.origChecksum = var1.getInt();
         this.index = var2;
      }
   }

   static class WoffHeader {
      int signature;
      int flavor;
      int length;
      short numTables;
      short reserved;
      int totalSfntSize;
      short majorVersion;
      short minorVersion;
      int metaOffset;
      int metaLength;
      int metaOrigLength;
      int privateOffset;
      int privateLength;

      WoffHeader(FontFileReader.Buffer var1) {
         this.signature = var1.getInt();
         this.flavor = var1.getInt();
         this.length = var1.getInt();
         this.numTables = var1.getShort();
         this.reserved = var1.getShort();
         this.totalSfntSize = var1.getInt();
         this.majorVersion = var1.getShort();
         this.minorVersion = var1.getShort();
         this.metaOffset = var1.getInt();
         this.metaLength = var1.getInt();
         this.metaOrigLength = var1.getInt();
         this.privateOffset = var1.getInt();
         this.privateLength = var1.getInt();
      }
   }
}
