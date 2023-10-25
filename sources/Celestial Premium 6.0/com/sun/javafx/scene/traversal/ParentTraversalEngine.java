/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Parent
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.TraversalEngine;
import javafx.scene.Parent;

public final class ParentTraversalEngine
extends TraversalEngine {
    private final Parent root;
    private Boolean overridenTraversability;

    public ParentTraversalEngine(Parent parent, Algorithm algorithm) {
        super(algorithm);
        this.root = parent;
    }

    public ParentTraversalEngine(Parent parent) {
        this.root = parent;
    }

    public void setOverriddenFocusTraversability(Boolean bl) {
        this.overridenTraversability = bl;
    }

    @Override
    protected Parent getRoot() {
        return this.root;
    }

    public boolean isParentTraversable() {
        return this.overridenTraversability != null ? this.root.isFocusTraversable() && this.overridenTraversability.booleanValue() : this.root.isFocusTraversable();
    }
}

