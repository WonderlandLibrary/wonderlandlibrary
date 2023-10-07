/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.xml;

import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.xml.DOMConfigurator;

class XMLWatchdog
extends FileWatchdog {
    XMLWatchdog(String filename) {
        super(filename);
    }

    public void doOnChange() {
        new DOMConfigurator().doConfigure(this.filename, LogManager.getLoggerRepository());
    }
}

