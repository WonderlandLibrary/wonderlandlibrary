package io.netty.channel.group;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ConcurrentSet;
import io.netty.util.internal.StringUtil;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultChannelGroup extends AbstractSet<Channel> implements ChannelGroup {
  private static final AtomicInteger nextId = new AtomicInteger();
  
  private final String name;
  
  private final EventExecutor executor;
  
  private final ConcurrentSet<Channel> serverChannels = new ConcurrentSet();
  
  private final ConcurrentSet<Channel> nonServerChannels = new ConcurrentSet();
  
  private final ChannelFutureListener remover = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) throws Exception {
        DefaultChannelGroup.this.remove(future.channel());
      }
    };
  
  public DefaultChannelGroup(EventExecutor executor) {
    this("group-0x" + Integer.toHexString(nextId.incrementAndGet()), executor);
  }
  
  public DefaultChannelGroup(String name, EventExecutor executor) {
    if (name == null)
      throw new NullPointerException("name"); 
    this.name = name;
    this.executor = executor;
  }
  
  public String name() {
    return this.name;
  }
  
  public boolean isEmpty() {
    return (this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty());
  }
  
  public int size() {
    return this.nonServerChannels.size() + this.serverChannels.size();
  }
  
  public boolean contains(Object o) {
    if (o instanceof Channel) {
      Channel c = (Channel)o;
      if (o instanceof io.netty.channel.ServerChannel)
        return this.serverChannels.contains(c); 
      return this.nonServerChannels.contains(c);
    } 
    return false;
  }
  
  public boolean add(Channel channel) {
    ConcurrentSet<Channel> set = (channel instanceof io.netty.channel.ServerChannel) ? this.serverChannels : this.nonServerChannels;
    boolean added = set.add(channel);
    if (added)
      channel.closeFuture().addListener((GenericFutureListener)this.remover); 
    return added;
  }
  
  public boolean remove(Object o) {
    boolean removed;
    if (!(o instanceof Channel))
      return false; 
    Channel c = (Channel)o;
    if (c instanceof io.netty.channel.ServerChannel) {
      removed = this.serverChannels.remove(c);
    } else {
      removed = this.nonServerChannels.remove(c);
    } 
    if (!removed)
      return false; 
    c.closeFuture().removeListener((GenericFutureListener)this.remover);
    return true;
  }
  
  public void clear() {
    this.nonServerChannels.clear();
    this.serverChannels.clear();
  }
  
  public Iterator<Channel> iterator() {
    return new CombinedIterator<Channel>(this.serverChannels.iterator(), this.nonServerChannels.iterator());
  }
  
  public Object[] toArray() {
    Collection<Channel> channels = new ArrayList<Channel>(size());
    channels.addAll((Collection<? extends Channel>)this.serverChannels);
    channels.addAll((Collection<? extends Channel>)this.nonServerChannels);
    return channels.toArray();
  }
  
  public <T> T[] toArray(T[] a) {
    Collection<Channel> channels = new ArrayList<Channel>(size());
    channels.addAll((Collection<? extends Channel>)this.serverChannels);
    channels.addAll((Collection<? extends Channel>)this.nonServerChannels);
    return channels.toArray(a);
  }
  
  public ChannelGroupFuture close() {
    return close(ChannelMatchers.all());
  }
  
  public ChannelGroupFuture disconnect() {
    return disconnect(ChannelMatchers.all());
  }
  
  public ChannelGroupFuture deregister() {
    return deregister(ChannelMatchers.all());
  }
  
  public ChannelGroupFuture write(Object message) {
    return write(message, ChannelMatchers.all());
  }
  
  private static Object safeDuplicate(Object message) {
    if (message instanceof ByteBuf)
      return ((ByteBuf)message).duplicate().retain(); 
    if (message instanceof ByteBufHolder)
      return ((ByteBufHolder)message).duplicate().retain(); 
    return ReferenceCountUtil.retain(message);
  }
  
  public ChannelGroupFuture write(Object message, ChannelMatcher matcher) {
    if (message == null)
      throw new NullPointerException("message"); 
    if (matcher == null)
      throw new NullPointerException("matcher"); 
    Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
    for (Channel c : this.nonServerChannels) {
      if (matcher.matches(c))
        futures.put(c, c.write(safeDuplicate(message))); 
    } 
    ReferenceCountUtil.release(message);
    return new DefaultChannelGroupFuture(this, futures, this.executor);
  }
  
  public ChannelGroup flush() {
    return flush(ChannelMatchers.all());
  }
  
  public ChannelGroupFuture flushAndWrite(Object message) {
    return writeAndFlush(message);
  }
  
  public ChannelGroupFuture writeAndFlush(Object message) {
    return writeAndFlush(message, ChannelMatchers.all());
  }
  
  public ChannelGroupFuture disconnect(ChannelMatcher matcher) {
    if (matcher == null)
      throw new NullPointerException("matcher"); 
    Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
    for (Channel c : this.serverChannels) {
      if (matcher.matches(c))
        futures.put(c, c.disconnect()); 
    } 
    for (Channel c : this.nonServerChannels) {
      if (matcher.matches(c))
        futures.put(c, c.disconnect()); 
    } 
    return new DefaultChannelGroupFuture(this, futures, this.executor);
  }
  
  public ChannelGroupFuture close(ChannelMatcher matcher) {
    if (matcher == null)
      throw new NullPointerException("matcher"); 
    Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
    for (Channel c : this.serverChannels) {
      if (matcher.matches(c))
        futures.put(c, c.close()); 
    } 
    for (Channel c : this.nonServerChannels) {
      if (matcher.matches(c))
        futures.put(c, c.close()); 
    } 
    return new DefaultChannelGroupFuture(this, futures, this.executor);
  }
  
  public ChannelGroupFuture deregister(ChannelMatcher matcher) {
    if (matcher == null)
      throw new NullPointerException("matcher"); 
    Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
    for (Channel c : this.serverChannels) {
      if (matcher.matches(c))
        futures.put(c, c.deregister()); 
    } 
    for (Channel c : this.nonServerChannels) {
      if (matcher.matches(c))
        futures.put(c, c.deregister()); 
    } 
    return new DefaultChannelGroupFuture(this, futures, this.executor);
  }
  
  public ChannelGroup flush(ChannelMatcher matcher) {
    for (Channel c : this.nonServerChannels) {
      if (matcher.matches(c))
        c.flush(); 
    } 
    return this;
  }
  
  public ChannelGroupFuture flushAndWrite(Object message, ChannelMatcher matcher) {
    return writeAndFlush(message, matcher);
  }
  
  public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher) {
    if (message == null)
      throw new NullPointerException("message"); 
    Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
    for (Channel c : this.nonServerChannels) {
      if (matcher.matches(c))
        futures.put(c, c.writeAndFlush(safeDuplicate(message))); 
    } 
    ReferenceCountUtil.release(message);
    return new DefaultChannelGroupFuture(this, futures, this.executor);
  }
  
  public int hashCode() {
    return System.identityHashCode(this);
  }
  
  public boolean equals(Object o) {
    return (this == o);
  }
  
  public int compareTo(ChannelGroup o) {
    int v = name().compareTo(o.name());
    if (v != 0)
      return v; 
    return System.identityHashCode(this) - System.identityHashCode(o);
  }
  
  public String toString() {
    return StringUtil.simpleClassName(this) + "(name: " + name() + ", size: " + size() + ')';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\group\DefaultChannelGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */