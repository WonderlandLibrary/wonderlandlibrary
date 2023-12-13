package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Effect;

public class AccessHelper {
   private static StateAccessor theStateAccessor;

   public static void setStateAccessor(StateAccessor var0) {
      if (theStateAccessor != null) {
         throw new InternalError("EffectAccessor already initialized");
      } else {
         theStateAccessor = var0;
      }
   }

   public static Object getState(Effect var0) {
      return var0 == null ? null : theStateAccessor.getState(var0);
   }

   public interface StateAccessor {
      Object getState(Effect var1);
   }
}
