/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Camera
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.stage.Window
 */
package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.util.Utils;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

public final class SceneHelper {
    private static SceneAccessor sceneAccessor;

    private SceneHelper() {
    }

    public static void enableInputMethodEvents(Scene scene, boolean bl) {
        sceneAccessor.enableInputMethodEvents(scene, bl);
    }

    public static void processKeyEvent(Scene scene, KeyEvent keyEvent) {
        sceneAccessor.processKeyEvent(scene, keyEvent);
    }

    public static void processMouseEvent(Scene scene, MouseEvent mouseEvent) {
        sceneAccessor.processMouseEvent(scene, mouseEvent);
    }

    public static void preferredSize(Scene scene) {
        sceneAccessor.preferredSize(scene);
    }

    public static void disposePeer(Scene scene) {
        sceneAccessor.disposePeer(scene);
    }

    public static void initPeer(Scene scene) {
        sceneAccessor.initPeer(scene);
    }

    public static void setWindow(Scene scene, Window window) {
        sceneAccessor.setWindow(scene, window);
    }

    public static TKScene getPeer(Scene scene) {
        return sceneAccessor.getPeer(scene);
    }

    public static void setAllowPGAccess(boolean bl) {
        sceneAccessor.setAllowPGAccess(bl);
    }

    public static void parentEffectiveOrientationInvalidated(Scene scene) {
        sceneAccessor.parentEffectiveOrientationInvalidated(scene);
    }

    public static Camera getEffectiveCamera(Scene scene) {
        return sceneAccessor.getEffectiveCamera(scene);
    }

    public static Scene createPopupScene(Parent parent) {
        return sceneAccessor.createPopupScene(parent);
    }

    public static Accessible getAccessible(Scene scene) {
        return sceneAccessor.getAccessible(scene);
    }

    public static void setSceneAccessor(SceneAccessor sceneAccessor) {
        if (SceneHelper.sceneAccessor != null) {
            throw new IllegalStateException();
        }
        SceneHelper.sceneAccessor = sceneAccessor;
    }

    public static SceneAccessor getSceneAccessor() {
        if (sceneAccessor == null) {
            throw new IllegalStateException();
        }
        return sceneAccessor;
    }

    static {
        Utils.forceInit(Scene.class);
    }

    public static interface SceneAccessor {
        public void enableInputMethodEvents(Scene var1, boolean var2);

        public void processKeyEvent(Scene var1, KeyEvent var2);

        public void processMouseEvent(Scene var1, MouseEvent var2);

        public void preferredSize(Scene var1);

        public void disposePeer(Scene var1);

        public void initPeer(Scene var1);

        public void setWindow(Scene var1, Window var2);

        public TKScene getPeer(Scene var1);

        public void setAllowPGAccess(boolean var1);

        public void parentEffectiveOrientationInvalidated(Scene var1);

        public Camera getEffectiveCamera(Scene var1);

        public Scene createPopupScene(Parent var1);

        public void setTransientFocusContainer(Scene var1, Node var2);

        public Accessible getAccessible(Scene var1);
    }
}

