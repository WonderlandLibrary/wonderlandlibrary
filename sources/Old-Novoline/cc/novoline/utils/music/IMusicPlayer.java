package cc.novoline.utils.music;

public interface IMusicPlayer {

    void setup(String pathToAudio) throws Throwable;
    void play();
    void stop();
    void playLooping();

}
