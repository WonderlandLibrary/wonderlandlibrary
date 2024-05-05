/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.gui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.geom.Rectangle;
import me.kiras.aimwhere.libraries.slick.gui.ComponentListener;
import me.kiras.aimwhere.libraries.slick.gui.GUIContext;
import me.kiras.aimwhere.libraries.slick.util.InputAdapter;

public abstract class AbstractComponent
extends InputAdapter {
    private static AbstractComponent currentFocus = null;
    protected GUIContext container;
    protected Set listeners;
    private boolean focus = false;
    protected Input input;

    public AbstractComponent(GUIContext container) {
        this.container = container;
        this.listeners = new HashSet();
        this.input = container.getInput();
        this.input.addPrimaryListener(this);
        this.setLocation(0, 0);
    }

    public void addListener(ComponentListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ComponentListener listener) {
        this.listeners.remove(listener);
    }

    protected void notifyListeners() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((ComponentListener)it.next()).componentActivated(this);
        }
    }

    public abstract void render(GUIContext var1, Graphics var2) throws SlickException;

    public abstract void setLocation(int var1, int var2);

    public abstract int getX();

    public abstract int getY();

    public abstract int getWidth();

    public abstract int getHeight();

    public void setFocus(boolean focus) {
        if (focus) {
            if (currentFocus != null) {
                currentFocus.setFocus(false);
            }
            currentFocus = this;
        } else if (currentFocus == this) {
            currentFocus = null;
        }
        this.focus = focus;
    }

    public boolean hasFocus() {
        return this.focus;
    }

    protected void consumeEvent() {
        this.input.consumeEvent();
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        this.setFocus(Rectangle.contains(x, y, this.getX(), this.getY(), this.getWidth(), this.getHeight()));
    }
}

