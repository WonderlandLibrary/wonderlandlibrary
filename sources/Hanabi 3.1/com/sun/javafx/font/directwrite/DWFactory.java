package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.text.GlyphLayout;
import com.sun.prism.GraphicsPipeline;

public class DWFactory extends PrismFontFactory {
   private static IDWriteFactory DWRITE_FACTORY = null;
   private static IDWriteFontCollection FONT_COLLECTION = null;
   private static IWICImagingFactory WIC_FACTORY = null;
   private static ID2D1Factory D2D_FACTORY = null;
   private static Thread d2dThread;

   public static PrismFontFactory getFactory() {
      return getDWriteFactory() == null ? null : new DWFactory();
   }

   private DWFactory() {
   }

   protected PrismFontFile createFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      return new DWFontFile(var1, var2, var3, var4, var5, var6, var7);
   }

   public GlyphLayout createGlyphLayout() {
      return new DWGlyphLayout();
   }

   protected boolean registerEmbeddedFont(String var1) {
      IDWriteFactory var2 = getDWriteFactory();
      IDWriteFontFile var3 = var2.CreateFontFileReference(var1);
      if (var3 == null) {
         return false;
      } else {
         boolean[] var4 = new boolean[1];
         int[] var5 = new int[1];
         int[] var6 = new int[1];
         int[] var7 = new int[1];
         int var8 = var3.Analyze(var4, var5, var6, var7);
         var3.Release();
         return var8 != 0 ? false : var4[0];
      }
   }

   static IDWriteFactory getDWriteFactory() {
      if (DWRITE_FACTORY == null) {
         DWRITE_FACTORY = OS.DWriteCreateFactory(0);
      }

      return DWRITE_FACTORY;
   }

   static IDWriteFontCollection getFontCollection() {
      if (FONT_COLLECTION == null) {
         FONT_COLLECTION = getDWriteFactory().GetSystemFontCollection(false);
      }

      return FONT_COLLECTION;
   }

   private static void checkThread() {
      Thread var0 = Thread.currentThread();
      if (d2dThread == null) {
         d2dThread = var0;
      }

      if (d2dThread != var0) {
         throw new IllegalStateException("This operation is not permitted on the current thread [" + var0.getName() + "]");
      }
   }

   static synchronized IWICImagingFactory getWICFactory() {
      checkThread();
      if (WIC_FACTORY == null) {
         if (!OS.CoInitializeEx(6)) {
            return null;
         }

         WIC_FACTORY = OS.WICCreateImagingFactory();
         if (WIC_FACTORY == null) {
            return null;
         }

         GraphicsPipeline.getPipeline().addDisposeHook(() -> {
            checkThread();
            WIC_FACTORY.Release();
            OS.CoUninitialize();
            WIC_FACTORY = null;
         });
      }

      return WIC_FACTORY;
   }

   static synchronized ID2D1Factory getD2DFactory() {
      checkThread();
      if (D2D_FACTORY == null) {
         D2D_FACTORY = OS.D2D1CreateFactory(0);
      }

      return D2D_FACTORY;
   }
}
