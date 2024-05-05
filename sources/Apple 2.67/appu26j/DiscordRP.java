package appu26j;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence.Builder;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordRP
{
	private static volatile boolean running = true;
	private static final long milliseconds = System.currentTimeMillis();
	
	/**
	 * Initializes the Discord Rich Presence
	 */
	public static void start()
	{
		try
		{
		    DiscordEventHandlers discordEventHandlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback()
	        {
	            @Override
	            public void apply(DiscordUser discordUser)
	            {
	                update("Launching Apple Client " + Apple.VERSION);
	            }
	        }).build();
	        
	        DiscordRPC.discordInitialize("1096373593597812756", discordEventHandlers, true);
	        
	        new Thread(() ->
	        {
	            while (running)
	            {
	                DiscordRPC.discordRunCallbacks();
	            }
	        }).start();
		}
		
		catch (Error e)
		{
		    ;
		}
	}
	
	/**
	 * Stops the Discord Rich Presence
	 */
	public static void shutdown()
	{
	    try
	    {
	        running = false;
	        DiscordRPC.discordShutdown();
	    }
	    
	    catch (Error e)
	    {
	        ;
	    }
	}
	
	/**
	 * Changes the details
	 * @param line
	 */
	public static void update(String line)
	{
		try
		{
		    Builder builder = new Builder("dsc.gg/appleclient");
	        builder.setDetails(line);
	        builder.setBigImage("appleclient", "Apple Client");
	        builder.setStartTimestamps(milliseconds);
	        DiscordRPC.discordUpdatePresence(builder.build());
		}
		
		catch (Error e)
		{
		    ;
		}
	}
}
