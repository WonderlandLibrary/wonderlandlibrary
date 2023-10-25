/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform;

import com.sun.jna.platform.win32.W32FileMonitor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FileMonitor {
    public static final int FILE_CREATED = 1;
    public static final int FILE_DELETED = 2;
    public static final int FILE_MODIFIED = 4;
    public static final int FILE_ACCESSED = 8;
    public static final int FILE_NAME_CHANGED_OLD = 16;
    public static final int FILE_NAME_CHANGED_NEW = 32;
    public static final int FILE_RENAMED = 48;
    public static final int FILE_SIZE_CHANGED = 64;
    public static final int FILE_ATTRIBUTES_CHANGED = 128;
    public static final int FILE_SECURITY_CHANGED = 256;
    public static final int FILE_ANY = 511;
    private final Map<File, Integer> watched = new HashMap<File, Integer>();
    private List<FileListener> listeners = new ArrayList<FileListener>();

    protected abstract void watch(File var1, int var2, boolean var3) throws IOException;

    protected abstract void unwatch(File var1);

    public abstract void dispose();

    public void addWatch(File dir) throws IOException {
        this.addWatch(dir, 511);
    }

    public void addWatch(File dir, int mask) throws IOException {
        this.addWatch(dir, mask, dir.isDirectory());
    }

    public void addWatch(File dir, int mask, boolean recursive) throws IOException {
        this.watched.put(dir, new Integer(mask));
        this.watch(dir, mask, recursive);
    }

    public void removeWatch(File file) {
        if (this.watched.remove(file) != null) {
            this.unwatch(file);
        }
    }

    protected void notify(FileEvent e) {
        for (FileListener listener : this.listeners) {
            listener.fileChanged(e);
        }
    }

    public synchronized void addFileListener(FileListener listener) {
        ArrayList<FileListener> list = new ArrayList<FileListener>(this.listeners);
        list.add(listener);
        this.listeners = list;
    }

    public synchronized void removeFileListener(FileListener x) {
        ArrayList<FileListener> list = new ArrayList<FileListener>(this.listeners);
        list.remove(x);
        this.listeners = list;
    }

    protected void finalize() {
        for (File watchedFile : this.watched.keySet()) {
            this.removeWatch(watchedFile);
        }
        this.dispose();
    }

    public static FileMonitor getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final FileMonitor INSTANCE;

        private Holder() {
        }

        static {
            String os = System.getProperty("os.name");
            if (!os.startsWith("Windows")) {
                throw new Error("FileMonitor not implemented for " + os);
            }
            INSTANCE = new W32FileMonitor();
        }
    }

    public class FileEvent
    extends EventObject {
        private final File file;
        private final int type;

        public FileEvent(File file, int type) {
            super(FileMonitor.this);
            this.file = file;
            this.type = type;
        }

        public File getFile() {
            return this.file;
        }

        public int getType() {
            return this.type;
        }

        public String toString() {
            return "FileEvent: " + this.file + ":" + this.type;
        }
    }

    public static interface FileListener {
        public void fileChanged(FileEvent var1);
    }
}

