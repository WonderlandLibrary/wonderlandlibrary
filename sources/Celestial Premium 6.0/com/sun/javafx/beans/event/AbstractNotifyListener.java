/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.WeakInvalidationListener
 */
package com.sun.javafx.beans.event;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;

public abstract class AbstractNotifyListener
implements InvalidationListener {
    private final WeakInvalidationListener weakListener = new WeakInvalidationListener((InvalidationListener)this);

    public InvalidationListener getWeakListener() {
        return this.weakListener;
    }
}

