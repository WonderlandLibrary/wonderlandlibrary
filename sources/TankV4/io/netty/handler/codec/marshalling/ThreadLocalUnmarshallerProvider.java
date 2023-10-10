package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

public class ThreadLocalUnmarshallerProvider implements UnmarshallerProvider {
   private final ThreadLocal unmarshallers = new ThreadLocal();
   private final MarshallerFactory factory;
   private final MarshallingConfiguration config;

   public ThreadLocalUnmarshallerProvider(MarshallerFactory var1, MarshallingConfiguration var2) {
      this.factory = var1;
      this.config = var2;
   }

   public Unmarshaller getUnmarshaller(ChannelHandlerContext var1) throws Exception {
      Unmarshaller var2 = (Unmarshaller)this.unmarshallers.get();
      if (var2 == null) {
         var2 = this.factory.createUnmarshaller(this.config);
         this.unmarshallers.set(var2);
      }

      return var2;
   }
}
