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
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.CrossStateTransition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TestState1
extends BasicGameState {
    public static final int ID = 1;
    private Font font;
    private StateBasedGame game;

    public int getID() {
        return 1;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.game = game;
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g2) {
        g2.setFont(this.font);
        g2.setColor(Color.white);
        g2.drawString("State Based Game Test", 100.0f, 100.0f);
        g2.drawString("Numbers 1-3 will switch between states.", 150.0f, 300.0f);
        g2.setColor(Color.red);
        g2.drawString("This is State 1", 200.0f, 50.0f);
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
    }

    public void keyReleased(int key, char c2) {
        if (key == 3) {
            GameState target = this.game.getState(2);
            final long start = System.currentTimeMillis();
            CrossStateTransition t2 = new CrossStateTransition(target){

                public boolean isComplete() {
                    return System.currentTimeMillis() - start > 2000L;
                }

                public void init(GameState firstState, GameState secondState) {
                }
            };
            this.game.enterState(2, t2, new EmptyTransition());
        }
        if (key == 4) {
            this.game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }
}

