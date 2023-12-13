package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyBinding {
   private KeyCode code;
   private EventType eventType;
   private String action;
   private OptionalBoolean shift;
   private OptionalBoolean ctrl;
   private OptionalBoolean alt;
   private OptionalBoolean meta;

   public KeyBinding(KeyCode var1, String var2) {
      this.eventType = KeyEvent.KEY_PRESSED;
      this.shift = OptionalBoolean.FALSE;
      this.ctrl = OptionalBoolean.FALSE;
      this.alt = OptionalBoolean.FALSE;
      this.meta = OptionalBoolean.FALSE;
      this.code = var1;
      this.action = var2;
   }

   public KeyBinding(KeyCode var1, EventType var2, String var3) {
      this.eventType = KeyEvent.KEY_PRESSED;
      this.shift = OptionalBoolean.FALSE;
      this.ctrl = OptionalBoolean.FALSE;
      this.alt = OptionalBoolean.FALSE;
      this.meta = OptionalBoolean.FALSE;
      this.code = var1;
      this.eventType = var2;
      this.action = var3;
   }

   public KeyBinding shift() {
      return this.shift(OptionalBoolean.TRUE);
   }

   public KeyBinding shift(OptionalBoolean var1) {
      this.shift = var1;
      return this;
   }

   public KeyBinding ctrl() {
      return this.ctrl(OptionalBoolean.TRUE);
   }

   public KeyBinding ctrl(OptionalBoolean var1) {
      this.ctrl = var1;
      return this;
   }

   public KeyBinding alt() {
      return this.alt(OptionalBoolean.TRUE);
   }

   public KeyBinding alt(OptionalBoolean var1) {
      this.alt = var1;
      return this;
   }

   public KeyBinding meta() {
      return this.meta(OptionalBoolean.TRUE);
   }

   public KeyBinding meta(OptionalBoolean var1) {
      this.meta = var1;
      return this;
   }

   public KeyBinding shortcut() {
      if (Toolkit.getToolkit().getClass().getName().endsWith("StubToolkit")) {
         return Utils.isMac() ? this.meta() : this.ctrl();
      } else {
         switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
            case SHIFT:
               return this.shift();
            case CONTROL:
               return this.ctrl();
            case ALT:
               return this.alt();
            case META:
               return this.meta();
            default:
               return this;
         }
      }
   }

   public final KeyCode getCode() {
      return this.code;
   }

   public final EventType getType() {
      return this.eventType;
   }

   public final String getAction() {
      return this.action;
   }

   public final OptionalBoolean getShift() {
      return this.shift;
   }

   public final OptionalBoolean getCtrl() {
      return this.ctrl;
   }

   public final OptionalBoolean getAlt() {
      return this.alt;
   }

   public final OptionalBoolean getMeta() {
      return this.meta;
   }

   public int getSpecificity(Control var1, KeyEvent var2) {
      boolean var3 = false;
      if (this.code != null && this.code != var2.getCode()) {
         return 0;
      } else {
         int var4 = 1;
         if (!this.shift.equals(var2.isShiftDown())) {
            return 0;
         } else {
            if (this.shift != OptionalBoolean.ANY) {
               ++var4;
            }

            if (!this.ctrl.equals(var2.isControlDown())) {
               return 0;
            } else {
               if (this.ctrl != OptionalBoolean.ANY) {
                  ++var4;
               }

               if (!this.alt.equals(var2.isAltDown())) {
                  return 0;
               } else {
                  if (this.alt != OptionalBoolean.ANY) {
                     ++var4;
                  }

                  if (!this.meta.equals(var2.isMetaDown())) {
                     return 0;
                  } else {
                     if (this.meta != OptionalBoolean.ANY) {
                        ++var4;
                     }

                     if (this.eventType != null && this.eventType != var2.getEventType()) {
                        return 0;
                     } else {
                        ++var4;
                        return var4;
                     }
                  }
               }
            }
         }
      }
   }

   public String toString() {
      return "KeyBinding [code=" + this.code + ", shift=" + this.shift + ", ctrl=" + this.ctrl + ", alt=" + this.alt + ", meta=" + this.meta + ", type=" + this.eventType + ", action=" + this.action + "]";
   }
}
