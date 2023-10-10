// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.Source;
import java.util.List;
import java.io.File;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.AbstractWatcher;

public class ConfigurationFileWatcher extends AbstractWatcher implements FileWatcher
{
    private File file;
    private long lastModifiedMillis;
    
    public ConfigurationFileWatcher(final Configuration configuration, final Reconfigurable reconfigurable, final List<ConfigurationListener> configurationListeners, final long lastModifiedMillis) {
        super(configuration, reconfigurable, configurationListeners);
        this.lastModifiedMillis = lastModifiedMillis;
    }
    
    @Override
    public long getLastModified() {
        return (this.file != null) ? this.file.lastModified() : 0L;
    }
    
    @Override
    public void fileModified(final File file) {
        this.lastModifiedMillis = file.lastModified();
    }
    
    @Override
    public void watching(final Source source) {
        this.file = source.getFile();
        this.lastModifiedMillis = this.file.lastModified();
        super.watching(source);
    }
    
    @Override
    public boolean isModified() {
        return this.lastModifiedMillis != this.file.lastModified();
    }
    
    @Override
    public Watcher newWatcher(final Reconfigurable reconfigurable, final List<ConfigurationListener> listeners, final long lastModifiedMillis) {
        final ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(this.getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
        if (this.getSource() != null) {
            watcher.watching(this.getSource());
        }
        return watcher;
    }
}
