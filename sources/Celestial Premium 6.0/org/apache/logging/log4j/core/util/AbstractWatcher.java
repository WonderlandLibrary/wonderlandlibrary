/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Watcher;

public abstract class AbstractWatcher
implements Watcher {
    private final Reconfigurable reconfigurable;
    private final List<ConfigurationListener> configurationListeners;
    private final Log4jThreadFactory threadFactory;
    private final Configuration configuration;
    private Source source;

    public AbstractWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners) {
        this.configuration = configuration;
        this.reconfigurable = reconfigurable;
        this.configurationListeners = configurationListeners;
        this.threadFactory = configurationListeners != null ? Log4jThreadFactory.createDaemonThreadFactory("ConfiguratonFileWatcher") : null;
    }

    @Override
    public List<ConfigurationListener> getListeners() {
        return this.configurationListeners;
    }

    @Override
    public void modified() {
        for (ConfigurationListener configurationListener : this.configurationListeners) {
            Thread thread = this.threadFactory.newThread(new ReconfigurationRunnable(configurationListener, this.reconfigurable));
            thread.start();
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public abstract long getLastModified();

    @Override
    public abstract boolean isModified();

    @Override
    public void watching(Source source) {
        this.source = source;
    }

    @Override
    public Source getSource() {
        return this.source;
    }

    public static class ReconfigurationRunnable
    implements Runnable {
        private final ConfigurationListener configurationListener;
        private final Reconfigurable reconfigurable;

        public ReconfigurationRunnable(ConfigurationListener configurationListener, Reconfigurable reconfigurable) {
            this.configurationListener = configurationListener;
            this.reconfigurable = reconfigurable;
        }

        @Override
        public void run() {
            this.configurationListener.onChange(this.reconfigurable);
        }
    }
}

