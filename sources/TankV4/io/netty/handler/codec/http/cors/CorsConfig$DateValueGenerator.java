package io.netty.handler.codec.http.cors;

import java.util.Date;
import java.util.concurrent.Callable;

public final class CorsConfig$DateValueGenerator implements Callable {
   public Date call() throws Exception {
      return new Date();
   }

   public Object call() throws Exception {
      return this.call();
   }
}
