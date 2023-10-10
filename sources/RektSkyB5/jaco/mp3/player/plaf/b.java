/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.mp3.player.MP3Player;
import jaco.mp3.player.plaf.MP3PlayerUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

final class b
implements ActionListener {
    private /* synthetic */ MP3PlayerUI a;
    private final /* synthetic */ MP3Player b;

    b(MP3PlayerUI mP3PlayerUI, MP3Player mP3Player) {
        this.a = mP3PlayerUI;
        this.b = mP3Player;
    }

    @Override
    public final void actionPerformed(ActionEvent object) {
        if ((object = ((EventObject)object).getSource()) == MP3PlayerUI.a(this.a)) {
            this.b.play();
            return;
        }
        if (object == MP3PlayerUI.b(this.a)) {
            this.b.pause();
            return;
        }
        if (object == MP3PlayerUI.c(this.a)) {
            this.b.stop();
            return;
        }
        if (object == MP3PlayerUI.d(this.a)) {
            this.b.skipBackward();
            return;
        }
        if (object == MP3PlayerUI.e(this.a)) {
            this.b.skipForward();
        }
    }
}

