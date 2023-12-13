package com.sun.javafx.webkit;

import com.sun.webkit.Pasteboard;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageFrame;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javax.imageio.ImageIO;

final class PasteboardImpl implements Pasteboard {
   private final Clipboard clipboard = Clipboard.getSystemClipboard();

   public String getPlainText() {
      return this.clipboard.getString();
   }

   public String getHtml() {
      return this.clipboard.getHtml();
   }

   public void writePlainText(String var1) {
      ClipboardContent var2 = new ClipboardContent();
      var2.putString(var1);
      this.clipboard.setContent(var2);
   }

   public void writeSelection(boolean var1, String var2, String var3) {
      ClipboardContent var4 = new ClipboardContent();
      var4.putString(var2);
      var4.putHtml(var3);
      this.clipboard.setContent(var4);
   }

   public void writeImage(WCImageFrame var1) {
      WCImage var2 = var1.getFrame();
      Image var3 = var2 != null && !var2.isNull() ? Image.impl_fromPlatformImage(var2.getPlatformImage()) : null;
      if (var3 != null) {
         ClipboardContent var4 = new ClipboardContent();
         var4.putImage(var3);
         String var5 = var2.getFileExtension();

         try {
            File var6 = File.createTempFile("jfx", "." + var5);
            var6.deleteOnExit();
            ImageIO.write(var2.toBufferedImage(), var5, var6);
            var4.putFiles(Arrays.asList(var6));
         } catch (SecurityException | IOException var7) {
         }

         this.clipboard.setContent(var4);
      }

   }

   public void writeUrl(String var1, String var2) {
      ClipboardContent var3 = new ClipboardContent();
      var3.putString(var1);
      var3.putHtml(var2);
      var3.putUrl(var1);
      this.clipboard.setContent(var3);
   }
}
