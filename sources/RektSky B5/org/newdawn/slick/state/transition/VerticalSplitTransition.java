/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public class VerticalSplitTransition
implements Transition {
    protected static SGL GL = Renderer.get();
    private GameState prev;
    private float offset;
    private boolean finish;
    private Color background;

    public VerticalSplitTransition() {
    }

    public VerticalSplitTransition(Color background) {
        this.background = background;
    }

    public void init(GameState firstState, GameState secondState) {
        this.prev = secondState;
    }

    public boolean isComplete() {
        return this.finish;
    }

    public void postRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        Color c2;
        g2.translate(0.0f, -this.offset);
        g2.setClip(0, (int)(-this.offset), container.getWidth(), container.getHeight() / 2);
        if (this.background != null) {
            c2 = g2.getColor();
            g2.setColor(this.background);
            g2.fillRect(0.0f, 0.0f, container.getWidth(), container.getHeight());
            g2.setColor(c2);
        }
        GL.glPushMatrix();
        this.prev.render(container, game, g2);
        GL.glPopMatrix();
        g2.clearClip();
        g2.resetTransform();
        g2.translate(0.0f, this.offset);
        g2.setClip(0, (int)((float)(container.getHeight() / 2) + this.offset), container.getWidth(), container.getHeight() / 2);
        if (this.background != null) {
            c2 = g2.getColor();
            g2.setColor(this.background);
            g2.fillRect(0.0f, 0.0f, container.getWidth(), container.getHeight());
            g2.setColor(c2);
        }
        GL.glPushMatrix();
        this.prev.render(container, game, g2);
        GL.glPopMatrix();
        g2.clearClip();
        g2.translate(0.0f, -this.offset);
    }

    public void preRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
    }

    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
        this.offset += (float)delta * 1.0f;
        if (this.offset > (float)(container.getHeight() / 2)) {
            this.finish = true;
        }
    }
}

