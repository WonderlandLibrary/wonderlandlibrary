/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.gui;

import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.gui.AbstractComponent;
import me.kiras.aimwhere.libraries.slick.gui.GUIContext;

public abstract class BasicComponent
extends AbstractComponent {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public BasicComponent(GUIContext container) {
        super(container);
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    public abstract void renderImpl(GUIContext var1, Graphics var2);

    @Override
    public void render(GUIContext container, Graphics g) throws SlickException {
        this.renderImpl(container, g);
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

