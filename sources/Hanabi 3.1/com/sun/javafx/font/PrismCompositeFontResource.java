package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class PrismCompositeFontResource implements CompositeFontResource {
   private FontResource primaryResource;
   private FallbackResource fallbackResource;
   CompositeGlyphMapper mapper;
   Map strikeMap = new ConcurrentHashMap();

   PrismCompositeFontResource(FontResource var1, String var2) {
      if (!(var1 instanceof PrismFontFile)) {
         Thread.dumpStack();
         throw new IllegalStateException("wrong resource type");
      } else {
         if (var2 != null) {
            PrismFontFactory var3 = PrismFontFactory.getFontFactory();
            var3.compResourceMap.put(var2, this);
         }

         this.primaryResource = var1;
         int var6 = var1.getDefaultAAMode();
         boolean var4 = var1.isBold();
         boolean var5 = var1.isItalic();
         this.fallbackResource = FallbackResource.getFallbackResource(var4, var5, var6);
      }
   }

   public int getNumSlots() {
      return this.fallbackResource.getNumSlots() + 1;
   }

   public int getSlotForFont(String var1) {
      return this.primaryResource.getFullName().equalsIgnoreCase(var1) ? 0 : this.fallbackResource.getSlotForFont(var1) + 1;
   }

   public FontResource getSlotResource(int var1) {
      if (var1 == 0) {
         return this.primaryResource;
      } else {
         FontResource var2 = this.fallbackResource.getSlotResource(var1 - 1);
         return var2 != null ? var2 : this.primaryResource;
      }
   }

   public String getFullName() {
      return this.primaryResource.getFullName();
   }

   public String getPSName() {
      return this.primaryResource.getPSName();
   }

   public String getFamilyName() {
      return this.primaryResource.getFamilyName();
   }

   public String getStyleName() {
      return this.primaryResource.getStyleName();
   }

   public String getLocaleFullName() {
      return this.primaryResource.getLocaleFullName();
   }

   public String getLocaleFamilyName() {
      return this.primaryResource.getLocaleFamilyName();
   }

   public String getLocaleStyleName() {
      return this.primaryResource.getLocaleStyleName();
   }

   public String getFileName() {
      return this.primaryResource.getFileName();
   }

   public int getFeatures() {
      return this.primaryResource.getFeatures();
   }

   public Object getPeer() {
      return this.primaryResource.getPeer();
   }

   public void setPeer(Object var1) {
      throw new UnsupportedOperationException("Not supported");
   }

   public boolean isEmbeddedFont() {
      return this.primaryResource.isEmbeddedFont();
   }

   public boolean isBold() {
      return this.primaryResource.isBold();
   }

   public boolean isItalic() {
      return this.primaryResource.isItalic();
   }

   public CharToGlyphMapper getGlyphMapper() {
      if (this.mapper == null) {
         this.mapper = new CompositeGlyphMapper(this);
      }

      return this.mapper;
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

   public Map getStrikeMap() {
      return this.strikeMap;
   }

   public int getDefaultAAMode() {
      return this.getSlotResource(0).getDefaultAAMode();
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

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof PrismCompositeFontResource)) {
         return false;
      } else {
         PrismCompositeFontResource var2 = (PrismCompositeFontResource)var1;
         return this.primaryResource.equals(var2.primaryResource);
      }
   }

   public int hashCode() {
      return this.primaryResource.hashCode();
   }
}
