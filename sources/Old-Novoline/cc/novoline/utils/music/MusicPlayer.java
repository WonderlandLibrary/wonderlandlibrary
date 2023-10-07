package cc.novoline.utils.music;

public class MusicPlayer {

    private WAVMusicPlayer activePlayer;

    public MusicPlayer(){
        activePlayer = new WAVMusicPlayer();
    }

    public void setup(String string){
        try {
            activePlayer.setup(string);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    public void play(){
        activePlayer.playLooping();
    }

    public void stop(){
        activePlayer.stop();
    }
}
