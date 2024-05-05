package appu26j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import appu26j.interfaces.MinecraftInterface;
import net.minecraft.util.ChatComponentText;

public class UUIDs implements MinecraftInterface
{
    private static HashMap<String, String> uuids = new HashMap<>();
    
    public static String getName(String UUID)
    {
        if (!uuids.containsKey(UUID))
        {
            name(UUID);
            uuids.put(UUID, "???");
        }
        
        return uuids.get(UUID);
    }
    
    public static void name(String UUID)
    {
        new Thread(() ->
        {
            HttpsURLConnection httpsURLConnection = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            String name = UUID;

            try
            {
                httpsURLConnection = (HttpsURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + name).openConnection();
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.connect();
                httpsURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    line = line.trim();

                    if (line.contains("\"name\" : "))
                    {
                        name = line.substring(10, line.length() - 2);
                        break;
                    }
                }
            }

            catch (Exception e)
            {
                ;
            }

            finally
            {
                if (bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }

                    catch (Exception e)
                    {
                        ;
                    }
                }
                
                if (inputStreamReader != null)
                {
                    try
                    {
                        inputStreamReader.close();
                    }

                    catch (Exception e)
                    {
                        ;
                    }
                }

                if (httpsURLConnection != null)
                {
                    httpsURLConnection.disconnect();
                }
            }
            
            uuids.replace(UUID, name);
            mc.ingameGUI.getChatGUI().drawnChatLines.clear();
            IRCChat.getMessages().forEach(string -> mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(UUIDs.getName(string.split(":")[0]) + ":" + string.split(":")[1])));
        }).start();
    }
}
