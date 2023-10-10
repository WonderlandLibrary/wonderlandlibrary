package my.NewSnake.event;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public abstract class Event {
   private boolean cancelled;

   private static final void call(Event var0) {
      FlexibleArray var1 = EventManager.get(var0.getClass());
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            MethodData var2 = (MethodData)var3.next();

            try {
               var2.target.invoke(var2.source, var0);
            } catch (IllegalAccessException var5) {
               System.out.println("Can't invoke '" + var2.target.getName() + "' because it's not accessible.");
            } catch (IllegalArgumentException var6) {
               System.out.println("Can't invoke '" + var2.target.getName() + "' because the parameter/s don't match.");
            } catch (InvocationTargetException var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }

   public Event call() {
      this.cancelled = false;
      call(this);
      return this;
   }

   public static enum State {
      PRE("PRE", 0),
      POST("POST", 1);

      private static final Event.State[] ENUM$VALUES = new Event.State[]{PRE, POST};

      private State(String var3, int var4) {
      }
   }
}
