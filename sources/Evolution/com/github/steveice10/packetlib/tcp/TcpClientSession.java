/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.bootstrap.Bootstrap
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFactory
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelOption
 *  io.netty.channel.ChannelPipeline
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.nio.NioEventLoopGroup
 *  io.netty.channel.oio.OioEventLoopGroup
 *  io.netty.channel.socket.nio.NioSocketChannel
 *  io.netty.util.concurrent.Future
 */
package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import com.github.steveice10.packetlib.tcp.ProxyOioChannelFactory;
import com.github.steveice10.packetlib.tcp.TcpPacketCodec;
import com.github.steveice10.packetlib.tcp.TcpPacketEncryptor;
import com.github.steveice10.packetlib.tcp.TcpPacketSizer;
import com.github.steveice10.packetlib.tcp.TcpSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import java.net.Proxy;
import java.util.Hashtable;
import javax.naming.directory.InitialDirContext;

public class TcpClientSession
extends TcpSession {
    private Client client;
    private Proxy proxy;
    private EventLoopGroup group;

    public TcpClientSession(String host, int port, PacketProtocol protocol, Client client, Proxy proxy) {
        super(host, port, protocol);
        this.client = client;
        this.proxy = proxy;
    }

    @Override
    public void connect(boolean wait) {
        if (this.disconnected) {
            throw new IllegalStateException("Session has already been disconnected.");
        }
        if (this.group != null) {
            return;
        }
        try {
            final Bootstrap bootstrap = new Bootstrap();
            if (this.proxy != null) {
                this.group = new OioEventLoopGroup();
                bootstrap.channelFactory((ChannelFactory)new ProxyOioChannelFactory(this.proxy));
            } else {
                this.group = new NioEventLoopGroup();
                bootstrap.channel(NioSocketChannel.class);
            }
            ((Bootstrap)((Bootstrap)bootstrap.handler((ChannelHandler)new ChannelInitializer<Channel>(){

                public void initChannel(Channel channel) throws Exception {
                    TcpClientSession.this.getPacketProtocol().newClientSession(TcpClientSession.this.client, TcpClientSession.this);
                    channel.config().setOption(ChannelOption.IP_TOS, 24);
                    channel.config().setOption(ChannelOption.TCP_NODELAY, false);
                    ChannelPipeline pipeline = channel.pipeline();
                    TcpClientSession.this.refreshReadTimeoutHandler(channel);
                    TcpClientSession.this.refreshWriteTimeoutHandler(channel);
                    pipeline.addLast("encryption", (ChannelHandler)new TcpPacketEncryptor(TcpClientSession.this));
                    pipeline.addLast("sizer", (ChannelHandler)new TcpPacketSizer(TcpClientSession.this));
                    pipeline.addLast("codec", (ChannelHandler)new TcpPacketCodec(TcpClientSession.this));
                    pipeline.addLast("manager", (ChannelHandler)TcpClientSession.this);
                }
            })).group(this.group)).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (this.getConnectTimeout() * 1000));
            Runnable connectTask = new Runnable(){

                @Override
                public void run() {
                    try {
                        String host = TcpClientSession.this.getHost();
                        int port = TcpClientSession.this.getPort();
                        try {
                            Hashtable<String, String> environment = new Hashtable<String, String>();
                            environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
                            environment.put("java.naming.provider.url", "dns:");
                            String[] result = new InitialDirContext(environment).getAttributes(String.valueOf(TcpClientSession.this.getPacketProtocol().getSRVRecordPrefix()) + "._tcp." + host, new String[]{"SRV"}).get("srv").get().toString().split(" ", 4);
                            host = result[3];
                            port = Integer.parseInt(result[2]);
                        }
                        catch (Throwable environment) {
                            // empty catch block
                        }
                        bootstrap.remoteAddress(host, port);
                        ChannelFuture future = bootstrap.connect().sync();
                        if (future.isSuccess()) {
                            while (!TcpClientSession.this.isConnected() && !TcpClientSession.this.disconnected) {
                                try {
                                    Thread.sleep(5L);
                                }
                                catch (InterruptedException interruptedException) {
                                    // empty catch block
                                }
                            }
                        }
                    }
                    catch (Throwable t) {
                        TcpClientSession.this.exceptionCaught(null, t);
                    }
                }
            };
            if (wait) {
                connectTask.run();
            } else {
                new Thread(connectTask).start();
            }
        }
        catch (Throwable t) {
            this.exceptionCaught(null, t);
        }
    }

    @Override
    public void disconnect(String reason, Throwable cause, boolean wait) {
        super.disconnect(reason, cause, wait);
        if (this.group != null) {
            Future future = this.group.shutdownGracefully();
            if (wait) {
                try {
                    future.await();
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
            this.group = null;
        }
    }
}

