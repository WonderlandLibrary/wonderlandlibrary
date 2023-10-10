/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class BasicGameState
implements GameState {
    public void inputStarted() {
    }

    public boolean isAcceptingInput() {
        return true;
    }

    public void setInput(Input input) {
    }

    public void inputEnded() {
    }

    public abstract int getID();

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

    public void keyPressed(int key, char c2) {
    }

    public void keyReleased(int key, char c2) {
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    public void mouseClicked(int button, int x2, int y2, int clickCount) {
    }

    public void mousePressed(int button, int x2, int y2) {
    }

    public void mouseReleased(int button, int x2, int y2) {
    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    }

    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    }

    public void mouseWheelMoved(int newValue) {
    }
}

