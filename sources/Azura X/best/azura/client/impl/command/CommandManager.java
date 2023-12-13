package best.azura.client.impl.command;

import best.azura.client.api.command.ICommand;
import best.azura.client.impl.command.impl.*;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
	private final ArrayList<ICommand> commands = new ArrayList<>();
	public final String prefix = ".";

	public CommandManager() {
		commands.addAll(Arrays.asList(
				new UsersCommand(),
				new ConfigCommand(),
				new ToggleCommand(),
				new BindCommand(),
				new SusCommand(),
				new SayCommand(),
				new ReloadCommand(),
				new LoadKillsultsCommand(),
				new KMSCommand(),
				new IRCCommand(),
				new GarbageCollectorCommand(),
				new VClipCommand(),
				new EmoteCommand(),
				new LoadFontsCommand(),
				new FriendCommand(),
				new GlatzCommand(),
				new BanCommand(),
				new HelpCommand(),
				new IGNCommand(),
				new CMDCommand(),
				new ReconnectCommand(),
				new ScriptManagerCommand()));
	}

	public ArrayList<ICommand> getCommands() {
		return commands;
	}

	public boolean handleCommand(String input) {
		try {
			if (input.startsWith(prefix)) {
				for (ICommand command : commands) {
					String[] aliases = command.getAliases();
					if (aliases == null || aliases.length == 0) aliases = new String[]{command.getName()};
					for (String ali : aliases) {
						if ((input.toLowerCase().startsWith(prefix + ali.toLowerCase() + " ")) ||
								input.toLowerCase().equals(prefix + ali.toLowerCase()) ||
								(input.toLowerCase().startsWith(prefix + command.getName().toLowerCase()))) {
							String[] args = input.split(" ");
							try {
								command.handleCommand(Arrays.copyOfRange(args, 1, args.length));
							} catch (Exception exception) {
								exception.printStackTrace();
							}
							return true;
						}
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	@EventHandler
	public final Listener<EventSentPacket> eventSentPacketListener = e -> {
		if (e.getPacket() instanceof C01PacketChatMessage) {
			final C01PacketChatMessage c01 = e.getPacket();
			if (c01.getMessage().toLowerCase().startsWith(prefix)) {
				e.setCancelled(true);
				handleCommand(c01.getMessage());
			}
		}
	};

}