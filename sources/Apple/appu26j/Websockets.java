package appu26j;

import java.net.URI;

import sasidharan.WebsocketClientEndpoint;

public class Websockets extends Thread
{
    private WebsocketClientEndpoint websocketClientEndpoint;
    
    @Override
    public void run()
    {
        long time = System.currentTimeMillis();
        
        while ((time + 2000) > System.currentTimeMillis())
        {
            ;
        }
        
        try
        {
            this.websocketClientEndpoint = new WebsocketClientEndpoint(new URI("ws://18.135.102.232:8081"));
            
            this.websocketClientEndpoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler()
            {
                public void handleMessage(String message)
                {
                    IRCChat.add(message);
                }
            });
        }
        
        catch (Exception e)
        {
            ;
        }
    }
    
    public WebsocketClientEndpoint getWebsocketClientEndpoint()
    {
        return this.websocketClientEndpoint;
    }
}
