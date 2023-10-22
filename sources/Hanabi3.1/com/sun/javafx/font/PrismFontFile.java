package com.sun.javafx.font;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PrismFontFile implements FontResource, FontConstants {
   private int fontInstallationType = -1;
   String familyName;
   String fullName;
   String psName;
   String localeFamilyName;
   String localeFullName;
   String styleName;
   String localeStyleName;
   String filename;
   int filesize;
   FontFileReader filereader;
   int numGlyphs = -1;
   short indexToLocFormat;
   int fontIndex;
   boolean isCFF;
   boolean isEmbedded = false;
   boolean isCopy = false;
   boolean isTracked = false;
   boolean isDecoded = false;
   boolean isRegistered = true;
   Map strikeMap = new ConcurrentHashMap();
   HashMap bbCache = null;
   static final int[] EMPTY_BOUNDS = new int[4];
   private Object peer;
   int directoryCount = 1;
   int numTables;
   DirectoryEntry[] tableDirectory;
   private static final int fsSelectionItalicBit = 1;
   private static final int fsSelectionBoldBit = 32;
   private static final int MACSTYLE_BOLD_BIT = 1;
   private static final int MACSTYLE_ITALIC_BIT = 2;
   private boolean isBold;
   private boolean isItalic;
   private float upem;
   private float ascent;
   private float descent;
   private float linegap;
   private int numHMetrics;
   public static final int MAC_PLATFORM_ID = 1;
   public static final int MACROMAN_SPECIFIC_ID = 0;
   public static final int MACROMAN_ENGLISH_LANG = 0;
   public static final int MS_PLATFORM_ID = 3;
   public static final short MS_ENGLISH_LOCALE_ID = 1033;
   public static final int FAMILY_NAME_ID = 1;
   public static final int STYLE_NAME_ID = 2;
   public static final int FULL_NAME_ID = 4;
   public static final int PS_NAME_ID = 6;
   private static Map lcidMap;
   static short nameLocaleID = getSystemLCID();
   private OpenTypeGlyphMapper mapper = null;
   char[] advanceWidths = null;
   private float[] styleMetrics;

   protected PrismFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      this.filename = var2;
      this.isRegistered = var4;
      this.isEmbedded = var5;
      this.isCopy = var6;
      this.isTracked = var7;
      this.init(var1, var3);
   }

   WeakReference createFileDisposer(PrismFontFactory var1) {
      FileDisposer var2 = new FileDisposer(this.filename, this.isTracked);
      WeakReference var3 = Disposer.addRecord(this, var2);
      var2.setFactory(var1, var3);
      return var3;
   }

   void setIsDecoded(boolean var1) {
      this.isDecoded = var1;
   }

   protected synchronized void disposeOnShutdown() {
      if (this.isCopy || this.isDecoded) {
         AccessController.doPrivileged(() -> {
            try {
               boolean var1 = (new File(this.filename)).delete();
               if (!var1 && PrismFontFactory.debugFonts) {
                  System.err.println("Temp file not deleted : " + this.filename);
               }

               this.isCopy = this.isDecoded = false;
            } catch (Exception var2) {
            }

            return null;
         });
         if (PrismFontFactory.debugFonts) {
            System.err.println("Temp file deleted: " + this.filename);
         }
      }

   }

   public int getDefaultAAMode() {
      return 0;
   }

   public boolean isInstalledFont() {
      if (this.fontInstallationType == -1) {
         PrismFontFactory var1 = PrismFontFactory.getFontFactory();
         this.fontInstallationType = var1.isInstalledFont(this.filename) ? 1 : 0;
      }

      return this.fontInstallationType > 0;
   }

   public String getFileName() {
      return this.filename;
   }

   protected int getFileSize() {
      return this.filesize;
   }

   protected int getFontIndex() {
      return this.fontIndex;
   }

   public String getFullName() {
      return this.fullName;
   }

   public String getPSName() {
      if (this.psName == null) {
         this.psName = this.fullName;
      }

      return this.psName;
   }

   public String getFamilyName() {
      return this.familyName;
   }

   public String getStyleName() {
      return this.styleName;
   }

   public String getLocaleFullName() {
      return this.localeFullName;
   }

   public String getLocaleFamilyName() {
      return this.localeFamilyName;
   }

   public String getLocaleStyleName() {
      return this.localeStyleName;
   }

   public int getFeatures() {
      return -1;
   }

   public Map getStrikeMap() {
      return this.strikeMap;
   }

   protected abstract PrismFontStrike createStrike(float var1, BaseTransform var2, int var3, FontStrikeDesc var4);

   public FontStrike getStrike(float var1, BaseTransform var2, int var3) {
      FontStrikeDesc var4 = new FontStrikeDesc(var1, var2, var3);
      WeakReference var5 = (WeakReference)this.strikeMap.get(var4);
      PrismFontStrike var6 = null;
      if (var5 != null) {
         var6 = (PrismFontStrike)var5.get();
      }

      if (var6 == null) {
         var6 = this.createStrike(var1, var2, var3, var4);
         DisposerRecord var7 = var6.getDisposer();
         if (var7 != null) {
            var5 = Disposer.addRecord(var6, var7);
         } else {
            var5 = new WeakReference(var6);
         }

         this.strikeMap.put(var4, var5);
      }

      return var6;
   }

   protected abstract int[] createGlyphBoundingBox(int var1);

   public float[] getGlyphBoundingBox(int var1, float var2, float[] var3) {
      if (var3 == null || var3.length < 4) {
         var3 = new float[4];
      }

      if (var1 >= this.getNumGlyphs()) {
         var3[0] = var3[1] = var3[2] = var3[3] = 0.0F;
         return var3;
      } else {
         if (this.bbCache == null) {
            this.bbCache = new HashMap();
         }

         int[] var4 = (int[])this.bbCache.get(var1);
         if (var4 == null) {
            var4 = this.createGlyphBoundingBox(var1);
            if (var4 == null) {
               var4 = EMPTY_BOUNDS;
            }

            this.bbCache.put(var1, var4);
         }

         float var5 = var2 / (float)this.getUnitsPerEm();
         var3[0] = (float)var4[0] * var5;
         var3[1] = (float)var4[1] * var5;
         var3[2] = (float)var4[2] * var5;
         var3[3] = (float)var4[3] * var5;
         return var3;
      }
   }

   int getNumGlyphs() {
      if (this.numGlyphs == -1) {
         FontFileReader.Buffer var1 = this.readTable(1835104368);
         this.numGlyphs = var1.getChar(4);
      }

      return this.numGlyphs;
   }

   protected boolean isCFF() {
      return this.isCFF;
   }

   public Object getPeer() {
      return this.peer;
   }

   public void setPeer(Object var1) {
      this.peer = var1;
   }

   int getTableLength(int var1) {
      int var2 = 0;
      DirectoryEntry var3 = this.getDirectoryEntry(var1);
      if (var3 != null) {
         var2 = var3.length;
      }

      return var2;
   }

   synchronized FontFileReader.Buffer readTable(int var1) {
      FontFileReader.Buffer var2 = null;
      boolean var3 = false;

      try {
         var3 = this.filereader.openFile();
         DirectoryEntry var4 = this.getDirectoryEntry(var1);
         if (var4 != null) {
            var2 = this.filereader.readBlock(var4.offset, var4.length);
         }
      } catch (Exception var13) {
         if (PrismFontFactory.debugFonts) {
            var13.printStackTrace();
         }
      } finally {
         if (var3) {
            try {
               this.filereader.closeFile();
            } catch (Exception var12) {
            }
         }

      }

      return var2;
   }

   public int getFontCount() {
      return this.directoryCount;
   }

   DirectoryEntry getDirectoryEntry(int var1) {
      for(int var2 = 0; var2 < this.numTables; ++var2) {
         if (this.tableDirectory[var2].tag == var1) {
            return this.tableDirectory[var2];
         }
      }

      return null;
   }

   private void init(String var1, int var2) throws Exception {
      this.filereader = new FontFileReader(this.filename);
      WoffDecoder var3 = null;

      try {
         if (!this.filereader.openFile()) {
            throw new FileNotFoundException("Unable to create FontResource for file " + this.filename);
         }

         FontFileReader.Buffer var4 = this.filereader.readBlock(0, 12);
         int var5 = var4.getInt();
         if (var5 == 2001684038) {
            var3 = new WoffDecoder();
            File var6 = var3.openFile();
            var3.decode(this.filereader);
            var3.closeFile();
            this.filereader.closeFile();
            this.filereader = new FontFileReader(var6.getPath());
            if (!this.filereader.openFile()) {
               throw new FileNotFoundException("Unable to create FontResource for file " + this.filename);
            }

            var4 = this.filereader.readBlock(0, 12);
            var5 = var4.getInt();
         }

         this.filesize = (int)this.filereader.getLength();
         int var19 = 0;
         if (var5 == 1953784678) {
            var4.getInt();
            this.directoryCount = var4.getInt();
            if (var2 >= this.directoryCount) {
               throw new Exception("Bad collection index");
            }

            this.fontIndex = var2;
            var4 = this.filereader.readBlock(12 + 4 * var2, 4);
            var19 = var4.getInt();
            var4 = this.filereader.readBlock(var19, 4);
            var5 = var4.getInt();
         }

         switch (var5) {
            case 1330926671:
               this.isCFF = true;
            case 65536:
            case 1953658213:
               var4 = this.filereader.readBlock(var19 + 4, 2);
               this.numTables = var4.getShort();
               int var7 = var19 + 12;
               FontFileReader.Buffer var8 = this.filereader.readBlock(var7, this.numTables * 16);
               this.tableDirectory = new DirectoryEntry[this.numTables];

               for(int var10 = 0; var10 < this.numTables; ++var10) {
                  DirectoryEntry var9;
                  this.tableDirectory[var10] = var9 = new DirectoryEntry();
                  var9.tag = var8.getInt();
                  var8.skip(4);
                  var9.offset = var8.getInt();
                  var9.length = var8.getInt();
                  if (var9.offset + var9.length > this.filesize) {
                     throw new Exception("bad table, tag=" + var9.tag);
                  }
               }

               DirectoryEntry var20 = this.getDirectoryEntry(1751474532);
               FontFileReader.Buffer var11 = this.filereader.readBlock(var20.offset, var20.length);
               this.upem = (float)(var11.getShort(18) & '\uffff');
               if (!(16.0F <= this.upem) || !(this.upem <= 16384.0F)) {
                  this.upem = 2048.0F;
               }

               this.indexToLocFormat = var11.getShort(50);
               if (this.indexToLocFormat >= 0 && this.indexToLocFormat <= 1) {
                  FontFileReader.Buffer var12 = this.readTable(1751672161);
                  if (var12 == null) {
                     this.numHMetrics = -1;
                  } else {
                     this.ascent = -((float)var12.getShort(4));
                     this.descent = -((float)var12.getShort(6));
                     this.linegap = (float)var12.getShort(8);
                     this.numHMetrics = var12.getChar(34) & '\uffff';
                     int var13 = this.getTableLength(1752003704) >> 2;
                     if (this.numHMetrics > var13) {
                        this.numHMetrics = var13;
                     }
                  }

                  this.getNumGlyphs();
                  this.setStyle();
                  this.checkCMAP();
                  this.initNames();
                  if (this.familyName != null && this.fullName != null) {
                     if (var3 != null) {
                        this.isDecoded = true;
                        this.filename = this.filereader.getFilename();
                        PrismFontFactory.getFontFactory().addDecodedFont(this);
                     }
                     break;
                  }

                  String var21 = var1 != null ? var1 : "";
                  if (this.fullName == null) {
                     this.fullName = this.familyName != null ? this.familyName : var21;
                  }

                  if (this.familyName == null) {
                     this.familyName = this.fullName != null ? this.fullName : var21;
                  }

                  throw new Exception("Font name not found.");
               }

               throw new Exception("Bad indexToLocFormat");
            default:
               throw new Exception("Unsupported sfnt " + this.filename);
         }
      } catch (Exception var17) {
         if (var3 != null) {
            var3.deleteFile();
         }

         throw var17;
      } finally {
         this.filereader.closeFile();
      }

   }

   private void setStyle() {
      DirectoryEntry var1 = this.getDirectoryEntry(1330851634);
      if (var1 != null) {
         FontFileReader.Buffer var2 = this.filereader.readBlock(var1.offset, var1.length);
         int var3 = var2.getChar(62) & '\uffff';
         this.isItalic = (var3 & 1) != 0;
         this.isBold = (var3 & 32) != 0;
      } else {
         DirectoryEntry var5 = this.getDirectoryEntry(1751474532);
         FontFileReader.Buffer var6 = this.filereader.readBlock(var5.offset, var5.length);
         short var4 = var6.getShort(44);
         this.isItalic = (var4 & 2) != 0;
         this.isBold = (var4 & 1) != 0;
      }

   }

   public boolean isBold() {
      return this.isBold;
   }

   public boolean isItalic() {
      return this.isItalic;
   }

   public boolean isDecoded() {
      return this.isDecoded;
   }

   public boolean isRegistered() {
      return this.isRegistered;
   }

   public boolean isEmbeddedFont() {
      return this.isEmbedded;
   }

   public int getUnitsPerEm() {
      return (int)this.upem;
   }

   public short getIndexToLocFormat() {
      return this.indexToLocFormat;
   }

   public int getNumHMetrics() {
      return this.numHMetrics;
   }

   void initNames() throws Exception {
      byte[] var1 = new byte[256];
      DirectoryEntry var2 = this.getDirectoryEntry(1851878757);
      FontFileReader.Buffer var3 = this.filereader.readBlock(var2.offset, var2.length);
      var3.skip(2);
      short var4 = var3.getShort();
      int var5 = var3.getShort() & '\uffff';

      for(int var6 = 0; var6 < var4; ++var6) {
         short var7 = var3.getShort();
         if (var7 != 3 && var7 != 1) {
            var3.skip(10);
         } else {
            short var8 = var3.getShort();
            if (var7 == 3 && var8 > 1 || var7 == 1 && var8 != 0) {
               var3.skip(8);
            } else {
               short var9 = var3.getShort();
               if (var7 == 1 && var9 != 0) {
                  var3.skip(6);
               } else {
                  short var10 = var3.getShort();
                  int var11 = var3.getShort() & '\uffff';
                  int var12 = (var3.getShort() & '\uffff') + var5;
                  String var13 = null;
                  String var14;
                  switch (var10) {
                     case 1:
                        if (this.familyName == null || var9 == 1033 || var9 == nameLocaleID) {
                           var3.get(var12, var1, 0, var11);
                           if (var7 == 1) {
                              var14 = "US-ASCII";
                           } else {
                              var14 = "UTF-16BE";
                           }

                           var13 = new String(var1, 0, var11, var14);
                           if (this.familyName == null || var9 == 1033) {
                              this.familyName = var13;
                           }

                           if (var9 == nameLocaleID) {
                              this.localeFamilyName = var13;
                           }
                        }
                        break;
                     case 2:
                        if (this.styleName == null || var9 == 1033 || var9 == nameLocaleID) {
                           var3.get(var12, var1, 0, var11);
                           if (var7 == 1) {
                              var14 = "US-ASCII";
                           } else {
                              var14 = "UTF-16BE";
                           }

                           var13 = new String(var1, 0, var11, var14);
                           if (this.styleName == null || var9 == 1033) {
                              this.styleName = var13;
                           }

                           if (var9 == nameLocaleID) {
                              this.localeStyleName = var13;
                           }
                        }
                     case 3:
                     case 5:
                     default:
                        break;
                     case 4:
                        if (this.fullName == null || var9 == 1033 || var9 == nameLocaleID) {
                           var3.get(var12, var1, 0, var11);
                           if (var7 == 1) {
                              var14 = "US-ASCII";
                           } else {
                              var14 = "UTF-16BE";
                           }

                           var13 = new String(var1, 0, var11, var14);
                           if (this.fullName == null || var9 == 1033) {
                              this.fullName = var13;
                           }

                           if (var9 == nameLocaleID) {
                              this.localeFullName = var13;
                           }
                        }
                        break;
                     case 6:
                        if (this.psName == null) {
                           var3.get(var12, var1, 0, var11);
                           if (var7 == 1) {
                              var14 = "US-ASCII";
                           } else {
                              var14 = "UTF-16BE";
                           }

                           this.psName = new String(var1, 0, var11, var14);
                        }
                  }

                  if (this.localeFamilyName == null) {
                     this.localeFamilyName = this.familyName;
                  }

                  if (this.localeFullName == null) {
                     this.localeFullName = this.fullName;
                  }

                  if (this.localeStyleName == null) {
                     this.localeStyleName = this.styleName;
                  }
               }
            }
         }
      }

   }

   private void checkCMAP() throws Exception {
      DirectoryEntry var1 = this.getDirectoryEntry(1668112752);
      if (var1 != null) {
         if (var1.length < 4) {
            throw new Exception("Invalid cmap table length");
         }

         FontFileReader.Buffer var2 = this.filereader.readBlock(var1.offset, 4);
         short var3 = var2.getShort();
         short var4 = var2.getShort();
         int var5 = var4 * 8;
         if (var4 <= 0 || var1.length < var5 + 4) {
            throw new Exception("Invalid cmap subtables count");
         }

         FontFileReader.Buffer var6 = this.filereader.readBlock(var1.offset + 4, var5);

         for(int var7 = 0; var7 < var4; ++var7) {
            short var8 = var6.getShort();
            short var9 = var6.getShort();
            int var10 = var6.getInt();
            if (var10 < 0 || var10 >= var1.length) {
               throw new Exception("Invalid cmap subtable offset");
            }
         }
      }

   }

   private static void addLCIDMapEntry(Map var0, String var1, short var2) {
      var0.put(var1, var2);
   }

   private static synchronized void createLCIDMap() {
      if (lcidMap == null) {
         HashMap var0 = new HashMap(200);
         addLCIDMapEntry(var0, "ar", (short)1025);
         addLCIDMapEntry(var0, "bg", (short)1026);
         addLCIDMapEntry(var0, "ca", (short)1027);
         addLCIDMapEntry(var0, "zh", (short)1028);
         addLCIDMapEntry(var0, "cs", (short)1029);
         addLCIDMapEntry(var0, "da", (short)1030);
         addLCIDMapEntry(var0, "de", (short)1031);
         addLCIDMapEntry(var0, "el", (short)1032);
         addLCIDMapEntry(var0, "es", (short)1034);
         addLCIDMapEntry(var0, "fi", (short)1035);
         addLCIDMapEntry(var0, "fr", (short)1036);
         addLCIDMapEntry(var0, "iw", (short)1037);
         addLCIDMapEntry(var0, "hu", (short)1038);
         addLCIDMapEntry(var0, "is", (short)1039);
         addLCIDMapEntry(var0, "it", (short)1040);
         addLCIDMapEntry(var0, "ja", (short)1041);
         addLCIDMapEntry(var0, "ko", (short)1042);
         addLCIDMapEntry(var0, "nl", (short)1043);
         addLCIDMapEntry(var0, "no", (short)1044);
         addLCIDMapEntry(var0, "pl", (short)1045);
         addLCIDMapEntry(var0, "pt", (short)1046);
         addLCIDMapEntry(var0, "rm", (short)1047);
         addLCIDMapEntry(var0, "ro", (short)1048);
         addLCIDMapEntry(var0, "ru", (short)1049);
         addLCIDMapEntry(var0, "hr", (short)1050);
         addLCIDMapEntry(var0, "sk", (short)1051);
         addLCIDMapEntry(var0, "sq", (short)1052);
         addLCIDMapEntry(var0, "sv", (short)1053);
         addLCIDMapEntry(var0, "th", (short)1054);
         addLCIDMapEntry(var0, "tr", (short)1055);
         addLCIDMapEntry(var0, "ur", (short)1056);
         addLCIDMapEntry(var0, "in", (short)1057);
         addLCIDMapEntry(var0, "uk", (short)1058);
         addLCIDMapEntry(var0, "be", (short)1059);
         addLCIDMapEntry(var0, "sl", (short)1060);
         addLCIDMapEntry(var0, "et", (short)1061);
         addLCIDMapEntry(var0, "lv", (short)1062);
         addLCIDMapEntry(var0, "lt", (short)1063);
         addLCIDMapEntry(var0, "fa", (short)1065);
         addLCIDMapEntry(var0, "vi", (short)1066);
         addLCIDMapEntry(var0, "hy", (short)1067);
         addLCIDMapEntry(var0, "eu", (short)1069);
         addLCIDMapEntry(var0, "mk", (short)1071);
         addLCIDMapEntry(var0, "tn", (short)1074);
         addLCIDMapEntry(var0, "xh", (short)1076);
         addLCIDMapEntry(var0, "zu", (short)1077);
         addLCIDMapEntry(var0, "af", (short)1078);
         addLCIDMapEntry(var0, "ka", (short)1079);
         addLCIDMapEntry(var0, "fo", (short)1080);
         addLCIDMapEntry(var0, "hi", (short)1081);
         addLCIDMapEntry(var0, "mt", (short)1082);
         addLCIDMapEntry(var0, "se", (short)1083);
         addLCIDMapEntry(var0, "gd", (short)1084);
         addLCIDMapEntry(var0, "ms", (short)1086);
         addLCIDMapEntry(var0, "kk", (short)1087);
         addLCIDMapEntry(var0, "ky", (short)1088);
         addLCIDMapEntry(var0, "sw", (short)1089);
         addLCIDMapEntry(var0, "tt", (short)1092);
         addLCIDMapEntry(var0, "bn", (short)1093);
         addLCIDMapEntry(var0, "pa", (short)1094);
         addLCIDMapEntry(var0, "gu", (short)1095);
         addLCIDMapEntry(var0, "ta", (short)1097);
         addLCIDMapEntry(var0, "te", (short)1098);
         addLCIDMapEntry(var0, "kn", (short)1099);
         addLCIDMapEntry(var0, "ml", (short)1100);
         addLCIDMapEntry(var0, "mr", (short)1102);
         addLCIDMapEntry(var0, "sa", (short)1103);
         addLCIDMapEntry(var0, "mn", (short)1104);
         addLCIDMapEntry(var0, "cy", (short)1106);
         addLCIDMapEntry(var0, "gl", (short)1110);
         addLCIDMapEntry(var0, "dv", (short)1125);
         addLCIDMapEntry(var0, "qu", (short)1131);
         addLCIDMapEntry(var0, "mi", (short)1153);
         addLCIDMapEntry(var0, "ar_IQ", (short)2049);
         addLCIDMapEntry(var0, "zh_CN", (short)2052);
         addLCIDMapEntry(var0, "de_CH", (short)2055);
         addLCIDMapEntry(var0, "en_GB", (short)2057);
         addLCIDMapEntry(var0, "es_MX", (short)2058);
         addLCIDMapEntry(var0, "fr_BE", (short)2060);
         addLCIDMapEntry(var0, "it_CH", (short)2064);
         addLCIDMapEntry(var0, "nl_BE", (short)2067);
         addLCIDMapEntry(var0, "no_NO_NY", (short)2068);
         addLCIDMapEntry(var0, "pt_PT", (short)2070);
         addLCIDMapEntry(var0, "ro_MD", (short)2072);
         addLCIDMapEntry(var0, "ru_MD", (short)2073);
         addLCIDMapEntry(var0, "sr_CS", (short)2074);
         addLCIDMapEntry(var0, "sv_FI", (short)2077);
         addLCIDMapEntry(var0, "az_AZ", (short)2092);
         addLCIDMapEntry(var0, "se_SE", (short)2107);
         addLCIDMapEntry(var0, "ga_IE", (short)2108);
         addLCIDMapEntry(var0, "ms_BN", (short)2110);
         addLCIDMapEntry(var0, "uz_UZ", (short)2115);
         addLCIDMapEntry(var0, "qu_EC", (short)2155);
         addLCIDMapEntry(var0, "ar_EG", (short)3073);
         addLCIDMapEntry(var0, "zh_HK", (short)3076);
         addLCIDMapEntry(var0, "de_AT", (short)3079);
         addLCIDMapEntry(var0, "en_AU", (short)3081);
         addLCIDMapEntry(var0, "fr_CA", (short)3084);
         addLCIDMapEntry(var0, "sr_CS", (short)3098);
         addLCIDMapEntry(var0, "se_FI", (short)3131);
         addLCIDMapEntry(var0, "qu_PE", (short)3179);
         addLCIDMapEntry(var0, "ar_LY", (short)4097);
         addLCIDMapEntry(var0, "zh_SG", (short)4100);
         addLCIDMapEntry(var0, "de_LU", (short)4103);
         addLCIDMapEntry(var0, "en_CA", (short)4105);
         addLCIDMapEntry(var0, "es_GT", (short)4106);
         addLCIDMapEntry(var0, "fr_CH", (short)4108);
         addLCIDMapEntry(var0, "hr_BA", (short)4122);
         addLCIDMapEntry(var0, "ar_DZ", (short)5121);
         addLCIDMapEntry(var0, "zh_MO", (short)5124);
         addLCIDMapEntry(var0, "de_LI", (short)5127);
         addLCIDMapEntry(var0, "en_NZ", (short)5129);
         addLCIDMapEntry(var0, "es_CR", (short)5130);
         addLCIDMapEntry(var0, "fr_LU", (short)5132);
         addLCIDMapEntry(var0, "bs_BA", (short)5146);
         addLCIDMapEntry(var0, "ar_MA", (short)6145);
         addLCIDMapEntry(var0, "en_IE", (short)6153);
         addLCIDMapEntry(var0, "es_PA", (short)6154);
         addLCIDMapEntry(var0, "fr_MC", (short)6156);
         addLCIDMapEntry(var0, "sr_BA", (short)6170);
         addLCIDMapEntry(var0, "ar_TN", (short)7169);
         addLCIDMapEntry(var0, "en_ZA", (short)7177);
         addLCIDMapEntry(var0, "es_DO", (short)7178);
         addLCIDMapEntry(var0, "sr_BA", (short)7194);
         addLCIDMapEntry(var0, "ar_OM", (short)8193);
         addLCIDMapEntry(var0, "en_JM", (short)8201);
         addLCIDMapEntry(var0, "es_VE", (short)8202);
         addLCIDMapEntry(var0, "ar_YE", (short)9217);
         addLCIDMapEntry(var0, "es_CO", (short)9226);
         addLCIDMapEntry(var0, "ar_SY", (short)10241);
         addLCIDMapEntry(var0, "en_BZ", (short)10249);
         addLCIDMapEntry(var0, "es_PE", (short)10250);
         addLCIDMapEntry(var0, "ar_JO", (short)11265);
         addLCIDMapEntry(var0, "en_TT", (short)11273);
         addLCIDMapEntry(var0, "es_AR", (short)11274);
         addLCIDMapEntry(var0, "ar_LB", (short)12289);
         addLCIDMapEntry(var0, "en_ZW", (short)12297);
         addLCIDMapEntry(var0, "es_EC", (short)12298);
         addLCIDMapEntry(var0, "ar_KW", (short)13313);
         addLCIDMapEntry(var0, "en_PH", (short)13321);
         addLCIDMapEntry(var0, "es_CL", (short)13322);
         addLCIDMapEntry(var0, "ar_AE", (short)14337);
         addLCIDMapEntry(var0, "es_UY", (short)14346);
         addLCIDMapEntry(var0, "ar_BH", (short)15361);
         addLCIDMapEntry(var0, "es_PY", (short)15370);
         addLCIDMapEntry(var0, "ar_QA", (short)16385);
         addLCIDMapEntry(var0, "es_BO", (short)16394);
         addLCIDMapEntry(var0, "es_SV", (short)17418);
         addLCIDMapEntry(var0, "es_HN", (short)18442);
         addLCIDMapEntry(var0, "es_NI", (short)19466);
         addLCIDMapEntry(var0, "es_PR", (short)20490);
         lcidMap = var0;
      }
   }

   private static short getLCIDFromLocale(Locale var0) {
      if (!var0.equals(Locale.US) && !var0.getLanguage().equals("en")) {
         if (lcidMap == null) {
            createLCIDMap();
         }

         int var3;
         for(String var1 = var0.toString(); !var1.isEmpty(); var1 = var1.substring(0, var3)) {
            Short var2 = (Short)lcidMap.get(var1);
            if (var2 != null) {
               return var2;
            }

            var3 = var1.lastIndexOf(95);
            if (var3 < 1) {
               return 1033;
            }
         }

         return 1033;
      } else {
         return 1033;
      }
   }

   private static short getSystemLCID() {
      return PrismFontFactory.isWindows ? PrismFontFactory.getSystemLCID() : getLCIDFromLocale(Locale.getDefault());
   }

   public CharToGlyphMapper getGlyphMapper() {
      if (this.mapper == null) {
         this.mapper = new OpenTypeGlyphMapper(this);
      }

      return this.mapper;
   }

   public FontStrike getStrike(float var1, BaseTransform var2) {
      return this.getStrike(var1, var2, this.getDefaultAAMode());
   }

   public float getAdvance(int var1, float var2) {
      if (var1 == 65535) {
         return 0.0F;
      } else {
         if (this.advanceWidths == null && this.numHMetrics > 0) {
            synchronized(this) {
               FontFileReader.Buffer var4 = this.readTable(1752003704);
               if (var4 == null) {
                  this.numHMetrics = -1;
                  return 0.0F;
               }

               char[] var5 = new char[this.numHMetrics];

               for(int var6 = 0; var6 < this.numHMetrics; ++var6) {
                  var5[var6] = var4.getChar(var6 * 4);
               }

               this.advanceWidths = var5;
            }
         }

         if (this.numHMetrics > 0) {
            char var3;
            if (var1 < this.numHMetrics) {
               var3 = this.advanceWidths[var1];
            } else {
               var3 = this.advanceWidths[this.numHMetrics - 1];
            }

            return (float)(var3 & '\uffff') * var2 / this.upem;
         } else {
            return 0.0F;
         }
      }
   }

   public PrismMetrics getFontMetrics(float var1) {
      return new PrismMetrics(this.ascent * var1 / this.upem, this.descent * var1 / this.upem, this.linegap * var1 / this.upem, this, var1);
   }

   float[] getStyleMetrics(float var1) {
      float[] var2;
      if (this.styleMetrics == null) {
         var2 = new float[9];
         FontFileReader.Buffer var3 = this.readTable(1330851634);
         int var4 = var3 != null ? var3.capacity() : 0;
         if (var4 >= 30) {
            var2[5] = (float)var3.getShort(26) / this.upem;
            var2[6] = (float)(-var3.getShort(28)) / this.upem;
         } else {
            var2[5] = 0.05F;
            var2[6] = -0.4F;
         }

         if (var4 >= 74) {
            var2[2] = (float)(-var3.getShort(68)) / this.upem;
            var2[3] = (float)(-var3.getShort(70)) / this.upem;
            var2[4] = (float)var3.getShort(72) / this.upem;
         } else {
            var2[2] = this.ascent / this.upem;
            var2[3] = this.descent / this.upem;
            var2[4] = this.linegap / this.upem;
         }

         if (var4 >= 90) {
            var2[0] = (float)var3.getShort(86) / this.upem;
            var2[1] = (float)var3.getShort(88);
            if ((double)(var2[1] / this.ascent) < 0.5) {
               var2[1] = 0.0F;
            } else {
               var2[1] /= this.upem;
            }
         }

         if (var2[0] == 0.0F || var2[1] == 0.0F) {
            FontStrike var5 = this.getStrike(var1, BaseTransform.IDENTITY_TRANSFORM);
            CharToGlyphMapper var6 = this.getGlyphMapper();
            int var7 = var6.getMissingGlyphCode();
            int var8;
            RectBounds var9;
            if (var2[0] == 0.0F) {
               var8 = var6.charToGlyph('x');
               if (var8 != var7) {
                  var9 = var5.getGlyph(var8).getBBox();
                  var2[0] = var9.getHeight() / var1;
               } else {
                  var2[0] = -this.ascent * 0.6F / this.upem;
               }
            }

            if (var2[1] == 0.0F) {
               var8 = var6.charToGlyph('H');
               if (var8 != var7) {
                  var9 = var5.getGlyph(var8).getBBox();
                  var2[1] = var9.getHeight() / var1;
               } else {
                  var2[1] = -this.ascent * 0.9F / this.upem;
               }
            }
         }

         FontFileReader.Buffer var11 = this.readTable(1886352244);
         if (var11 != null && var11.capacity() >= 12) {
            var2[8] = (float)(-var11.getShort(8)) / this.upem;
            var2[7] = (float)var11.getShort(10) / this.upem;
         } else {
            var2[8] = 0.1F;
            var2[7] = 0.05F;
         }

         this.styleMetrics = var2;
      }

      var2 = new float[9];

      for(int var10 = 0; var10 < 9; ++var10) {
         var2[var10] = this.styleMetrics[var10] * var1;
      }

      return var2;
   }

   byte[] getTableBytes(int var1) {
      FontFileReader.Buffer var2 = this.readTable(var1);
      byte[] var3 = null;
      if (var2 != null) {
         var3 = new byte[var2.capacity()];
         var2.get(0, var3, 0, var2.capacity());
      }

      return var3;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof PrismFontFile)) {
         return false;
      } else {
         PrismFontFile var2 = (PrismFontFile)var1;
         return this.filename.equals(var2.filename) && this.fullName.equals(var2.fullName);
      }
   }

   public int hashCode() {
      return this.filename.hashCode() + 71 * this.fullName.hashCode();
   }

   static class DirectoryEntry {
      int tag;
      int offset;
      int length;
   }

   static class FileDisposer implements DisposerRecord {
      String fileName;
      boolean isTracked;
      PrismFontFactory factory;
      WeakReference refKey;

      public FileDisposer(String var1, boolean var2) {
         this.fileName = var1;
         this.isTracked = var2;
      }

      public void setFactory(PrismFontFactory var1, WeakReference var2) {
         this.factory = var1;
         this.refKey = var2;
      }

      public synchronized void dispose() {
         if (this.fileName != null) {
            AccessController.doPrivileged(() -> {
               try {
                  File var1 = new File(this.fileName);
                  int var2 = (int)var1.length();
                  var1.delete();
                  if (this.isTracked) {
                     FontFileWriter.FontTracker.getTracker().subBytes(var2);
                  }

                  if (this.factory != null && this.refKey != null) {
                     Object var3 = this.refKey.get();
                     if (var3 == null) {
                        this.factory.removeTmpFont(this.refKey);
                        this.factory = null;
                        this.refKey = null;
                     }
                  }

                  if (PrismFontFactory.debugFonts) {
                     System.err.println("FileDisposer=" + this.fileName);
                  }
               } catch (Exception var4) {
                  if (PrismFontFactory.debugFonts) {
                     var4.printStackTrace();
                  }
               }

               return null;
            });
            this.fileName = null;
         }

      }
   }
}
