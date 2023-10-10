/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests.states;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TestState3
extends BasicGameState {
    public static final int ID = 3;
    private Font font;
    private String[] options = new String[]{"Start Game", "Credits", "Highscores", "Instructions", "Exit"};
    private int selected;
    private StateBasedGame game;

    public int getID() {
        return 3;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
        this.game = game;
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g2) {
        g2.setFont(this.font);
        g2.setColor(Color.blue);
        g2.drawString("This is State 3", 200.0f, 50.0f);
        g2.setColor(Color.white);
        for (int i2 = 0; i2 < this.options.length; ++i2) {
            g2.drawString(this.options[i2], 400 - this.font.getWidth(this.options[i2]) / 2, 200 + i2 * 50);
            if (this.selected != i2) continue;
            g2.drawRect(200.0f, 190 + i2 * 50, 400.0f, 50.0f);
        }
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
    }

    public void keyReleased(int key, char c2) {
        if (key == 208) {
            ++this.selected;
            if (this.selected >= this.options.length) {
                this.selected = 0;
            }
        }
        if (key == 200) {
            --this.selected;
            if (this.selected < 0) {
                this.selected = this.options.length - 1;
            }
        }
        if (key == 2) {
            this.game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
        if (key == 3) {
            this.game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }
}

