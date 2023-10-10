package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public final class AttributeKey extends UniqueName {
   private static final ConcurrentMap names = PlatformDependent.newConcurrentHashMap();

   public static AttributeKey valueOf(String var0) {
      return new AttributeKey(var0);
   }

   /** @deprecated */
   @Deprecated
   public AttributeKey(String var1) {
      super(names, var1);
   }
}
