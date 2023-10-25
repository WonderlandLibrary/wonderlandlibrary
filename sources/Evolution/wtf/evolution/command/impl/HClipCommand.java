package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(name = "hclip")
public class HClipCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        mc.player.setPosition(mc.player.posX - Math.sin(Math.toRadians(mc.player.rotationYaw)) * Double.parseDouble(args[1]), mc.player.posY, mc.player.posZ + Math.cos(Math.toRadians(mc.player.rotationYaw)) * Double.parseDouble(args[1]));
        ChatUtil.print("Clipped to " + args[1] + " blocks.");
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> hclip <значение>");
    }
}
