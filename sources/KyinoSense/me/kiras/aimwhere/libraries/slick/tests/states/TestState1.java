/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests.states;

import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.BasicGameState;
import me.kiras.aimwhere.libraries.slick.state.GameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;
import me.kiras.aimwhere.libraries.slick.state.transition.CrossStateTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.EmptyTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.FadeInTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.FadeOutTransition;

public class TestState1
extends BasicGameState {
    public static final int ID = 1;
    private Font font;
    private StateBasedGame game;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.game = game;
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setFont(this.font);
        g.setColor(Color.white);
        g.drawString("State Based Game Test", 100.0f, 100.0f);
        g.drawString("Numbers 1-3 will switch between states.", 150.0f, 300.0f);
        g.setColor(Color.red);
        g.drawString("This is State 1", 200.0f, 50.0f);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == 3) {
            GameState target = this.game.getState(2);
            final long start = System.currentTimeMillis();
            CrossStateTransition t = new CrossStateTransition(target){

                @Override
                public boolean isComplete() {
                    return System.currentTimeMillis() - start > 2000L;
                }

                @Override
                public void init(GameState firstState, GameState secondState) {
                }
            };
            this.game.enterState(2, t, new EmptyTransition());
        }
        if (key == 4) {
            this.game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }
}

