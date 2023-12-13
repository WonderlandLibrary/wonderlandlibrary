package com.sun.javafx.font;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.text.GlyphLayout;
import com.sun.prism.impl.PrismSettings;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public abstract class PrismFontFactory implements FontFactory {
   public static final boolean debugFonts;
   public static final boolean isWindows = PlatformUtil.isWindows();
   public static final boolean isLinux = PlatformUtil.isLinux();
   public static final boolean isMacOSX = PlatformUtil.isMac();
   public static final boolean isIOS = PlatformUtil.isIOS();
   public static final boolean isAndroid = PlatformUtil.isAndroid();
   public static final boolean isEmbedded = PlatformUtil.isEmbedded();
   public static final int cacheLayoutSize;
   private static int subPixelMode;
   public static final int SUB_PIXEL_ON = 1;
   public static final int SUB_PIXEL_Y = 2;
   public static final int SUB_PIXEL_NATIVE = 4;
   private static float fontSizeLimit = 80.0F;
   private static boolean lcdEnabled;
   private static float lcdContrast = -1.0F;
   private static String jreFontDir;
   private static final String jreDefaultFont = "Lucida Sans Regular";
   private static final String jreDefaultFontLC = "lucida sans regular";
   private static final String jreDefaultFontFile = "LucidaSansRegular.ttf";
   private static final String CT_FACTORY = "com.sun.javafx.font.coretext.CTFactory";
   private static final String DW_FACTORY = "com.sun.javafx.font.directwrite.DWFactory";
   private static final String FT_FACTORY = "com.sun.javafx.font.freetype.FTFactory";
   HashMap fontResourceMap = new HashMap();
   HashMap compResourceMap = new HashMap();
   private static PrismFontFactory theFontFactory;
   private HashMap fileNameToFontResourceMap = new HashMap();
   private ArrayList tmpFonts;
   private static final String[] STR_ARRAY;
   private volatile HashMap fontToFileMap = null;
   private HashMap fileToFontMap = null;
   private HashMap fontToFamilyNameMap = null;
   private HashMap familyToFontListMap = null;
   private static String sysFontDir;
   private static String userFontDir;
   private static ArrayList allFamilyNames;
   private static ArrayList allFontNames;
   private static Thread fileCloser;
   private HashMap embeddedFonts;
   private int numEmbeddedFonts = 0;
   private static float systemFontSize;
   private static String systemFontFamily;
   private static String monospaceFontFamily;

   private static String getNativeFactoryName() {
      if (isWindows) {
         return "com.sun.javafx.font.directwrite.DWFactory";
      } else if (!isMacOSX && !isIOS) {
         return !isLinux && !isAndroid ? null : "com.sun.javafx.font.freetype.FTFactory";
      } else {
         return "com.sun.javafx.font.coretext.CTFactory";
      }
   }

   public static float getFontSizeLimit() {
      return fontSizeLimit;
   }

   public static synchronized PrismFontFactory getFontFactory() {
      if (theFontFactory != null) {
         return theFontFactory;
      } else {
         String var0 = getNativeFactoryName();
         if (var0 == null) {
            throw new InternalError("cannot find a native font factory");
         } else {
            if (debugFonts) {
               System.err.println("Loading FontFactory " + var0);
               if (subPixelMode != 0) {
                  String var1 = "Subpixel: enabled";
                  if ((subPixelMode & 2) != 0) {
                     var1 = var1 + ", vertical";
                  }

                  if ((subPixelMode & 4) != 0) {
                     var1 = var1 + ", native";
                  }

                  System.err.println(var1);
               }
            }

            theFontFactory = getFontFactory(var0);
            if (theFontFactory == null) {
               throw new InternalError("cannot load font factory: " + var0);
            } else {
               return theFontFactory;
            }
         }
      }
   }

   private static synchronized PrismFontFactory getFontFactory(String var0) {
      try {
         Class var1 = Class.forName(var0);
         Method var2 = var1.getMethod("getFactory", (Class[])null);
         return (PrismFontFactory)var2.invoke((Object)null);
      } catch (Throwable var3) {
         if (debugFonts) {
            System.err.println("Loading font factory failed " + var0);
         }

         return null;
      }
   }

   protected abstract PrismFontFile createFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception;

   public abstract GlyphLayout createGlyphLayout();

   private PrismFontFile createFontResource(String var1, int var2) {
      return this.createFontResource(var1, var2, true, false, false, false);
   }

   private PrismFontFile createFontResource(String var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      String var7 = (var1 + var2).toLowerCase();
      PrismFontFile var8 = (PrismFontFile)this.fileNameToFontResourceMap.get(var7);
      if (var8 != null) {
         return var8;
      } else {
         try {
            var8 = this.createFontFile((String)null, var1, var2, var3, var4, var5, var6);
            if (var3) {
               this.storeInMap(var8.getFullName(), var8);
               this.fileNameToFontResourceMap.put(var7, var8);
            }

            return var8;
         } catch (Exception var10) {
            if (debugFonts) {
               var10.printStackTrace();
            }

            return null;
         }
      }
   }

   private PrismFontFile createFontResource(String var1, String var2) {
      return this.createFontResource(var1, var2, true, false, false, false);
   }

   private PrismFontFile createFontResource(String var1, String var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      if (var2 == null) {
         return null;
      } else {
         String var7 = var2.toLowerCase();
         if (!var7.endsWith(".ttc")) {
            return this.createFontResource(var2, 0, var3, var4, var5, var6);
         } else {
            int var8 = 0;
            PrismFontFile var10 = null;

            PrismFontFile var9;
            do {
               String var11 = (var2 + var8).toLowerCase();

               label59: {
                  try {
                     var9 = (PrismFontFile)this.fileNameToFontResourceMap.get(var11);
                     if (var9 != null) {
                        if (var1.equals(var9.getFullName())) {
                           return var9;
                        }
                        break label59;
                     }

                     var9 = this.createFontFile(var1, var2, var8, var3, var4, var5, var6);
                  } catch (Exception var13) {
                     if (debugFonts) {
                        var13.printStackTrace();
                     }

                     return null;
                  }

                  String var12 = var9.getFullName();
                  if (var3) {
                     this.storeInMap(var12, var9);
                     this.fileNameToFontResourceMap.put(var11, var9);
                  }

                  if (var8 == 0 || var1.equals(var12)) {
                     var10 = var9;
                  }
               }

               ++var8;
            } while(var8 < var9.getFontCount());

            return var10;
         }
      }
   }

   private String dotStyleStr(boolean var1, boolean var2) {
      if (!var1) {
         return !var2 ? "" : ".italic";
      } else {
         return !var2 ? ".bold" : ".bolditalic";
      }
   }

   private void storeInMap(String var1, FontResource var2) {
      if (var1 != null && var2 != null) {
         if (var2 instanceof PrismCompositeFontResource) {
            System.err.println(var1 + " is a composite " + var2);
            Thread.dumpStack();
         } else {
            this.fontResourceMap.put(var1.toLowerCase(), var2);
         }
      }
   }

   synchronized void addDecodedFont(PrismFontFile var1) {
      var1.setIsDecoded(true);
      this.addTmpFont(var1);
   }

   private synchronized void addTmpFont(PrismFontFile var1) {
      if (this.tmpFonts == null) {
         this.tmpFonts = new ArrayList();
      }

      WeakReference var2;
      if (var1.isRegistered()) {
         var2 = new WeakReference(var1);
      } else {
         var2 = var1.createFileDisposer(this);
      }

      this.tmpFonts.add(var2);
      this.addFileCloserHook();
   }

   synchronized void removeTmpFont(WeakReference var1) {
      if (this.tmpFonts != null) {
         this.tmpFonts.remove(var1);
      }

   }

   public synchronized FontResource getFontResource(String var1, boolean var2, boolean var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         String var5 = var1.toLowerCase();
         String var6 = this.dotStyleStr(var2, var3);
         FontResource var7 = this.lookupResource(var5 + var6, var4);
         if (var7 != null) {
            return var7;
         } else {
            if (this.embeddedFonts != null && var4) {
               var7 = this.lookupResource(var5 + var6, false);
               if (var7 != null) {
                  return new PrismCompositeFontResource(var7, var5 + var6);
               }

               Iterator var8 = this.embeddedFonts.values().iterator();

               while(var8.hasNext()) {
                  PrismFontFile var9 = (PrismFontFile)var8.next();
                  String var10 = var9.getFamilyName().toLowerCase();
                  if (var10.equals(var5)) {
                     return new PrismCompositeFontResource(var9, var5 + var6);
                  }
               }
            }

            Object var17;
            if (isWindows) {
               int var18 = (var2 ? 1 : 0) + (var3 ? 2 : 0);
               String var20 = WindowsFontMap.findFontFile(var5, var18);
               if (var20 != null) {
                  var17 = this.createFontResource((String)null, var20);
                  if (var17 != null) {
                     if (var2 == ((FontResource)var17).isBold() && var3 == ((FontResource)var17).isItalic() && !var6.isEmpty()) {
                        this.storeInMap(var5 + var6, (FontResource)var17);
                     }

                     if (var4) {
                        var17 = new PrismCompositeFontResource((FontResource)var17, var5 + var6);
                     }

                     return (FontResource)var17;
                  }
               }
            }

            this.getFullNameToFileMap();
            ArrayList var19 = (ArrayList)this.familyToFontListMap.get(var5);
            if (var19 == null) {
               return null;
            } else {
               Object var21 = null;
               Object var22 = null;
               Object var11 = null;
               Object var12 = null;
               Iterator var13 = var19.iterator();

               while(true) {
                  while(true) {
                     if (!var13.hasNext()) {
                        if (!var2 && !var3) {
                           if (var22 != null) {
                              var17 = var22;
                           } else if (var11 != null) {
                              var17 = var11;
                           } else {
                              var17 = var12;
                           }
                        } else if (var2 && !var3) {
                           if (var21 != null) {
                              var17 = var21;
                           } else if (var12 != null) {
                              var17 = var12;
                           } else {
                              var17 = var11;
                           }
                        } else if (!var2 && var3) {
                           if (var12 != null) {
                              var17 = var12;
                           } else if (var21 != null) {
                              var17 = var21;
                           } else {
                              var17 = var22;
                           }
                        } else if (var11 != null) {
                           var17 = var11;
                        } else if (var22 != null) {
                           var17 = var22;
                        } else {
                           var17 = var21;
                        }

                        if (var17 != null) {
                           this.storeInMap(var5 + var6, (FontResource)var17);
                           if (var4) {
                              var17 = new PrismCompositeFontResource((FontResource)var17, var5 + var6);
                           }
                        }

                        return (FontResource)var17;
                     }

                     String var14 = (String)var13.next();
                     String var15 = var14.toLowerCase();
                     var17 = (FontResource)this.fontResourceMap.get(var15);
                     if (var17 != null) {
                        break;
                     }

                     String var16 = this.findFile(var15);
                     if (var16 != null) {
                        var17 = this.getFontResource(var14, var16);
                     }

                     if (var17 != null) {
                        this.storeInMap(var15, (FontResource)var17);
                        break;
                     }
                  }

                  if (var2 == ((FontResource)var17).isBold() && var3 == ((FontResource)var17).isItalic()) {
                     this.storeInMap(var5 + var6, (FontResource)var17);
                     if (var4) {
                        var17 = new PrismCompositeFontResource((FontResource)var17, var5 + var6);
                     }

                     return (FontResource)var17;
                  }

                  if (!((FontResource)var17).isBold()) {
                     if (!((FontResource)var17).isItalic()) {
                        var21 = var17;
                     } else {
                        var11 = var17;
                     }
                  } else if (!((FontResource)var17).isItalic()) {
                     var22 = var17;
                  } else {
                     var12 = var17;
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public synchronized PGFont createFont(String var1, boolean var2, boolean var3, float var4) {
      FontResource var5 = null;
      if (var1 != null && !var1.isEmpty()) {
         PGFont var6 = LogicalFont.getLogicalFont(var1, var2, var3, var4);
         if (var6 != null) {
            return var6;
         }

         var5 = this.getFontResource(var1, var2, var3, true);
      }

      return (PGFont)(var5 == null ? LogicalFont.getLogicalFont("System", var2, var3, var4) : new PrismFont(var5, var5.getFullName(), var4));
   }

   public synchronized PGFont createFont(String var1, float var2) {
      FontResource var3 = null;
      if (var1 != null && !var1.isEmpty()) {
         PGFont var4 = LogicalFont.getLogicalFont(var1, var2);
         if (var4 != null) {
            return var4;
         }

         var3 = this.getFontResource(var1, (String)null, true);
      }

      return (PGFont)(var3 == null ? LogicalFont.getLogicalFont("System Regular", var2) : new PrismFont(var3, var3.getFullName(), var2));
   }

   private PrismFontFile getFontResource(String var1, String var2) {
      PrismFontFile var3 = null;
      if (isMacOSX) {
         DFontDecoder var4 = null;
         if (var1 != null && var2.endsWith(".dfont")) {
            var4 = new DFontDecoder();

            try {
               var4.openFile();
               var4.decode(var1);
               var4.closeFile();
               var2 = var4.getFile().getPath();
            } catch (Exception var6) {
               var2 = null;
               var4.deleteFile();
               var4 = null;
               if (debugFonts) {
                  var6.printStackTrace();
               }
            }
         }

         if (var2 != null) {
            var3 = this.createFontResource(var1, var2);
         }

         if (var4 != null) {
            if (var3 != null) {
               this.addDecodedFont(var3);
            } else {
               var4.deleteFile();
            }
         }
      } else {
         var3 = this.createFontResource(var1, var2);
      }

      return var3;
   }

   public synchronized PGFont deriveFont(PGFont var1, boolean var2, boolean var3, float var4) {
      FontResource var5 = var1.getFontResource();
      return new PrismFont(var5, var5.getFullName(), var4);
   }

   private FontResource lookupResource(String var1, boolean var2) {
      return var2 ? (FontResource)this.compResourceMap.get(var1) : (FontResource)this.fontResourceMap.get(var1);
   }

   public synchronized FontResource getFontResource(String var1, String var2, boolean var3) {
      Object var4 = null;
      String var5;
      if (var1 != null) {
         var5 = var1.toLowerCase();
         FontResource var6 = this.lookupResource(var5, var3);
         if (var6 != null) {
            return var6;
         }

         if (this.embeddedFonts != null && var3) {
            var4 = this.lookupResource(var5, false);
            if (var4 != null) {
               var4 = new PrismCompositeFontResource((FontResource)var4, var5);
            }

            if (var4 != null) {
               return (FontResource)var4;
            }
         }
      }

      if (isWindows && var1 != null) {
         var5 = var1.toLowerCase();
         String var8 = WindowsFontMap.findFontFile(var5, -1);
         if (var8 != null) {
            var4 = this.createFontResource((String)null, var8);
            if (var4 != null) {
               if (var3) {
                  var4 = new PrismCompositeFontResource((FontResource)var4, var5);
               }

               return (FontResource)var4;
            }
         }
      }

      this.getFullNameToFileMap();
      if (var1 != null && var2 != null) {
         var4 = this.getFontResource(var1, var2);
         if (var4 != null) {
            if (var3) {
               var4 = new PrismCompositeFontResource((FontResource)var4, var1.toLowerCase());
            }

            return (FontResource)var4;
         }
      }

      FontResource var7;
      if (var1 != null) {
         var7 = this.getFontResourceByFullName(var1, var3);
         if (var7 != null) {
            return var7;
         }
      }

      if (var2 != null) {
         var7 = this.getFontResourceByFileName(var2, var3);
         if (var7 != null) {
            return var7;
         }
      }

      return null;
   }

   boolean isInstalledFont(String var1) {
      String var2;
      File var3;
      if (isWindows) {
         if (var1.toLowerCase().contains("\\windows\\fonts")) {
            return true;
         }

         var3 = new File(var1);
         var2 = var3.getName();
      } else {
         if (isMacOSX && var1.toLowerCase().contains("/library/fonts")) {
            return true;
         }

         var3 = new File(var1);
         var2 = var3.getPath();
      }

      this.getFullNameToFileMap();
      return this.fileToFontMap.get(var2.toLowerCase()) != null;
   }

   private synchronized FontResource getFontResourceByFileName(String var1, boolean var2) {
      if (this.fontToFileMap.size() <= 1) {
         return null;
      } else {
         String var3 = (String)this.fileToFontMap.get(var1.toLowerCase());
         Object var4 = null;
         String var5;
         if (var3 == null) {
            var4 = this.createFontResource(var1, 0);
            if (var4 != null) {
               var5 = ((FontResource)var4).getFullName().toLowerCase();
               this.storeInMap(var5, (FontResource)var4);
               if (var2) {
                  var4 = new PrismCompositeFontResource((FontResource)var4, var5);
               }
            }
         } else {
            var5 = var3.toLowerCase();
            var4 = this.lookupResource(var5, var2);
            if (var4 == null) {
               String var6 = this.findFile(var5);
               if (var6 != null) {
                  var4 = this.getFontResource(var3, var6);
                  if (var4 != null) {
                     this.storeInMap(var5, (FontResource)var4);
                  }

                  if (var2) {
                     var4 = new PrismCompositeFontResource((FontResource)var4, var5);
                  }
               }
            }
         }

         return (FontResource)var4;
      }
   }

   private synchronized FontResource getFontResourceByFullName(String var1, boolean var2) {
      String var3 = var1.toLowerCase();
      if (this.fontToFileMap.size() <= 1) {
         var1 = "Lucida Sans Regular";
      }

      Object var4 = null;
      String var5 = this.findFile(var3);
      if (var5 != null) {
         var4 = this.getFontResource(var1, var5);
         if (var4 != null) {
            this.storeInMap(var3, (FontResource)var4);
            if (var2) {
               var4 = new PrismCompositeFontResource((FontResource)var4, var3);
            }
         }
      }

      return (FontResource)var4;
   }

   FontResource getDefaultFontResource(boolean var1) {
      Object var2 = this.lookupResource("lucida sans regular", var1);
      if (var2 == null) {
         var2 = this.createFontResource("Lucida Sans Regular", jreFontDir + "LucidaSansRegular.ttf");
         if (var2 == null) {
            Iterator var3 = this.fontToFileMap.keySet().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               String var5 = this.findFile(var4);
               var2 = this.createFontResource("lucida sans regular", var5);
               if (var2 != null) {
                  break;
               }
            }

            if (var2 == null && isLinux) {
               String var6 = FontConfigManager.getDefaultFontPath();
               if (var6 != null) {
                  var2 = this.createFontResource("lucida sans regular", var6);
               }
            }

            if (var2 == null) {
               return null;
            }
         }

         this.storeInMap("lucida sans regular", (FontResource)var2);
         if (var1) {
            var2 = new PrismCompositeFontResource((FontResource)var2, "lucida sans regular");
         }
      }

      return (FontResource)var2;
   }

   private String findFile(String var1) {
      if (var1.equals("lucida sans regular")) {
         return jreFontDir + "LucidaSansRegular.ttf";
      } else {
         this.getFullNameToFileMap();
         String var2 = (String)this.fontToFileMap.get(var1);
         if (isWindows) {
            var2 = getPathNameWindows(var2);
         }

         return var2;
      }
   }

   private static native byte[] getFontPath();

   private static native String regReadFontLink(String var0);

   private static native String getEUDCFontFile();

   private static void getPlatformFontDirs() {
      if (userFontDir == null && sysFontDir == null) {
         byte[] var0 = getFontPath();
         String var1 = new String(var0);
         int var2 = var1.indexOf(59);
         if (var2 < 0) {
            sysFontDir = var1;
         } else {
            sysFontDir = var1.substring(0, var2);
            userFontDir = var1.substring(var2 + 1, var1.length());
         }

      }
   }

   static ArrayList[] getLinkedFonts(String var0, boolean var1) {
      ArrayList[] var2 = new ArrayList[]{new ArrayList(), new ArrayList()};
      if (isMacOSX) {
         var2[0].add("/Library/Fonts/Arial Unicode.ttf");
         var2[1].add("Arial Unicode MS");
         var2[0].add(jreFontDir + "LucidaSansRegular.ttf");
         var2[1].add("Lucida Sans Regular");
         var2[0].add("/System/Library/Fonts/Apple Symbols.ttf");
         var2[1].add("Apple Symbols");
         var2[0].add("/System/Library/Fonts/STHeiti Light.ttf");
         var2[1].add("Heiti SC Light");
         return var2;
      } else if (!isWindows) {
         return var2;
      } else {
         if (var1) {
            var2[0].add((Object)null);
            var2[1].add(var0);
         }

         String var3 = regReadFontLink(var0);
         if (var3 != null && var3.length() > 0) {
            String[] var4 = var3.split("\u0000");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String[] var7 = var4[var6].split(",");
               int var8 = var7.length;
               String var9 = getPathNameWindows(var7[0]);
               String var10 = var8 > 1 ? var7[1] : null;
               if ((var10 == null || !var2[1].contains(var10)) && (var10 != null || !var2[0].contains(var9))) {
                  var2[0].add(var9);
                  var2[1].add(var10);
               }
            }
         }

         String var11 = getEUDCFontFile();
         if (var11 != null) {
            var2[0].add(var11);
            var2[1].add((Object)null);
         }

         var2[0].add(jreFontDir + "LucidaSansRegular.ttf");
         var2[1].add("Lucida Sans Regular");
         if (PlatformUtil.isWinVistaOrLater()) {
            var2[0].add(getPathNameWindows("mingliub.ttc"));
            var2[1].add("MingLiU-ExtB");
            if (PlatformUtil.isWin7OrLater()) {
               var2[0].add(getPathNameWindows("seguisym.ttf"));
               var2[1].add("Segoe UI Symbol");
            } else {
               var2[0].add(getPathNameWindows("cambria.ttc"));
               var2[1].add("Cambria Math");
            }
         }

         return var2;
      }
   }

   private void resolveWindowsFonts(HashMap var1, HashMap var2, HashMap var3) {
      ArrayList var4 = null;
      Iterator var5 = var2.keySet().iterator();

      int var8;
      String var9;
      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         String var7 = (String)var1.get(var6);
         if (var7 == null) {
            var8 = var6.indexOf("  ");
            if (var8 > 0) {
               var9 = var6.substring(0, var8);
               var9 = var9.concat(var6.substring(var8 + 1));
               var7 = (String)var1.get(var9);
               if (var7 != null && !var2.containsKey(var9)) {
                  var1.remove(var9);
                  var1.put(var6, var7);
               }
            } else if (var6.equals("marlett")) {
               var1.put(var6, "marlett.ttf");
            } else if (var6.equals("david")) {
               var7 = (String)var1.get("david regular");
               if (var7 != null) {
                  var1.remove("david regular");
                  var1.put("david", var7);
               }
            } else {
               if (var4 == null) {
                  var4 = new ArrayList();
               }

               var4.add(var6);
            }
         }
      }

      if (var4 != null) {
         HashSet var12 = new HashSet();
         HashMap var13 = new HashMap();
         var13.putAll(var1);
         Iterator var14 = var2.keySet().iterator();

         String var15;
         while(var14.hasNext()) {
            var15 = (String)var14.next();
            var13.remove(var15);
         }

         var14 = var13.keySet().iterator();

         while(var14.hasNext()) {
            var15 = (String)var14.next();
            var12.add(var13.get(var15));
            var1.remove(var15);
         }

         this.resolveFontFiles(var12, var4, var1, var2, var3);
         if (var4.size() > 0) {
            int var16 = var4.size();

            for(var8 = 0; var8 < var16; ++var8) {
               var9 = (String)var4.get(var8);
               String var10 = (String)var2.get(var9);
               if (var10 != null) {
                  ArrayList var11 = (ArrayList)var3.get(var10);
                  if (var11 != null && var11.size() <= 1) {
                     var3.remove(var10);
                  }
               }

               var2.remove(var9);
            }
         }
      }

   }

   private void resolveFontFiles(HashSet var1, ArrayList var2, HashMap var3, HashMap var4, HashMap var5) {
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();

         try {
            int var8 = 0;
            String var10 = getPathNameWindows(var7);

            while(true) {
               PrismFontFile var9 = this.createFontResource(var10, var8++);
               if (var9 == null) {
                  break;
               }

               String var11 = var9.getFullName().toLowerCase();
               String var12 = var9.getLocaleFullName().toLowerCase();
               if (var2.contains(var11) || var2.contains(var12)) {
                  var3.put(var11, var7);
                  var2.remove(var11);
                  if (var2.contains(var12)) {
                     var2.remove(var12);
                     String var13 = var9.getFamilyName();
                     String var14 = var13.toLowerCase();
                     var4.remove(var12);
                     var4.put(var11, var13);
                     ArrayList var15 = (ArrayList)var5.get(var14);
                     if (var15 != null) {
                        var15.remove(var9.getLocaleFullName());
                     } else {
                        String var16 = var9.getLocaleFamilyName().toLowerCase();
                        var15 = (ArrayList)var5.get(var16);
                        if (var15 != null) {
                           var5.remove(var16);
                        }

                        var15 = new ArrayList();
                        var5.put(var14, var15);
                     }

                     var15.add(var9.getFullName());
                  }
               }

               if (var8 >= var9.getFontCount()) {
                  break;
               }
            }
         } catch (Exception var17) {
            if (debugFonts) {
               var17.printStackTrace();
            }
         }
      }

   }

   static native void populateFontFileNameMap(HashMap var0, HashMap var1, HashMap var2, Locale var3);

   static String getPathNameWindows(final String var0) {
      if (var0 == null) {
         return null;
      } else {
         getPlatformFontDirs();
         File var1 = new File(var0);
         if (var1.isAbsolute()) {
            return var0;
         } else if (userFontDir == null) {
            return sysFontDir + "\\" + var0;
         } else {
            String var2 = (String)AccessController.doPrivileged(new PrivilegedAction() {
               public String run() {
                  File var1 = new File(PrismFontFactory.sysFontDir + "\\" + var0);
                  return var1.exists() ? var1.getAbsolutePath() : PrismFontFactory.userFontDir + "\\" + var0;
               }
            });
            return var2 != null ? var2 : null;
         }
      }
   }

   public String[] getFontFamilyNames() {
      if (allFamilyNames == null) {
         ArrayList var1 = new ArrayList();
         LogicalFont.addFamilies(var1);
         Iterator var2;
         if (this.embeddedFonts != null) {
            var2 = this.embeddedFonts.values().iterator();

            while(var2.hasNext()) {
               PrismFontFile var3 = (PrismFontFile)var2.next();
               if (!var1.contains(var3.getFamilyName())) {
                  var1.add(var3.getFamilyName());
               }
            }
         }

         this.getFullNameToFileMap();
         var2 = this.fontToFamilyNameMap.values().iterator();

         while(var2.hasNext()) {
            String var4 = (String)var2.next();
            if (!var1.contains(var4)) {
               var1.add(var4);
            }
         }

         Collections.sort(var1);
         allFamilyNames = new ArrayList(var1);
      }

      return (String[])allFamilyNames.toArray(STR_ARRAY);
   }

   public String[] getFontFullNames() {
      if (allFontNames == null) {
         ArrayList var1 = new ArrayList();
         LogicalFont.addFullNames(var1);
         Iterator var2;
         if (this.embeddedFonts != null) {
            var2 = this.embeddedFonts.values().iterator();

            while(var2.hasNext()) {
               PrismFontFile var3 = (PrismFontFile)var2.next();
               if (!var1.contains(var3.getFullName())) {
                  var1.add(var3.getFullName());
               }
            }
         }

         this.getFullNameToFileMap();
         var2 = this.familyToFontListMap.values().iterator();

         while(var2.hasNext()) {
            ArrayList var6 = (ArrayList)var2.next();
            Iterator var4 = var6.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               var1.add(var5);
            }
         }

         Collections.sort(var1);
         allFontNames = var1;
      }

      return (String[])allFontNames.toArray(STR_ARRAY);
   }

   public String[] getFontFullNames(String var1) {
      String[] var2 = LogicalFont.getFontsInFamily(var1);
      if (var2 != null) {
         return var2;
      } else {
         ArrayList var3;
         if (this.embeddedFonts != null) {
            var3 = null;
            Iterator var4 = this.embeddedFonts.values().iterator();

            while(var4.hasNext()) {
               PrismFontFile var5 = (PrismFontFile)var4.next();
               if (var5.getFamilyName().equalsIgnoreCase(var1)) {
                  if (var3 == null) {
                     var3 = new ArrayList();
                  }

                  var3.add(var5.getFullName());
               }
            }

            if (var3 != null) {
               return (String[])var3.toArray(STR_ARRAY);
            }
         }

         this.getFullNameToFileMap();
         var1 = var1.toLowerCase();
         var3 = (ArrayList)this.familyToFontListMap.get(var1);
         return var3 != null ? (String[])var3.toArray(STR_ARRAY) : STR_ARRAY;
      }
   }

   public final int getSubPixelMode() {
      return subPixelMode;
   }

   public boolean isLCDTextSupported() {
      return lcdEnabled;
   }

   public boolean isPlatformFont(String var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.toLowerCase();
         if (LogicalFont.isLogicalFont(var2)) {
            return true;
         } else if (var2.startsWith("lucida sans")) {
            return true;
         } else {
            String var3 = getSystemFont("System").toLowerCase();
            return var2.startsWith(var3);
         }
      }
   }

   public static boolean isJreFont(FontResource var0) {
      String var1 = var0.getFileName();
      return var1.startsWith(jreFontDir);
   }

   public static float getLCDContrast() {
      if (lcdContrast == -1.0F) {
         if (isWindows) {
            lcdContrast = (float)getLCDContrastWin32() / 1000.0F;
         } else {
            lcdContrast = 1.3F;
         }
      }

      return lcdContrast;
   }

   private synchronized void addFileCloserHook() {
      if (fileCloser == null) {
         Runnable var1 = () -> {
            Iterator var1;
            if (this.embeddedFonts != null) {
               var1 = this.embeddedFonts.values().iterator();

               while(var1.hasNext()) {
                  PrismFontFile var2 = (PrismFontFile)var1.next();
                  var2.disposeOnShutdown();
               }
            }

            if (this.tmpFonts != null) {
               var1 = this.tmpFonts.iterator();

               while(var1.hasNext()) {
                  WeakReference var4 = (WeakReference)var1.next();
                  PrismFontFile var3 = (PrismFontFile)var4.get();
                  if (var3 != null) {
                     var3.disposeOnShutdown();
                  }
               }
            }

         };
         AccessController.doPrivileged(() -> {
            ThreadGroup var1x = Thread.currentThread().getThreadGroup();

            for(ThreadGroup var2 = var1x; var2 != null; var2 = var2.getParent()) {
               var1x = var2;
            }

            fileCloser = new Thread(var1x, var1);
            fileCloser.setContextClassLoader((ClassLoader)null);
            Runtime.getRuntime().addShutdownHook(fileCloser);
            return null;
         });
      }

   }

   public PGFont loadEmbeddedFont(String var1, InputStream var2, float var3, boolean var4) {
      if (!this.hasPermission()) {
         return this.createFont("System Regular", var3);
      } else if (FontFileWriter.hasTempPermission()) {
         return this.loadEmbeddedFont0(var1, var2, var3, var4);
      } else {
         FontFileWriter.FontTracker var5 = FontFileWriter.FontTracker.getTracker();
         boolean var6 = false;

         PGFont var7;
         try {
            var6 = var5.acquirePermit();
            if (var6) {
               var7 = this.loadEmbeddedFont0(var1, var2, var3, var4);
               return var7;
            }

            var7 = null;
         } catch (InterruptedException var12) {
            Object var8 = null;
            return (PGFont)var8;
         } finally {
            if (var6) {
               var5.releasePermit();
            }

         }

         return var7;
      }
   }

   private PGFont loadEmbeddedFont0(String var1, InputStream var2, float var3, boolean var4) {
      PrismFontFile var5 = null;
      FontFileWriter var6 = new FontFileWriter();

      try {
         File var7 = var6.openFile();
         byte[] var8 = new byte[8192];

         while(true) {
            int var9 = var2.read(var8);
            if (var9 < 0) {
               var6.closeFile();
               var5 = this.loadEmbeddedFont(var1, var7.getPath(), var4, true, var6.isTracking());
               if (var5 != null && var5.isDecoded()) {
                  var6.deleteFile();
               }

               this.addFileCloserHook();
               break;
            }

            var6.writeBytes(var8, 0, var9);
         }
      } catch (Exception var13) {
         var6.deleteFile();
      } finally {
         if (var5 == null) {
            var6.deleteFile();
         }

      }

      if (var5 != null) {
         if (var3 <= 0.0F) {
            var3 = getSystemFontSize();
         }

         return new PrismFont(var5, var5.getFullName(), var3);
      } else {
         return null;
      }
   }

   public PGFont loadEmbeddedFont(String var1, String var2, float var3, boolean var4) {
      if (!this.hasPermission()) {
         return this.createFont("System Regular", var3);
      } else {
         this.addFileCloserHook();
         PrismFontFile var5 = this.loadEmbeddedFont(var1, var2, var4, false, false);
         if (var5 != null) {
            if (var3 <= 0.0F) {
               var3 = getSystemFontSize();
            }

            return new PrismFont(var5, var5.getFullName(), var3);
         } else {
            return null;
         }
      }
   }

   private void removeEmbeddedFont(String var1) {
      PrismFontFile var2 = (PrismFontFile)this.embeddedFonts.get(var1);
      if (var2 != null) {
         this.embeddedFonts.remove(var1);
         String var3 = var1.toLowerCase();
         this.fontResourceMap.remove(var3);
         this.compResourceMap.remove(var3);
         Iterator var4 = this.compResourceMap.values().iterator();

         while(var4.hasNext()) {
            CompositeFontResource var5 = (CompositeFontResource)var4.next();
            if (var5.getSlotResource(0) == var2) {
               var4.remove();
            }
         }

      }
   }

   protected boolean registerEmbeddedFont(String var1) {
      return true;
   }

   public int test_getNumEmbeddedFonts() {
      return this.numEmbeddedFonts;
   }

   private synchronized PrismFontFile loadEmbeddedFont(String var1, String var2, boolean var3, boolean var4, boolean var5) {
      ++this.numEmbeddedFonts;
      PrismFontFile var6 = this.createFontResource(var1, var2, var3, true, var4, var5);
      if (var6 == null) {
         return null;
      } else {
         String var7 = var6.getFamilyName();
         if (var7 != null && var7.length() != 0) {
            String var8 = var6.getFullName();
            if (var8 != null && var8.length() != 0) {
               String var9 = var6.getPSName();
               if (var9 != null && var9.length() != 0) {
                  boolean var10 = true;
                  if (this.embeddedFonts != null) {
                     FontResource var11 = (FontResource)this.embeddedFonts.get(var8);
                     if (var11 != null && var6.equals(var11)) {
                        var10 = false;
                     }
                  }

                  if (var10 && !this.registerEmbeddedFont(var6.getFileName())) {
                     return null;
                  } else if (!var3) {
                     if (var4 && !var6.isDecoded()) {
                        this.addTmpFont(var6);
                     }

                     return var6;
                  } else {
                     if (this.embeddedFonts == null) {
                        this.embeddedFonts = new HashMap();
                     }

                     if (var1 != null && !var1.isEmpty()) {
                        this.embeddedFonts.put(var1, var6);
                        this.storeInMap(var1, var6);
                     }

                     this.removeEmbeddedFont(var8);
                     this.embeddedFonts.put(var8, var6);
                     this.storeInMap(var8, var6);
                     var7 = var7 + this.dotStyleStr(var6.isBold(), var6.isItalic());
                     this.storeInMap(var7, var6);
                     this.compResourceMap.remove(var7.toLowerCase());
                     return var6;
                  }
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   private void logFontInfo(String var1, HashMap var2, HashMap var3, HashMap var4) {
      System.err.println(var1);
      Iterator var5 = var2.keySet().iterator();

      String var6;
      while(var5.hasNext()) {
         var6 = (String)var5.next();
         System.err.println("font=" + var6 + " file=" + (String)var2.get(var6));
      }

      var5 = var3.keySet().iterator();

      while(var5.hasNext()) {
         var6 = (String)var5.next();
         System.err.println("font=" + var6 + " family=" + (String)var3.get(var6));
      }

      var5 = var4.keySet().iterator();

      while(var5.hasNext()) {
         var6 = (String)var5.next();
         System.err.println("family=" + var6 + " fonts=" + var4.get(var6));
      }

   }

   private synchronized HashMap getFullNameToFileMap() {
      if (this.fontToFileMap == null) {
         HashMap var1 = new HashMap(100);
         this.fontToFamilyNameMap = new HashMap(100);
         this.familyToFontListMap = new HashMap(50);
         this.fileToFontMap = new HashMap(100);
         if (isWindows) {
            getPlatformFontDirs();
            populateFontFileNameMap(var1, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            if (debugFonts) {
               System.err.println("Windows Locale ID=" + getSystemLCID());
               this.logFontInfo(" *** WINDOWS FONTS BEFORE RESOLVING", var1, this.fontToFamilyNameMap, this.familyToFontListMap);
            }

            this.resolveWindowsFonts(var1, this.fontToFamilyNameMap, this.familyToFontListMap);
            if (debugFonts) {
               this.logFontInfo(" *** WINDOWS FONTS AFTER RESOLVING", var1, this.fontToFamilyNameMap, this.familyToFontListMap);
            }
         } else if (!isMacOSX && !isIOS) {
            if (isLinux) {
               FontConfigManager.populateMaps(var1, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.getDefault());
               if (debugFonts) {
                  this.logFontInfo(" *** FONTCONFIG LOCATED FONTS:", var1, this.fontToFamilyNameMap, this.familyToFontListMap);
               }
            } else {
               if (!isAndroid) {
                  this.fontToFileMap = var1;
                  return this.fontToFileMap;
               }

               AndroidFontFinder.populateFontFileNameMap(var1, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            }
         } else {
            MacFontFinder.populateFontFileNameMap(var1, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
         }

         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            String var4 = (String)var1.get(var3);
            this.fileToFontMap.put(var4.toLowerCase(), var3);
         }

         this.fontToFileMap = var1;
         if (isAndroid) {
            this.populateFontFileNameMapGeneric(AndroidFontFinder.getSystemFontsDir());
         }

         this.populateFontFileNameMapGeneric(jreFontDir);
      }

      return this.fontToFileMap;
   }

   public final boolean hasPermission() {
      try {
         SecurityManager var1 = System.getSecurityManager();
         if (var1 != null) {
            var1.checkPermission(new AllPermission());
         }

         return true;
      } catch (SecurityException var2) {
         return false;
      }
   }

   void addToMaps(PrismFontFile var1) {
      if (var1 != null) {
         String var2 = var1.getFullName();
         String var3 = var1.getFamilyName();
         if (var2 != null && var3 != null) {
            String var4 = var2.toLowerCase();
            String var5 = var3.toLowerCase();
            this.fontToFileMap.put(var4, var1.getFileName());
            this.fontToFamilyNameMap.put(var4, var3);
            ArrayList var6 = (ArrayList)this.familyToFontListMap.get(var5);
            if (var6 == null) {
               var6 = new ArrayList();
               this.familyToFontListMap.put(var5, var6);
            }

            var6.add(var2);
         }
      }
   }

   void populateFontFileNameMapGeneric(String var1) {
      File var2 = new File(var1);
      String[] var3 = null;

      try {
         var3 = (String[])AccessController.doPrivileged(() -> {
            return var2.list(PrismFontFactory.TTFilter.getInstance());
         });
      } catch (Exception var8) {
      }

      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            try {
               String var5 = var1 + File.separator + var3[var4];
               if (this.registerEmbeddedFont(var5)) {
                  int var6 = 0;
                  PrismFontFile var7 = this.createFontResource(var5, var6++);
                  if (var7 != null) {
                     this.addToMaps(var7);

                     while(var6 < var7.getFontCount()) {
                        var7 = this.createFontResource(var5, var6++);
                        if (var7 == null) {
                           break;
                        }

                        this.addToMaps(var7);
                     }
                  }
               }
            } catch (Exception var9) {
            }
         }

      }
   }

   static native int getLCDContrastWin32();

   private static native float getSystemFontSizeNative(float var0);

   private static native String getSystemFontNative();

   public static float getSystemFontSize() {
      if (systemFontSize == -1.0F) {
         if (isWindows) {
            systemFontSize = getSystemFontSizeNative(PrismSettings.winMinDPIScale);
         } else if (!isMacOSX && !isIOS) {
            if (isAndroid) {
               systemFontSize = AndroidFontFinder.getSystemFontSize();
            } else if (isEmbedded) {
               try {
                  int var0 = Screen.getMainScreen().getResolutionY();
                  systemFontSize = (float)var0 / 6.0F;
               } catch (NullPointerException var1) {
                  systemFontSize = 13.0F;
               }
            } else {
               systemFontSize = 13.0F;
            }
         } else {
            systemFontSize = MacFontFinder.getSystemFontSize();
         }
      }

      return systemFontSize;
   }

   public static String getSystemFont(String var0) {
      if (!var0.equals("System")) {
         if (var0.equals("SansSerif")) {
            return "Arial";
         } else if (var0.equals("Serif")) {
            return "Times New Roman";
         } else {
            if (monospaceFontFamily == null && isMacOSX) {
            }

            if (monospaceFontFamily == null) {
               monospaceFontFamily = "Courier New";
            }

            return monospaceFontFamily;
         }
      } else {
         if (systemFontFamily == null) {
            if (isWindows) {
               systemFontFamily = getSystemFontNative();
               if (systemFontFamily == null) {
                  systemFontFamily = "Arial";
               }
            } else if (!isMacOSX && !isIOS) {
               if (isAndroid) {
                  systemFontFamily = AndroidFontFinder.getSystemFont();
               } else {
                  systemFontFamily = "Lucida Sans";
               }
            } else {
               systemFontFamily = MacFontFinder.getSystemFont();
               if (systemFontFamily == null) {
                  systemFontFamily = "Lucida Grande";
               }
            }
         }

         return systemFontFamily;
      }
   }

   static native short getSystemLCID();

   static {
      int[] var0 = new int[]{65536};
      debugFonts = (Boolean)AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font");
         String var1 = System.getProperty("prism.debugfonts", "");
         boolean var2 = "true".equals(var1);
         jreFontDir = System.getProperty("java.home", "") + File.separator + "lib" + File.separator + "fonts" + File.separator;
         String var3 = System.getProperty("com.sun.javafx.fontSize");
         systemFontSize = -1.0F;
         if (var3 != null) {
            try {
               systemFontSize = Float.parseFloat(var3);
            } catch (NumberFormatException var10) {
               System.err.println("Cannot parse font size '" + var3 + "'");
            }
         }

         var3 = System.getProperty("prism.subpixeltext", "on");
         if (var3.indexOf("on") != -1 || var3.indexOf("true") != -1) {
            subPixelMode = 1;
         }

         if (var3.indexOf("native") != -1) {
            subPixelMode |= 5;
         }

         if (var3.indexOf("vertical") != -1) {
            subPixelMode |= 7;
         }

         var3 = System.getProperty("prism.fontSizeLimit");
         if (var3 != null) {
            try {
               fontSizeLimit = Float.parseFloat(var3);
               if (fontSizeLimit <= 0.0F) {
                  fontSizeLimit = Float.POSITIVE_INFINITY;
               }
            } catch (NumberFormatException var9) {
               System.err.println("Cannot parse fontSizeLimit '" + var3 + "'");
            }
         }

         boolean var4 = isIOS || isAndroid || isEmbedded;
         String var5 = var4 ? "false" : "true";
         String var6 = System.getProperty("prism.lcdtext", var5);
         lcdEnabled = var6.equals("true");
         var3 = System.getProperty("prism.cacheLayoutSize");
         if (var3 != null) {
            try {
               var0[0] = Integer.parseInt(var3);
               if (var0[0] < 0) {
                  var0[0] = 0;
               }
            } catch (NumberFormatException var8) {
               System.err.println("Cannot parse cache layout size '" + var3 + "'");
            }
         }

         return var2;
      });
      cacheLayoutSize = var0[0];
      theFontFactory = null;
      STR_ARRAY = new String[0];
      sysFontDir = null;
      userFontDir = null;
      fileCloser = null;
      systemFontFamily = null;
      monospaceFontFamily = null;
   }

   private static class TTFilter implements FilenameFilter {
      static TTFilter ttFilter;

      public boolean accept(File var1, String var2) {
         int var3 = var2.length() - 4;
         if (var3 <= 0) {
            return false;
         } else {
            return var2.startsWith(".ttf", var3) || var2.startsWith(".TTF", var3) || var2.startsWith(".ttc", var3) || var2.startsWith(".TTC", var3) || var2.startsWith(".otf", var3) || var2.startsWith(".OTF", var3);
         }
      }

      static TTFilter getInstance() {
         if (ttFilter == null) {
            ttFilter = new TTFilter();
         }

         return ttFilter;
      }
   }
}
