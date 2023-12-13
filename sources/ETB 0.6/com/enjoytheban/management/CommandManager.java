package com.enjoytheban.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventChat;
import com.enjoytheban.command.Command;
import com.enjoytheban.command.commands.Bind;
import com.enjoytheban.command.commands.Cheats;
import com.enjoytheban.command.commands.Config;
import com.enjoytheban.command.commands.Enchant;
import com.enjoytheban.command.commands.Help;
import com.enjoytheban.command.commands.Toggle;
import com.enjoytheban.command.commands.VClip;
import com.enjoytheban.command.commands.Xraycmd;
import com.enjoytheban.utils.Helper;

public class CommandManager implements Manager {
	// create a new list that holds commands
	private List<Command> commands;

	// init method
	@Override
	public void init() {
		(this.commands = new ArrayList<Command>()).add(new Command("test", new String[] { "test" }, "", "testing") {
			@Override
			public String execute(final String[] args) {
				for (final Command cmd : CommandManager.this.commands) {
				}
				return null;
			}
		});

		// Add commands here
		this.commands.add(new Help());
		this.commands.add(new Toggle());
		this.commands.add(new Bind());
		this.commands.add(new VClip());
		this.commands.add(new Cheats());
		this.commands.add(new Config());
		this.commands.add(new Xraycmd());
		this.commands.add(new Enchant());
		// Register with the eventbus
		EventBus.getInstance().register(this);
	}

	// returns a list of commands
	public List<Command> getCommands() {
		return this.commands;
	}

	// returns the command by name
	public Optional<Command> getCommandByName(final String name) {
		return this.commands.stream().filter(c2 -> {
			boolean isAlias = false;
			for (String str : c2.getAlias()) {
				if (!str.equalsIgnoreCase(name))
					continue;
				isAlias = true;
				break;
			}
			return c2.getName().equalsIgnoreCase(name) || isAlias;
		}).findFirst();
	}

	// method to add commands
	public void add(final Command command) {
		this.commands.add(command);
	}

	// issa method
	@EventHandler
	private void onChat(final EventChat e) {
		if (e.getMessage().length() > 1 && e.getMessage().startsWith(".")) {
			e.setCancelled(true);
			String[] args = e.getMessage().trim().substring(1).split(" ");
			Optional<Command> possibleCmd = this.getCommandByName(args[0]);
			if (possibleCmd.isPresent()) {
				String result = possibleCmd.get().execute(Arrays.copyOfRange(args, 1, args.length));
				if (result != null && !result.isEmpty()) {
					Helper.sendMessage(result);
				}
			} else {
				Helper.sendMessage(String.format("Command not found Try '%shelp'", "."));
			}
		}
	}
}
