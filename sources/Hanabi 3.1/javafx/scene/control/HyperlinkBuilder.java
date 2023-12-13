package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class HyperlinkBuilder extends ButtonBaseBuilder implements Builder {
   private boolean __set;
   private boolean visited;

   protected HyperlinkBuilder() {
   }

   public static HyperlinkBuilder create() {
      return new HyperlinkBuilder();
   }

   public void applyTo(Hyperlink var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setVisited(this.visited);
      }

   }

   public HyperlinkBuilder visited(boolean var1) {
      this.visited = var1;
      this.__set = true;
      return this;
   }

   public Hyperlink build() {
      Hyperlink var1 = new Hyperlink();
      this.applyTo(var1);
      return var1;
   }
}
