/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player.plaf;

import jaco.mp3.player.MP3Player;
import jaco.mp3.player.plaf.MP3PlayerUICompact;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class d
implements ActionListener {
    private final /* synthetic */ MP3Player a;

    d(MP3PlayerUICompact mP3PlayerUICompact, MP3Player mP3Player) {
        this.a = mP3Player;
    }

    @Override
    public final void actionPerformed(ActionEvent actionEvent) {
        if (this.a.isPaused() || this.a.isStopped()) {
            this.a.play();
            return;
        }
        this.a.pause();
    }
}

