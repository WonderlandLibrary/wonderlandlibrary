/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.state.transition;

import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.GameState;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;

public interface Transition {
    public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException;

    public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException;

    public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException;

    public boolean isComplete();

    public void init(GameState var1, GameState var2);
}

