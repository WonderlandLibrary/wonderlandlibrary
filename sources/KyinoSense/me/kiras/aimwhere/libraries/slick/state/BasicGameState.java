/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.state;

import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.GameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;

public abstract class BasicGameState
implements GameState {
    @Override
    public void inputStarted() {
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public abstract int getID();

    @Override
    public void controllerButtonPressed(int controller, int button) {
    }

    @Override
    public void controllerButtonReleased(int controller, int button) {
    }

    @Override
    public void controllerDownPressed(int controller) {
    }

    @Override
    public void controllerDownReleased(int controller) {
    }

    @Override
    public void controllerLeftPressed(int controller) {
    }

    @Override
    public void controllerLeftReleased(int controller) {
    }

    @Override
    public void controllerRightPressed(int controller) {
    }

    @Override
    public void controllerRightReleased(int controller) {
    }

    @Override
    public void controllerUpPressed(int controller) {
    }

    @Override
    public void controllerUpReleased(int controller) {
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void mouseWheelMoved(int newValue) {
    }
}

