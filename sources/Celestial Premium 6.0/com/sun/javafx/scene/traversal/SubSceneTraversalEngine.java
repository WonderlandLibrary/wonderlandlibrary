/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Parent
 *  javafx.scene.SubScene
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import javafx.scene.Parent;
import javafx.scene.SubScene;

public final class SubSceneTraversalEngine
extends TopMostTraversalEngine {
    private final SubScene subScene;

    public SubSceneTraversalEngine(SubScene subScene) {
        this.subScene = subScene;
    }

    @Override
    protected Parent getRoot() {
        return this.subScene.getRoot();
    }
}

