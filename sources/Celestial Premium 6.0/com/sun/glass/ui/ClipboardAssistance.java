/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import java.util.HashMap;

public class ClipboardAssistance {
    private final HashMap<String, Object> cacheData = new HashMap();
    private final Clipboard clipboard;
    private int supportedActions = 0x4FFFFFFF;

    public ClipboardAssistance(String string) {
        Application.checkEventThread();
        this.clipboard = Clipboard.get(string);
        this.clipboard.add(this);
    }

    public void close() {
        Application.checkEventThread();
        this.clipboard.remove(this);
    }

    public void flush() {
        Application.checkEventThread();
        this.clipboard.flush(this, this.cacheData, this.supportedActions);
    }

    public void emptyCache() {
        Application.checkEventThread();
        this.cacheData.clear();
    }

    public boolean isCacheEmpty() {
        Application.checkEventThread();
        return this.cacheData.isEmpty();
    }

    public void setData(String string, Object object) {
        Application.checkEventThread();
        this.cacheData.put(string, object);
    }

    public Object getData(String string) {
        Application.checkEventThread();
        return this.clipboard.getData(string);
    }

    public void setSupportedActions(int n) {
        Application.checkEventThread();
        this.supportedActions = n;
    }

    public int getSupportedSourceActions() {
        Application.checkEventThread();
        return this.clipboard.getSupportedSourceActions();
    }

    public void setTargetAction(int n) {
        Application.checkEventThread();
        this.clipboard.setTargetAction(n);
    }

    public void contentChanged() {
    }

    public void actionPerformed(int n) {
    }

    public String[] getMimeTypes() {
        Application.checkEventThread();
        return this.clipboard.getMimeTypes();
    }

    public String toString() {
        return "ClipboardAssistance[" + this.clipboard + "]";
    }
}

