/*package me.aidanmees.trivia.client.modules.Hidden;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.lwjgl.input.Keyboard;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.GuiItemCreator;
import me.aidanmees.trivia.gui.GuiMusic;
import me.aidanmees.trivia.module.Module;

public class Music extends Module {
	
	 private String channelName;
	    private int channelId = 2;
	    public static String music_link;
	    private Player player;

	  
        

	public Music() {
		super("Music", Keyboard.KEY_NONE, Category.HIDDEN,
				"Plays sum noice moosic");
		
	}

	

	 @Override
	    public void onDisable() {
	        this.player.close();
	        new Thread(() -> playMusic(music_link)).stop();
	        super.onDisable();
	    }

	    @Override
	    public void onEnable() {
	        refreshLink(this.channelId);
	        new Thread(() -> playMusic(music_link)).start();
	        super.onEnable();
	    }

	@Override
	public void onUpdate() {
		this.refreshLink(channelId);
        
		super.onUpdate();
	}
	
	 private void playMusic(String url) {
	        BufferedInputStream inputStream;
	        try {
	            inputStream = new BufferedInputStream(new URL(url).openStream());
	            player = new Player(inputStream);
	            player.play();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (JavaLayerException e) {
	            e.printStackTrace();
	        }
	    }

	    private void refreshLink(int id) {
	        this.music_link= GuiMusic.URL;
	    }

	
}*/