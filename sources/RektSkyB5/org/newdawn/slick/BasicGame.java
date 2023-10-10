/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;

public abstract class BasicGame
implements Game,
InputListener {
    private static final int MAX_CONTROLLERS = 20;
    private static final int MAX_CONTROLLER_BUTTONS = 100;
    private String title;
    protected boolean[] controllerLeft = new boolean[20];
    protected boolean[] controllerRight = new boolean[20];
    protected boolean[] controllerUp = new boolean[20];
    protected boolean[] controllerDown = new boolean[20];
    protected boolean[][] controllerButton = new boolean[20][100];

    public BasicGame(String title) {
        this.title = title;
    }

    public void setInput(Input input) {
    }

    public boolean closeRequested() {
        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public abstract void init(GameContainer var1) throws SlickException;

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

    public void controllerButtonPressed(int controller, int button) {
        this.controllerButton[controller][button] = true;
    }

    public void controllerButtonReleased(int controller, int button) {
        this.controllerButton[controller][button] = false;
    }

    public void controllerDownPressed(int controller) {
        this.controllerDown[controller] = true;
    }

    public void controllerDownReleased(int controller) {
        this.controllerDown[controller] = false;
    }

    public void controllerLeftPressed(int controller) {
        this.controllerLeft[controller] = true;
    }

    public void controllerLeftReleased(int controller) {
        this.controllerLeft[controller] = false;
    }

    public void controllerRightPressed(int controller) {
        this.controllerRight[controller] = true;
    }

    public void controllerRightReleased(int controller) {
        this.controllerRight[controller] = false;
    }

    public void controllerUpPressed(int controller) {
        this.controllerUp[controller] = true;
    }

    public void controllerUpReleased(int controller) {
        this.controllerUp[controller] = false;
    }

    public void mouseReleased(int button, int x2, int y2) {
    }

    public abstract void update(GameContainer var1, int var2) throws SlickException;

    public void mouseWheelMoved(int change) {
    }

    public boolean isAcceptingInput() {
        return true;
    }

    public void inputEnded() {
    }

    public void inputStarted() {
    }
}

