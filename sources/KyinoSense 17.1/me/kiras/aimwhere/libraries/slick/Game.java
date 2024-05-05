/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick;

import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;

public interface Game {
    public void init(GameContainer var1) throws SlickException;

    public void update(GameContainer var1, int var2) throws SlickException;

    public void render(GameContainer var1, Graphics var2) throws SlickException;

    public boolean closeRequested();

    public String getTitle();
}

