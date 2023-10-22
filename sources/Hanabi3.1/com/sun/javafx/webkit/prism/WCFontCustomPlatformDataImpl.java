package com.sun.javafx.webkit.prism;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PGFont;
import com.sun.prism.GraphicsPipeline;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCFontCustomPlatformData;
import java.io.IOException;
import java.io.InputStream;

final class WCFontCustomPlatformDataImpl extends WCFontCustomPlatformData {
   private final PGFont font;

   WCFontCustomPlatformDataImpl(InputStream var1) throws IOException {
      FontFactory var2 = GraphicsPipeline.getPipeline().getFontFactory();
      this.font = var2.loadEmbeddedFont((String)null, (InputStream)var1, 10.0F, false);
      if (this.font == null) {
         throw new IOException("Error loading font");
      }
   }

   protected WCFont createFont(int var1, boolean var2, boolean var3) {
      FontFactory var4 = GraphicsPipeline.getPipeline().getFontFactory();
      return new WCFontImpl(var4.deriveFont(this.font, var2, var3, (float)var1));
   }
}
