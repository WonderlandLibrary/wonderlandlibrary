/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util;

import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

public class InputAdapter
implements InputListener {
    private boolean acceptingInput = true;

    public void controllerButtonPressed(int controller, int button) {
    }

    public void controllerButtonReleased(int controller, int button) {
    }

    public void controllerDownPressed(int controller) {
    }

    public void controllerDownReleased(int controller) {
    }

    public void controllerLeftPressed(int controller) {
    }

    public void controllerLeftReleased(int controller) {
    }

    public void controllerRightPressed(int controller) {
    }

    public void controllerRightReleased(int controller) {
    }

    public void controllerUpPressed(int controller) {
    }

    public void controllerUpReleased(int controller) {
    }

    public void inputEnded() {
    }

    public boolean isAcceptingInput() {
        return this.acceptingInput;
    }

    public void setAcceptingInput(boolean acceptingInput) {
        this.acceptingInput = acceptingInput;
    }

    public void keyPressed(int key, char c2) {
    }

    public void keyReleased(int key, char c2) {
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    }

    public void mousePressed(int button, int x2, int y2) {
    }

    public void mouseReleased(int button, int x2, int y2) {
    }

    public void mouseWheelMoved(int change) {
    }

    public void setInput(Input input) {
    }

    public void mouseClicked(int button, int x2, int y2, int clickCount) {
    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    public void inputStarted() {
    }
}

