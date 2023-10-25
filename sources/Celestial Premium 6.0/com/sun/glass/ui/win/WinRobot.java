/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.MouseButton
 *  javafx.scene.paint.Color
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.GlassRobot;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

final class WinRobot
extends GlassRobot {
    WinRobot() {
    }

    @Override
    public void create() {
    }

    @Override
    public void destroy() {
    }

    protected native void _keyPress(int var1);

    @Override
    public void keyPress(KeyCode keyCode) {
        Application.checkEventThread();
        this._keyPress(keyCode.getCode());
    }

    protected native void _keyRelease(int var1);

    @Override
    public void keyRelease(KeyCode keyCode) {
        Application.checkEventThread();
        this._keyRelease(keyCode.getCode());
    }

    protected native void _mouseMove(int var1, int var2);

    @Override
    public void mouseMove(double d, double d2) {
        Application.checkEventThread();
        this._mouseMove((int)d, (int)d2);
    }

    protected native void _mousePress(int var1);

    @Override
    public void mousePress(MouseButton ... arrmouseButton) {
        Application.checkEventThread();
        this._mousePress(GlassRobot.convertToRobotMouseButton(arrmouseButton));
    }

    protected native void _mouseRelease(int var1);

    @Override
    public void mouseRelease(MouseButton ... arrmouseButton) {
        Application.checkEventThread();
        this._mouseRelease(GlassRobot.convertToRobotMouseButton(arrmouseButton));
    }

    protected native void _mouseWheel(int var1);

    @Override
    public void mouseWheel(int n) {
        Application.checkEventThread();
        this._mouseWheel(n);
    }

    protected native float _getMouseX();

    @Override
    public double getMouseX() {
        Application.checkEventThread();
        return this._getMouseX();
    }

    protected native float _getMouseY();

    @Override
    public double getMouseY() {
        Application.checkEventThread();
        return this._getMouseY();
    }

    protected native int _getPixelColor(int var1, int var2);

    @Override
    public Color getPixelColor(double d, double d2) {
        Application.checkEventThread();
        return GlassRobot.convertFromIntArgb(this._getPixelColor((int)d, (int)d2));
    }

    protected native void _getScreenCapture(int var1, int var2, int var3, int var4, int[] var5);

    @Override
    public void getScreenCapture(int n, int n2, int n3, int n4, int[] arrn, boolean bl) {
        Application.checkEventThread();
        this._getScreenCapture(n, n2, n3, n4, arrn);
    }
}

