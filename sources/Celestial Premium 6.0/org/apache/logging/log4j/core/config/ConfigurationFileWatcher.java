/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.io.File;
import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.util.AbstractWatcher;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Watcher;

public class ConfigurationFileWatcher
extends AbstractWatcher
implements FileWatcher {
    private File file;
    private long lastModifiedMillis;

    public ConfigurationFileWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
        super(configuration, reconfigurable, configurationListeners);
        this.lastModifiedMillis = lastModifiedMillis;
    }

    @Override
    public long getLastModified() {
        return this.file != null ? this.file.lastModified() : 0L;
    }

    @Override
    public void fileModified(File file) {
        this.lastModifiedMillis = file.lastModified();
    }

    @Override
    public void watching(Source source) {
        this.file = source.getFile();
        this.lastModifiedMillis = this.file.lastModified();
        super.watching(source);
    }

    @Override
    public boolean isModified() {
        return this.lastModifiedMillis != this.file.lastModified();
    }

    @Override
    public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
        ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(this.getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
        if (this.getSource() != null) {
            watcher.watching(this.getSource());
        }
        return watcher;
    }
}

