/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

public class TLSSocketManager
extends TCPSocketManager {
    public static final int DEFAULT_PORT = 6514;
    private static final TLSSocketManagerFactory FACTORY = new TLSSocketManagerFactory();
    private SSLConfiguration sslConfig;

    public TLSSocketManager(String name, OutputStream os, Socket sock, SSLConfiguration sslConfig, InetAddress addr, String host, int port, int delay, boolean immediateFail, Layout layout) {
        super(name, os, sock, addr, host, port, delay, immediateFail, layout);
        this.sslConfig = sslConfig;
    }

    public static TLSSocketManager getSocketManager(SSLConfiguration sslConfig, String host, int port, int delay, boolean immediateFail, Layout layout) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 6514;
        }
        if (delay == 0) {
            delay = 30000;
        }
        return (TLSSocketManager)TLSSocketManager.getManager("TLS:" + host + ":" + port, new TLSFactoryData(sslConfig, host, port, delay, immediateFail, layout), FACTORY);
    }

    @Override
    protected Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory socketFactory = TLSSocketManager.createSSLSocketFactory(this.sslConfig);
        return socketFactory.createSocket(host, port);
    }

    private static SSLSocketFactory createSSLSocketFactory(SSLConfiguration sslConf) {
        SSLSocketFactory socketFactory = sslConf != null ? sslConf.getSSLSocketFactory() : (SSLSocketFactory)SSLSocketFactory.getDefault();
        return socketFactory;
    }

    private static class TLSSocketManagerFactory
    implements ManagerFactory<TLSSocketManager, TLSFactoryData> {
        private TLSSocketManagerFactory() {
        }

        @Override
        public TLSSocketManager createManager(String name, TLSFactoryData data) {
            InetAddress address = null;
            OutputStream os = null;
            Socket socket = null;
            try {
                address = this.resolveAddress(data.host);
                socket = this.createSocket(data);
                os = socket.getOutputStream();
                this.checkDelay(data.delay, os);
            }
            catch (IOException e) {
                LOGGER.error("TLSSocketManager (" + name + ") " + e);
                os = new ByteArrayOutputStream();
            }
            catch (TLSSocketManagerFactoryException e) {
                return null;
            }
            return this.createManager(name, os, socket, data.sslConfig, address, data.host, data.port, data.delay, data.immediateFail, data.layout);
        }

        private InetAddress resolveAddress(String hostName) throws TLSSocketManagerFactoryException {
            InetAddress address;
            try {
                address = InetAddress.getByName(hostName);
            }
            catch (UnknownHostException ex) {
                LOGGER.error("Could not find address of " + hostName, (Throwable)ex);
                throw new TLSSocketManagerFactoryException();
            }
            return address;
        }

        private void checkDelay(int delay, OutputStream os) throws TLSSocketManagerFactoryException {
            if (delay == 0 && os == null) {
                throw new TLSSocketManagerFactoryException();
            }
        }

        private Socket createSocket(TLSFactoryData data) throws IOException {
            SSLSocketFactory socketFactory = TLSSocketManager.createSSLSocketFactory(data.sslConfig);
            SSLSocket socket = (SSLSocket)socketFactory.createSocket(data.host, data.port);
            return socket;
        }

        private TLSSocketManager createManager(String name, OutputStream os, Socket socket, SSLConfiguration sslConfig, InetAddress address, String host, int port, int delay, boolean immediateFail, Layout layout) {
            return new TLSSocketManager(name, os, socket, sslConfig, address, host, port, delay, immediateFail, layout);
        }

        private class TLSSocketManagerFactoryException
        extends Exception {
            private TLSSocketManagerFactoryException() {
            }
        }
    }

    private static class TLSFactoryData {
        protected SSLConfiguration sslConfig;
        private final String host;
        private final int port;
        private final int delay;
        private final boolean immediateFail;
        private final Layout layout;

        public TLSFactoryData(SSLConfiguration sslConfig, String host, int port, int delay, boolean immediateFail, Layout layout) {
            this.host = host;
            this.port = port;
            this.delay = delay;
            this.immediateFail = immediateFail;
            this.layout = layout;
            this.sslConfig = sslConfig;
        }
    }
}

