package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;

public class DefaultMarshallerProvider implements MarshallerProvider {
   private final MarshallerFactory factory;
   private final MarshallingConfiguration config;

   public DefaultMarshallerProvider(MarshallerFactory var1, MarshallingConfiguration var2) {
      this.factory = var1;
      this.config = var2;
   }

   public Marshaller getMarshaller(ChannelHandlerContext var1) throws Exception {
      return this.factory.createMarshaller(this.config);
   }
}
