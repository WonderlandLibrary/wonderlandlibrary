/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.state.transition;

import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.Renderer;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.SGL;
import me.kiras.aimwhere.libraries.slick.state.GameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;
import me.kiras.aimwhere.libraries.slick.state.transition.Transition;

public class HorizontalSplitTransition
implements Transition {
    protected static SGL GL = Renderer.get();
    private GameState prev;
    private float offset;
    private boolean finish;
    private Color background;

    public HorizontalSplitTransition() {
    }

    public HorizontalSplitTransition(Color background) {
        this.background = background;
    }

    @Override
    public void init(GameState firstState, GameState secondState) {
        this.prev = secondState;
    }

    @Override
    public boolean isComplete() {
        return this.finish;
    }

    @Override
    public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
        Color c;
        g.translate(-this.offset, 0.0f);
        g.setClip((int)(-this.offset), 0, container.getWidth() / 2, container.getHeight());
        if (this.background != null) {
            c = g.getColor();
            g.setColor(this.background);
            g.fillRect(0.0f, 0.0f, container.getWidth(), container.getHeight());
            g.setColor(c);
        }
        GL.glPushMatrix();
        this.prev.render(container, game, g);
        GL.glPopMatrix();
        g.clearClip();
        g.translate(this.offset * 2.0f, 0.0f);
        g.setClip((int)((float)(container.getWidth() / 2) + this.offset), 0, container.getWidth() / 2, container.getHeight());
        if (this.background != null) {
            c = g.getColor();
            g.setColor(this.background);
            g.fillRect(0.0f, 0.0f, container.getWidth(), container.getHeight());
            g.setColor(c);
        }
        GL.glPushMatrix();
        this.prev.render(container, game, g);
        GL.glPopMatrix();
        g.clearClip();
        g.translate(-this.offset, 0.0f);
    }

    @Override
    public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    }

    @Override
    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
        this.offset += (float)delta * 1.0f;
        if (this.offset > (float)(container.getWidth() / 2)) {
            this.finish = true;
        }
    }
}

