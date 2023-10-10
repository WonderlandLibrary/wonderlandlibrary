package io.netty.util;

import io.netty.util.concurrent.FastThreadLocal;
import java.util.Map;
import java.util.WeakHashMap;

final class Recycler$2 extends FastThreadLocal {
   protected Map initialValue() {
      return new WeakHashMap();
   }

   protected Object initialValue() throws Exception {
      return this.initialValue();
   }
}
