/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.quantum.GestureRecognizer;
import java.util.Collection;
import java.util.Vector;

class GestureRecognizers
implements GestureRecognizer {
    private Collection<GestureRecognizer> recognizers = new Vector<GestureRecognizer>();
    private GestureRecognizer[] workList;

    GestureRecognizers() {
    }

    void add(GestureRecognizer gestureRecognizer) {
        if (!this.contains(gestureRecognizer)) {
            this.recognizers.add(gestureRecognizer);
            this.workList = null;
        }
    }

    void remove(GestureRecognizer gestureRecognizer) {
        if (this.contains(gestureRecognizer)) {
            this.recognizers.remove(gestureRecognizer);
            this.workList = null;
        }
    }

    boolean contains(GestureRecognizer gestureRecognizer) {
        return this.recognizers.contains(gestureRecognizer);
    }

    private GestureRecognizer[] synchWorkList() {
        if (this.workList == null) {
            this.workList = this.recognizers.toArray(new GestureRecognizer[0]);
        }
        return this.workList;
    }

    @Override
    public void notifyBeginTouchEvent(long l, int n, boolean bl, int n2) {
        GestureRecognizer[] arrgestureRecognizer = this.synchWorkList();
        for (int i = 0; i != arrgestureRecognizer.length; ++i) {
            arrgestureRecognizer[i].notifyBeginTouchEvent(l, n, bl, n2);
        }
    }

    @Override
    public void notifyNextTouchEvent(long l, int n, long l2, int n2, int n3, int n4, int n5) {
        GestureRecognizer[] arrgestureRecognizer = this.synchWorkList();
        for (int i = 0; i != arrgestureRecognizer.length; ++i) {
            arrgestureRecognizer[i].notifyNextTouchEvent(l, n, l2, n2, n3, n4, n5);
        }
    }

    @Override
    public void notifyEndTouchEvent(long l) {
        GestureRecognizer[] arrgestureRecognizer = this.synchWorkList();
        for (int i = 0; i != arrgestureRecognizer.length; ++i) {
            arrgestureRecognizer[i].notifyEndTouchEvent(l);
        }
    }
}

