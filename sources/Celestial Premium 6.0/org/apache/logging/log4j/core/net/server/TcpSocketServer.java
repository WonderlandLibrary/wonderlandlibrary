/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.beust.jcommander.Parameter
 *  com.beust.jcommander.validators.PositiveInteger
 */
package org.apache.logging.log4j.core.net.server;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.net.server.AbstractSocketServer;
import org.apache.logging.log4j.core.net.server.JsonInputStreamLogEventBridge;
import org.apache.logging.log4j.core.net.server.LogEventBridge;
import org.apache.logging.log4j.core.net.server.ObjectInputStreamLogEventBridge;
import org.apache.logging.log4j.core.net.server.XmlInputStreamLogEventBridge;
import org.apache.logging.log4j.core.util.BasicCommandLineArguments;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.message.EntryMessage;

public class TcpSocketServer<T extends InputStream>
extends AbstractSocketServer<T> {
    private final ConcurrentMap<Long, SocketHandler> handlers = new ConcurrentHashMap<Long, SocketHandler>();
    private final ServerSocket serverSocket;

    public static TcpSocketServer<InputStream> createJsonSocketServer(int port) throws IOException {
        LOGGER.entry(new Object[]{"createJsonSocketServer", port});
        TcpSocketServer<InputStream> socketServer = new TcpSocketServer<InputStream>(port, new JsonInputStreamLogEventBridge());
        return LOGGER.exit(socketServer);
    }

    public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(int port) throws IOException {
        LOGGER.entry(port);
        TcpSocketServer<ObjectInputStream> socketServer = new TcpSocketServer<ObjectInputStream>(port, new ObjectInputStreamLogEventBridge());
        return LOGGER.exit(socketServer);
    }

    public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(int port, int backlog, InetAddress localBindAddress) throws IOException {
        LOGGER.entry(port);
        TcpSocketServer<ObjectInputStream> socketServer = new TcpSocketServer<ObjectInputStream>(port, backlog, localBindAddress, new ObjectInputStreamLogEventBridge());
        return LOGGER.exit(socketServer);
    }

    public static TcpSocketServer<InputStream> createXmlSocketServer(int port) throws IOException {
        LOGGER.entry(port);
        TcpSocketServer<InputStream> socketServer = new TcpSocketServer<InputStream>(port, new XmlInputStreamLogEventBridge());
        return LOGGER.exit(socketServer);
    }

    public static void main(String[] args) throws Exception {
        CommandLineArguments cla = BasicCommandLineArguments.parseCommandLine(args, TcpSocketServer.class, new CommandLineArguments());
        if (cla.isHelp()) {
            return;
        }
        if (cla.getConfigLocation() != null) {
            ConfigurationFactory.setConfigurationFactory(new AbstractSocketServer.ServerConfigurationFactory(cla.getConfigLocation()));
        }
        TcpSocketServer<ObjectInputStream> socketServer = TcpSocketServer.createSerializedSocketServer(cla.getPort(), cla.getBacklog(), cla.getLocalBindAddress());
        Thread serverThread = socketServer.startNewThread();
        if (cla.isInteractive()) {
            socketServer.awaitTermination(serverThread);
        }
    }

    public TcpSocketServer(int port, int backlog, InetAddress localBindAddress, LogEventBridge<T> logEventInput) throws IOException {
        this(port, logEventInput, new ServerSocket(port, backlog, localBindAddress));
    }

    public TcpSocketServer(int port, LogEventBridge<T> logEventInput) throws IOException {
        this(port, logEventInput, TcpSocketServer.extracted(port));
    }

    private static ServerSocket extracted(int port) throws IOException {
        return new ServerSocket(port);
    }

    public TcpSocketServer(int port, LogEventBridge<T> logEventInput, ServerSocket serverSocket) throws IOException {
        super(port, logEventInput);
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        EntryMessage entry = this.logger.traceEntry();
        while (this.isActive()) {
            if (this.serverSocket.isClosed()) {
                return;
            }
            try {
                this.logger.debug("Listening for a connection {}...", this.serverSocket);
                Socket clientSocket = this.serverSocket.accept();
                this.logger.debug("Acepted connection on {}...", this.serverSocket);
                this.logger.debug("Socket accepted: {}", clientSocket);
                clientSocket.setSoLinger(true, 0);
                SocketHandler handler = new SocketHandler(clientSocket);
                this.handlers.put(handler.getId(), handler);
                handler.start();
            }
            catch (IOException e) {
                if (this.serverSocket.isClosed()) {
                    this.logger.traceExit(entry);
                    return;
                }
                this.logger.error("Exception encountered on accept. Ignoring. Stack trace :", (Throwable)e);
            }
        }
        for (Map.Entry handlerEntry : this.handlers.entrySet()) {
            SocketHandler handler = (SocketHandler)handlerEntry.getValue();
            handler.shutdown();
            try {
                handler.join();
            }
            catch (InterruptedException interruptedException) {}
        }
        this.logger.traceExit(entry);
    }

    @Override
    public void shutdown() throws IOException {
        EntryMessage entry = this.logger.traceEntry();
        this.setActive(false);
        Thread.currentThread().interrupt();
        this.serverSocket.close();
        this.logger.traceExit(entry);
    }

    private class SocketHandler
    extends Log4jThread {
        private final T inputStream;
        private volatile boolean shutdown = false;

        public SocketHandler(Socket socket) throws IOException {
            this.inputStream = TcpSocketServer.this.logEventInput.wrapStream(socket.getInputStream());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            EntryMessage entry = TcpSocketServer.this.logger.traceEntry();
            boolean closed = false;
            try {
                try {
                    while (!this.shutdown) {
                        TcpSocketServer.this.logEventInput.logEvents(this.inputStream, TcpSocketServer.this);
                    }
                }
                catch (EOFException e) {
                    closed = true;
                }
                catch (OptionalDataException e) {
                    TcpSocketServer.this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, (Throwable)e);
                }
                catch (IOException e) {
                    TcpSocketServer.this.logger.error("IOException encountered while reading from socket", (Throwable)e);
                }
                if (!closed) {
                    Closer.closeSilently(this.inputStream);
                }
            }
            finally {
                TcpSocketServer.this.handlers.remove(this.getId());
            }
            TcpSocketServer.this.logger.traceExit(entry);
        }

        public void shutdown() {
            this.shutdown = true;
            this.interrupt();
        }
    }

    protected static class CommandLineArguments
    extends AbstractSocketServer.CommandLineArguments {
        @Parameter(names={"--backlog", "-b"}, validateWith=PositiveInteger.class, description="Server socket backlog.")
        private int backlog = 50;

        protected CommandLineArguments() {
        }

        int getBacklog() {
            return this.backlog;
        }

        void setBacklog(int backlog) {
            this.backlog = backlog;
        }
    }
}

