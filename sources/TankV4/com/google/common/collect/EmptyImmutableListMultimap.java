package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(
   serializable = true
)
class EmptyImmutableListMultimap extends ImmutableListMultimap {
   static final EmptyImmutableListMultimap INSTANCE = new EmptyImmutableListMultimap();
   private static final long serialVersionUID = 0L;

   private EmptyImmutableListMultimap() {
      super(ImmutableMap.of(), 0);
   }

   private Object readResolve() {
      return INSTANCE;
   }
}
