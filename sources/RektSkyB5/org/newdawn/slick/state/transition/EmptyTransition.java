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

public class EmptyTransition
implements Transition {
    public boolean isComplete() {
        return true;
    }

    public void postRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }

    public void preRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }

    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
    }

    public void init(GameState firstState, GameState secondState) {
    }
}

