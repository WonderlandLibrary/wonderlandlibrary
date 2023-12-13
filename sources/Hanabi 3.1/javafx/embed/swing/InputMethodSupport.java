package javafx.embed.swing;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import java.awt.Rectangle;
import java.awt.event.InputMethodEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;

class InputMethodSupport {
   public static ObservableList inputMethodEventComposed(String var0, int var1) {
      ArrayList var2 = new ArrayList();
      if (var1 < var0.length()) {
         var2.add(new InputMethodTextRun(var0.substring(var1), InputMethodHighlight.UNSELECTED_RAW));
      }

      return new ObservableListWrapper(var2);
   }

   public static String getTextForEvent(InputMethodEvent var0) {
      AttributedCharacterIterator var1 = var0.getText();
      if (var0.getText() == null) {
         return "";
      } else {
         char var2 = var1.first();

         StringBuilder var3;
         for(var3 = new StringBuilder(); var2 != '\uffff'; var2 = var1.next()) {
            var3.append(var2);
         }

         return var3.toString();
      }
   }

   public static class InputMethodRequestsAdapter implements InputMethodRequests {
      private final javafx.scene.input.InputMethodRequests fxRequests;

      public InputMethodRequestsAdapter(javafx.scene.input.InputMethodRequests var1) {
         this.fxRequests = var1;
      }

      public Rectangle getTextLocation(TextHitInfo var1) {
         Point2D var2 = this.fxRequests.getTextLocation(var1.getInsertionIndex());
         return new Rectangle((int)var2.getX(), (int)var2.getY(), 0, 0);
      }

      public TextHitInfo getLocationOffset(int var1, int var2) {
         int var3 = this.fxRequests.getLocationOffset(var1, var2);
         return TextHitInfo.afterOffset(var3);
      }

      public int getInsertPositionOffset() {
         return this.fxRequests instanceof ExtendedInputMethodRequests ? ((ExtendedInputMethodRequests)this.fxRequests).getInsertPositionOffset() : 0;
      }

      public AttributedCharacterIterator getCommittedText(int var1, int var2, AttributedCharacterIterator.Attribute[] var3) {
         String var4 = null;
         if (this.fxRequests instanceof ExtendedInputMethodRequests) {
            var4 = ((ExtendedInputMethodRequests)this.fxRequests).getCommittedText(var1, var2);
         }

         if (var4 == null) {
            var4 = "";
         }

         return (new AttributedString(var4)).getIterator();
      }

      public int getCommittedTextLength() {
         return this.fxRequests instanceof ExtendedInputMethodRequests ? ((ExtendedInputMethodRequests)this.fxRequests).getCommittedTextLength() : 0;
      }

      public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] var1) {
         return null;
      }

      public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] var1) {
         String var2 = this.fxRequests.getSelectedText();
         if (var2 == null) {
            var2 = "";
         }

         return (new AttributedString(var2)).getIterator();
      }
   }
}
