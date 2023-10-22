package javafx.scene.paint;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.tk.Toolkit;

public abstract class Paint {
   Paint() {
   }

   boolean acc_isMutable() {
      return false;
   }

   abstract Object acc_getPlatformPaint();

   void acc_addListener(AbstractNotifyListener var1) {
      throw new UnsupportedOperationException("Not Supported.");
   }

   void acc_removeListener(AbstractNotifyListener var1) {
      throw new UnsupportedOperationException("Not Supported.");
   }

   public abstract boolean isOpaque();

   public static Paint valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException("paint must be specified");
      } else if (var0.startsWith("linear-gradient(")) {
         return LinearGradient.valueOf(var0);
      } else {
         return (Paint)(var0.startsWith("radial-gradient(") ? RadialGradient.valueOf(var0) : Color.valueOf(var0));
      }
   }

   static {
      Toolkit.setPaintAccessor(new Toolkit.PaintAccessor() {
         public boolean isMutable(Paint var1) {
            return var1.acc_isMutable();
         }

         public Object getPlatformPaint(Paint var1) {
            return var1.acc_getPlatformPaint();
         }

         public void addListener(Paint var1, AbstractNotifyListener var2) {
            var1.acc_addListener(var2);
         }

         public void removeListener(Paint var1, AbstractNotifyListener var2) {
            var1.acc_removeListener(var2);
         }
      });
   }
}
