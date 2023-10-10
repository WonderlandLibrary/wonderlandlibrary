package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public final class ImmutableClassToInstanceMap extends ForwardingMap implements ClassToInstanceMap, Serializable {
   private final ImmutableMap delegate;

   public static ImmutableClassToInstanceMap.Builder builder() {
      return new ImmutableClassToInstanceMap.Builder();
   }

   public static ImmutableClassToInstanceMap copyOf(Map var0) {
      if (var0 instanceof ImmutableClassToInstanceMap) {
         ImmutableClassToInstanceMap var1 = (ImmutableClassToInstanceMap)var0;
         return var1;
      } else {
         return (new ImmutableClassToInstanceMap.Builder()).putAll(var0).build();
      }
   }

   private ImmutableClassToInstanceMap(ImmutableMap var1) {
      this.delegate = var1;
   }

   protected Map delegate() {
      return this.delegate;
   }

   @Nullable
   public Object getInstance(Class var1) {
      return this.delegate.get(Preconditions.checkNotNull(var1));
   }

   /** @deprecated */
   @Deprecated
   public Object putInstance(Class var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   protected Object delegate() {
      return this.delegate();
   }

   ImmutableClassToInstanceMap(ImmutableMap var1, Object var2) {
      this(var1);
   }

   public static final class Builder {
      private final ImmutableMap.Builder mapBuilder = ImmutableMap.builder();

      public ImmutableClassToInstanceMap.Builder put(Class var1, Object var2) {
         this.mapBuilder.put(var1, var2);
         return this;
      }

      public ImmutableClassToInstanceMap.Builder putAll(Map var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Class var4 = (Class)var3.getKey();
            Object var5 = var3.getValue();
            this.mapBuilder.put(var4, cast(var4, var5));
         }

         return this;
      }

      private static Object cast(Class var0, Object var1) {
         return Primitives.wrap(var0).cast(var1);
      }

      public ImmutableClassToInstanceMap build() {
         return new ImmutableClassToInstanceMap(this.mapBuilder.build());
      }
   }
}
