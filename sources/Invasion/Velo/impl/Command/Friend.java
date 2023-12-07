package Velo.impl.Command;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import Velo.api.Command.Command;
import Velo.api.Friend.FriendManager;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;

public class Friend extends Command {
	
	public Friend() {
		super("Friend", "Adds a friend.", "friend <add/remove> <name>", "f");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length == 2) {
			 if (args != null && args.length >= 2) {
		         try {
		             if(args[0].equalsIgnoreCase("List")) {
			                Iterator var1 = FriendManager.friendsList.iterator();

				               while(var1.hasNext()) {
				                  Velo.api.Friend.Friend friend = (Velo.api.Friend.Friend)var1.next();
				              ChatUtil.addChatMessage(friend.name);
				               }
			            }
		            if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("a")) {
		               if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("d")) {
		                  if (FriendManager.isFriend(args[1])) {
		                     FriendManager.removeFriend(args[1]);
		                     ChatUtil.addChatMessage("Removed friend: " + args[1]);
		                  } else {
		                     ChatUtil.addChatMessage("" + args[1] + " is not your friend.");
		                  }
		               }
		            } else {
		               if (FriendManager.isFriend(args[1])) {
		                  ChatUtil.addChatMessage(args[1] + " is already your friend.");
		               }

		               FriendManager.removeFriend(args[1]);
		               FriendManager.addFriend(args[1], args.length == 3 ? args[2] : args[1]);
		               ChatUtil.addChatMessage("Added " + args[1]);
		               
		           
		            }
		            
		     
		            
		            
		         } catch (NullPointerException var3) {
		     
		         }

		      } else {
		
		      }
		}
	}
}
