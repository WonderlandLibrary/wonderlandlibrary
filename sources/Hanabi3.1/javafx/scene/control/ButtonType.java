package javafx.scene.control;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.NamedArg;

public final class ButtonType {
   public static final ButtonType APPLY;
   public static final ButtonType OK;
   public static final ButtonType CANCEL;
   public static final ButtonType CLOSE;
   public static final ButtonType YES;
   public static final ButtonType NO;
   public static final ButtonType FINISH;
   public static final ButtonType NEXT;
   public static final ButtonType PREVIOUS;
   private final String key;
   private final String text;
   private final ButtonBar.ButtonData buttonData;

   public ButtonType(@NamedArg("text") String var1) {
      this(var1, ButtonBar.ButtonData.OTHER);
   }

   public ButtonType(@NamedArg("text") String var1, @NamedArg("buttonData") ButtonBar.ButtonData var2) {
      this((String)null, var1, var2);
   }

   private ButtonType(String var1, String var2, ButtonBar.ButtonData var3) {
      this.key = var1;
      this.text = var2;
      this.buttonData = var3;
   }

   public final ButtonBar.ButtonData getButtonData() {
      return this.buttonData;
   }

   public final String getText() {
      return this.text == null && this.key != null ? ControlResources.getString(this.key) : this.text;
   }

   public String toString() {
      return "ButtonType [text=" + this.getText() + ", buttonData=" + this.getButtonData() + "]";
   }

   static {
      APPLY = new ButtonType("Dialog.apply.button", (String)null, ButtonBar.ButtonData.APPLY);
      OK = new ButtonType("Dialog.ok.button", (String)null, ButtonBar.ButtonData.OK_DONE);
      CANCEL = new ButtonType("Dialog.cancel.button", (String)null, ButtonBar.ButtonData.CANCEL_CLOSE);
      CLOSE = new ButtonType("Dialog.close.button", (String)null, ButtonBar.ButtonData.CANCEL_CLOSE);
      YES = new ButtonType("Dialog.yes.button", (String)null, ButtonBar.ButtonData.YES);
      NO = new ButtonType("Dialog.no.button", (String)null, ButtonBar.ButtonData.NO);
      FINISH = new ButtonType("Dialog.finish.button", (String)null, ButtonBar.ButtonData.FINISH);
      NEXT = new ButtonType("Dialog.next.button", (String)null, ButtonBar.ButtonData.NEXT_FORWARD);
      PREVIOUS = new ButtonType("Dialog.previous.button", (String)null, ButtonBar.ButtonData.BACK_PREVIOUS);
   }
}
