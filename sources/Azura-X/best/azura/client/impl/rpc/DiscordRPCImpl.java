package best.azura.client.impl.rpc;


import best.azura.client.impl.Client;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRPCImpl {

	public void startup() {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			Client.INSTANCE.getLogger().info("Welcome " + user.username + "#" + user.discriminator + "!");
		}).build();
		DiscordRPC.discordInitialize("906986898445201419", handlers, true);
	}

	public void createNewPresence() {
		final DiscordRichPresence rich = new DiscordRichPresence.Builder("Logging in...")
				.setBigImage("big", Client.NAME + " - " + Client.RELEASE)
				.build();
		DiscordRPC.discordUpdatePresence(rich);
	}


	public static void updateNewPresence(final String details, final String state) {
		final DiscordRichPresence rich = new DiscordRichPresence.Builder(state)
				.setBigImage("big", Client.NAME + " - " + Client.RELEASE)
				.setDetails(details)
				.setStartTimestamps(Client.INSTANCE.startTime)
				.build();
		DiscordRPC.discordUpdatePresence(rich);
	}

	public static void updateNewPresence(final String state) {
		final DiscordRichPresence rich = new DiscordRichPresence.Builder(state)
				.setBigImage("big", Client.NAME + " - " + Client.RELEASE)
				.setStartTimestamps(Client.INSTANCE.startTime)
				.build();
		DiscordRPC.discordUpdatePresence(rich);
	}
}

