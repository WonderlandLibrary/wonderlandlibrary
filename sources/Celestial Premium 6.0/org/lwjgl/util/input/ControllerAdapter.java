/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.input;

import org.lwjgl.input.Controller;

public class ControllerAdapter
implements Controller {
    public String getName() {
        return "Dummy Controller";
    }

    public int getIndex() {
        return 0;
    }

    public int getButtonCount() {
        return 0;
    }

    public String getButtonName(int index) {
        return "button n/a";
    }

    public boolean isButtonPressed(int index) {
        return false;
    }

    public void poll() {
    }

    public float getPovX() {
        return 0.0f;
    }

    public float getPovY() {
        return 0.0f;
    }

    public float getDeadZone(int index) {
        return 0.0f;
    }

    public void setDeadZone(int index, float zone) {
    }

    public int getAxisCount() {
        return 0;
    }

    public String getAxisName(int index) {
        return "axis n/a";
    }

    public float getAxisValue(int index) {
        return 0.0f;
    }

    public float getXAxisValue() {
        return 0.0f;
    }

    public float getXAxisDeadZone() {
        return 0.0f;
    }

    public void setXAxisDeadZone(float zone) {
    }

    public float getYAxisValue() {
        return 0.0f;
    }

    public float getYAxisDeadZone() {
        return 0.0f;
    }

    public void setYAxisDeadZone(float zone) {
    }

    public float getZAxisValue() {
        return 0.0f;
    }

    public float getZAxisDeadZone() {
        return 0.0f;
    }

    public void setZAxisDeadZone(float zone) {
    }

    public float getRXAxisValue() {
        return 0.0f;
    }

    public float getRXAxisDeadZone() {
        return 0.0f;
    }

    public void setRXAxisDeadZone(float zone) {
    }

    public float getRYAxisValue() {
        return 0.0f;
    }

    public float getRYAxisDeadZone() {
        return 0.0f;
    }

    public void setRYAxisDeadZone(float zone) {
    }

    public float getRZAxisValue() {
        return 0.0f;
    }

    public float getRZAxisDeadZone() {
        return 0.0f;
    }

    public void setRZAxisDeadZone(float zone) {
    }

    public int getRumblerCount() {
        return 0;
    }

    public String getRumblerName(int index) {
        return "rumber n/a";
    }

    public void setRumblerStrength(int index, float strength) {
    }
}

