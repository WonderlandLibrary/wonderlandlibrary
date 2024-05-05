/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.state;

import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.InputListener;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.state.StateBasedGame;

public interface GameState
extends InputListener {
    public int getID();

    public void init(GameContainer var1, StateBasedGame var2) throws SlickException;

    public void render(GameContainer var1, StateBasedGame var2, Graphics var3) throws SlickException;

    public void update(GameContainer var1, StateBasedGame var2, int var3) throws SlickException;

    public void enter(GameContainer var1, StateBasedGame var2) throws SlickException;

    public void leave(GameContainer var1, StateBasedGame var2) throws SlickException;
}

