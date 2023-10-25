package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public interface ChannelPipeline extends Iterable<Map.Entry<String, ChannelHandler>> {
  ChannelPipeline addFirst(String paramString, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addFirst(EventExecutorGroup paramEventExecutorGroup, String paramString, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addLast(String paramString, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addLast(EventExecutorGroup paramEventExecutorGroup, String paramString, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addBefore(String paramString1, String paramString2, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addBefore(EventExecutorGroup paramEventExecutorGroup, String paramString1, String paramString2, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addAfter(String paramString1, String paramString2, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addAfter(EventExecutorGroup paramEventExecutorGroup, String paramString1, String paramString2, ChannelHandler paramChannelHandler);
  
  ChannelPipeline addFirst(ChannelHandler... paramVarArgs);
  
  ChannelPipeline addFirst(EventExecutorGroup paramEventExecutorGroup, ChannelHandler... paramVarArgs);
  
  ChannelPipeline addLast(ChannelHandler... paramVarArgs);
  
  ChannelPipeline addLast(EventExecutorGroup paramEventExecutorGroup, ChannelHandler... paramVarArgs);
  
  ChannelPipeline remove(ChannelHandler paramChannelHandler);
  
  ChannelHandler remove(String paramString);
  
  <T extends ChannelHandler> T remove(Class<T> paramClass);
  
  ChannelHandler removeFirst();
  
  ChannelHandler removeLast();
  
  ChannelPipeline replace(ChannelHandler paramChannelHandler1, String paramString, ChannelHandler paramChannelHandler2);
  
  ChannelHandler replace(String paramString1, String paramString2, ChannelHandler paramChannelHandler);
  
  <T extends ChannelHandler> T replace(Class<T> paramClass, String paramString, ChannelHandler paramChannelHandler);
  
  ChannelHandler first();
  
  ChannelHandlerContext firstContext();
  
  ChannelHandler last();
  
  ChannelHandlerContext lastContext();
  
  ChannelHandler get(String paramString);
  
  <T extends ChannelHandler> T get(Class<T> paramClass);
  
  ChannelHandlerContext context(ChannelHandler paramChannelHandler);
  
  ChannelHandlerContext context(String paramString);
  
  ChannelHandlerContext context(Class<? extends ChannelHandler> paramClass);
  
  Channel channel();
  
  List<String> names();
  
  Map<String, ChannelHandler> toMap();
  
  ChannelPipeline fireChannelRegistered();
  
  ChannelPipeline fireChannelUnregistered();
  
  ChannelPipeline fireChannelActive();
  
  ChannelPipeline fireChannelInactive();
  
  ChannelPipeline fireExceptionCaught(Throwable paramThrowable);
  
  ChannelPipeline fireUserEventTriggered(Object paramObject);
  
  ChannelPipeline fireChannelRead(Object paramObject);
  
  ChannelPipeline fireChannelReadComplete();
  
  ChannelPipeline fireChannelWritabilityChanged();
  
  ChannelFuture bind(SocketAddress paramSocketAddress);
  
  ChannelFuture connect(SocketAddress paramSocketAddress);
  
  ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
  
  ChannelFuture disconnect();
  
  ChannelFuture close();
  
  ChannelFuture deregister();
  
  ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise);
  
  ChannelFuture disconnect(ChannelPromise paramChannelPromise);
  
  ChannelFuture close(ChannelPromise paramChannelPromise);
  
  ChannelFuture deregister(ChannelPromise paramChannelPromise);
  
  ChannelPipeline read();
  
  ChannelFuture write(Object paramObject);
  
  ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise);
  
  ChannelPipeline flush();
  
  ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise);
  
  ChannelFuture writeAndFlush(Object paramObject);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelPipeline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */