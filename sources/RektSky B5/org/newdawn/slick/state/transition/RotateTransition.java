/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public class RotateTransition
implements Transition {
    private GameState prev;
    private float ang;
    private boolean finish;
    private float scale = 1.0f;
    private Color background;

    public RotateTransition() {
    }

    public RotateTransition(Color background) {
        this.background = background;
    }

    public void init(GameState firstState, GameState secondState) {
        this.prev = secondState;
    }

    public boolean isComplete() {
        return this.finish;
    }

    public void postRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        g2.translate(container.getWidth() / 2, container.getHeight() / 2);
        g2.scale(this.scale, this.scale);
        g2.rotate(0.0f, 0.0f, this.ang);
        g2.translate(-container.getWidth() / 2, -container.getHeight() / 2);
        if (this.background != null) {
            Color c2 = g2.getColor();
            g2.setColor(this.background);
            g2.fillRect(0.0f, 0.0f, container.getWidth(), container.getHeight());
            g2.setColor(c2);
        }
        this.prev.render(container, game, g2);
        g2.translate(container.getWidth() / 2, container.getHeight() / 2);
        g2.rotate(0.0f, 0.0f, -this.ang);
        g2.scale(1.0f / this.scale, 1.0f / this.scale);
        g2.translate(-container.getWidth() / 2, -container.getHeight() / 2);
    }

    public void preRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }

    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
        this.ang += (float)delta * 0.5f;
        if (this.ang > 500.0f) {
            this.finish = true;
        }
        this.scale -= (float)delta * 0.001f;
        if (this.scale < 0.0f) {
            this.scale = 0.0f;
        }
    }
}

