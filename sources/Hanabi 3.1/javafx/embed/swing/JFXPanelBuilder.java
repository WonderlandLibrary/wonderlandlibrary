package javafx.embed.swing;

import javafx.scene.Scene;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class JFXPanelBuilder implements Builder {
   private int __set;
   private boolean opaque;
   private Scene scene;

   protected JFXPanelBuilder() {
   }

   public static JFXPanelBuilder create() {
      return new JFXPanelBuilder();
   }

   public void applyTo(JFXPanel var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setOpaque(this.opaque);
      }

      if ((var2 & 2) != 0) {
         var1.setScene(this.scene);
      }

   }

   public JFXPanelBuilder opaque(boolean var1) {
      this.opaque = var1;
      this.__set |= 1;
      return this;
   }

   public JFXPanelBuilder scene(Scene var1) {
      this.scene = var1;
      this.__set |= 2;
      return this;
   }

   public JFXPanel build() {
      JFXPanel var1 = new JFXPanel();
      this.applyTo(var1);
      return var1;
   }
}
