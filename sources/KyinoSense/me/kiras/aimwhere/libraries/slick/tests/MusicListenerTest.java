/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Music;
import me.kiras.aimwhere.libraries.slick.MusicListener;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class MusicListenerTest
extends BasicGame
implements MusicListener {
    private boolean musicEnded = false;
    private boolean musicSwapped = false;
    private Music music;
    private Music stream;

    public MusicListenerTest() {
        super("Music Listener Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.music = new Music("testdata/restart.ogg", false);
        this.stream = new Music("testdata/restart.ogg", false);
        this.music.addListener(this);
        this.stream.addListener(this);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void musicEnded(Music music) {
        this.musicEnded = true;
    }

    @Override
    public void musicSwapped(Music music, Music newMusic) {
        this.musicSwapped = true;
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawString("Press M to play music", 100.0f, 100.0f);
        g.drawString("Press S to stream music", 100.0f, 150.0f);
        if (this.musicEnded) {
            g.drawString("Music Ended", 100.0f, 200.0f);
        }
        if (this.musicSwapped) {
            g.drawString("Music Swapped", 100.0f, 250.0f);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 50) {
            this.musicEnded = false;
            this.musicSwapped = false;
            this.music.play();
        }
        if (key == 31) {
            this.musicEnded = false;
            this.musicSwapped = false;
            this.stream.play();
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new MusicListenerTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

