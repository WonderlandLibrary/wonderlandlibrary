/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.slf4j.ILoggerFactory
 *  org.slf4j.IMarkerFactory
 *  org.slf4j.spi.MDCAdapter
 *  org.slf4j.spi.SLF4JServiceProvider
 */
package org.apache.logging.slf4j;

import org.apache.logging.slf4j.Log4jLoggerFactory;
import org.apache.logging.slf4j.Log4jMDCAdapter;
import org.apache.logging.slf4j.Log4jMarkerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MDCAdapter;

public class SLF4JServiceProvider
implements org.slf4j.spi.SLF4JServiceProvider {
    public static final String REQUESTED_API_VERSION = "1.8.99";
    private ILoggerFactory loggerFactory;
    private Log4jMarkerFactory markerFactory;
    private MDCAdapter mdcAdapter;

    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }

    public MDCAdapter getMDCAdapter() {
        return this.mdcAdapter;
    }

    public String getRequesteApiVersion() {
        return REQUESTED_API_VERSION;
    }

    public void initialize() {
        this.markerFactory = new Log4jMarkerFactory();
        this.loggerFactory = new Log4jLoggerFactory(this.markerFactory);
        this.mdcAdapter = new Log4jMDCAdapter();
    }
}

