package appu26j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.lwjgl.opengl.Display;

/**
 * This class is used to queue the Apple Client server every 15 seconds or so
 */
public class ServerThread extends Thread
{
    private long time = System.currentTimeMillis();
    private boolean temp = true;
    
    @Override
    public void run()
    {
        super.run();
        
        while (true)
        {
            if (!Display.isCreated())
            {
                break;
            }
            
            else
            {
                /**
                 * If 15 seconds have elapsed
                 */
                if ((this.time + 15000) < System.currentTimeMillis())
                {
                    Apple.CLIENT.sendAddUUIDRequestToServer();
                    this.time = System.currentTimeMillis();
                    this.temp = true;
                }
                
                /**
                 * If more than 1 second has elapsed (this code is only run once)
                 */
                else if ((this.time + 1000) < System.currentTimeMillis())
                {
                    if (this.temp)
                    {
                        HttpURLConnection httpURLConnection = null;
                        InputStreamReader inputStreamReader = null;
                        BufferedReader bufferedReader = null;
                        
                        try
                        {
                            httpURLConnection = (HttpURLConnection) new URL("http://18.135.102.232:8080/uuidlist").openConnection();
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.connect();
                            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                            bufferedReader = new BufferedReader(inputStreamReader);
                            String line;
                            
                            while ((line = bufferedReader.readLine()) != null)
                            {
                                Apple.CLIENT.usersPlayingAppleClient.add(line);
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
                            
                            if (httpURLConnection != null)
                            {
                                httpURLConnection.disconnect();
                            }
                        }
                        
                        this.temp = false;
                    }
                }
            }
        }
    }
}
