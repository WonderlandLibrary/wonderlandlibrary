/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.state.transition;

import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.GameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;
import me.kiras.aimwhere.libraries.slick.state.transition.Transition;

public abstract class CrossStateTransition
implements Transition {
    private GameState secondState;

    public CrossStateTransition(GameState secondState) {
        this.secondState = secondState;
    }

    @Override
    public abstract boolean isComplete();

    @Override
    public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
        this.preRenderSecondState(game, container, g);
        this.secondState.render(container, game, g);
        this.postRenderSecondState(game, container, g);
    }

    @Override
    public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
        this.preRenderFirstState(game, container, g);
    }

    @Override
    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
    }

    public void preRenderFirstState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    }

    public void preRenderSecondState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    }

    public void postRenderSecondState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    }
}

