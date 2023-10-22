package javafx.animation;

import javafx.beans.NamedArg;
import javafx.beans.value.WritableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableNumberValue;
import javafx.beans.value.WritableValue;

public final class KeyValue {
   private static final Interpolator DEFAULT_INTERPOLATOR;
   private final Type type;
   private final WritableValue target;
   private final Object endValue;
   private final Interpolator interpolator;

   /** @deprecated */
   @Deprecated
   public Type getType() {
      return this.type;
   }

   public WritableValue getTarget() {
      return this.target;
   }

   public Object getEndValue() {
      return this.endValue;
   }

   public Interpolator getInterpolator() {
      return this.interpolator;
   }

   public KeyValue(@NamedArg("target") WritableValue var1, @NamedArg("endValue") Object var2, @NamedArg("interpolator") Interpolator var3) {
      if (var1 == null) {
         throw new NullPointerException("Target needs to be specified");
      } else if (var3 == null) {
         throw new NullPointerException("Interpolator needs to be specified");
      } else {
         this.target = var1;
         this.endValue = var2;
         this.interpolator = var3;
         this.type = var1 instanceof WritableNumberValue ? (var1 instanceof WritableDoubleValue ? KeyValue.Type.DOUBLE : (var1 instanceof WritableIntegerValue ? KeyValue.Type.INTEGER : (var1 instanceof WritableFloatValue ? KeyValue.Type.FLOAT : (var1 instanceof WritableLongValue ? KeyValue.Type.LONG : KeyValue.Type.OBJECT)))) : (var1 instanceof WritableBooleanValue ? KeyValue.Type.BOOLEAN : KeyValue.Type.OBJECT);
      }
   }

   public KeyValue(@NamedArg("target") WritableValue var1, @NamedArg("endValue") Object var2) {
      this(var1, var2, DEFAULT_INTERPOLATOR);
   }

   public String toString() {
      return "KeyValue [target=" + this.target + ", endValue=" + this.endValue + ", interpolator=" + this.interpolator + "]";
   }

   public int hashCode() {
      assert this.target != null && this.interpolator != null;

      int var2 = 1;
      var2 = 31 * var2 + this.target.hashCode();
      var2 = 31 * var2 + (this.endValue == null ? 0 : this.endValue.hashCode());
      var2 = 31 * var2 + this.interpolator.hashCode();
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof KeyValue)) {
         return false;
      } else {
         KeyValue var2 = (KeyValue)var1;

         assert this.target != null && this.interpolator != null && var2.target != null && var2.interpolator != null;

         boolean var10000;
         if (this.target.equals(var2.target)) {
            label30: {
               if (this.endValue == null) {
                  if (var2.endValue != null) {
                     break label30;
                  }
               } else if (!this.endValue.equals(var2.endValue)) {
                  break label30;
               }

               if (this.interpolator.equals(var2.interpolator)) {
                  var10000 = true;
                  return var10000;
               }
            }
         }

         var10000 = false;
         return var10000;
      }
   }

   static {
      DEFAULT_INTERPOLATOR = Interpolator.LINEAR;
   }

   /** @deprecated */
   @Deprecated
   public static enum Type {
      BOOLEAN,
      DOUBLE,
      FLOAT,
      INTEGER,
      LONG,
      OBJECT;
   }
}
