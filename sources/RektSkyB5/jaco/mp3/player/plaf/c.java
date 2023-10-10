/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.mp3.player.plaf.MP3PlayerUICompact;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

final class c
implements LayoutManager {
    private /* synthetic */ MP3PlayerUICompact a;

    c(MP3PlayerUICompact mP3PlayerUICompact) {
        this.a = mP3PlayerUICompact;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void layoutContainer(Container serializable) {
        Object object = serializable.getTreeLock();
        synchronized (object) {
            serializable = serializable.getSize();
            Dimension dimension = MP3PlayerUICompact.a(this.a).getPreferredSize();
            Rectangle rectangle = new Rectangle(dimension);
            new Rectangle(dimension).x = (((Dimension)serializable).width - dimension.width) / 2;
            rectangle.y = (((Dimension)serializable).height - dimension.height) / 2;
            MP3PlayerUICompact.a(this.a).setBounds(rectangle);
            return;
        }
    }

    @Override
    public final Dimension preferredLayoutSize(Container serializable) {
        serializable = serializable.getInsets();
        Dimension dimension = new Dimension(MP3PlayerUICompact.a(((c)((Object)dimension)).a).getPreferredSize());
        new Dimension(MP3PlayerUICompact.a(((c)((Object)dimension)).a).getPreferredSize()).width = dimension.width + ((Insets)serializable).left + ((Insets)serializable).right;
        dimension.height = dimension.height + ((Insets)serializable).top + ((Insets)serializable).bottom;
        return dimension;
    }

    @Override
    public final Dimension minimumLayoutSize(Container serializable) {
        serializable = serializable.getInsets();
        Dimension dimension = new Dimension(MP3PlayerUICompact.a(((c)((Object)dimension)).a).getMinimumSize());
        new Dimension(MP3PlayerUICompact.a(((c)((Object)dimension)).a).getMinimumSize()).width = dimension.width + ((Insets)serializable).left + ((Insets)serializable).right;
        dimension.height = dimension.height + ((Insets)serializable).top + ((Insets)serializable).bottom;
        return dimension;
    }

    @Override
    public final void removeLayoutComponent(Component component) {
    }

    @Override
    public final void addLayoutComponent(String string, Component component) {
    }
}

