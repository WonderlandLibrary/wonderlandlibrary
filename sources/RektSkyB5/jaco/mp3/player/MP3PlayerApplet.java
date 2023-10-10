/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.player;

import jaco.mp3.player.MP3Player;
import jaco.mp3.player.plaf.MP3PlayerUICompact;
import java.awt.Color;
import java.net.URL;
import javax.swing.JApplet;

public class MP3PlayerApplet
extends JApplet {
    @Override
    public void init() {
        try {
            try {
                this.getContentPane().setBackground(Color.decode(this.getParameter("background")));
            }
            catch (Exception exception) {}
            if ("true".equals(this.getParameter("compact"))) {
                MP3Player.setDefaultUI(MP3PlayerUICompact.class);
            }
            MP3Player mP3Player = new MP3Player();
            mP3Player.setRepeat(true);
            Object object = this.getParameter("playlist").split(",");
            String[] stringArray = object;
            int n2 = ((String[])object).length;
            int n3 = 0;
            while (n3 < n2) {
                object = stringArray[n3];
                mP3Player.addToPlayList(new URL(this.getCodeBase() + ((String)object).trim()));
                ++n3;
            }
            this.getContentPane().add(mP3Player);
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }
}

