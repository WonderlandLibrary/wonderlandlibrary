/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;
import me.kiras.aimwhere.libraries.slick.tests.states.TestState1;
import me.kiras.aimwhere.libraries.slick.tests.states.TestState2;
import me.kiras.aimwhere.libraries.slick.tests.states.TestState3;

public class StateBasedTest
extends StateBasedGame {
    public StateBasedTest() {
        super("State Based Test");
    }

    @Override
    public void initStatesList(GameContainer container) {
        this.addState(new TestState1());
        this.addState(new TestState2());
        this.addState(new TestState3());
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new StateBasedTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

