package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public final class DefaultMessageSizeEstimator implements MessageSizeEstimator {
   public static final MessageSizeEstimator DEFAULT = new DefaultMessageSizeEstimator(0);
   private final MessageSizeEstimator.Handle handle;

   public DefaultMessageSizeEstimator(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("unknownSize: " + var1 + " (expected: >= 0)");
      } else {
         this.handle = new DefaultMessageSizeEstimator.HandleImpl(var1);
      }
   }

   public MessageSizeEstimator.Handle newHandle() {
      return this.handle;
   }

   private static final class HandleImpl implements MessageSizeEstimator.Handle {
      private final int unknownSize;

      private HandleImpl(int var1) {
         this.unknownSize = var1;
      }

      public int size(Object var1) {
         if (var1 instanceof ByteBuf) {
            return ((ByteBuf)var1).readableBytes();
         } else if (var1 instanceof ByteBufHolder) {
            return ((ByteBufHolder)var1).content().readableBytes();
         } else {
            return var1 instanceof FileRegion ? 0 : this.unknownSize;
         }
      }

      HandleImpl(int var1, Object var2) {
         this(var1);
      }
   }
}
