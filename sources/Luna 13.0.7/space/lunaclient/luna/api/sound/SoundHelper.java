package space.lunaclient.luna.api.sound;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class SoundHelper
{
  public static Thread music;
  public static boolean started = false;
  
  public SoundHelper() {}
  
  public static void createThread(String url)
  {
    music = new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Player player = new Player(new URL(this.val$url).openStream());
          player.play();
        }
        catch (MalformedURLException e)
        {
          System.out.println("§4Error: URLCOR; Radio failed to load music from URL.");
          e.printStackTrace();
        }
        catch (JavaLayerException e)
        {
          System.out.println("§4Error: JLAYER; Radio failed to load, try again.");
          e.printStackTrace();
        }
        catch (IOException e)
        {
          System.out.println("§4Error: IOEXC; Radio failed to load (Lag, radio down?)");
          e.printStackTrace();
        }
      }
    });
  }
}
