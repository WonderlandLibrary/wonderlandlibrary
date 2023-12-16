package moonsense.utils;

import moonsense.Client;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.minecraft.client.Minecraft;

public class DiscordRP {
	
	private boolean running = true;
	private long created = 0;
	
	public void start() {
		
		this.created = System.currentTimeMillis();
		
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
			
			@Override
			public void apply(DiscordUser user) {
				System.out.println("Welcome " + user.username + "#" + user.discriminator + ".");
				update("Playing Moonsense Client", "");
				
				if(Minecraft.getMinecraft().session.getUsername().equals("xNefarious") || Minecraft.getMinecraft().session.getUsername().equals("GamerJon2017")) {
					update("Owner | Moonsense Client", "");
				}
				
				if(Minecraft.getMinecraft().session.getUsername().equals("forgchamp")) {
					update("Staff | Moonsense Client", "");
				}
				
				if(Minecraft.getMinecraft().session.getUsername().equals("testplayer1")) {
					update("Moonsense Client", "");
				}
				
			}
		}).build();
		
		DiscordRPC.discordInitialize("1015697299797712896", handlers, true);
		
		new Thread("Discord RPC Callback") {
			
			@Override
			public void run() {
				
				while(running) {
					DiscordRPC.discordRunCallbacks();
				}
				
			}
			
		}.start();
		
	}
	
	public void shutdown() {
		running = false;
		DiscordRPC.discordShutdown();
	}
	
	public void update(String firstLine, String secondLine) {
		DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
		b.setBigImage("large", "Moonsense Client");
		b.setDetails(firstLine);
		b.setStartTimestamps(created);
		
		DiscordRPC.discordUpdatePresence(b.build());
	}

}
