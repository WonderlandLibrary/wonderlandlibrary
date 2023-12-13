package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

class EventSubscriber {
   private final Object target;
   private final Method method;

   EventSubscriber(Object var1, Method var2) {
      Preconditions.checkNotNull(var1, "EventSubscriber target cannot be null.");
      Preconditions.checkNotNull(var2, "EventSubscriber method cannot be null.");
      this.target = var1;
      this.method = var2;
      var2.setAccessible(true);
   }

   public void handleEvent(Object var1) throws InvocationTargetException {
      Preconditions.checkNotNull(var1);

      try {
         this.method.invoke(this.target, var1);
      } catch (IllegalArgumentException var3) {
         throw new Error("Method rejected target/argument: " + var1, var3);
      } catch (IllegalAccessException var4) {
         throw new Error("Method became inaccessible: " + var1, var4);
      } catch (InvocationTargetException var5) {
         if (var5.getCause() instanceof Error) {
            throw (Error)var5.getCause();
         } else {
            throw var5;
         }
      }
   }

   public String toString() {
      return "[wrapper " + this.method + "]";
   }

   public int hashCode() {
      boolean var1 = true;
      return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof EventSubscriber)) {
         return false;
      } else {
         EventSubscriber var2 = (EventSubscriber)var1;
         return this.target == var2.target && this.method.equals(var2.method);
      }
   }

   public Object getSubscriber() {
      return this.target;
   }

   public Method getMethod() {
      return this.method;
   }
}
