package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;

public abstract class AbstractServerChannel extends AbstractChannel implements ServerChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);

   protected AbstractServerChannel() {
      super((Channel)null);
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public SocketAddress remoteAddress() {
      return null;
   }

   protected SocketAddress remoteAddress0() {
      return null;
   }

   protected void doDisconnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new AbstractServerChannel.DefaultServerUnsafe(this);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   private final class DefaultServerUnsafe extends AbstractChannel.AbstractUnsafe {
      final AbstractServerChannel this$0;

      private DefaultServerUnsafe(AbstractServerChannel var1) {
         super();
         this.this$0 = var1;
      }

      public void write(Object var1, ChannelPromise var2) {
         ReferenceCountUtil.release(var1);
         this.reject(var2);
      }

      public void flush() {
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         this.reject(var3);
      }

      private void reject(ChannelPromise var1) {
         var1.setFailure(new UnsupportedOperationException());
      }

      DefaultServerUnsafe(AbstractServerChannel var1, Object var2) {
         this(var1);
      }
   }
}
