/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state.transition;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public class CombinedTransition
implements Transition {
    private ArrayList transitions = new ArrayList();

    public void addTransition(Transition t2) {
        this.transitions.add(t2);
    }

    public boolean isComplete() {
        for (int i2 = 0; i2 < this.transitions.size(); ++i2) {
            if (((Transition)this.transitions.get(i2)).isComplete()) continue;
            return false;
        }
        return true;
    }

    public void postRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        for (int i2 = this.transitions.size() - 1; i2 >= 0; --i2) {
            ((Transition)this.transitions.get(i2)).postRender(game, container, g2);
        }
    }

    public void preRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        for (int i2 = 0; i2 < this.transitions.size(); ++i2) {
            ((Transition)this.transitions.get(i2)).postRender(game, container, g2);
        }
    }

    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
        for (int i2 = 0; i2 < this.transitions.size(); ++i2) {
            Transition t2 = (Transition)this.transitions.get(i2);
            if (t2.isComplete()) continue;
            t2.update(game, container, delta);
        }
    }

    public void init(GameState firstState, GameState secondState) {
        for (int i2 = this.transitions.size() - 1; i2 >= 0; --i2) {
            ((Transition)this.transitions.get(i2)).init(firstState, secondState);
        }
    }
}

