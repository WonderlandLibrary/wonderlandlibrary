/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.io.File;
import java.util.List;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.ConfigurationMonitor;
import org.apache.logging.log4j.core.config.Reconfigurable;

public class FileConfigurationMonitor
implements ConfigurationMonitor {
    private static final int MASK = 15;
    private static final int MIN_INTERVAL = 5;
    private static final int MILLIS_PER_SECOND = 1000;
    private final File file;
    private long lastModified;
    private final List<ConfigurationListener> listeners;
    private final int interval;
    private long nextCheck;
    private volatile int counter = 0;
    private final Reconfigurable reconfigurable;

    public FileConfigurationMonitor(Reconfigurable reconfigurable, File file, List<ConfigurationListener> listeners, int interval) {
        this.reconfigurable = reconfigurable;
        this.file = file;
        this.lastModified = file.lastModified();
        this.listeners = listeners;
        this.interval = (interval < 5 ? 5 : interval) * 1000;
        this.nextCheck = System.currentTimeMillis() + (long)interval;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void checkConfiguration() {
        if ((++this.counter & 0xF) == 0) {
            FileConfigurationMonitor fileConfigurationMonitor = this;
            synchronized (fileConfigurationMonitor) {
                long current = System.currentTimeMillis();
                if (current >= this.nextCheck) {
                    this.nextCheck = current + (long)this.interval;
                    if (this.file.lastModified() > this.lastModified) {
                        this.lastModified = this.file.lastModified();
                        for (ConfigurationListener listener : this.listeners) {
                            listener.onChange(this.reconfigurable);
                        }
                    }
                }
            }
        }
    }
}

