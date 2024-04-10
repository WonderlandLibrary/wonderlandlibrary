package info.sigmaclient.sigma.gui.clickgui.musicplayer;


import info.sigmaclient.sigma.sigma5.jellomusic.Player;
import info.sigmaclient.sigma.sigma5.jellomusic.player.JelloMusic;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class JavaXMusicPlayer {
//    public static Media media;
//    public static MediaPlayer mediaPlayer;
//
//    public static void shutUP(){
//        if(mediaPlayer == null) return;
//        mediaPlayer.stop();
//    }
//
//    public static void pause(){
//        if(mediaPlayer == null) return;
//        mediaPlayer.pause();
//    }
//    public static void start(){
//        if(mediaPlayer == null) return;
//        mediaPlayer.play();
//    }
//
//    public static void startFX(String path){
//        System.out.println(path);
//        new Thread(()->{
//            SwingUtilities.invokeLater(() -> {
//                new JFXPanel();
//                Platform.runLater(() -> StageBuilder.create()
//                        .scene(SceneBuilder.create()
//                                .width(0)
//                                .height(0)
//                                .root(LabelBuilder.create()
//                                        .font(new javafx.scene.text.Font("Arial", 54d))
//                                        .text("Music Player")
//                                        .build())
//                                .build())
//                        .onCloseRequest(windowEvent -> System.exit(0))
//                        .build());
//            });
//
//            media = new Media(path);
//            mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.setAudioSpectrumInterval(0.03);
//            mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
//                for(int i = 0; i < MusicWaveRender.volces.length; i++) {
//                    MusicWaveRender.volces[i] = (float) ((magnitudes[i] + 60) * (-1.5));
//                }
//            });
//            mediaPlayer.play();
//            mediaPlayer.setVolume(JelloMusicPlayer.volumeControl);
//        }).start();
//
//    }
    public static void shutUP(){
        Player.stop();
    }

    public static void pause(){
        Player.pause();
    }
    public static void start(){
        Player.resume();
    }

    public static void startFX(String path){
        System.out.println(path);
//        new Thread(()->{
//            SwingUtilities.invokeLater(() -> {
//                new JFXPanel();
//                Platform.runLater(() -> StageBuilder.create()
//                        .scene(SceneBuilder.create()
//                                .width(0)
//                                .height(0)
//                                .root(LabelBuilder.create()
//                                        .font(new javafx.scene.text.Font("Arial", 54d))
//                                        .text("Music Player")
//                                        .build())
//                                .build())
//                        .onCloseRequest(windowEvent -> System.exit(0))
//                        .build());
//            });
//
//            media = new Media(path);
//            mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.setAudioSpectrumInterval(0.03);
//            mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
//                for(int i = 0; i < MusicWaveRender.volces.length; i++) {
//                    MusicWaveRender.volces[i] = (float) ((magnitudes[i] + 60) * (-1.5));
//                }
//            });
        try {
            Player.play(new File(path).toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Player.setVolume((int) (JelloMusicPlayer.volumeControl * 100));
//        }).start();

    }
}
