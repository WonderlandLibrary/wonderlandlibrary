/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.mp3.player.MP3Player;
import jaco.mp3.player.plaf.a;
import jaco.mp3.player.plaf.b;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class MP3PlayerUI
extends BasicPanelUI {
    private JButton a;
    private JButton b;
    private JButton c;
    private JButton d;
    private JButton e;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MP3PlayerUI();
    }

    @Override
    public final void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.a((MP3Player)jComponent);
    }

    @Override
    public final void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        MP3Player mP3Player = (MP3Player)jComponent;
        mP3Player.removeAll();
        mP3Player.removeAllMP3PlayerListeners();
    }

    protected void a(MP3Player mP3Player) {
        mP3Player.setOpaque(false);
        this.a = new a(this, jaco.a.b.a(this.getClass().getResource("resources/mp3PlayerPlay.png")));
        this.b = new a(this, jaco.a.b.a(this.getClass().getResource("resources/mp3PlayerPause.png")));
        this.c = new a(this, jaco.a.b.a(this.getClass().getResource("resources/mp3PlayerStop.png")));
        this.d = new a(this, jaco.a.b.a(this.getClass().getResource("resources/mp3PlayerSkipBackward.png")));
        this.e = new a(this, jaco.a.b.a(this.getClass().getResource("resources/mp3PlayerSkipForward.png")));
        b b2 = new b(this, mP3Player);
        this.a.addActionListener(b2);
        this.b.addActionListener(b2);
        this.c.addActionListener(b2);
        this.d.addActionListener(b2);
        this.e.addActionListener(b2);
        mP3Player.setLayout(new FlowLayout(1, 1, 1));
        mP3Player.add(this.a);
        mP3Player.add(this.b);
        mP3Player.add(this.c);
        mP3Player.add(this.d);
        mP3Player.add(this.e);
    }

    static /* synthetic */ JButton a(MP3PlayerUI mP3PlayerUI) {
        return mP3PlayerUI.a;
    }

    static /* synthetic */ JButton b(MP3PlayerUI mP3PlayerUI) {
        return mP3PlayerUI.b;
    }

    static /* synthetic */ JButton c(MP3PlayerUI mP3PlayerUI) {
        return mP3PlayerUI.c;
    }

    static /* synthetic */ JButton d(MP3PlayerUI mP3PlayerUI) {
        return mP3PlayerUI.d;
    }

    static /* synthetic */ JButton e(MP3PlayerUI mP3PlayerUI) {
        return mP3PlayerUI.e;
    }
}

