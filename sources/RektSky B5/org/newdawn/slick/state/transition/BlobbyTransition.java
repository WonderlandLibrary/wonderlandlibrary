/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.state.transition;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.util.MaskUtil;

public class BlobbyTransition
implements Transition {
    protected static SGL GL = Renderer.get();
    private GameState prev;
    private boolean finish;
    private Color background;
    private ArrayList blobs = new ArrayList();
    private int timer = 1000;
    private int blobCount = 10;

    public BlobbyTransition() {
    }

    public BlobbyTransition(Color background) {
        this.background = background;
    }

    public void init(GameState firstState, GameState secondState) {
        this.prev = secondState;
    }

    public boolean isComplete() {
        return this.finish;
    }

    public void postRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        MaskUtil.resetMask();
    }

    public void preRender(StateBasedGame game, GameContainer container, Graphics g2) throws SlickException {
        this.prev.render(container, game, g2);
        MaskUtil.defineMask();
        for (int i2 = 0; i2 < this.blobs.size(); ++i2) {
            ((Blob)this.blobs.get(i2)).render(g2);
        }
        MaskUtil.finishDefineMask();
        MaskUtil.drawOnMask();
        if (this.background != null) {
            Color c2 = g2.getColor();
            g2.setColor(this.background);
            g2.fillRect(0.0f, 0.0f, container.getWidth(), container.getHeight());
            g2.setColor(c2);
        }
    }

    public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
        int i2;
        if (this.blobs.size() == 0) {
            for (i2 = 0; i2 < this.blobCount; ++i2) {
                this.blobs.add(new Blob(container));
            }
        }
        for (i2 = 0; i2 < this.blobs.size(); ++i2) {
            ((Blob)this.blobs.get(i2)).update(delta);
        }
        this.timer -= delta;
        if (this.timer < 0) {
            this.finish = true;
        }
    }

    private class Blob {
        private float x;
        private float y;
        private float growSpeed;
        private float rad;

        public Blob(GameContainer container) {
            this.x = (float)(Math.random() * (double)container.getWidth());
            this.y = (float)(Math.random() * (double)container.getWidth());
            this.growSpeed = (float)(1.0 + Math.random() * 1.0);
        }

        public void update(int delta) {
            this.rad += this.growSpeed * (float)delta * 0.4f;
        }

        public void render(Graphics g2) {
            g2.fillOval(this.x - this.rad, this.y - this.rad, this.rad * 2.0f, this.rad * 2.0f);
        }
    }
}

