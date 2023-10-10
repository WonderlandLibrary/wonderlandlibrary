package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Beta
public final class FakeTimeLimiter implements TimeLimiter {
   public Object newProxy(Object var1, Class var2, long var3, TimeUnit var5) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var5);
      return var1;
   }

   public Object callWithTimeout(Callable var1, long var2, TimeUnit var4, boolean var5) throws Exception {
      Preconditions.checkNotNull(var4);
      return var1.call();
   }
}
