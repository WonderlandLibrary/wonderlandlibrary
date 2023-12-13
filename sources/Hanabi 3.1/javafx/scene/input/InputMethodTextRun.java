package javafx.scene.input;

import java.io.Serializable;
import javafx.beans.NamedArg;

public class InputMethodTextRun implements Serializable {
   private final String text;
   private final InputMethodHighlight highlight;

   public InputMethodTextRun(@NamedArg("text") String var1, @NamedArg("highlight") InputMethodHighlight var2) {
      this.text = var1;
      this.highlight = var2;
   }

   public final String getText() {
      return this.text;
   }

   public final InputMethodHighlight getHighlight() {
      return this.highlight;
   }

   public String toString() {
      return "InputMethodTextRun text [" + this.getText() + "], highlight [" + this.getHighlight() + "]";
   }
}
