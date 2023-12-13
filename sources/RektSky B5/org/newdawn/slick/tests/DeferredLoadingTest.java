/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

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

    public void render(GameContainer container, Graphics g2) {
        if (this.nextResource != null) {
            g2.drawString("Loading: " + this.nextResource.getDescription(), 100.0f, 100.0f);
        }
        int total = LoadingList.get().getTotalResources();
        int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
        float bar = (float)loaded / (float)total;
        g2.fillRect(100.0f, 150.0f, loaded * 40, 20.0f);
        g2.drawRect(100.0f, 150.0f, total * 40, 20.0f);
        if (this.started) {
            this.image.draw(100.0f, 200.0f);
            this.font.drawString(100.0f, 500.0f, "LOADING COMPLETE");
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (this.nextResource != null) {
            try {
                this.nextResource.load();
                try {
                    Thread.sleep(50L);
                }
                catch (Exception e2) {}
            }
            catch (IOException e3) {
                throw new SlickException("Failed to load: " + this.nextResource.getDescription(), e3);
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
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
    }
}

