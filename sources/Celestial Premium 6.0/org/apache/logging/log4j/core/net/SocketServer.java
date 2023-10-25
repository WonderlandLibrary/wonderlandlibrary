/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

public class SocketServer
extends AbstractServer
implements Runnable {
    private final Logger logger;
    private static final int MAX_PORT = 65534;
    private volatile boolean isActive = true;
    private final ServerSocket server;
    private final ConcurrentMap<Long, SocketHandler> handlers = new ConcurrentHashMap<Long, SocketHandler>();

    public SocketServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.logger = LogManager.getLogger(this.getClass().getName() + '.' + port);
    }

    public static void main(String[] args) throws Exception {
        String line;
        if (args.length < 1 || args.length > 2) {
            System.err.println("Incorrect number of arguments");
            SocketServer.printUsage();
            return;
        }
        int port = Integer.parseInt(args[0]);
        if (port <= 0 || port >= 65534) {
            System.err.println("Invalid port number");
            SocketServer.printUsage();
            return;
        }
        if (args.length == 2 && args[1].length() > 0) {
            ConfigurationFactory.setConfigurationFactory(new ServerConfigurationFactory(args[1]));
        }
        SocketServer sserver = new SocketServer(port);
        Thread server = new Thread(sserver);
        server.start();
        Charset enc = Charset.defaultCharset();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, enc));
        while (!((line = reader.readLine()) == null || line.equalsIgnoreCase("Quit") || line.equalsIgnoreCase("Stop") || line.equalsIgnoreCase("Exit"))) {
        }
        sserver.shutdown();
        server.join();
    }

    private static void printUsage() {
        System.out.println("Usage: ServerSocket port configFilePath");
    }

    public void shutdown() {
        this.isActive = false;
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        while (this.isActive) {
            try {
                Socket clientSocket = this.server.accept();
                clientSocket.setSoLinger(true, 0);
                SocketHandler handler = new SocketHandler(clientSocket);
                this.handlers.put(handler.getId(), handler);
                handler.start();
            }
            catch (IOException ioe) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }
        for (Map.Entry entry : this.handlers.entrySet()) {
            SocketHandler handler = (SocketHandler)entry.getValue();
            handler.shutdown();
            try {
                handler.join();
            }
            catch (InterruptedException ie) {}
        }
    }

    private static class ServerConfigurationFactory
    extends XMLConfigurationFactory {
        private final String path;

        public ServerConfigurationFactory(String path) {
            this.path = path;
        }

        @Override
        public Configuration getConfiguration(String name, URI configLocation) {
            if (this.path != null && this.path.length() > 0) {
                File file = null;
                ConfigurationFactory.ConfigurationSource source = null;
                try {
                    file = new File(this.path);
                    FileInputStream is = new FileInputStream(file);
                    source = new ConfigurationFactory.ConfigurationSource((InputStream)is, file);
                }
                catch (FileNotFoundException ex) {
                    // empty catch block
                }
                if (source == null) {
                    try {
                        URL url = new URL(this.path);
                        source = new ConfigurationFactory.ConfigurationSource(url.openStream(), this.path);
                    }
                    catch (MalformedURLException mue) {
                    }
                    catch (IOException ioe) {
                        // empty catch block
                    }
                }
                try {
                    if (source != null) {
                        return new XMLConfiguration(source);
                    }
                }
                catch (Exception ex) {
                    // empty catch block
                }
                System.err.println("Unable to process configuration at " + this.path + ", using default.");
            }
            return super.getConfiguration(name, configLocation);
        }
    }

    private class SocketHandler
    extends Thread {
        private final ObjectInputStream ois;
        private boolean shutdown = false;

        public SocketHandler(Socket socket) throws IOException {
            this.ois = new ObjectInputStream(socket.getInputStream());
        }

        public void shutdown() {
            this.shutdown = true;
            this.interrupt();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            block11: {
                boolean closed = false;
                try {
                    try {
                        while (!this.shutdown) {
                            LogEvent event = (LogEvent)this.ois.readObject();
                            if (event == null) continue;
                            SocketServer.this.log(event);
                        }
                    }
                    catch (EOFException eof) {
                        closed = true;
                    }
                    catch (OptionalDataException opt) {
                        SocketServer.this.logger.error("OptionalDataException eof=" + opt.eof + " length=" + opt.length, (Throwable)opt);
                    }
                    catch (ClassNotFoundException cnfe) {
                        SocketServer.this.logger.error("Unable to locate LogEvent class", (Throwable)cnfe);
                    }
                    catch (IOException ioe) {
                        SocketServer.this.logger.error("IOException encountered while reading from socket", (Throwable)ioe);
                    }
                    if (closed) break block11;
                    try {
                        this.ois.close();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                finally {
                    SocketServer.this.handlers.remove(this.getId());
                }
            }
        }
    }
}

