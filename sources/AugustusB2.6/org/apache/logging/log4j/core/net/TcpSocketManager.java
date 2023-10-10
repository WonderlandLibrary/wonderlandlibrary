// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.util.ArrayList;
import org.apache.logging.log4j.core.util.NullOutputStream;
import java.net.UnknownHostException;
import org.apache.logging.log4j.core.util.Closer;
import java.util.Iterator;
import java.util.List;
import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.Logger;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.util.Strings;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.net.InetAddress;
import java.io.OutputStream;
import java.net.Socket;

public class TcpSocketManager extends AbstractSocketManager
{
    public static final int DEFAULT_RECONNECTION_DELAY_MILLIS = 30000;
    private static final int DEFAULT_PORT = 4560;
    private static final TcpSocketManagerFactory<TcpSocketManager, FactoryData> FACTORY;
    private final int reconnectionDelayMillis;
    private Reconnector reconnector;
    private Socket socket;
    private final SocketOptions socketOptions;
    private final boolean retry;
    private final boolean immediateFail;
    private final int connectTimeoutMillis;
    
    @Deprecated
    public TcpSocketManager(final String name, final OutputStream os, final Socket socket, final InetAddress inetAddress, final String host, final int port, final int connectTimeoutMillis, final int reconnectionDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        this(name, os, socket, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, null);
    }
    
    public TcpSocketManager(final String name, final OutputStream os, final Socket socket, final InetAddress inetAddress, final String host, final int port, final int connectTimeoutMillis, final int reconnectionDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        super(name, os, inetAddress, host, port, layout, true, bufferSize);
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.reconnectionDelayMillis = reconnectionDelayMillis;
        this.socket = socket;
        this.immediateFail = immediateFail;
        this.retry = (reconnectionDelayMillis > 0);
        if (socket == null) {
            (this.reconnector = this.createReconnector()).start();
        }
        this.socketOptions = socketOptions;
    }
    
