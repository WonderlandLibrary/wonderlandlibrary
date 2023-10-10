/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class SoundTest
extends BasicGame {
    private GameContainer myContainer;
    private Sound sound;
    private Sound charlie;
    private Sound burp;
    private Music music;
    private Music musica;
    private Music musicb;
    private Audio engine;
    private int volume = 10;
    private int[] engines = new int[3];

    public SoundTest() {
        super("Sound And Music Test");
    }

    public void init(GameContainer container) throws SlickException {
        SoundStore.get().setMaxSources(32);
        this.myContainer = container;
        this.sound = new Sound("testdata/restart.ogg");
        this.charlie = new Sound("testdata/cbrown01.wav");
        try {
            this.engine = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("testdata/engine.wav"));
        }
        catch (IOException e2) {
            throw new SlickException("Failed to load engine", e2);
        }
        this.music = this.musica = new Music("testdata/SMB-X.XM");
        this.musicb = new Music("testdata/kirby.ogg", true);
        this.burp = new Sound("testdata/burp.aif");
        this.music.play();
    }

    public void render(GameContainer container, Graphics g2) {
        g2.setColor(Color.white);
        g2.drawString("The OGG loop is now streaming from the file, woot.", 100.0f, 60.0f);
        g2.drawString("Press space for sound effect (OGG)", 100.0f, 100.0f);
        g2.drawString("Press P to pause/resume music (XM)", 100.0f, 130.0f);
        g2.drawString("Press E to pause/resume engine sound (WAV)", 100.0f, 190.0f);
        g2.drawString("Press enter for charlie (WAV)", 100.0f, 160.0f);
        g2.drawString("Press C to change music", 100.0f, 210.0f);
        g2.drawString("Press B to burp (AIF)", 100.0f, 240.0f);
        g2.drawString("Press + or - to change global volume of music", 100.0f, 270.0f);
        g2.drawString("Press Y or X to change individual volume of music", 100.0f, 300.0f);
        g2.drawString("Press N or M to change global volume of sound fx", 100.0f, 330.0f);
        g2.setColor(Color.blue);
        g2.drawString("Global Sound Volume Level: " + container.getSoundVolume(), 150.0f, 390.0f);
        g2.drawString("Global Music Volume Level: " + container.getMusicVolume(), 150.0f, 420.0f);
        g2.drawString("Current Music Volume Level: " + this.music.getVolume(), 150.0f, 450.0f);
    }

    public void update(GameContainer container, int delta) {
    }

    public void keyPressed(int key, char c2) {
        int vol;
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            this.sound.play();
        }
        if (key == 48) {
            this.burp.play();
        }
        if (key == 30) {
            this.sound.playAt(-1.0f, 0.0f, 0.0f);
        }
        if (key == 38) {
            this.sound.playAt(1.0f, 0.0f, 0.0f);
        }
        if (key == 28) {
            this.charlie.play(1.0f, 1.0f);
        }
        if (key == 25) {
            if (this.music.playing()) {
                this.music.pause();
            } else {
                this.music.resume();
            }
        }
        if (key == 46) {
            this.music.stop();
            this.music = this.music == this.musica ? this.musicb : this.musica;
            this.music.loop();
        }
        for (int i2 = 0; i2 < 3; ++i2) {
            if (key != 2 + i2) continue;
            if (this.engines[i2] != 0) {
                System.out.println("Stop " + i2);
                SoundStore.get().stopSoundEffect(this.engines[i2]);
                this.engines[i2] = 0;
                continue;
            }
            System.out.println("Start " + i2);
            this.engines[i2] = this.engine.playAsSoundEffect(1.0f, 1.0f, true);
        }
        if (c2 == '+') {
            ++this.volume;
            this.setVolume();
        }
        if (c2 == '-') {
            --this.volume;
            this.setVolume();
        }
        if (key == 21) {
            vol = (int)(this.music.getVolume() * 10.0f);
            if (--vol < 0) {
                vol = 0;
            }
            this.music.setVolume((float)vol / 10.0f);
        }
        if (key == 45) {
            vol = (int)(this.music.getVolume() * 10.0f);
            if (++vol > 10) {
                vol = 10;
            }
            this.music.setVolume((float)vol / 10.0f);
        }
        if (key == 49) {
            vol = (int)(this.myContainer.getSoundVolume() * 10.0f);
            if (--vol < 0) {
                vol = 0;
            }
            this.myContainer.setSoundVolume((float)vol / 10.0f);
        }
        if (key == 50) {
            vol = (int)(this.myContainer.getSoundVolume() * 10.0f);
            if (++vol > 10) {
                vol = 10;
            }
            this.myContainer.setSoundVolume((float)vol / 10.0f);
        }
    }

    private void setVolume() {
        if (this.volume > 10) {
            this.volume = 10;
        } else if (this.volume < 0) {
            this.volume = 0;
        }
        this.myContainer.setMusicVolume((float)this.volume / 10.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new SoundTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

