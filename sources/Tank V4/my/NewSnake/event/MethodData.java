package my.NewSnake.event;

import java.lang.reflect.Method;

public class MethodData {
   public final byte priority;
   public final Object source;
   public final Method target;

   MethodData(Object var1, Method var2, byte var3) {
      this.source = var1;
      this.target = var2;
      this.priority = var3;
   }
}