    @Deprecated
    public static TcpSocketManager getSocketManager(final String host, final int port, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        return getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, null);
    }
    
    public static TcpSocketManager getSocketManager(final String host, int port, final int connectTimeoutMillis, int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 4560;
        }
        if (reconnectDelayMillis == 0) {
            reconnectDelayMillis = 30000;
        }
        return (TcpSocketManager)OutputStreamManager.getManager("TCP:" + host + ':' + port, new FactoryData(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), TcpSocketManager.FACTORY);
    }
    
    @Override
    protected void write(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) {
        if (this.socket == null) {
            if (this.reconnector != null && !this.immediateFail) {
                this.reconnector.latch();
            }
            if (this.socket == null) {
                throw new AppenderLoggingException("Error writing to " + this.getName() + ": socket not available");
            }
        }
        synchronized (this) {
            try {
                this.writeAndFlush(bytes, offset, length, immediateFlush);
            }
            catch (IOException causeEx) {
                final String config = this.inetAddress + ":" + this.port;
                if (this.retry && this.reconnector == null) {
                    this.reconnector = this.createReconnector();
                    try {
                        this.reconnector.reconnect();
                    }
                    catch (IOException reconnEx) {
                        TcpSocketManager.LOGGER.debug("Cannot reestablish socket connection to {}: {}; starting reconnector thread {}", config, reconnEx.getLocalizedMessage(), this.reconnector.getName(), reconnEx);
                        this.reconnector.start();
                        throw new AppenderLoggingException(String.format("Error sending to %s for %s", this.getName(), config), causeEx);
                    }
                    try {
                        this.writeAndFlush(bytes, offset, length, immediateFlush);
                    }
                    catch (IOException e) {
                        throw new AppenderLoggingException(String.format("Error writing to %s after reestablishing connection for %s", this.getName(), config), causeEx);
                    }
                    return;
                }
                final String message = String.format("Error writing to %s for connection %s", this.getName(), config);
                throw new AppenderLoggingException(message, causeEx);
            }
        }
    }
    
    private void writeAndFlush(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) throws IOException {
        final OutputStream outputStream = this.getOutputStream();
        outputStream.write(bytes, offset, length);
        if (immediateFlush) {
            outputStream.flush();
        }
    }
    
    @Override
    protected synchronized boolean closeOutputStream() {
        final boolean closed = super.closeOutputStream();
        if (this.reconnector != null) {
            this.reconnector.shutdown();
            this.reconnector.interrupt();
            this.reconnector = null;
        }
        final Socket oldSocket = this.socket;
        this.socket = null;
        if (oldSocket != null) {
            try {
                oldSocket.close();
            }
            catch (IOException e) {
                TcpSocketManager.LOGGER.error("Could not close socket {}", this.socket);
                return false;
            }
        }
        return closed;
    }
    
    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("protocol", "tcp");
        result.put("direction", "out");
        return result;
    }
    
    private Reconnector createReconnector() {
        final Reconnector recon = new Reconnector(this);
        recon.setDaemon(true);
        recon.setPriority(1);
        return recon;
    }
    
    protected Socket createSocket(final InetSocketAddress socketAddress) throws IOException {
        return createSocket(socketAddress, this.socketOptions, this.connectTimeoutMillis);
    }
    
    protected static Socket createSocket(final InetSocketAddress socketAddress, final SocketOptions socketOptions, final int connectTimeoutMillis) throws IOException {
        TcpSocketManager.LOGGER.debug("Creating socket {}", socketAddress.toString());
        final Socket newSocket = new Socket();
        if (socketOptions != null) {
            socketOptions.apply(newSocket);
        }
        newSocket.connect(socketAddress, connectTimeoutMillis);
        if (socketOptions != null) {
            socketOptions.apply(newSocket);
        }
        return newSocket;
    }
    
    public static void setHostResolver(final HostResolver resolver) {
        TcpSocketManagerFactory.resolver = resolver;
    }
    
    public SocketOptions getSocketOptions() {
        return this.socketOptions;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public int getReconnectionDelayMillis() {
        return this.reconnectionDelayMillis;
    }
    
    @Override
    public String toString() {
        return "TcpSocketManager [reconnectionDelayMillis=" + this.reconnectionDelayMillis + ", reconnector=" + this.reconnector + ", socket=" + this.socket + ", socketOptions=" + this.socketOptions + ", retry=" + this.retry + ", immediateFail=" + this.immediateFail + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", inetAddress=" + this.inetAddress + ", host=" + this.host + ", port=" + this.port + ", layout=" + this.layout + ", byteBuffer=" + this.byteBuffer + ", count=" + this.count + "]";
    }
    
    static {
        FACTORY = new TcpSocketManagerFactory<TcpSocketManager, FactoryData>();
    }
    
    private class Reconnector extends Log4jThread
    {
        private final CountDownLatch latch;
        private boolean shutdown;
        private final Object owner;
        
        public Reconnector(final OutputStreamManager owner) {
            super("TcpSocketManager-Reconnector");
            this.latch = new CountDownLatch(1);
            this.shutdown = false;
            this.owner = owner;
        }
        
        public void latch() {
            try {
                this.latch.await();
            }
            catch (InterruptedException ex) {}
        }
        
        public void shutdown() {
            this.shutdown = true;
        }
        
        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Thread.sleep(TcpSocketManager.this.reconnectionDelayMillis);
                    this.reconnect();
                }
                catch (InterruptedException ie) {
                    TcpSocketManager.LOGGER.debug("Reconnection interrupted.");
                }
                catch (ConnectException ex) {
                    TcpSocketManager.LOGGER.debug("{}:{} refused connection", TcpSocketManager.this.host, TcpSocketManager.this.port);
                }
                catch (IOException ioe) {
                    TcpSocketManager.LOGGER.debug("Unable to reconnect to {}:{}", TcpSocketManager.this.host, TcpSocketManager.this.port);
                }
                finally {
                    this.latch.countDown();
                }
            }
        }
        
        void reconnect() throws IOException {
            TcpSocketManager.FACTORY;
            final List<InetSocketAddress> socketAddresses = TcpSocketManagerFactory.resolver.resolveHost(TcpSocketManager.this.host, TcpSocketManager.this.port);
            if (socketAddresses.size() == 1) {
                TcpSocketManager.LOGGER.debug("Reconnecting " + socketAddresses.get(0));
                this.connect(socketAddresses.get(0));
                return;
            }
            IOException ioe = null;
            for (final InetSocketAddress socketAddress : socketAddresses) {
                try {
                    TcpSocketManager.LOGGER.debug("Reconnecting " + socketAddress);
                    this.connect(socketAddress);
                    return;
                }
                catch (IOException ex) {
                    ioe = ex;
                    continue;
                }
                break;
            }
            throw ioe;
        }
        
        private void connect(final InetSocketAddress socketAddress) throws IOException {
            final Socket sock = TcpSocketManager.this.createSocket(socketAddress);
            final OutputStream newOS = sock.getOutputStream();
            final InetAddress prev = (TcpSocketManager.this.socket != null) ? TcpSocketManager.this.socket.getInetAddress() : null;
            synchronized (this.owner) {
                Closer.closeSilently(OutputStreamManager.this.getOutputStream());
                OutputStreamManager.this.setOutputStream(newOS);
                TcpSocketManager.this.socket = sock;
                TcpSocketManager.this.reconnector = null;
                this.shutdown = true;
            }
            final String type = (prev != null && prev.getHostAddress().equals(socketAddress.getAddress().getHostAddress())) ? "reestablished" : "established";
            TcpSocketManager.LOGGER.debug("Connection to {}:{} {}: {}", TcpSocketManager.this.host, TcpSocketManager.this.port, type, TcpSocketManager.this.socket);
        }
        
        @Override
        public String toString() {
            return "Reconnector [latch=" + this.latch + ", shutdown=" + this.shutdown + "]";
        }
    }
    
    static class FactoryData
    {
        protected final String host;
        protected final int port;
        protected final int connectTimeoutMillis;
        protected final int reconnectDelayMillis;
        protected final boolean immediateFail;
        protected final Layout<? extends Serializable> layout;
        protected final int bufferSize;
        protected final SocketOptions socketOptions;
        
        public FactoryData(final String host, final int port, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
            this.host = host;
            this.port = port;
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.reconnectDelayMillis = reconnectDelayMillis;
            this.immediateFail = immediateFail;
            this.layout = layout;
            this.bufferSize = bufferSize;
            this.socketOptions = socketOptions;
        }
        
        @Override
        public String toString() {
            return "FactoryData [host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
        }
    }
    
    protected static class TcpSocketManagerFactory<M extends TcpSocketManager, T extends FactoryData> implements ManagerFactory<M, T>
    {
        static HostResolver resolver;
        
        @Override
        public M createManager(final String name, final T data) {
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getByName(data.host);
            }
            catch (UnknownHostException ex) {
                TcpSocketManager.LOGGER.error("Could not find address of {}: {}", data.host, ex, ex);
                return null;
            }
            Socket socket = null;
            try {
                socket = this.createSocket(data);
                final OutputStream os = socket.getOutputStream();
                return this.createManager(name, os, socket, inetAddress, data);
            }
            catch (IOException ex2) {
                TcpSocketManager.LOGGER.error("TcpSocketManager ({}) caught exception and will continue:", name, ex2);
                final OutputStream os = NullOutputStream.getInstance();
                if (data.reconnectDelayMillis == 0) {
                    Closer.closeSilently(socket);
                    return null;
                }
                return this.createManager(name, os, null, inetAddress, data);
            }
        }
        
        M createManager(final String name, final OutputStream os, final Socket socket, final InetAddress inetAddress, final T data) {
            return (M)new TcpSocketManager(name, os, socket, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
        }
        
        Socket createSocket(final T data) throws IOException {
            final List<InetSocketAddress> socketAddresses = TcpSocketManagerFactory.resolver.resolveHost(data.host, data.port);
            IOException ioe = null;
            for (final InetSocketAddress socketAddress : socketAddresses) {
                try {
                    return TcpSocketManager.createSocket(socketAddress, data.socketOptions, data.connectTimeoutMillis);
                }
                catch (IOException ex) {
                    ioe = ex;
                    continue;
                }
                break;
            }
            throw new IOException(this.errorMessage(data, socketAddresses), ioe);
        }
        
        protected String errorMessage(final T data, final List<InetSocketAddress> socketAddresses) {
            final StringBuilder sb = new StringBuilder("Unable to create socket for ");
            sb.append(data.host).append(" at port ").append(data.port);
            if (socketAddresses.size() == 1) {
                if (!socketAddresses.get(0).getAddress().getHostAddress().equals(data.host)) {
                    sb.append(" using ip address ").append(socketAddresses.get(0).getAddress().getHostAddress());
                    sb.append(" and port ").append(socketAddresses.get(0).getPort());
                }
            }
            else {
                sb.append(" using ip addresses and ports ");
                for (int i = 0; i < socketAddresses.size(); ++i) {
                    if (i > 0) {
                        sb.append(", ");
                        sb.append(socketAddresses.get(i).getAddress().getHostAddress());
                        sb.append(":").append(socketAddresses.get(i).getPort());
                    }
                }
            }
            return sb.toString();
        }
        
        static {
            TcpSocketManagerFactory.resolver = new HostResolver();
        }
    }
    
    public static class HostResolver
    {
        public List<InetSocketAddress> resolveHost(final String host, final int port) throws UnknownHostException {
            final InetAddress[] addresses = InetAddress.getAllByName(host);
            final List<InetSocketAddress> socketAddresses = new ArrayList<InetSocketAddress>(addresses.length);
            for (final InetAddress address : addresses) {
                socketAddresses.add(new InetSocketAddress(address, port));
            }
            return socketAddresses;
        }
    }
}
