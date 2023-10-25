/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.util.AbstractWatcher;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Watcher;

public class WrappedFileWatcher
extends AbstractWatcher
implements FileWatcher {
    private final FileWatcher watcher;
    private volatile long lastModifiedMillis;

    public WrappedFileWatcher(FileWatcher watcher, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
        super(configuration, reconfigurable, configurationListeners);
        this.watcher = watcher;
        this.lastModifiedMillis = lastModifiedMillis;
    }

    public WrappedFileWatcher(FileWatcher watcher) {
        super(null, null, null);
        this.watcher = watcher;
    }

    @Override
    public long getLastModified() {
        return this.lastModifiedMillis;
    }

    @Override
    public void fileModified(File file) {
        this.watcher.fileModified(file);
    }

    @Override
    public boolean isModified() {
        long lastModified = this.getSource().getFile().lastModified();
        if (this.lastModifiedMillis != lastModified) {
            this.lastModifiedMillis = lastModified;
            return true;
        }
        return false;
    }

    @Override
    public List<ConfigurationListener> getListeners() {
        if (super.getListeners() != null) {
            return Collections.unmodifiableList(super.getListeners());
        }
        return null;
    }

    @Override
    public void modified() {
        if (this.getListeners() != null) {
            super.modified();
        }
        this.fileModified(this.getSource().getFile());
        this.lastModifiedMillis = this.getSource().getFile().lastModified();
    }

    @Override
    public void watching(Source source) {
        this.lastModifiedMillis = source.getFile().lastModified();
        super.watching(source);
    }

    @Override
    public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
        WrappedFileWatcher watcher = new WrappedFileWatcher(this.watcher, this.getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
        if (this.getSource() != null) {
            watcher.watching(this.getSource());
        }
        return watcher;
    }
}

