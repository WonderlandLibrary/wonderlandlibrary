package com.sun.javafx.stage;

import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Scene;
import javafx.stage.Window;

public class EmbeddedWindow extends Window {
   private HostInterface host;

   public EmbeddedWindow(HostInterface var1) {
      this.host = var1;
   }

   public final void setScene(Scene var1) {
      super.setScene(var1);
   }

   public final void show() {
      super.show();
   }

   protected void impl_visibleChanging(boolean var1) {
      super.impl_visibleChanging(var1);
      Toolkit var2 = Toolkit.getToolkit();
      if (var1 && this.impl_peer == null) {
         this.impl_peer = var2.createTKEmbeddedStage(this.host, WindowHelper.getAccessControlContext(this));
         this.peerListener = new WindowPeerListener(this);
      }

   }
}
