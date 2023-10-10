/*
 * Decompiled with CFR 0.152.
 */
package jaco.a;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

final class c
implements Icon {
    private BufferedImage a;

    private c(BufferedImage bufferedImage, byte by) {
        this.a = bufferedImage;
    }

    @Override
    public final void paintIcon(Component component, Graphics graphics, int n2, int n3) {
        graphics.drawImage(this.a, n2, n3, component);
    }

    @Override
    public final int getIconWidth() {
        return this.a.getWidth();
    }

    @Override
    public final int getIconHeight() {
        return this.a.getHeight();
    }

    /* synthetic */ c(BufferedImage bufferedImage) {
        this(bufferedImage, 0);
    }
}

