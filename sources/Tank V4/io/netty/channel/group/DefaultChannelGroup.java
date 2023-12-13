package io.netty.channel.group;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ServerChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ConcurrentSet;
import io.netty.util.internal.StringUtil;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultChannelGroup extends AbstractSet implements ChannelGroup {
   private static final AtomicInteger nextId = new AtomicInteger();
   private final String name;
   private final EventExecutor executor;
   private final ConcurrentSet serverChannels;
   private final ConcurrentSet nonServerChannels;
   private final ChannelFutureListener remover;

   public DefaultChannelGroup(EventExecutor var1) {
      this("group-0x" + Integer.toHexString(nextId.incrementAndGet()), var1);
   }

   public DefaultChannelGroup(String var1, EventExecutor var2) {
      this.serverChannels = new ConcurrentSet();
      this.nonServerChannels = new ConcurrentSet();
      this.remover = new ChannelFutureListener(this) {
         final DefaultChannelGroup this$0;

         {
            this.this$0 = var1;
         }

         public void operationComplete(ChannelFuture var1) throws Exception {
            this.this$0.remove(var1.channel());
         }

         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      };
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         this.name = var1;
         this.executor = var2;
      }
   }

   public String name() {
      return this.name;
   }

   public boolean isEmpty() {
      return this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty();
   }

   public int size() {
      return this.nonServerChannels.size() + this.serverChannels.size();
   }

   public boolean contains(Object var1) {
      if (var1 instanceof Channel) {
         Channel var2 = (Channel)var1;
         return var1 instanceof ServerChannel ? this.serverChannels.contains(var2) : this.nonServerChannels.contains(var2);
      } else {
         return false;
      }
   }

   public boolean add(Channel var1) {
      ConcurrentSet var2 = var1 instanceof ServerChannel ? this.serverChannels : this.nonServerChannels;
      boolean var3 = var2.add(var1);
      if (var3) {
         var1.closeFuture().addListener(this.remover);
      }

      return var3;
   }

   public boolean remove(Object var1) {
      if (!(var1 instanceof Channel)) {
         return false;
      } else {
         Channel var3 = (Channel)var1;
         boolean var2;
         if (var3 instanceof ServerChannel) {
            var2 = this.serverChannels.remove(var3);
         } else {
            var2 = this.nonServerChannels.remove(var3);
         }

         if (!var2) {
            return false;
         } else {
            var3.closeFuture().removeListener(this.remover);
            return true;
         }
      }
   }

   public void clear() {
      this.nonServerChannels.clear();
      this.serverChannels.clear();
   }

   public Iterator iterator() {
      return new CombinedIterator(this.serverChannels.iterator(), this.nonServerChannels.iterator());
   }

   public Object[] toArray() {
      ArrayList var1 = new ArrayList(this.size());
      var1.addAll(this.serverChannels);
      var1.addAll(this.nonServerChannels);
      return var1.toArray();
   }

   public Object[] toArray(Object[] var1) {
      ArrayList var2 = new ArrayList(this.size());
      var2.addAll(this.serverChannels);
      var2.addAll(this.nonServerChannels);
      return var2.toArray(var1);
   }

   public ChannelGroupFuture close() {
      return this.close(ChannelMatchers.all());
   }

   public ChannelGroupFuture disconnect() {
      return this.disconnect(ChannelMatchers.all());
   }

   /** @deprecated */
   @Deprecated
   public ChannelGroupFuture deregister() {
      return this.deregister(ChannelMatchers.all());
   }

   public ChannelGroupFuture write(Object var1) {
      return this.write(var1, ChannelMatchers.all());
   }

   private static Object safeDuplicate(Object var0) {
      if (var0 instanceof ByteBuf) {
         return ((ByteBuf)var0).duplicate().retain();
      } else {
         return var0 instanceof ByteBufHolder ? ((ByteBufHolder)var0).duplicate().retain() : ReferenceCountUtil.retain(var0);
      }
   }

   public ChannelGroupFuture write(Object var1, ChannelMatcher var2) {
      if (var1 == null) {
         throw new NullPointerException("message");
      } else if (var2 == null) {
         throw new NullPointerException("matcher");
      } else {
         LinkedHashMap var3 = new LinkedHashMap(this.size());
         Iterator var4 = this.nonServerChannels.iterator();

         while(var4.hasNext()) {
            Channel var5 = (Channel)var4.next();
            if (var2.matches(var5)) {
               var3.put(var5, var5.write(safeDuplicate(var1)));
            }
         }

         ReferenceCountUtil.release(var1);
         return new DefaultChannelGroupFuture(this, var3, this.executor);
      }
   }

   public ChannelGroup flush() {
      return this.flush(ChannelMatchers.all());
   }

   public ChannelGroupFuture flushAndWrite(Object var1) {
      return this.writeAndFlush(var1);
   }

   public ChannelGroupFuture writeAndFlush(Object var1) {
      return this.writeAndFlush(var1, ChannelMatchers.all());
   }

   public ChannelGroupFuture disconnect(ChannelMatcher var1) {
      if (var1 == null) {
         throw new NullPointerException("matcher");
      } else {
         LinkedHashMap var2 = new LinkedHashMap(this.size());
         Iterator var3 = this.serverChannels.iterator();

         Channel var4;
         while(var3.hasNext()) {
            var4 = (Channel)var3.next();
            if (var1.matches(var4)) {
               var2.put(var4, var4.disconnect());
            }
         }

         var3 = this.nonServerChannels.iterator();

         while(var3.hasNext()) {
            var4 = (Channel)var3.next();
            if (var1.matches(var4)) {
               var2.put(var4, var4.disconnect());
            }
         }

         return new DefaultChannelGroupFuture(this, var2, this.executor);
      }
   }

   public ChannelGroupFuture close(ChannelMatcher var1) {
      if (var1 == null) {
         throw new NullPointerException("matcher");
      } else {
         LinkedHashMap var2 = new LinkedHashMap(this.size());
         Iterator var3 = this.serverChannels.iterator();

         Channel var4;
         while(var3.hasNext()) {
            var4 = (Channel)var3.next();
            if (var1.matches(var4)) {
               var2.put(var4, var4.close());
            }
         }

         var3 = this.nonServerChannels.iterator();

         while(var3.hasNext()) {
            var4 = (Channel)var3.next();
            if (var1.matches(var4)) {
               var2.put(var4, var4.close());
            }
         }

         return new DefaultChannelGroupFuture(this, var2, this.executor);
      }
   }

   /** @deprecated */
   @Deprecated
   public ChannelGroupFuture deregister(ChannelMatcher var1) {
      if (var1 == null) {
         throw new NullPointerException("matcher");
      } else {
         LinkedHashMap var2 = new LinkedHashMap(this.size());
         Iterator var3 = this.serverChannels.iterator();

         Channel var4;
         while(var3.hasNext()) {
            var4 = (Channel)var3.next();
            if (var1.matches(var4)) {
               var2.put(var4, var4.deregister());
            }
         }

         var3 = this.nonServerChannels.iterator();

         while(var3.hasNext()) {
            var4 = (Channel)var3.next();
            if (var1.matches(var4)) {
               var2.put(var4, var4.deregister());
            }
         }

         return new DefaultChannelGroupFuture(this, var2, this.executor);
      }
   }

   public ChannelGroup flush(ChannelMatcher var1) {
      Iterator var2 = this.nonServerChannels.iterator();

      while(var2.hasNext()) {
         Channel var3 = (Channel)var2.next();
         if (var1.matches(var3)) {
            var3.flush();
         }
      }

      return this;
   }

   public ChannelGroupFuture flushAndWrite(Object var1, ChannelMatcher var2) {
      return this.writeAndFlush(var1, var2);
   }

   public ChannelGroupFuture writeAndFlush(Object var1, ChannelMatcher var2) {
      if (var1 == null) {
         throw new NullPointerException("message");
      } else {
         LinkedHashMap var3 = new LinkedHashMap(this.size());
         Iterator var4 = this.nonServerChannels.iterator();

         while(var4.hasNext()) {
            Channel var5 = (Channel)var4.next();
            if (var2.matches(var5)) {
               var3.put(var5, var5.writeAndFlush(safeDuplicate(var1)));
            }
         }

         ReferenceCountUtil.release(var1);
         return new DefaultChannelGroupFuture(this, var3, this.executor);
      }
   }

   public int hashCode() {
      return System.identityHashCode(this);
   }

   public boolean equals(Object var1) {
      return this == var1;
   }

   public int compareTo(ChannelGroup var1) {
      int var2 = this.name().compareTo(var1.name());
      return var2 != 0 ? var2 : System.identityHashCode(this) - System.identityHashCode(var1);
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "(name: " + this.name() + ", size: " + this.size() + ')';
   }

   public boolean add(Object var1) {
      return this.add((Channel)var1);
   }

   public int compareTo(Object var1) {
      return this.compareTo((ChannelGroup)var1);
   }
}
