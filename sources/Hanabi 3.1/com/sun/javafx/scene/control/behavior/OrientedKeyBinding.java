package com.sun.javafx.scene.control.behavior;

import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class OrientedKeyBinding extends KeyBinding {
   private OptionalBoolean vertical;

   public OrientedKeyBinding(KeyCode var1, String var2) {
      super(var1, var2);
      this.vertical = OptionalBoolean.FALSE;
   }

   public OrientedKeyBinding(KeyCode var1, EventType var2, String var3) {
      super(var1, var2, var3);
      this.vertical = OptionalBoolean.FALSE;
   }

   public OrientedKeyBinding vertical() {
      this.vertical = OptionalBoolean.TRUE;
      return this;
   }

   protected abstract boolean getVertical(Control var1);

   public int getSpecificity(Control var1, KeyEvent var2) {
      boolean var3 = this.getVertical(var1);
      if (!this.vertical.equals(var3)) {
         return 0;
      } else {
         int var4 = super.getSpecificity(var1, var2);
         if (var4 == 0) {
            return 0;
         } else {
            return this.vertical != OptionalBoolean.ANY ? var4 + 1 : var4;
         }
      }
   }

   public String toString() {
      return "OrientedKeyBinding [code=" + this.getCode() + ", shift=" + this.getShift() + ", ctrl=" + this.getCtrl() + ", alt=" + this.getAlt() + ", meta=" + this.getMeta() + ", type=" + this.getType() + ", vertical=" + this.vertical + ", action=" + this.getAction() + "]";
   }
}
