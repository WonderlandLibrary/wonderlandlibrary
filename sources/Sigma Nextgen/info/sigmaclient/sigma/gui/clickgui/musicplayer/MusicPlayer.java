package info.sigmaclient.sigma.gui.clickgui.musicplayer;

import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.modules.gui.hide.ClickGUI;
import info.sigmaclient.sigma.utils.music.Music;
import java.awt.Color;
import java.net.URL;

public class MusicPlayer {
        URL url;
        boolean isNotNull;
        public boolean isOnPlaying;
        public static Color firstColor;
        public static Color secondColor;

        public MusicPlayer() {
            this.isNotNull = false;
            this.isOnPlaying = false;
        }

        public void SetPlayAudioPathYT(String path, Music music) {
            firstColor = null;
            secondColor = null;

            try {
                new Thread(() -> {
                    if (this.isNotNull) {
                        JavaXMusicPlayer.shutUP();
                        this.isOnPlaying = false;
                        this.isNotNull = false;
                    }

                    JavaXMusicPlayer.startFX(path);
                    this.isOnPlaying = true;
                    this.isNotNull = true;
                }).start();
                new Thread(() -> {
                    while(ClickGUI.clickGui.musicPlayer.currentFile.textureImage == null || ClickGUI.clickGui.musicPlayer.currentFile.textureImage.pixels == null) {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException var3) {
                            throw new RuntimeException(var3);
                        }
                    }

                    NotificationManager.notify("Now Playing", music.aliasName + " - " + music.name, 8000, ClickGUI.clickGui.musicPlayer.currentFile.textureImage);
                    int with = ClickGUI.clickGui.musicPlayer.currentFile.textureImage.pixels.getWidth();
                    int hei = ClickGUI.clickGui.musicPlayer.currentFile.textureImage.pixels.getHeight();
                    firstColor = MusicWaveRender.averageColor(ClickGUI.clickGui.musicPlayer.currentFile.textureImage.pixels, with, hei, 1);
                    secondColor = MusicWaveRender.averageColor(ClickGUI.clickGui.musicPlayer.currentFile.textureImage.pixels, with, hei, 2);
                }).start();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    void play() {
            JavaXMusicPlayer.start();
            this.isOnPlaying = true;
        }

    void stop() {
            JavaXMusicPlayer.pause();
            this.isOnPlaying = false;
        }

    static {
            MusicPlayer.firstColor = null;
            MusicPlayer.secondColor = null;
        }
    }
