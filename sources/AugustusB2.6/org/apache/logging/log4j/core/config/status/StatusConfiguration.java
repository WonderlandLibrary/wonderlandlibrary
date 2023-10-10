// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.status;

import java.util.Iterator;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import java.io.File;
import java.net.URI;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.NetUtils;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Collection;
import org.apache.logging.log4j.Level;
import java.io.PrintStream;

public class StatusConfiguration
{
    private static final PrintStream DEFAULT_STREAM;
    private static final Level DEFAULT_STATUS;
    private static final Verbosity DEFAULT_VERBOSITY;
    private final Collection<String> errorMessages;
    private final StatusLogger logger;
    private volatile boolean initialized;
    private PrintStream destination;
    private Level status;
    private Verbosity verbosity;
    private String[] verboseClasses;
    
    public StatusConfiguration() {
        this.errorMessages = new LinkedBlockingQueue<String>();
        this.logger = StatusLogger.getLogger();
        this.destination = StatusConfiguration.DEFAULT_STREAM;
        this.status = StatusConfiguration.DEFAULT_STATUS;
        this.verbosity = StatusConfiguration.DEFAULT_VERBOSITY;
    }
    
    public void error(final String message) {
        if (!this.initialized) {
            this.errorMessages.add(message);
        }
        else {
            this.logger.error(message);
        }
    }
    
    public StatusConfiguration withDestination(final String destination) {
        try {
            this.destination = this.parseStreamName(destination);
        }
        catch (URISyntaxException e) {
            this.error("Could not parse URI [" + destination + "]. Falling back to default of stdout.");
            this.destination = StatusConfiguration.DEFAULT_STREAM;
        }
        catch (FileNotFoundException e2) {
            this.error("File could not be found at [" + destination + "]. Falling back to default of stdout.");
            this.destination = StatusConfiguration.DEFAULT_STREAM;
        }
        return this;
    }
    
    private PrintStream parseStreamName(final String name) throws URISyntaxException, FileNotFoundException {
        if (name == null || name.equalsIgnoreCase("out")) {
            return StatusConfiguration.DEFAULT_STREAM;
        }
        if (name.equalsIgnoreCase("err")) {
            return System.err;
        }
        final URI destUri = NetUtils.toURI(name);
        final File output = FileUtils.fileFromUri(destUri);
        if (output == null) {
            return StatusConfiguration.DEFAULT_STREAM;
        }
        final FileOutputStream fos = new FileOutputStream(output);
        return new PrintStream(fos, true);
    }
    
    public StatusConfiguration withStatus(final String status) {
        this.status = Level.toLevel(status, null);
        if (this.status == null) {
            this.error("Invalid status level specified: " + status + ". Defaulting to ERROR.");
            this.status = Level.ERROR;
        }
        return this;
    }
    
    public StatusConfiguration withStatus(final Level status) {
        this.status = status;
        return this;
    }
    
    public StatusConfiguration withVerbosity(final String verbosity) {
        this.verbosity = Verbosity.toVerbosity(verbosity);
        return this;
    }
    
    public StatusConfiguration withVerboseClasses(final String... verboseClasses) {
        this.verboseClasses = verboseClasses;
        return this;
    }
    
    public void initialize() {
        if (!this.initialized) {
            if (this.status == Level.OFF) {
                this.initialized = true;
            }
            else {
                final boolean configured = this.configureExistingStatusConsoleListener();
                if (!configured) {
                    this.registerNewStatusConsoleListener();
                }
                this.migrateSavedLogMessages();
            }
        }
    }
    
    private boolean configureExistingStatusConsoleListener() {
        boolean configured = false;
        for (final StatusListener statusListener : this.logger.getListeners()) {
            if (statusListener instanceof StatusConsoleListener) {
                final StatusConsoleListener listener = (StatusConsoleListener)statusListener;
                listener.setLevel(this.status);
                this.logger.updateListenerLevel(this.status);
                if (this.verbosity == Verbosity.QUIET) {
                    listener.setFilters(this.verboseClasses);
                }
                configured = true;
            }
        }
        return configured;
    }
    
    private void registerNewStatusConsoleListener() {
        final StatusConsoleListener listener = new StatusConsoleListener(this.status, this.destination);
        if (this.verbosity == Verbosity.QUIET) {
            listener.setFilters(this.verboseClasses);
        }
        this.logger.registerListener(listener);
    }
    
    private void migrateSavedLogMessages() {
        for (final String message : this.errorMessages) {
            this.logger.error(message);
        }
        this.initialized = true;
        this.errorMessages.clear();
    }
    
    static {
        DEFAULT_STREAM = System.out;
        DEFAULT_STATUS = Level.ERROR;
        DEFAULT_VERBOSITY = Verbosity.QUIET;
    }
    
    public enum Verbosity
    {
        QUIET, 
        VERBOSE;
        
        public static Verbosity toVerbosity(final String value) {
            return Boolean.parseBoolean(value) ? Verbosity.VERBOSE : Verbosity.QUIET;
        }
    }
}
