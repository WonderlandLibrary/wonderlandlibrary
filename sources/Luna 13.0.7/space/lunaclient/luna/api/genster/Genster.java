package space.lunaclient.luna.api.genster;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Genster
{
  public static String gName;
  public static String gPass;
  
  public Genster() {}
  
  public static void initGenster()
  {
    try
    {
      System.setProperty("http.agent", "AgentRA_5333");
      URL url = new URL("https://lunaclient.app/redirect.php");
      System.setProperty("http.agent", "AgentRA_5333");
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.addRequestProperty("User-Agent", "AgentRA_5333");
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null)
      {
        String[] split = line.split(":");
        gName = split[0];
        gPass = split[1];
      }
      reader.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
