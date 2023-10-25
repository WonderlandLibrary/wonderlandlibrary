/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.binding.ExpressionHelper
 *  javafx.beans.InvalidationListener
 *  javafx.beans.binding.BooleanExpression
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.stage.Window
 */
package com.sun.javafx.scene;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.scene.NodeHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

public class TreeShowingExpression
extends BooleanExpression {
    private final ChangeListener<Boolean> windowShowingChangedListener = (observableValue, bl, bl2) -> this.updateTreeShowing();
    private final ChangeListener<Window> sceneWindowChangedListener = (observableValue, window, window2) -> this.windowChanged((Window)window, (Window)window2);
    private final ChangeListener<Scene> nodeSceneChangedListener = (observableValue, scene, scene2) -> this.sceneChanged((Scene)scene, (Scene)scene2);
    private final Node node;
    private ExpressionHelper<Boolean> helper;
    private boolean valid;
    private boolean treeShowing;

    public TreeShowingExpression(Node node) {
        this.node = node;
        this.node.sceneProperty().addListener(this.nodeSceneChangedListener);
        NodeHelper.treeVisibleProperty(node).addListener(this.windowShowingChangedListener);
        this.sceneChanged(null, node.getScene());
    }

    public void dispose() {
        this.node.sceneProperty().removeListener(this.nodeSceneChangedListener);
        NodeHelper.treeVisibleProperty(this.node).removeListener(this.windowShowingChangedListener);
        this.valid = false;
        this.sceneChanged(this.node.getScene(), null);
    }

    public void addListener(InvalidationListener invalidationListener) {
        this.helper = ExpressionHelper.addListener(this.helper, (ObservableValue)this, (InvalidationListener)invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, (InvalidationListener)invalidationListener);
    }

    public void addListener(ChangeListener<? super Boolean> changeListener) {
        this.helper = ExpressionHelper.addListener(this.helper, (ObservableValue)this, changeListener);
    }

    public void removeListener(ChangeListener<? super Boolean> changeListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
    }

    protected void invalidate() {
        if (this.valid) {
            this.valid = false;
            ExpressionHelper.fireValueChangedEvent(this.helper);
        }
    }

    public boolean get() {
        if (!this.valid) {
            this.updateTreeShowing();
            this.valid = true;
        }
        return this.treeShowing;
    }

    private void sceneChanged(Scene scene, Scene scene2) {
        if (scene != null) {
            scene.windowProperty().removeListener(this.sceneWindowChangedListener);
        }
        if (scene2 != null) {
            scene2.windowProperty().addListener(this.sceneWindowChangedListener);
        }
        this.windowChanged(scene == null ? null : scene.getWindow(), scene2 == null ? null : scene2.getWindow());
    }

    private void windowChanged(Window window, Window window2) {
        if (window != null) {
            window.showingProperty().removeListener(this.windowShowingChangedListener);
        }
        if (window2 != null) {
            window2.showingProperty().addListener(this.windowShowingChangedListener);
        }
        this.updateTreeShowing();
    }

    private void updateTreeShowing() {
        boolean bl = NodeHelper.isTreeShowing(this.node);
        if (bl != this.treeShowing) {
            this.treeShowing = bl;
            this.invalidate();
        }
    }
}

