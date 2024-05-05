package appu26j.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import appu26j.Apple;
import net.minecraft.client.main.Main;

public class UpdateUtil
{
    private static File updater = new File("updater.jar");
    private volatile static boolean update = false;
    
    public static void addUpdateHook()
    {
        checkUpdaterJar();
        HttpsURLConnection httpsURLConnection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        
        try
        {
            URL url = new URL("https://pastebin.com/raw/eZYvUZHs");
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(false);
            httpsURLConnection.connect();
            inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            float version = Float.parseFloat(bufferedReader.readLine());
            
            if (Float.parseFloat(Apple.VERSION) < version)
            {
                update = true;
            }
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                
                catch (Exception exception)
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
                
                catch (Exception exception)
                {
                    ;
                }
            }
            
            if (httpsURLConnection != null)
            {
                httpsURLConnection.disconnect();
            }
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            if (update)
            {
                try
                {
                    Runtime.getRuntime().exec("java -jar updater.jar");
                }
                
                catch (Exception e)
                {
                    ;
                }
            }
        }));
    }
    
    private static void checkUpdaterJar()
    {
        if (!updater.exists() || !String.valueOf(updater.length()).startsWith("3"))
        {
            try
            {
                if (updater.exists())
                {
                    updater.delete();
                }
                
                updater.createNewFile();
                FileUtils.copyURLToFile(new URL("https://github.com/AppleClient/Updater/releases/download/Updater/updater.jar"), updater);
            }
            
            catch (Exception e)
            {
                ;
            }
        }
    }
}
