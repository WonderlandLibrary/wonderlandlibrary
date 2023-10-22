package com.sun.javafx.webkit.prism.theme;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.prism.Graphics;
import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.Scene;
import javafx.scene.control.Control;

public final class PrismRenderer extends Renderer {
   protected void render(Control var1, WCGraphicsContext var2) {
      Scene.impl_setAllowPGAccess(true);
      NGNode var3 = var1.impl_getPeer();
      Scene.impl_setAllowPGAccess(false);
      var3.render((Graphics)var2.getPlatformGraphics());
   }
}
