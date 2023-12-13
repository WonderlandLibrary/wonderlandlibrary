package javafx.scene.input;

import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class KeyEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType KEY_PRESSED;
   public static final EventType KEY_RELEASED;
   public static final EventType KEY_TYPED;
   public static final String CHAR_UNDEFINED;
   private final String character;
   private final String text;
   private final KeyCode code;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;
   private final boolean metaDown;

   public KeyEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("character") String var4, @NamedArg("text") String var5, @NamedArg("code") KeyCode var6, @NamedArg("shiftDown") boolean var7, @NamedArg("controlDown") boolean var8, @NamedArg("altDown") boolean var9, @NamedArg("metaDown") boolean var10) {
      super(var1, var2, var3);
      boolean var11 = var3 == KEY_TYPED;
      this.character = var11 ? var4 : CHAR_UNDEFINED;
      this.text = var11 ? "" : var5;
      this.code = var11 ? KeyCode.UNDEFINED : var6;
      this.shiftDown = var7;
      this.controlDown = var8;
      this.altDown = var9;
      this.metaDown = var10;
   }

   public KeyEvent(@NamedArg("eventType") EventType var1, @NamedArg("character") String var2, @NamedArg("text") String var3, @NamedArg("code") KeyCode var4, @NamedArg("shiftDown") boolean var5, @NamedArg("controlDown") boolean var6, @NamedArg("altDown") boolean var7, @NamedArg("metaDown") boolean var8) {
      super(var1);
      boolean var9 = var1 == KEY_TYPED;
      this.character = var9 ? var2 : CHAR_UNDEFINED;
      this.text = var9 ? "" : var3;
      this.code = var9 ? KeyCode.UNDEFINED : var4;
      this.shiftDown = var5;
      this.controlDown = var6;
      this.altDown = var7;
      this.metaDown = var8;
   }

   public final String getCharacter() {
      return this.character;
   }

   public final String getText() {
      return this.text;
   }

   public final KeyCode getCode() {
      return this.code;
   }

   public final boolean isShiftDown() {
      return this.shiftDown;
   }

   public final boolean isControlDown() {
      return this.controlDown;
   }

   public final boolean isAltDown() {
      return this.altDown;
   }

   public final boolean isMetaDown() {
      return this.metaDown;
   }

   public final boolean isShortcutDown() {
      switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
         case SHIFT:
            return this.shiftDown;
         case CONTROL:
            return this.controlDown;
         case ALT:
            return this.altDown;
         case META:
            return this.metaDown;
         default:
            return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("KeyEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", character = ").append(this.getCharacter());
      var1.append(", text = ").append(this.getText());
      var1.append(", code = ").append(this.getCode());
      if (this.isShiftDown()) {
         var1.append(", shiftDown");
      }

      if (this.isControlDown()) {
         var1.append(", controlDown");
      }

      if (this.isAltDown()) {
         var1.append(", altDown");
      }

      if (this.isMetaDown()) {
         var1.append(", metaDown");
      }

      if (this.isShortcutDown()) {
         var1.append(", shortcutDown");
      }

      return var1.append("]").toString();
   }

   public KeyEvent copyFor(Object var1, EventTarget var2) {
      return (KeyEvent)super.copyFor(var1, var2);
   }

   public KeyEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      KeyEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(InputEvent.ANY, "KEY");
      KEY_PRESSED = new EventType(ANY, "KEY_PRESSED");
      KEY_RELEASED = new EventType(ANY, "KEY_RELEASED");
      KEY_TYPED = new EventType(ANY, "KEY_TYPED");
      FXRobotHelper.FXRobotInputAccessor var0 = new FXRobotHelper.FXRobotInputAccessor() {
         public int getCodeForKeyCode(KeyCode var1) {
            return var1.code;
         }

         public KeyCode getKeyCodeForCode(int var1) {
            return KeyCodeMap.valueOf(var1);
         }

         public KeyEvent createKeyEvent(EventType var1, KeyCode var2, String var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8) {
            return new KeyEvent(var1, var3, var4, var2, var5, var6, var7, var8);
         }

         public MouseEvent createMouseEvent(EventType var1, int var2, int var3, int var4, int var5, MouseButton var6, int var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15) {
            return new MouseEvent(var1, (double)var2, (double)var3, (double)var4, (double)var5, var6, var7, var8, var9, var10, var11, var13, var14, var15, false, var12, false, (PickResult)null);
         }

         public ScrollEvent createScrollEvent(EventType var1, int var2, int var3, ScrollEvent.HorizontalTextScrollUnits var4, int var5, ScrollEvent.VerticalTextScrollUnits var6, int var7, int var8, int var9, int var10, int var11, boolean var12, boolean var13, boolean var14, boolean var15) {
            return new ScrollEvent(ScrollEvent.SCROLL, (double)var8, (double)var9, (double)var10, (double)var11, var12, var13, var14, var15, false, false, (double)var2, (double)var3, 0.0, 0.0, var4, (double)var5, var6, (double)var7, 0, (PickResult)null);
         }
      };
      FXRobotHelper.setInputAccessor(var0);
      CHAR_UNDEFINED = KeyCode.UNDEFINED.ch;
   }
}
