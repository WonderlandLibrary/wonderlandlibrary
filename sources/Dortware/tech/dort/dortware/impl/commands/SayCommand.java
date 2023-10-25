package tech.dort.dortware.impl.commands;

import net.minecraft.network.play.client.C01PacketChatMessage;
import tech.dort.dortware.api.command.Command;
import tech.dort.dortware.api.command.CommandData;
import tech.dort.dortware.impl.utils.networking.PacketUtil;
import tech.dort.dortware.impl.utils.player.ChatUtil;

public class SayCommand extends Command {

    public SayCommand() {
        super(new CommandData("say"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            if ("say".equalsIgnoreCase(command)) {
                PacketUtil.sendPacketNoEvent(new C01PacketChatMessage(args[1]));
                ChatUtil.displayChatMessage("Said your message.");
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\nsay [message]");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("An unknown error occurred.");
        }
    }
}
