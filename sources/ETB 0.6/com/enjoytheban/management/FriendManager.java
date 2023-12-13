package com.enjoytheban.management;

import java.util.HashMap;
import java.util.List;

import com.enjoytheban.Client;
import com.enjoytheban.command.Command;
import com.enjoytheban.utils.Helper;

import net.minecraft.util.EnumChatFormatting;

/**
 * Ugly friend manager
 * 
 * @author Purity
 */

public class FriendManager implements Manager {

	// store our friends in a hashmap
	private static HashMap<String, String> friends;

	@Override
	public void init() {
		// set it to a new hasmap so it isnt null
		this.friends = new HashMap();
		// Loads friends
		List<String> frriends = FileManager.read("Friends.txt");
		for (String v : frriends) {
			if(v.contains(":")) {
				String name = v.split(":")[0];
				String alias = v.split(":")[1];
				this.friends.put(name, alias);
			} else {
				this.friends.put(v, v);
			}
		}
		// addd the commmand
		Client.instance.getCommandManager().add(new Command("f", new String[] { "friend", "fren", "fr" },
				"add/del/list name alias", "Manage client friends") {
			private final FriendManager fm = FriendManager.this;
			
			// command bullshit
			@Override
			public String execute(String[] args) {
				// adding
				if (args.length >= 3) {
					if (args[0].equalsIgnoreCase("add")) {
						String f = "";
						f += String.format("%s:%s%s", args[1], args[2], System.lineSeparator());
						this.fm.friends.put(args[1], args[2]);
						Helper.sendMessage("> "+String.format("%s has been added as %s", args[1], args[2]));
						FileManager.save("Friends.txt", f, true);
						// deleting
					} else if (args[0].equalsIgnoreCase("del")) {
						this.fm.friends.remove(args[1]);
						Helper.sendMessage("> "+
								String.format("%s has been removed from your friends list", args[1]));
						// list the friends
					} else if (args[0].equalsIgnoreCase("list")) {
						if (this.fm.friends.size() > 0) {
							int friends = 1;
							for (String fr : this.fm.friends.values()) {
								Helper.sendMessage("> "+String.format("%s. %s", friends, fr));
								friends++;
							}
						} else {
							// lol
							Helper.sendMessage("> "+"get some friends fag lmao");
						}
					}
					// now for adding with 2 args
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("add")) {
						String f = "";
						f += String.format("%s%s", args[1], System.lineSeparator());
						this.fm.friends.put(args[1], args[1]);
						Helper.sendMessage("> "+String.format("%s has been added as %s", args[1], args[1]));
						FileManager.save("Friends.txt", f, true);
						// del
					} else if (args[0].equalsIgnoreCase("del")) {
						this.fm.friends.remove(args[1]);
						Helper.sendMessage("> "+
								String.format("%s has been removed from your friends list", args[1]));
						// list
					} else if (args[0].equalsIgnoreCase("list")) {
						if (this.fm.friends.size() > 0) {
							int friends = 1;
							for (String fr : this.fm.friends.values()) {
								Helper.sendMessage("> "+String.format("%s. %s", friends, fr));
								friends++;
							}
						} else {
							Helper.sendMessage("> "+"you dont have any you lonely fuck");
						}
					}
					// you get the point
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("list")) {
						if (this.fm.friends.size() > 0) {
							int friends = 1;
							for (String fr : this.fm.friends.values()) {
								Helper.sendMessage(String.format("%s. %s", friends, fr));
								friends++;
							}
						} else {
							Helper.sendMessage("you dont have any you lonely fuck");
						}
					} else {
						if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("del")) {
							Helper.sendMessage("> " + EnumChatFormatting.GRAY
									+ "Please enter a players name");
						} else {
							Helper.sendMessage("> " + "Correct usage: " + EnumChatFormatting.GRAY
									+ "Valid .f add/del <player>");
						}
					}
				} else if (args.length == 0) {
					Helper.sendMessage("> " + "Correct usage: " + EnumChatFormatting.GRAY
							+ "Valid .f add/del <player>");
				}
				return null;
			}
		});
	}

	// issa friend?
	public static boolean isFriend(String name) {
		return FriendManager.friends.containsKey(name);
	}

	// grab the players name by alias
	public static String getAlias(String name) {
		return FriendManager.friends.get(name);
	}

	// returns the friends hashmap
	public static HashMap<String, String> getFriends() {
		return friends;
	}
}