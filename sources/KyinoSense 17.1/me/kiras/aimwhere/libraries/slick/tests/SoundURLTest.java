/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Music;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.Sound;
import me.kiras.aimwhere.libraries.slick.util.ResourceLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SoundURLTest
extends BasicGame {
    private Sound sound;
    private Sound charlie;
    private Sound burp;
    private Music music;
    private Music musica;
    private Music musicb;
    private Sound engine;
    private int volume = 1;

    public SoundURLTest() {
        super("Sound URL Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.sound = new Sound(ResourceLoader.getResource("testdata/restart.ogg"));
        this.charlie = new Sound(ResourceLoader.getResource("testdata/cbrown01.wav"));
        this.engine = new Sound(ResourceLoader.getResource("testdata/engine.wav"));
        this.music = this.musica = new Music(ResourceLoader.getResource("testdata/restart.ogg"), false);
        this.musicb = new Music(ResourceLoader.getResource("testdata/kirby.ogg"), false);
        this.burp = new Sound(ResourceLoader.getResource("testdata/burp.aif"));
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(Color.white);
        g.drawString("The OGG loop is now streaming from the file, woot.", 100.0f, 60.0f);
        g.drawString("Press space for sound effect (OGG)", 100.0f, 100.0f);
        g.drawString("Press P to pause/resume music (XM)", 100.0f, 130.0f);
        g.drawString("Press E to pause/resume engine sound (WAV)", 100.0f, 190.0f);
        g.drawString("Press enter for charlie (WAV)", 100.0f, 160.0f);
        g.drawString("Press C to change music", 100.0f, 210.0f);
        g.drawString("Press B to burp (AIF)", 100.0f, 240.0f);
        g.drawString("Press + or - to change volume of music", 100.0f, 270.0f);
        g.setColor(Color.blue);
        g.drawString("Music Volume Level: " + (float)this.volume / 10.0f, 150.0f, 300.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
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
        if (key == 18) {
            if (this.engine.playing()) {
                this.engine.stop();
            } else {
                this.engine.loop();
            }
        }
        if (c == '+') {
            ++this.volume;
            this.setVolume();
        }
        if (c == '-') {
            --this.volume;
            this.setVolume();
        }
    }

    private void setVolume() {
        if (this.volume > 10) {
            this.volume = 10;
        } else if (this.volume < 0) {
            this.volume = 0;
        }
        this.music.setVolume((float)this.volume / 10.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new SoundURLTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

