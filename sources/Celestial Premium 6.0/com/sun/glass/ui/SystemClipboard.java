/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.ClipboardAssistance;
import java.util.HashMap;

public abstract class SystemClipboard
extends Clipboard {
    protected SystemClipboard(String string) {
        super(string);
        Application.checkEventThread();
    }

    protected abstract boolean isOwner();

    protected abstract void pushToSystem(HashMap<String, Object> var1, int var2);

    protected abstract void pushTargetActionToSystem(int var1);

    protected abstract Object popFromSystem(String var1);

    protected abstract int supportedSourceActionsFromSystem();

    protected abstract String[] mimesFromSystem();

    @Override
    public void flush(ClipboardAssistance clipboardAssistance, HashMap<String, Object> hashMap, int n) {
        Application.checkEventThread();
        this.setSharedData(clipboardAssistance, hashMap, n);
        this.pushToSystem(hashMap, n);
    }

    @Override
    public int getSupportedSourceActions() {
        Application.checkEventThread();
        if (this.isOwner()) {
            return super.getSupportedSourceActions();
        }
        return this.supportedSourceActionsFromSystem();
    }

    @Override
    public void setTargetAction(int n) {
        Application.checkEventThread();
        this.pushTargetActionToSystem(n);
    }

    public Object getLocalData(String string) {
        return super.getData(string);
    }

    @Override
    public Object getData(String string) {
        Application.checkEventThread();
        if (this.isOwner()) {
            return this.getLocalData(string);
        }
        return this.popFromSystem(string);
    }

    @Override
    public String[] getMimeTypes() {
        Application.checkEventThread();
        if (this.isOwner()) {
            return super.getMimeTypes();
        }
        return this.mimesFromSystem();
    }

    @Override
    public String toString() {
        Application.checkEventThread();
        return "System Clipboard";
    }
}

