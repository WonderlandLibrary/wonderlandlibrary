package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.SigmaNG;

public class FirendCommand extends Command {
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length < 1){
            sendUsages();
            return;
        }
        if(args.length == 1) {
            if (args[0].equals("clear")) {
                SigmaNG.getSigmaNG().friendsManager.friends.clear();
                ChatUtils.sendMessageWithPrefix("Clear all friends!");
                return;
            } else if (args[0].equals("list")) {
                String[] sr = new String[SigmaNG.getSigmaNG().friendsManager.friends.size()];
                int i = 0;
                for(String names : SigmaNG.getSigmaNG().friendsManager.friends) {
                    sr[i] = names;
                    i++;
                }
                ChatUtils.sendMessageWithPrefix("Your friends: " + String.join(", ", sr));
            }else{
                sendUsages();
            }
        }else{
            args[1] = args[1].toLowerCase();
            if (args[0].equals("remove")) {
                if(SigmaNG.getSigmaNG().friendsManager.friends.contains(args[1])){
                    ChatUtils.sendMessageWithPrefix("Remove friend: " + args[1]);
                    SigmaNG.getSigmaNG().friendsManager.friends.remove(args[1]);
                }else{
                    ChatUtils.sendMessageWithPrefix("Not found friend: " + args[1]);
                }
            }else if (args[0].equals("add")) {
                if(SigmaNG.getSigmaNG().friendsManager.friends.contains(args[1])){
                    ChatUtils.sendMessageWithPrefix("Friend " + args[1] + " is exists.");
                }else{
                    ChatUtils.sendMessageWithPrefix("Add friend: " + args[1]);
                    SigmaNG.getSigmaNG().friendsManager.friends.add(args[1]);
                }
            }else{
                sendUsages();
            }
        }
    }

    @Override
    public String usages() {
        return "[remove/add/clear/list] [player name]";
    }

    @Override
    public String describe() {
        return "add friend.";
    }

    @Override
    public String[] getName() {
        return new String[]{"friend"};
    }
}
