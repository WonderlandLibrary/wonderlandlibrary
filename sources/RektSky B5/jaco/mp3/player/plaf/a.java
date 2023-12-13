/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.a.b;
import jaco.mp3.player.plaf.MP3PlayerUI;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JButton;

final class a
extends JButton {
    private a(MP3PlayerUI object, BufferedImage bufferedImage, byte by) {
        BufferedImage bufferedImage2 = b.a(bufferedImage, 0.05f);
        object = b.b(bufferedImage, 0.05f);
        this.setIcon(jaco.a.a.a(bufferedImage));
        this.setRolloverIcon(jaco.a.a.a(bufferedImage2));
        this.setPressedIcon(jaco.a.a.a((BufferedImage)object));
        this.setCursor(Cursor.getPredefinedCursor(12));
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setFocusPainted(false);
    }

    /* synthetic */ a(MP3PlayerUI mP3PlayerUI, BufferedImage bufferedImage) {
        this(mP3PlayerUI, bufferedImage, 0);
    }
}

