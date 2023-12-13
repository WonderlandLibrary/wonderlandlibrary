package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogicalFont implements CompositeFontResource {
   public static final String SYSTEM = "System";
   public static final String SERIF = "Serif";
   public static final String SANS_SERIF = "SansSerif";
   public static final String MONOSPACED = "Monospaced";
   public static final String STYLE_REGULAR = "Regular";
   public static final String STYLE_BOLD = "Bold";
   public static final String STYLE_ITALIC = "Italic";
   public static final String STYLE_BOLD_ITALIC = "Bold Italic";
   static final HashMap canonicalFamilyMap = new HashMap();
   static LogicalFont[] logicalFonts;
   boolean isBold;
   boolean isItalic;
   private String fullName;
   private String familyName;
   private String styleName;
   private String physicalFamily;
   private String physicalFullName;
   private String physicalFileName;
   private FontResource slot0FontResource;
   private ArrayList linkedFontFiles;
   private ArrayList linkedFontNames;
   private FontResource[] fallbacks;
   private FontResource[] nativeFallbacks;
   CompositeGlyphMapper mapper;
   Map strikeMap = new ConcurrentHashMap();
   private static final int SANS_SERIF_INDEX = 0;
   private static final int SERIF_INDEX = 1;
   private static final int MONOSPACED_INDEX = 2;
   private static final int SYSTEM_INDEX = 3;
   static String[][] logFamilies;
   private int hash;

   static boolean isLogicalFont(String var0) {
      int var1 = var0.indexOf(32);
      if (var1 != -1) {
         var0 = var0.substring(0, var1);
      }

      return canonicalFamilyMap.get(var0) != null;
   }

   private static String getCanonicalFamilyName(String var0) {
      if (var0 == null) {
         return "SansSerif";
      } else {
         String var1 = var0.toLowerCase();
         return (String)canonicalFamilyMap.get(var1);
      }
   }

   static PGFont getLogicalFont(String var0, boolean var1, boolean var2, float var3) {
      String var4 = getCanonicalFamilyName(var0);
      if (var4 == null) {
         return null;
      } else {
         boolean var5 = false;
         int var7;
         if (var4.equals("SansSerif")) {
            var7 = 0;
         } else if (var4.equals("Serif")) {
            var7 = 4;
         } else if (var4.equals("Monospaced")) {
            var7 = 8;
         } else {
            var7 = 12;
         }

         if (var1) {
            ++var7;
         }

         if (var2) {
            var7 += 2;
         }

         LogicalFont var6 = logicalFonts[var7];
         if (var6 == null) {
            var6 = new LogicalFont(var4, var1, var2);
            logicalFonts[var7] = var6;
         }

         return new PrismFont(var6, var6.getFullName(), var3);
      }
   }

   static PGFont getLogicalFont(String var0, float var1) {
      int var2 = var0.indexOf(32);
      if (var2 != -1 && var2 != var0.length() - 1) {
         String var3 = var0.substring(0, var2);
         String var4 = getCanonicalFamilyName(var3);
         if (var4 == null) {
            return null;
         } else {
            String var5 = var0.substring(var2 + 1).toLowerCase();
            boolean var6 = false;
            boolean var7 = false;
            if (!var5.equals("regular")) {
               if (var5.equals("bold")) {
                  var6 = true;
               } else if (var5.equals("italic")) {
                  var7 = true;
               } else {
                  if (!var5.equals("bold italic")) {
                     return null;
                  }

                  var6 = true;
                  var7 = true;
               }
            }

            return getLogicalFont(var4, var6, var7, var1);
         }
      } else {
         return null;
      }
   }

   private LogicalFont(String var1, boolean var2, boolean var3) {
      this.familyName = var1;
      this.isBold = var2;
      this.isItalic = var3;
      if (!var2 && !var3) {
         this.styleName = "Regular";
      } else if (var2 && !var3) {
         this.styleName = "Bold";
      } else if (!var2 && var3) {
         this.styleName = "Italic";
      } else {
         this.styleName = "Bold Italic";
      }

      this.fullName = this.familyName + " " + this.styleName;
      if (PrismFontFactory.isLinux) {
         FontConfigManager.FcCompFont var4 = FontConfigManager.getFontConfigFont(var1, var2, var3);
         this.physicalFullName = var4.firstFont.fullName;
         this.physicalFileName = var4.firstFont.fontFile;
      } else {
         this.physicalFamily = PrismFontFactory.getSystemFont(this.familyName);
      }

   }

   private FontResource getSlot0Resource() {
      if (this.slot0FontResource == null) {
         PrismFontFactory var1 = PrismFontFactory.getFontFactory();
         if (this.physicalFamily != null) {
            this.slot0FontResource = var1.getFontResource(this.physicalFamily, this.isBold, this.isItalic, false);
         } else {
            this.slot0FontResource = var1.getFontResource(this.physicalFullName, this.physicalFileName, false);
         }

         if (this.slot0FontResource == null) {
            this.slot0FontResource = var1.getDefaultFontResource(false);
         }
      }

      return this.slot0FontResource;
   }

   private void getLinkedFonts() {
      if (this.fallbacks == null) {
         if (PrismFontFactory.isLinux) {
            FontConfigManager.FcCompFont var2 = FontConfigManager.getFontConfigFont(this.familyName, this.isBold, this.isItalic);
            this.linkedFontFiles = FontConfigManager.getFileNames(var2, true);
            this.linkedFontNames = FontConfigManager.getFontNames(var2, true);
         } else {
            ArrayList[] var1 = PrismFontFactory.getLinkedFonts("Tahoma", true);
            this.linkedFontFiles = var1[0];
            this.linkedFontNames = var1[1];
         }

         this.fallbacks = new FontResource[this.linkedFontFiles.size()];
      }

   }

   public int getNumSlots() {
      this.getLinkedFonts();
      int var1 = this.linkedFontFiles.size();
      if (this.nativeFallbacks != null) {
         var1 += this.nativeFallbacks.length;
      }

      return var1 + 1;
   }

   public int getSlotForFont(String var1) {
      this.getLinkedFonts();
      int var2 = 1;

      for(Iterator var3 = this.linkedFontNames.iterator(); var3.hasNext(); ++var2) {
         String var4 = (String)var3.next();
         if (var1.equalsIgnoreCase(var4)) {
            return var2;
         }
      }

      if (this.nativeFallbacks != null) {
         FontResource[] var7 = this.nativeFallbacks;
         int var9 = var7.length;

         for(int var5 = 0; var5 < var9; ++var5) {
            FontResource var6 = var7[var5];
            if (var1.equalsIgnoreCase(var6.getFullName())) {
               return var2;
            }

            ++var2;
         }
      }

      if (var2 >= 126) {
         if (PrismFontFactory.debugFonts) {
            System.err.println("\tToo many font fallbacks!");
         }

         return -1;
      } else {
         PrismFontFactory var8 = PrismFontFactory.getFontFactory();
         FontResource var10 = var8.getFontResource(var1, (String)null, false);
         if (var10 == null) {
            if (PrismFontFactory.debugFonts) {
               System.err.println("\t Font name not supported \"" + var1 + "\".");
            }

            return -1;
         } else {
            FontResource[] var11;
            if (this.nativeFallbacks == null) {
               var11 = new FontResource[1];
            } else {
               var11 = new FontResource[this.nativeFallbacks.length + 1];
               System.arraycopy(this.nativeFallbacks, 0, var11, 0, this.nativeFallbacks.length);
            }

            var11[var11.length - 1] = var10;
            this.nativeFallbacks = var11;
            return var2;
         }
      }
   }

   public FontResource getSlotResource(int var1) {
      if (var1 == 0) {
         return this.getSlot0Resource();
      } else {
         this.getLinkedFonts();
         --var1;
         if (var1 >= this.fallbacks.length) {
            var1 -= this.fallbacks.length;
            return this.nativeFallbacks != null && var1 < this.nativeFallbacks.length ? this.nativeFallbacks[var1] : null;
         } else {
            if (this.fallbacks[var1] == null) {
               String var2 = (String)this.linkedFontFiles.get(var1);
               String var3 = (String)this.linkedFontNames.get(var1);
               this.fallbacks[var1] = PrismFontFactory.getFontFactory().getFontResource(var3, var2, false);
               if (this.fallbacks[var1] == null) {
                  this.fallbacks[var1] = this.getSlot0Resource();
               }
            }

            return this.fallbacks[var1];
         }
      }
   }

   public String getFullName() {
      return this.fullName;
   }

   public String getPSName() {
      return this.fullName;
   }

   public String getFamilyName() {
      return this.familyName;
   }

   public String getStyleName() {
      return this.styleName;
   }

   public String getLocaleFullName() {
      return this.fullName;
   }

   public String getLocaleFamilyName() {
      return this.familyName;
   }

   public String getLocaleStyleName() {
      return this.styleName;
   }

   public boolean isBold() {
      return this.getSlotResource(0).isBold();
   }

   public boolean isItalic() {
      return this.getSlotResource(0).isItalic();
   }

   public String getFileName() {
      return this.getSlotResource(0).getFileName();
   }

   public int getFeatures() {
      return this.getSlotResource(0).getFeatures();
   }

   public Object getPeer() {
      return null;
   }

   public boolean isEmbeddedFont() {
      return this.getSlotResource(0).isEmbeddedFont();
   }

   public void setPeer(Object var1) {
      throw new UnsupportedOperationException("Not supported");
   }

   public float[] getGlyphBoundingBox(int var1, float var2, float[] var3) {
      int var4 = var1 >>> 24;
      int var5 = var1 & 16777215;
      FontResource var6 = this.getSlotResource(var4);
      return var6.getGlyphBoundingBox(var5, var2, var3);
   }

   public float getAdvance(int var1, float var2) {
      int var3 = var1 >>> 24;
      int var4 = var1 & 16777215;
      FontResource var5 = this.getSlotResource(var3);
      return var5.getAdvance(var4, var2);
   }

   public CharToGlyphMapper getGlyphMapper() {
      if (this.mapper == null) {
         this.mapper = new CompositeGlyphMapper(this);
      }

      return this.mapper;
   }

   public Map getStrikeMap() {
      return this.strikeMap;
   }

   public int getDefaultAAMode() {
      return this.getSlot0Resource().getDefaultAAMode();
   }

   public FontStrike getStrike(float var1, BaseTransform var2) {
      return this.getStrike(var1, var2, this.getDefaultAAMode());
   }

   public FontStrike getStrike(float var1, BaseTransform var2, int var3) {
      FontStrikeDesc var4 = new FontStrikeDesc(var1, var2, var3);
      WeakReference var5 = (WeakReference)this.strikeMap.get(var4);
      CompositeStrike var6 = null;
      if (var5 != null) {
         var6 = (CompositeStrike)var5.get();
      }

      if (var6 == null) {
         var6 = new CompositeStrike(this, var1, var2, var3, var4);
         if (var6.disposer != null) {
            var5 = Disposer.addRecord(var6, var6.disposer);
         } else {
            var5 = new WeakReference(var6);
         }

         this.strikeMap.put(var4, var5);
      }

      return var6;
   }

   private static void buildFamily(String[] var0, String var1) {
      var0[0] = var1 + " " + "Regular";
      var0[1] = var1 + " " + "Bold";
      var0[2] = var1 + " " + "Italic";
      var0[3] = var1 + " " + "Bold Italic";
   }

   private static void buildFamilies() {
      if (logFamilies == null) {
         String[][] var0 = new String[4][4];
         buildFamily(var0[0], "SansSerif");
         buildFamily(var0[1], "Serif");
         buildFamily(var0[2], "Monospaced");
         buildFamily(var0[3], "System");
         logFamilies = var0;
      }

   }

   static void addFamilies(ArrayList var0) {
      var0.add("SansSerif");
      var0.add("Serif");
      var0.add("Monospaced");
      var0.add("System");
   }

   static void addFullNames(ArrayList var0) {
      buildFamilies();

      for(int var1 = 0; var1 < logFamilies.length; ++var1) {
         for(int var2 = 0; var2 < logFamilies[var1].length; ++var2) {
            var0.add(logFamilies[var1][var2]);
         }
      }

   }

   static String[] getFontsInFamily(String var0) {
      String var1 = getCanonicalFamilyName(var0);
      if (var1 == null) {
         return null;
      } else {
         buildFamilies();
         if (var1.equals("SansSerif")) {
            return logFamilies[0];
         } else if (var1.equals("Serif")) {
            return logFamilies[1];
         } else {
            return var1.equals("Monospaced") ? logFamilies[2] : logFamilies[3];
         }
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof LogicalFont)) {
         return false;
      } else {
         LogicalFont var2 = (LogicalFont)var1;
         return this.fullName.equals(var2.fullName);
      }
   }

   public int hashCode() {
      if (this.hash != 0) {
         return this.hash;
      } else {
         this.hash = this.fullName.hashCode();
         return this.hash;
      }
   }

   static {
      canonicalFamilyMap.put("system", "System");
      canonicalFamilyMap.put("serif", "Serif");
      canonicalFamilyMap.put("sansserif", "SansSerif");
      canonicalFamilyMap.put("sans-serif", "SansSerif");
      canonicalFamilyMap.put("dialog", "SansSerif");
      canonicalFamilyMap.put("default", "SansSerif");
      canonicalFamilyMap.put("monospaced", "Monospaced");
      canonicalFamilyMap.put("monospace", "Monospaced");
      canonicalFamilyMap.put("dialoginput", "Monospaced");
      logicalFonts = new LogicalFont[16];
      logFamilies = (String[][])null;
   }
}
