// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.Iterator;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import java.util.List;
import org.apache.logging.log4j.core.config.Reconfigurable;

public abstract class AbstractWatcher implements Watcher
{
    private final Reconfigurable reconfigurable;
    private final List<ConfigurationListener> configurationListeners;
    private final Log4jThreadFactory threadFactory;
    private final Configuration configuration;
    private Source source;
    
    public AbstractWatcher(final Configuration configuration, final Reconfigurable reconfigurable, final List<ConfigurationListener> configurationListeners) {
        this.configuration = configuration;
        this.reconfigurable = reconfigurable;
        this.configurationListeners = configurationListeners;
        this.threadFactory = ((configurationListeners != null) ? Log4jThreadFactory.createDaemonThreadFactory("ConfiguratonFileWatcher") : null);
    }
    
    @Override
    public List<ConfigurationListener> getListeners() {
        return this.configurationListeners;
    }
    
    @Override
    public void modified() {
        for (final ConfigurationListener configurationListener : this.configurationListeners) {
            final Thread thread = this.threadFactory.newThread(new ReconfigurationRunnable(configurationListener, this.reconfigurable));
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
    public void watching(final Source source) {
        this.source = source;
    }
    
    @Override
    public Source getSource() {
        return this.source;
    }
    
    public static class ReconfigurationRunnable implements Runnable
    {
        private final ConfigurationListener configurationListener;
        private final Reconfigurable reconfigurable;
        
        public ReconfigurationRunnable(final ConfigurationListener configurationListener, final Reconfigurable reconfigurable) {
            this.configurationListener = configurationListener;
            this.reconfigurable = reconfigurable;
        }
        
        @Override
        public void run() {
            this.configurationListener.onChange(this.reconfigurable);
        }
    }
}
