/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Cursor
 */
package me.kiras.aimwhere.libraries.slick.gui;

import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.opengl.ImageData;
import org.lwjgl.input.Cursor;

public interface GUIContext {
    public Input getInput();

    public long getTime();

    public int getScreenWidth();

    public int getScreenHeight();

    public int getWidth();

    public int getHeight();

    public Font getDefaultFont();

    public void setMouseCursor(String var1, int var2, int var3) throws SlickException;

    public void setMouseCursor(ImageData var1, int var2, int var3) throws SlickException;

    public void setMouseCursor(Cursor var1, int var2, int var3) throws SlickException;

    public void setDefaultMouseCursor();
}

