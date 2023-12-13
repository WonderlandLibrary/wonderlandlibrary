package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

@Beta
public final class ImmutableTypeToInstanceMap extends ForwardingMap implements TypeToInstanceMap {
   private final ImmutableMap delegate;

   public static ImmutableTypeToInstanceMap of() {
      return new ImmutableTypeToInstanceMap(ImmutableMap.of());
   }

   public static ImmutableTypeToInstanceMap.Builder builder() {
      return new ImmutableTypeToInstanceMap.Builder();
   }

   private ImmutableTypeToInstanceMap(ImmutableMap var1) {
      this.delegate = var1;
   }

   public Object getInstance(TypeToken var1) {
      return this.trustedGet(var1.rejectTypeVariables());
   }

   public Object putInstance(TypeToken var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public Object getInstance(Class var1) {
      return this.trustedGet(TypeToken.of(var1));
   }

   public Object putInstance(Class var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   protected Map delegate() {
      return this.delegate;
   }

   private Object trustedGet(TypeToken var1) {
      return this.delegate.get(var1);
   }

   protected Object delegate() {
      return this.delegate();
   }

   ImmutableTypeToInstanceMap(ImmutableMap var1, Object var2) {
      this(var1);
   }

   @Beta
   public static final class Builder {
      private final ImmutableMap.Builder mapBuilder;

      private Builder() {
         this.mapBuilder = ImmutableMap.builder();
      }

      public ImmutableTypeToInstanceMap.Builder put(Class var1, Object var2) {
         this.mapBuilder.put(TypeToken.of(var1), var2);
         return this;
      }

      public ImmutableTypeToInstanceMap.Builder put(TypeToken var1, Object var2) {
         this.mapBuilder.put(var1.rejectTypeVariables(), var2);
         return this;
      }

      public ImmutableTypeToInstanceMap build() {
         return new ImmutableTypeToInstanceMap(this.mapBuilder.build());
      }

      Builder(Object var1) {
         this();
      }
   }
}
