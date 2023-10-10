/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.a.a;
import jaco.a.b;
import jaco.mp3.player.MP3Player;
import jaco.mp3.player.plaf.MP3PlayerUI;
import jaco.mp3.player.plaf.c;
import jaco.mp3.player.plaf.d;
import jaco.mp3.player.plaf.e;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class MP3PlayerUICompact
extends MP3PlayerUI {
    private JButton a;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MP3PlayerUICompact();
    }

    @Override
    protected final void a(MP3Player mP3Player) {
        mP3Player.setOpaque(false);
        BufferedImage bufferedImage = b.a(this.getClass().getResource("resources/mp3PlayerSoundOn.png"));
        BufferedImage bufferedImage2 = b.a(bufferedImage, 0.05f);
        BufferedImage bufferedImage3 = b.b(bufferedImage, 0.05f);
        BufferedImage bufferedImage4 = b.a(this.getClass().getResource("resources/mp3PlayerSoundOff.png"));
        BufferedImage bufferedImage5 = b.a(bufferedImage4, 0.05f);
        BufferedImage bufferedImage6 = b.b(bufferedImage4, 0.05f);
        this.a = new JButton();
        this.a.setCursor(Cursor.getPredefinedCursor(12));
        this.a.setBorder(BorderFactory.createEmptyBorder());
        this.a.setMargin(new Insets(0, 0, 0, 0));
        this.a.setContentAreaFilled(false);
        this.a.setFocusable(false);
        this.a.setFocusPainted(false);
        this.a.setIcon(jaco.a.a.a(bufferedImage4));
        this.a.setRolloverIcon(jaco.a.a.a(bufferedImage5));
        this.a.setPressedIcon(jaco.a.a.a(bufferedImage6));
        this.a.addActionListener(new d(this, mP3Player));
        mP3Player.addMP3PlayerListener(new e(this, bufferedImage, bufferedImage2, bufferedImage3, bufferedImage4, bufferedImage5, bufferedImage6));
        mP3Player.add(this.a);
        mP3Player.setLayout(new c(this));
    }

    static /* synthetic */ JButton a(MP3PlayerUICompact mP3PlayerUICompact) {
        return mP3PlayerUICompact.a;
    }
}

