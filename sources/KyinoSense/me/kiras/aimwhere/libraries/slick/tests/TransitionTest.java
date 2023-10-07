/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.BasicGameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;
import me.kiras.aimwhere.libraries.slick.state.transition.BlobbyTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.FadeInTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.FadeOutTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.HorizontalSplitTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.RotateTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.SelectTransition;
import me.kiras.aimwhere.libraries.slick.state.transition.Transition;
import me.kiras.aimwhere.libraries.slick.state.transition.VerticalSplitTransition;
import me.kiras.aimwhere.libraries.slick.util.Log;

public class TransitionTest
extends StateBasedGame {
    private Class[][] transitions = new Class[][]{{null, VerticalSplitTransition.class}, {FadeOutTransition.class, FadeInTransition.class}, {null, RotateTransition.class}, {null, HorizontalSplitTransition.class}, {null, BlobbyTransition.class}, {null, SelectTransition.class}};
    private int index;

    public TransitionTest() {
        super("Transition Test - Hit Space To Transition");
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new ImageState(0, "testdata/wallpaper/paper1.png", 1));
        this.addState(new ImageState(1, "testdata/wallpaper/paper2.png", 2));
        this.addState(new ImageState(2, "testdata/bigimage.tga", 0));
    }

    public Transition[] getNextTransitionPair() {
        Transition[] pair = new Transition[2];
        try {
            if (this.transitions[this.index][0] != null) {
                pair[0] = (Transition)this.transitions[this.index][0].newInstance();
            }
            if (this.transitions[this.index][1] != null) {
                pair[1] = (Transition)this.transitions[this.index][1].newInstance();
            }
        }
        catch (Throwable e) {
            Log.error(e);
        }
        ++this.index;
        if (this.index >= this.transitions.length) {
            this.index = 0;
        }
        return pair;
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TransitionTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private class ImageState
    extends BasicGameState {
        private int id;
        private int next;
        private String ref;
        private Image image;

        public ImageState(int id, String ref, int next) {
            this.ref = ref;
            this.id = id;
            this.next = next;
        }

        @Override
        public int getID() {
            return this.id;
        }

        @Override
        public void init(GameContainer container, StateBasedGame game) throws SlickException {
            this.image = new Image(this.ref);
        }

        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
            this.image.draw(0.0f, 0.0f, 800.0f, 600.0f);
            g.setColor(Color.red);
            g.fillRect(-50.0f, 200.0f, 50.0f, 50.0f);
        }

        @Override
        public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
            if (container.getInput().isKeyPressed(57)) {
                Transition[] pair = TransitionTest.this.getNextTransitionPair();
                game.enterState(this.next, pair[0], pair[1]);
            }
        }
    }
}

