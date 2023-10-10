package appu26j.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import appu26j.interfaces.MinecraftInterface;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class SoundUtil implements MinecraftInterface
{
	public static void playClickSound()
	{
		mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1));
	}
	
	public static synchronized void playBellSound()
	{
		new Thread(() ->
		{
			try
	    	{
	    		Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(mc.getDefaultResourcePack().getInputStream(new ResourceLocation("bell_ring.wav")));
		        clip.open(inputStream);
		        clip.start();
		    }
	    	
	    	catch (Exception e)
	    	{
	    		;
	    	}
		}).start();
	}
}
