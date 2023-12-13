package com.sun.webkit;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

public final class ColorChooser {
   private static final double COLOR_DOUBLE_TO_UCHAR_FACTOR = 255.0;
   private CustomColorDialog colorChooserDialog;
   private final long pdata;

   private ColorChooser(WebPage var1, Color var2, long var3) {
      this.pdata = var3;
      WebPageClient var5 = var1.getPageClient();
      this.colorChooserDialog = new CustomColorDialog(((WebView)var5.getContainer()).getScene().getWindow());
      this.colorChooserDialog.setSaveBtnToOk();
      this.colorChooserDialog.setShowUseBtn(false);
      this.colorChooserDialog.setShowOpacitySlider(false);
      this.colorChooserDialog.setOnSave(() -> {
         this.twkSetSelectedColor(this.pdata, (int)Math.round(this.colorChooserDialog.getCustomColor().getRed() * 255.0), (int)Math.round(this.colorChooserDialog.getCustomColor().getGreen() * 255.0), (int)Math.round(this.colorChooserDialog.getCustomColor().getBlue() * 255.0));
      });
      this.colorChooserDialog.setCurrentColor(var2);
      this.colorChooserDialog.show();
   }

   private static ColorChooser fwkCreateAndShowColorChooser(WebPage var0, int var1, int var2, int var3, long var4) {
      return new ColorChooser(var0, Color.rgb(var1, var2, var3), var4);
   }

   private void fwkShowColorChooser(int var1, int var2, int var3) {
      this.colorChooserDialog.setCurrentColor(Color.rgb(var1, var2, var3));
      this.colorChooserDialog.show();
   }

   private void fwkHideColorChooser() {
      this.colorChooserDialog.hide();
   }

   private native void twkSetSelectedColor(long var1, int var3, int var4, int var5);
}
