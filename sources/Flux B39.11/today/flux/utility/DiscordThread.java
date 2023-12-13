package today.flux.utility;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import today.flux.Flux;
import today.flux.irc.IRCClient;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.stream.Collectors;

public class DiscordThread extends Thread {
	public final static String DISCORD_ID = "895930191866634281";

	public boolean isDiscordRunning = false;
	public NumberFormat nf = new DecimalFormat("0000");

	public String pre;
	public String details;

	public TimeHelper timer = new TimeHelper();

	public int getCount() {
		return (int) ModuleManager.getModList().stream().filter(Module::isEnabled).count();
	}

	@Override
	public void run() {
		//重置计时器
		timer.reset();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Closing Discord hook.");
			this.isDiscordRunning = false;
			DiscordRPC.discordShutdown();
		}));

		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			System.out.println("Found Discord: " + user.username + "#" + user.discriminator + ".");
			this.isDiscordRunning = true;
		}).build();

		DiscordRPC.discordInitialize(DISCORD_ID, handlers, true);
		DiscordRPC.discordRegister(DISCORD_ID, "");

		while (true) {
			DiscordRPC.discordRunCallbacks();

			if (this.isDiscordRunning) {
				int killed = KillAura.killed;
				int win = ModuleManager.autoGGMod.win;
				DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder( "Kills: " + killed + " Wins:" + win);
				presence.setBigImage("rpc_icon", "Flux b" + Flux.VERSION + (Flux.DEBUG_MODE ? " [BETA]" : ""));
				presence.setDetails(IRCClient.loggedPacket.getRealUsername() + " | " + nf.format(IRCClient.loggedPacket.getUid()));

				if(!Minecraft.getMinecraft().isSingleplayer() && Minecraft.getMinecraft().getCurrentServerData() != null) {
					String ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
					ip = ip.contains(":") ? ip : ip + ":25565";
					boolean isHypickle = ServerUtils.getServer() == ServerUtils.Server.Hypixel;
					presence.setSmallImage(isHypickle ? "hypickle" : "server", isHypickle ? "mc.hypixel.net:25565" : ip);
					presence.setStartTimestamps(Flux.playTimeStart / 1000);
				}

				DiscordRPC.discordUpdatePresence(presence.build());
				
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			} else {
				if(this.timer.delay(10000)) {
					System.out.println("Timeout while finding Discord process! exiting...");
					break;
				}
			}
		}
	}
}
