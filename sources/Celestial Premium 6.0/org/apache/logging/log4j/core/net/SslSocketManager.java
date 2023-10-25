/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.SocketOptions;
import org.apache.logging.log4j.core.net.TcpSocketManager;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.util.Strings;

public class SslSocketManager
extends TcpSocketManager {
    public static final int DEFAULT_PORT = 6514;
    private static final SslSocketManagerFactory FACTORY = new SslSocketManagerFactory();
    private final SslConfiguration sslConfig;

    @Deprecated
    public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
        super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, null);
        this.sslConfig = sslConfig;
    }

    public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
        super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, socketOptions);
        this.sslConfig = sslConfig;
    }

    @Deprecated
    public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
        return SslSocketManager.getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, null);
    }

    public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 6514;
        }
        if (reconnectDelayMillis == 0) {
            reconnectDelayMillis = 30000;
        }
        String name = "TLS:" + host + ':' + port;
        return (SslSocketManager)SslSocketManager.getManager(name, new SslFactoryData(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
    }

    @Override
    protected Socket createSocket(InetSocketAddress socketAddress) throws IOException {
        SSLSocketFactory socketFactory = SslSocketManager.createSslSocketFactory(this.sslConfig);
        Socket newSocket = socketFactory.createSocket();
        newSocket.connect(socketAddress, this.getConnectTimeoutMillis());
        return newSocket;
    }

    private static SSLSocketFactory createSslSocketFactory(SslConfiguration sslConf) {
        SSLSocketFactory socketFactory = sslConf != null ? sslConf.getSslSocketFactory() : (SSLSocketFactory)SSLSocketFactory.getDefault();
        return socketFactory;
    }

    static Socket createSocket(InetSocketAddress socketAddress, int connectTimeoutMillis, SslConfiguration sslConfiguration, SocketOptions socketOptions) throws IOException {
        SSLSocketFactory socketFactory = SslSocketManager.createSslSocketFactory(sslConfiguration);
        SSLSocket socket = (SSLSocket)socketFactory.createSocket();
        if (socketOptions != null) {
            socketOptions.apply(socket);
        }
        socket.connect(socketAddress, connectTimeoutMillis);
        if (socketOptions != null) {
            socketOptions.apply(socket);
        }
        return socket;
    }

    private static class SslSocketManagerFactory
    extends TcpSocketManager.TcpSocketManagerFactory<SslSocketManager, SslFactoryData> {
        private SslSocketManagerFactory() {
        }

        @Override
        SslSocketManager createManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, SslFactoryData data) {
            return new SslSocketManager(name, os, socket, data.sslConfiguration, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
        }

        @Override
        Socket createSocket(SslFactoryData data) throws IOException {
            List<InetSocketAddress> socketAddresses = resolver.resolveHost(data.host, data.port);
            IOException ioe = null;
            for (InetSocketAddress socketAddress : socketAddresses) {
                try {
                    return SslSocketManager.createSocket(socketAddress, data.connectTimeoutMillis, data.sslConfiguration, data.socketOptions);
                }
                catch (IOException ex) {
                    ioe = ex;
                }
            }
            throw new IOException(this.errorMessage(data, socketAddresses), ioe);
        }
    }

    private static class SslFactoryData
    extends TcpSocketManager.FactoryData {
        protected SslConfiguration sslConfiguration;

        public SslFactoryData(SslConfiguration sslConfiguration, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
            super(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
            this.sslConfiguration = sslConfiguration;
        }

        @Override
        public String toString() {
            return "SslFactoryData [sslConfiguration=" + this.sslConfiguration + ", host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
        }
    }
}

