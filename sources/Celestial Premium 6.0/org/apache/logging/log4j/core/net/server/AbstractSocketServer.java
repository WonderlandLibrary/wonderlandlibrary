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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEventListener;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.apache.logging.log4j.core.net.server.LogEventBridge;
import org.apache.logging.log4j.core.util.BasicCommandLineArguments;
import org.apache.logging.log4j.core.util.InetAddressConverter;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.util.Strings;

public abstract class AbstractSocketServer<T extends InputStream>
extends LogEventListener
implements Runnable {
    protected static final int MAX_PORT = 65534;
    private volatile boolean active = true;
    protected final LogEventBridge<T> logEventInput;
    protected final Logger logger;

    public AbstractSocketServer(int port, LogEventBridge<T> logEventInput) {
        this.logger = LogManager.getLogger(this.getClass().getName() + '.' + port);
        this.logEventInput = Objects.requireNonNull(logEventInput, "LogEventInput");
    }

    protected boolean isActive() {
        return this.active;
    }

    protected void setActive(boolean isActive) {
        this.active = isActive;
    }

    public Thread startNewThread() {
        Log4jThread thread = new Log4jThread(this);
        thread.start();
        return thread;
    }

    public abstract void shutdown() throws Exception;

    public void awaitTermination(Thread serverThread) throws Exception {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!((line = reader.readLine()) == null || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("exit"))) {
        }
        this.shutdown();
        serverThread.join();
    }

    protected static class ServerConfigurationFactory
    extends XmlConfigurationFactory {
        private final String path;

        public ServerConfigurationFactory(String path) {
            this.path = path;
        }

        public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
            if (Strings.isNotEmpty(this.path)) {
                File file = null;
                ConfigurationSource source = null;
                try {
                    file = new File(this.path);
                    FileInputStream is = new FileInputStream(file);
                    source = new ConfigurationSource((InputStream)is, file);
                }
                catch (FileNotFoundException is) {
                    // empty catch block
                }
                if (source == null) {
                    try {
                        URL url = new URL(this.path);
                        source = new ConfigurationSource(url.openStream(), url);
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                try {
                    if (source != null) {
                        return new XmlConfiguration(loggerContext, source);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                System.err.println("Unable to process configuration at " + this.path + ", using default.");
            }
            return super.getConfiguration(loggerContext, name, configLocation);
        }
    }

    protected static class CommandLineArguments
    extends BasicCommandLineArguments {
        @Parameter(names={"--config", "-c"}, description="Log4j configuration file location (path or URL).")
        private String configLocation;
        @Parameter(names={"--interactive", "-i"}, description="Accepts commands on standard input (\"exit\" is the only command).")
        private boolean interactive;
        @Parameter(names={"--port", "-p"}, validateWith=PositiveInteger.class, description="Server socket port.")
        private int port;
        @Parameter(names={"--localbindaddress", "-a"}, converter=InetAddressConverter.class, description="Server socket local bind address.")
        private InetAddress localBindAddress;

        protected CommandLineArguments() {
        }

        String getConfigLocation() {
            return this.configLocation;
        }

        int getPort() {
            return this.port;
        }

        protected boolean isInteractive() {
            return this.interactive;
        }

        void setConfigLocation(String configLocation) {
            this.configLocation = configLocation;
        }

        void setInteractive(boolean interactive) {
            this.interactive = interactive;
        }

        void setPort(int port) {
            this.port = port;
        }

        InetAddress getLocalBindAddress() {
            return this.localBindAddress;
        }

        void setLocalBindAddress(InetAddress localBindAddress) {
            this.localBindAddress = localBindAddress;
        }
    }
}

