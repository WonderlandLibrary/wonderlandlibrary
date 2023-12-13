package io.netty.channel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface ChannelHandler {
   void handlerAdded(ChannelHandlerContext var1) throws Exception;

   void handlerRemoved(ChannelHandlerContext var1) throws Exception;

   /** @deprecated */
   @Deprecated
   void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception;

   @Inherited
   @Documented
   @Target({ElementType.TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   public @interface Sharable {
   }
}
