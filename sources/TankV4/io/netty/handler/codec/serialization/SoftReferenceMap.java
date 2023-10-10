package io.netty.handler.codec.serialization;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;

final class SoftReferenceMap extends ReferenceMap {
   public SoftReferenceMap(Map var1) {
      super(var1);
   }

   Reference fold(Object var1) {
      return new SoftReference(var1);
   }
}
