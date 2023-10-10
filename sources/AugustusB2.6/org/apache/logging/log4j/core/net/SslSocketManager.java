// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.util.Strings;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.net.InetAddress;
import java.net.Socket;
import java.io.OutputStream;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

public class SslSocketManager extends TcpSocketManager
{
    public static final int DEFAULT_PORT = 6514;
    private static final SslSocketManagerFactory FACTORY;
    private final SslConfiguration sslConfig;
    
    @Deprecated
    public SslSocketManager(final String name, final OutputStream os, final Socket sock, final SslConfiguration sslConfig, final InetAddress inetAddress, final String host, final int port, final int connectTimeoutMillis, final int reconnectionDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, null);
        this.sslConfig = sslConfig;
    }
    
    public SslSocketManager(final String name, final OutputStream os, final Socket sock, final SslConfiguration sslConfig, final InetAddress inetAddress, final String host, final int port, final int connectTimeoutMillis, final int reconnectionDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, socketOptions);
        this.sslConfig = sslConfig;
    }
    
    @Deprecated
    public static SslSocketManager getSocketManager(final SslConfiguration sslConfig, final String host, final int port, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        return getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, null);
    }
    
    public static SslSocketManager getSocketManager(final SslConfiguration sslConfig, final String host, int port, final int connectTimeoutMillis, int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 6514;
        }
        if (reconnectDelayMillis == 0) {
            reconnectDelayMillis = 30000;
        }
        final String name = "TLS:" + host + ':' + port;
        return (SslSocketManager)OutputStreamManager.getManager(name, new SslFactoryData(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), SslSocketManager.FACTORY);
    }
    
    @Override
    protected Socket createSocket(final InetSocketAddress socketAddress) throws IOException {
        final SSLSocketFactory socketFactory = createSslSocketFactory(this.sslConfig);
        final Socket newSocket = socketFactory.createSocket();
        newSocket.connect(socketAddress, this.getConnectTimeoutMillis());
        return newSocket;
    }
    
    private static SSLSocketFactory createSslSocketFactory(final SslConfiguration sslConf) {
        SSLSocketFactory socketFactory;
        if (sslConf != null) {
            socketFactory = sslConf.getSslSocketFactory();
        }
        else {
            socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        }
        return socketFactory;
    }
    
    static Socket createSocket(final InetSocketAddress socketAddress, final int connectTimeoutMillis, final SslConfiguration sslConfiguration, final SocketOptions socketOptions) throws IOException {
        final SSLSocketFactory socketFactory = createSslSocketFactory(sslConfiguration);
        final SSLSocket socket = (SSLSocket)socketFactory.createSocket();
        if (socketOptions != null) {
            socketOptions.apply(socket);
        }
        socket.connect(socketAddress, connectTimeoutMillis);
        if (socketOptions != null) {
            socketOptions.apply(socket);
        }
        return socket;
    }
    
    static {
        FACTORY = new SslSocketManagerFactory();
    }
    
    private static class SslFactoryData extends FactoryData
    {
        protected SslConfiguration sslConfiguration;
        
        public SslFactoryData(final SslConfiguration sslConfiguration, final String host, final int port, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
            super(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
            this.sslConfiguration = sslConfiguration;
        }
        
        @Override
        public String toString() {
            return "SslFactoryData [sslConfiguration=" + this.sslConfiguration + ", host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
        }
    }
    
    private static class SslSocketManagerFactory extends TcpSocketManagerFactory<SslSocketManager, SslFactoryData>
    {
        @Override
        SslSocketManager createManager(final String name, final OutputStream os, final Socket socket, final InetAddress inetAddress, final SslFactoryData data) {
            return new SslSocketManager(name, os, socket, data.sslConfiguration, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
        }
        
        @Override
        Socket createSocket(final SslFactoryData data) throws IOException {
            final List<InetSocketAddress> socketAddresses = SslSocketManagerFactory.resolver.resolveHost(data.host, data.port);
            IOException ioe = null;
            for (final InetSocketAddress socketAddress : socketAddresses) {
                try {
                    return SslSocketManager.createSocket(socketAddress, data.connectTimeoutMillis, data.sslConfiguration, data.socketOptions);
                }
                catch (IOException ex) {
                    ioe = ex;
                    continue;
                }
                break;
            }
            throw new IOException(((TcpSocketManagerFactory<M, SslFactoryData>)this).errorMessage(data, socketAddresses), ioe);
        }
    }
}
