/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.io.IOException;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Music;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.Sound;
import me.kiras.aimwhere.libraries.slick.openal.Audio;
import me.kiras.aimwhere.libraries.slick.openal.AudioLoader;
import me.kiras.aimwhere.libraries.slick.openal.SoundStore;
import me.kiras.aimwhere.libraries.slick.util.ResourceLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        SoundStore.get().setMaxSources(32);
        this.myContainer = container;
        this.sound = new Sound("testdata/restart.ogg");
        this.charlie = new Sound("testdata/cbrown01.wav");
        try {
            this.engine = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("testdata/engine.wav"));
        }
        catch (IOException e) {
            throw new SlickException("Failed to load engine", e);
        }
        this.music = this.musica = new Music("testdata/SMB-X.XM");
        this.musicb = new Music("testdata/kirby.ogg", true);
        this.burp = new Sound("testdata/burp.aif");
        this.music.play();
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
        g.drawString("Press + or - to change global volume of music", 100.0f, 270.0f);
        g.drawString("Press Y or X to change individual volume of music", 100.0f, 300.0f);
        g.drawString("Press N or M to change global volume of sound fx", 100.0f, 330.0f);
        g.setColor(Color.blue);
        g.drawString("Global Sound Volume Level: " + container.getSoundVolume(), 150.0f, 390.0f);
        g.drawString("Global Music Volume Level: " + container.getMusicVolume(), 150.0f, 420.0f);
        g.drawString("Current Music Volume Level: " + this.music.getVolume(), 150.0f, 450.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    @Override
    public void keyPressed(int key, char c) {
        int vol;
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
        for (int i = 0; i < 3; ++i) {
            if (key != 2 + i) continue;
            if (this.engines[i] != 0) {
                System.out.println("Stop " + i);
                SoundStore.get().stopSoundEffect(this.engines[i]);
                this.engines[i] = 0;
                continue;
            }
            System.out.println("Start " + i);
            this.engines[i] = this.engine.playAsSoundEffect(1.0f, 1.0f, true);
        }
        if (c == '+') {
            ++this.volume;
            this.setVolume();
        }
        if (c == '-') {
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
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

