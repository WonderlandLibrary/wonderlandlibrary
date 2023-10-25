/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.collections.ObservableIntegerArrayImpl
 *  javafx.collections.ObservableIntegerArray
 *  javafx.scene.shape.ObservableFaceArray
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.collections.ObservableIntegerArray;
import javafx.scene.shape.ObservableFaceArray;

public class ObservableFaceArrayImpl
extends ObservableIntegerArrayImpl
implements ObservableFaceArray {
    public ObservableFaceArrayImpl() {
    }

    public ObservableFaceArrayImpl(int ... arrn) {
        super(arrn);
    }

    public ObservableFaceArrayImpl(ObservableFaceArray observableFaceArray) {
        super((ObservableIntegerArray)observableFaceArray);
    }
}

