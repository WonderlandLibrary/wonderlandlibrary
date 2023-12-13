package io.netty.handler.codec.serialization;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;

final class WeakReferenceMap extends ReferenceMap {
   public WeakReferenceMap(Map var1) {
      super(var1);
   }

   Reference fold(Object var1) {
      return new WeakReference(var1);
   }
}
