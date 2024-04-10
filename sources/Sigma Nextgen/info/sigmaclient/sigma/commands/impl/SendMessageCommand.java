package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.commands.Command;

public class SendMessageCommand extends Command {
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length == 0){
            sendUsages();
            return;
        }
        mc.player.sendChatMessage(joinArgs);
    }

    @Override
    public String usages() {
        return "[message]";
    }

    @Override
    public String describe() {
        return "Send meesage without \".\".";
    }

    @Override
    public String[] getName() {
        return new String[]{"sendmessage", "send"};
    }
}
