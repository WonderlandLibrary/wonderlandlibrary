/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state.transition;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public abstract class CrossStateTransition
implements Transition {
    private GameState secondState;

    public CrossStateTransition(GameState secondState) {
        this.secondState = secondState;
    }

    public abstract boolean isComplete();

    public void postRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        this.preRenderSecondState(game, container, g2);
        this.secondState.render(container, game, g2);
        this.postRenderSecondState(game, container, g2);
    }

    public void preRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        this.preRenderFirstState(game, container, g2);
    }

    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
    }

    public void preRenderFirstState(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }

    public void preRenderSecondState(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }

    public void postRenderSecondState(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }
}

