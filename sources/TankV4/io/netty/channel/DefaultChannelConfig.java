package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.nio.AbstractNioByteChannel;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultChannelConfig implements ChannelConfig {
   private static final ByteBufAllocator DEFAULT_ALLOCATOR;
   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR;
   private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR;
   private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
   protected final Channel channel;
   private volatile ByteBufAllocator allocator;
   private volatile RecvByteBufAllocator rcvBufAllocator;
   private volatile MessageSizeEstimator msgSizeEstimator;
   private volatile int connectTimeoutMillis;
   private volatile int maxMessagesPerRead;
   private volatile int writeSpinCount;
   private volatile boolean autoRead;
   private volatile boolean autoClose;
   private volatile int writeBufferHighWaterMark;
   private volatile int writeBufferLowWaterMark;

   public DefaultChannelConfig(Channel var1) {
      this.allocator = DEFAULT_ALLOCATOR;
      this.rcvBufAllocator = DEFAULT_RCVBUF_ALLOCATOR;
      this.msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
      this.connectTimeoutMillis = 30000;
      this.writeSpinCount = 16;
      this.autoRead = true;
      this.autoClose = true;
      this.writeBufferHighWaterMark = 65536;
      this.writeBufferLowWaterMark = 32768;
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else {
         this.channel = var1;
         if (!(var1 instanceof ServerChannel) && !(var1 instanceof AbstractNioByteChannel)) {
            this.maxMessagesPerRead = 1;
         } else {
            this.maxMessagesPerRead = 16;
         }

      }
   }

   public Map getOptions() {
      return this.getOptions((Map)null, ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR);
   }

   protected Map getOptions(Map var1, ChannelOption... var2) {
      if (var1 == null) {
         var1 = new IdentityHashMap();
      }

      ChannelOption[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ChannelOption var6 = var3[var5];
         ((Map)var1).put(var6, this.getOption(var6));
      }

      return (Map)var1;
   }

   public boolean setOptions(Map var1) {
      if (var1 == null) {
         throw new NullPointerException("options");
      } else {
         boolean var2 = true;
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if ((ChannelOption)var4.getKey() == var4.getValue()) {
               var2 = false;
            }
         }

         return var2;
      }
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == null) {
         throw new NullPointerException("option");
      } else if (var1 == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
         return this.getConnectTimeoutMillis();
      } else if (var1 == ChannelOption.MAX_MESSAGES_PER_READ) {
         return this.getMaxMessagesPerRead();
      } else if (var1 == ChannelOption.WRITE_SPIN_COUNT) {
         return this.getWriteSpinCount();
      } else if (var1 == ChannelOption.ALLOCATOR) {
         return this.getAllocator();
      } else if (var1 == ChannelOption.RCVBUF_ALLOCATOR) {
         return this.getRecvByteBufAllocator();
      } else if (var1 == ChannelOption.AUTO_READ) {
         return this.isAutoRead();
      } else if (var1 == ChannelOption.AUTO_CLOSE) {
         return this.isAutoClose();
      } else if (var1 == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
         return this.getWriteBufferHighWaterMark();
      } else if (var1 == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
         return this.getWriteBufferLowWaterMark();
      } else {
         return var1 == ChannelOption.MESSAGE_SIZE_ESTIMATOR ? this.getMessageSizeEstimator() : null;
      }
   }

   protected void validate(ChannelOption var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("option");
      } else {
         var1.validate(var2);
      }
   }

   public int getConnectTimeoutMillis() {
      return this.connectTimeoutMillis;
   }

   public ChannelConfig setConnectTimeoutMillis(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", var1));
      } else {
         this.connectTimeoutMillis = var1;
         return this;
      }
   }

   public int getMaxMessagesPerRead() {
      return this.maxMessagesPerRead;
   }

   public ChannelConfig setMaxMessagesPerRead(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxMessagesPerRead: " + var1 + " (expected: > 0)");
      } else {
         this.maxMessagesPerRead = var1;
         return this;
      }
   }

   public int getWriteSpinCount() {
      return this.writeSpinCount;
   }

   public ChannelConfig setWriteSpinCount(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
      } else {
         this.writeSpinCount = var1;
         return this;
      }
   }

   public ByteBufAllocator getAllocator() {
      return this.allocator;
   }

   public ChannelConfig setAllocator(ByteBufAllocator var1) {
      if (var1 == null) {
         throw new NullPointerException("allocator");
      } else {
         this.allocator = var1;
         return this;
      }
   }

   public RecvByteBufAllocator getRecvByteBufAllocator() {
      return this.rcvBufAllocator;
   }

   public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      if (var1 == null) {
         throw new NullPointerException("allocator");
      } else {
         this.rcvBufAllocator = var1;
         return this;
      }
   }

   public boolean isAutoRead() {
      return this.autoRead;
   }

   public ChannelConfig setAutoRead(boolean var1) {
      boolean var2 = this.autoRead;
      this.autoRead = var1;
      if (var1 && !var2) {
         this.channel.read();
      }

      return this;
   }

   public boolean isAutoClose() {
      return this.autoClose;
   }

   public ChannelConfig setAutoClose(boolean var1) {
      this.autoClose = var1;
      return this;
   }

   public int getWriteBufferHighWaterMark() {
      return this.writeBufferHighWaterMark;
   }

   public ChannelConfig setWriteBufferHighWaterMark(int var1) {
      if (var1 < this.getWriteBufferLowWaterMark()) {
         throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + this.getWriteBufferLowWaterMark() + "): " + var1);
      } else if (var1 < 0) {
         throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
      } else {
         this.writeBufferHighWaterMark = var1;
         return this;
      }
   }

   public int getWriteBufferLowWaterMark() {
      return this.writeBufferLowWaterMark;
   }

   public ChannelConfig setWriteBufferLowWaterMark(int var1) {
      if (var1 > this.getWriteBufferHighWaterMark()) {
         throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + this.getWriteBufferHighWaterMark() + "): " + var1);
      } else if (var1 < 0) {
         throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
      } else {
         this.writeBufferLowWaterMark = var1;
         return this;
      }
   }

   public MessageSizeEstimator getMessageSizeEstimator() {
      return this.msgSizeEstimator;
   }

   public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      if (var1 == null) {
         throw new NullPointerException("estimator");
      } else {
         this.msgSizeEstimator = var1;
         return this;
      }
   }

   static {
      DEFAULT_ALLOCATOR = UnpooledByteBufAllocator.DEFAULT;
      DEFAULT_RCVBUF_ALLOCATOR = AdaptiveRecvByteBufAllocator.DEFAULT;
      DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
   }
}
