/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests.states;

import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.BasicGameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;
import me.kiras.aimwhere.libraries.slick.state.transition.FadeInTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.FadeOutTransition;

public class TestState2
extends BasicGameState {
    public static final int ID = 2;
    private Font font;
    private Image image;
    private float ang;
    private StateBasedGame game;

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.game = game;
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
        this.image = new Image("testdata/logo.tga");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setFont(this.font);
        g.setColor(Color.green);
        g.drawString("This is State 2", 200.0f, 50.0f);
        g.rotate(400.0f, 300.0f, this.ang);
        g.drawImage(this.image, 400 - this.image.getWidth() / 2, 300 - this.image.getHeight() / 2);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        this.ang += (float)delta * 0.1f;
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == 2) {
            this.game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
        if (key == 4) {
            this.game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }
}

