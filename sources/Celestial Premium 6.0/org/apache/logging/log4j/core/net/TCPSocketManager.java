/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.AbstractSocketManager;

public class TCPSocketManager
extends AbstractSocketManager {
    public static final int DEFAULT_RECONNECTION_DELAY = 30000;
    private static final int DEFAULT_PORT = 4560;
    private static final TCPSocketManagerFactory FACTORY = new TCPSocketManagerFactory();
    private final int reconnectionDelay;
    private Reconnector connector = null;
    private Socket socket;
    private final boolean retry;
    private final boolean immediateFail;

    public TCPSocketManager(String name, OutputStream os, Socket sock, InetAddress addr, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
        super(name, os, addr, host, port, layout);
        this.reconnectionDelay = delay;
        this.socket = sock;
        this.immediateFail = immediateFail;
        boolean bl = this.retry = delay > 0;
        if (sock == null) {
            this.connector = new Reconnector(this);
            this.connector.setDaemon(true);
            this.connector.setPriority(1);
            this.connector.start();
        }
    }

    public static TCPSocketManager getSocketManager(String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 4560;
        }
        if (delay == 0) {
            delay = 30000;
        }
        return (TCPSocketManager)TCPSocketManager.getManager("TCP:" + host + ":" + port, new FactoryData(host, port, delay, immediateFail, layout), FACTORY);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void write(byte[] bytes, int offset, int length) {
        if (this.socket == null) {
            if (this.connector != null && !this.immediateFail) {
                this.connector.latch();
            }
            if (this.socket == null) {
                String msg = "Error writing to " + this.getName() + " socket not available";
                throw new AppenderLoggingException(msg);
            }
        }
        TCPSocketManager tCPSocketManager = this;
        synchronized (tCPSocketManager) {
            try {
                this.getOutputStream().write(bytes, offset, length);
            }
            catch (IOException ex) {
                if (this.retry && this.connector == null) {
                    this.connector = new Reconnector(this);
                    this.connector.setDaemon(true);
                    this.connector.setPriority(1);
                    this.connector.start();
                }
                String msg = "Error writing to " + this.getName();
                throw new AppenderLoggingException(msg, ex);
            }
        }
    }

    @Override
    protected synchronized void close() {
        super.close();
        if (this.connector != null) {
            this.connector.shutdown();
            this.connector.interrupt();
            this.connector = null;
        }
    }

    @Override
    public Map<String, String> getContentFormat() {
        HashMap<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("protocol", "tcp");
        result.put("direction", "out");
        return result;
    }

    protected Socket createSocket(InetAddress host, int port) throws IOException {
        return this.createSocket(host.getHostName(), port);
    }

    protected Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    protected static class TCPSocketManagerFactory
    implements ManagerFactory<TCPSocketManager, FactoryData> {
        protected TCPSocketManagerFactory() {
        }

        @Override
        public TCPSocketManager createManager(String name, FactoryData data) {
            InetAddress address;
            try {
                address = InetAddress.getByName(data.host);
            }
            catch (UnknownHostException ex) {
                LOGGER.error("Could not find address of " + data.host, (Throwable)ex);
                return null;
            }
            try {
                Socket socket = new Socket(data.host, data.port);
                OutputStream os = socket.getOutputStream();
                return new TCPSocketManager(name, os, socket, address, data.host, data.port, data.delay, data.immediateFail, data.layout);
            }
            catch (IOException ex) {
                LOGGER.error("TCPSocketManager (" + name + ") " + ex);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                if (data.delay == 0) {
                    return null;
                }
                return new TCPSocketManager(name, os, null, address, data.host, data.port, data.delay, data.immediateFail, data.layout);
            }
        }
    }

    private static class FactoryData {
        private final String host;
        private final int port;
        private final int delay;
        private final boolean immediateFail;
        private final Layout<? extends Serializable> layout;

        public FactoryData(String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
            this.host = host;
            this.port = port;
            this.delay = delay;
            this.immediateFail = immediateFail;
            this.layout = layout;
        }
    }

    private class Reconnector
    extends Thread {
        private final CountDownLatch latch = new CountDownLatch(1);
        private boolean shutdown = false;
        private final Object owner;

        public Reconnector(OutputStreamManager owner) {
            this.owner = owner;
        }

        public void latch() {
            try {
                this.latch.await();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }

        public void shutdown() {
            this.shutdown = true;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Reconnector.sleep(TCPSocketManager.this.reconnectionDelay);
                    Socket sock = TCPSocketManager.this.createSocket(TCPSocketManager.this.address, TCPSocketManager.this.port);
                    OutputStream newOS = sock.getOutputStream();
                    Object object = this.owner;
                    synchronized (object) {
                        try {
                            TCPSocketManager.this.getOutputStream().close();
                        }
                        catch (IOException ioe) {
                            // empty catch block
                        }
                        TCPSocketManager.this.setOutputStream(newOS);
                        TCPSocketManager.this.socket = sock;
                        TCPSocketManager.this.connector = null;
                        this.shutdown = true;
                    }
                    LOGGER.debug("Connection to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " reestablished.");
                }
                catch (InterruptedException ie) {
                    LOGGER.debug("Reconnection interrupted.");
                }
                catch (ConnectException ex) {
                    LOGGER.debug(TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " refused connection");
                }
                catch (IOException ioe) {
                    LOGGER.debug("Unable to reconnect to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port);
                }
                finally {
                    this.latch.countDown();
                }
            }
        }
    }
}

