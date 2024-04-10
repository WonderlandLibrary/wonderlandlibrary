package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.utils.ChatUtils;

public class VClipCommand extends Command {
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length != 1){
            sendUsages();
            return;
        }
        float h = 0;
        try{
            h = Float.parseFloat(args[0]);
        }catch (Exception e){
            ChatUtils.sendMessageWithPrefix("Cant VClip: " + args[0]);
        }
        mc.player.setPosition(
                mc.player.getPosX(), mc.player.getPosY() + h, mc.player.getPosZ()
        );
        ChatUtils.sendMessageWithPrefix("VClip to " + mc.player.getPosY());
    }

    @Override
    public String usages() {
        return "[downBlocks]";
    }

    @Override
    public String describe() {
        return "clip of blocks.";
    }

    @Override
    public String[] getName() {
        return new String[]{"vclip"};
    }
}
