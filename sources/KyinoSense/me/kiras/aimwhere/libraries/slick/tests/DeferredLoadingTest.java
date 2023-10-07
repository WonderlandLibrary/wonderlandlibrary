/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.io.IOException;
import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.Music;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.Sound;
import me.kiras.aimwhere.libraries.slick.loading.DeferredResource;
import me.kiras.aimwhere.libraries.slick.loading.LoadingList;

public class DeferredLoadingTest
extends BasicGame {
    private Music music;
    private Sound sound;
    private Image image;
    private Font font;
    private DeferredResource nextResource;
    private boolean started;

    public DeferredLoadingTest() {
        super("Deferred Loading Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        LoadingList.setDeferredLoading(true);
        new Sound("testdata/cbrown01.wav");
        new Sound("testdata/engine.wav");
        this.sound = new Sound("testdata/restart.ogg");
        new Music("testdata/testloop.ogg");
        this.music = new Music("testdata/SMB-X.XM");
        new Image("testdata/cursor.png");
        new Image("testdata/cursor.tga");
        new Image("testdata/cursor.png");
        new Image("testdata/cursor.png");
        new Image("testdata/dungeontiles.gif");
        new Image("testdata/logo.gif");
        this.image = new Image("testdata/logo.tga");
        new Image("testdata/logo.png");
        new Image("testdata/rocket.png");
        new Image("testdata/testpack.png");
        this.font = new AngelCodeFont("testdata/demo.fnt", "testdata/demo_00.tga");
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        if (this.nextResource != null) {
            g.drawString("Loading: " + this.nextResource.getDescription(), 100.0f, 100.0f);
        }
        int total = LoadingList.get().getTotalResources();
        int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
        float bar = (float)loaded / (float)total;
        g.fillRect(100.0f, 150.0f, loaded * 40, 20.0f);
        g.drawRect(100.0f, 150.0f, total * 40, 20.0f);
        if (this.started) {
            this.image.draw(100.0f, 200.0f);
            this.font.drawString(100.0f, 500.0f, "LOADING COMPLETE");
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (this.nextResource != null) {
            try {
                this.nextResource.load();
                try {
                    Thread.sleep(50L);
                }
                catch (Exception exception) {}
            }
            catch (IOException e) {
                throw new SlickException("Failed to load: " + this.nextResource.getDescription(), e);
            }
            this.nextResource = null;
        }
        if (LoadingList.get().getRemainingResources() > 0) {
            this.nextResource = LoadingList.get().getNext();
        } else if (!this.started) {
            this.started = true;
            this.music.loop();
            this.sound.play();
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new DeferredLoadingTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
    }
}

