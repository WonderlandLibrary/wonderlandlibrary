/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ConnectTimeoutException
 *  io.netty.channel.SimpleChannelInboundHandler
 *  io.netty.handler.timeout.ReadTimeoutException
 *  io.netty.handler.timeout.ReadTimeoutHandler
 *  io.netty.handler.timeout.WriteTimeoutException
 *  io.netty.handler.timeout.WriteTimeoutHandler
 *  io.netty.util.concurrent.GenericFutureListener
 */
package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.event.session.SessionEvent;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class TcpSession
extends SimpleChannelInboundHandler<Packet>
implements Session {
    private String host;
    private int port;
    private PacketProtocol protocol;
    private int compressionThreshold = -1;
    private int connectTimeout = 30;
    private int readTimeout = 30;
    private int writeTimeout = 0;
    private Map<String, Object> flags = new HashMap<String, Object>();
    private List<SessionListener> listeners = new CopyOnWriteArrayList<SessionListener>();
    private Channel channel;
    protected boolean disconnected = false;
    private BlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();
    private Thread packetHandleThread;

    public TcpSession(String host, int port, PacketProtocol protocol) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    @Override
    public void connect() {
        this.connect(true);
    }

    @Override
    public void connect(boolean wait) {
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public SocketAddress getLocalAddress() {
        return this.channel != null ? this.channel.localAddress() : null;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return this.channel != null ? this.channel.remoteAddress() : null;
    }

    @Override
    public PacketProtocol getPacketProtocol() {
        return this.protocol;
    }

    @Override
    public Map<String, Object> getFlags() {
        return new HashMap<String, Object>(this.flags);
    }

    @Override
    public boolean hasFlag(String key) {
        return this.getFlags().containsKey(key);
    }

    @Override
    public <T> T getFlag(String key) {
        Object value = this.getFlags().get(key);
        if (value == null) {
            return null;
        }
        try {
            return (T)value;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }

    @Override
    public void setFlag(String key, Object value) {
        this.flags.put(key, value);
    }

    @Override
    public List<SessionListener> getListeners() {
        return new ArrayList<SessionListener>(this.listeners);
    }

    @Override
    public void addListener(SessionListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(SessionListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void callEvent(SessionEvent event) {
        try {
            for (SessionListener listener : this.listeners) {
                event.call(listener);
            }
        }
        catch (Throwable t) {
            this.exceptionCaught(null, t);
        }
    }

    @Override
    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    @Override
    public void setCompressionThreshold(int threshold) {
        this.compressionThreshold = threshold;
        if (this.channel != null) {
            if (this.compressionThreshold >= 0) {
                if (this.channel.pipeline().get("compression") == null) {
                    this.channel.pipeline().addBefore("codec", "compression", (ChannelHandler)new TcpPacketCompression(this));
                }
            } else if (this.channel.pipeline().get("compression") != null) {
                this.channel.pipeline().remove("compression");
            }
        }
    }

    @Override
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    @Override
    public void setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
    }

    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
        this.refreshReadTimeoutHandler();
    }

    @Override
    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    @Override
    public void setWriteTimeout(int timeout) {
        this.writeTimeout = timeout;
        this.refreshWriteTimeoutHandler();
    }

    @Override
    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen() && !this.disconnected;
    }

    @Override
    public void send(Packet packet) {
        if (this.channel == null) {
            return;
        }
        PacketSendingEvent sendingEvent = new PacketSendingEvent(this, packet);
        this.callEvent(sendingEvent);
        if (!sendingEvent.isCancelled()) {
            Object toSend = sendingEvent.getPacket();
            ChannelFuture future = this.channel.writeAndFlush(toSend).addListener((GenericFutureListener<ChannelFuture>)new ChannelFutureListener(){
                private final /* synthetic */ Packet val$toSend;
                {
                    this.val$toSend = packet;
                }

                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        TcpSession.this.callEvent(new PacketSentEvent(TcpSession.this, this.val$toSend));
                    } else {
                        TcpSession.this.exceptionCaught(null, future.cause());
                    }
                }
            });
            if (((Packet) toSend).isPriority()) {
                try {
                    future.await();
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
        }
    }

    @Override
    public void disconnect(String reason) {
        this.disconnect(reason, false);
    }

    @Override
    public void disconnect(String reason, boolean wait) {
        this.disconnect(reason, null, wait);
    }

    @Override
    public void disconnect(String reason, Throwable cause) {
        this.disconnect(reason, cause, false);
    }

    @Override
    public void disconnect(final String reason, final Throwable cause, boolean wait) {
        if (this.disconnected) {
            return;
        }
        this.disconnected = true;
        if (this.packetHandleThread != null) {
            this.packetHandleThread.interrupt();
            this.packetHandleThread = null;
        }
        if (this.channel != null && this.channel.isOpen()) {
            this.callEvent(new DisconnectingEvent(this, reason, cause));
            ChannelFuture future = this.channel.flush().close().addListener((GenericFutureListener<ChannelFuture>)new ChannelFutureListener(){

                public void operationComplete(ChannelFuture future) throws Exception {
                    TcpSession.this.callEvent(new DisconnectedEvent(TcpSession.this, reason != null ? reason : "Connection closed.", cause));
                }
            });
            if (wait) {
                try {
                    future.await();
                }
                catch (InterruptedException interruptedException) {}
            }
        } else {
            this.callEvent(new DisconnectedEvent(this, reason != null ? reason : "Connection closed.", cause));
        }
        this.channel = null;
    }

    protected void refreshReadTimeoutHandler() {
        this.refreshReadTimeoutHandler(this.channel);
    }

    protected void refreshReadTimeoutHandler(Channel channel) {
        if (channel != null) {
            if (this.readTimeout <= 0) {
                if (channel.pipeline().get("readTimeout") != null) {
                    channel.pipeline().remove("readTimeout");
                }
            } else if (channel.pipeline().get("readTimeout") == null) {
                channel.pipeline().addFirst("readTimeout", (ChannelHandler)new ReadTimeoutHandler(this.readTimeout));
            } else {
                channel.pipeline().replace("readTimeout", "readTimeout", (ChannelHandler)new ReadTimeoutHandler(this.readTimeout));
            }
        }
    }

    protected void refreshWriteTimeoutHandler() {
        this.refreshWriteTimeoutHandler(this.channel);
    }

    protected void refreshWriteTimeoutHandler(Channel channel) {
        if (channel != null) {
            if (this.writeTimeout <= 0) {
                if (channel.pipeline().get("writeTimeout") != null) {
                    channel.pipeline().remove("writeTimeout");
                }
            } else if (channel.pipeline().get("writeTimeout") == null) {
                channel.pipeline().addFirst("writeTimeout", (ChannelHandler)new WriteTimeoutHandler(this.writeTimeout));
            } else {
                channel.pipeline().replace("writeTimeout", "writeTimeout", (ChannelHandler)new WriteTimeoutHandler(this.writeTimeout));
            }
        }
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (this.disconnected || this.channel != null) {
            ctx.channel().close();
            return;
        }
        this.channel = ctx.channel();
        this.packetHandleThread = new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Packet packet;
                    while ((packet = (Packet)TcpSession.this.packets.take()) != null) {
                        TcpSession.this.callEvent(new PacketReceivedEvent(TcpSession.this, packet));
                    }
                }
                catch (InterruptedException packet) {
                }
                catch (Throwable t) {
                    TcpSession.this.exceptionCaught(null, t);
                }
            }
        });
        this.packetHandleThread.start();
        this.callEvent(new ConnectedEvent(this));
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel() == this.channel) {
            this.disconnect("Connection closed.");
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String message = null;
        message = cause instanceof ConnectTimeoutException || cause instanceof ConnectException && cause.getMessage().contains("connection timed out") ? "Connection timed out." : (cause instanceof ReadTimeoutException ? "Read timed out." : (cause instanceof WriteTimeoutException ? "Write timed out." : cause.toString()));
        this.disconnect(message, cause);
    }

    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if (!packet.isPriority()) {
            this.packets.add(packet);
        }
    }
}

