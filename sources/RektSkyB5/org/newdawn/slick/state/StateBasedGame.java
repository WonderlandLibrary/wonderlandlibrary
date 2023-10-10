/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state;

import java.util.HashMap;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.Transition;

public abstract class StateBasedGame
implements Game,
InputListener {
    private HashMap states = new HashMap();
    private GameState currentState;
    private GameState nextState;
    private GameContainer container;
    private String title;
    private Transition enterTransition;
    private Transition leaveTransition;

    public StateBasedGame(String name) {
        this.title = name;
        this.currentState = new BasicGameState(){

            public int getID() {
                return -1;
            }

            public void init(GameContainer container, StateBasedGame game) throws SlickException {
            }

            public void render(StateBasedGame game, Graphics g2) throws SlickException {
            }

            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
            }

            public void render(GameContainer container, StateBasedGame game, Graphics g2) throws SlickException {
            }
        };
    }

    public void inputStarted() {
    }

    public int getStateCount() {
        return this.states.keySet().size();
    }

    public int getCurrentStateID() {
        return this.currentState.getID();
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public void setInput(Input input) {
    }

    public void addState(GameState state) {
        this.states.put(new Integer(state.getID()), state);
        if (this.currentState.getID() == -1) {
            this.currentState = state;
        }
    }

    public GameState getState(int id) {
        return (GameState)this.states.get(new Integer(id));
    }

    public void enterState(int id) {
        this.enterState(id, new EmptyTransition(), new EmptyTransition());
    }

    public void enterState(int id, Transition leave, Transition enter) {
        if (leave == null) {
            leave = new EmptyTransition();
        }
        if (enter == null) {
            enter = new EmptyTransition();
        }
        this.leaveTransition = leave;
        this.enterTransition = enter;
        this.nextState = this.getState(id);
        if (this.nextState == null) {
            throw new RuntimeException("No game state registered with the ID: " + id);
        }
        this.leaveTransition.init(this.currentState, this.nextState);
    }

    public final void init(GameContainer container) throws SlickException {
        this.container = container;
        this.initStatesList(container);
        for (GameState state : this.states.values()) {
            state.init(container, this);
        }
        if (this.currentState != null) {
            this.currentState.enter(container, this);
        }
    }

    public abstract void initStatesList(GameContainer var1) throws SlickException;

    public final void render(GameContainer container, Graphics g2) throws SlickException {
        this.preRenderState(container, g2);
        if (this.leaveTransition != null) {
            this.leaveTransition.preRender(this, container, g2);
        } else if (this.enterTransition != null) {
            this.enterTransition.preRender(this, container, g2);
        }
        this.currentState.render(container, this, g2);
        if (this.leaveTransition != null) {
            this.leaveTransition.postRender(this, container, g2);
        } else if (this.enterTransition != null) {
            this.enterTransition.postRender(this, container, g2);
        }
        this.postRenderState(container, g2);
    }

    protected void preRenderState(GameContainer container, Graphics g2) throws SlickException {
    }

    protected void postRenderState(GameContainer container, Graphics g2) throws SlickException {
    }

    public final void update(GameContainer container, int delta) throws SlickException {
        this.preUpdateState(container, delta);
        if (this.leaveTransition != null) {
            this.leaveTransition.update(this, container, delta);
            if (this.leaveTransition.isComplete()) {
                this.currentState.leave(container, this);
                GameState prevState = this.currentState;
                this.currentState = this.nextState;
                this.nextState = null;
                this.leaveTransition = null;
                this.currentState.enter(container, this);
                if (this.enterTransition != null) {
                    this.enterTransition.init(this.currentState, prevState);
                }
            } else {
                return;
            }
        }
        if (this.enterTransition != null) {
            this.enterTransition.update(this, container, delta);
            if (this.enterTransition.isComplete()) {
                this.enterTransition = null;
            } else {
                return;
            }
        }
        this.currentState.update(container, this, delta);
        this.postUpdateState(container, delta);
    }

    protected void preUpdateState(GameContainer container, int delta) throws SlickException {
    }

    protected void postUpdateState(GameContainer container, int delta) throws SlickException {
    }

    private boolean transitioning() {
        return this.leaveTransition != null || this.enterTransition != null;
    }

    public boolean closeRequested() {
        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public GameContainer getContainer() {
        return this.container;
    }

    public void controllerButtonPressed(int controller, int button) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerButtonPressed(controller, button);
    }

    public void controllerButtonReleased(int controller, int button) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerButtonReleased(controller, button);
    }

    public void controllerDownPressed(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerDownPressed(controller);
    }

    public void controllerDownReleased(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerDownReleased(controller);
    }

    public void controllerLeftPressed(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerLeftPressed(controller);
    }

    public void controllerLeftReleased(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerLeftReleased(controller);
    }

    public void controllerRightPressed(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerRightPressed(controller);
    }

    public void controllerRightReleased(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerRightReleased(controller);
    }

    public void controllerUpPressed(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerUpPressed(controller);
    }

    public void controllerUpReleased(int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerUpReleased(controller);
    }

    public void keyPressed(int key, char c2) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.keyPressed(key, c2);
    }

    public void keyReleased(int key, char c2) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.keyReleased(key, c2);
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseMoved(oldx, oldy, newx, newy);
    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseDragged(oldx, oldy, newx, newy);
    }

    public void mouseClicked(int button, int x2, int y2, int clickCount) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseClicked(button, x2, y2, clickCount);
    }

    public void mousePressed(int button, int x2, int y2) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mousePressed(button, x2, y2);
    }

    public void mouseReleased(int button, int x2, int y2) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseReleased(button, x2, y2);
    }

    public boolean isAcceptingInput() {
        if (this.transitioning()) {
            return false;
        }
        return this.currentState.isAcceptingInput();
    }

    public void inputEnded() {
    }

    public void mouseWheelMoved(int newValue) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseWheelMoved(newValue);
    }
}

