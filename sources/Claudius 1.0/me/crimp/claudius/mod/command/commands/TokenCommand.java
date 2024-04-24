package me.crimp.claudius.mod.command.commands;



import me.crimp.claudius.mod.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import static net.minecraft.client.gui.GuiScreen.setClipboardString;

public class TokenCommand extends Command {
    public TokenCommand() {
        super("Token", new String[]{});
    }

    @Override
    public void execute(String[] commands) {
        Command.sendMessage("ur claudius token is 78612hjsdha80d78qjnsahdaudh727dsh82s71313sdawew3");
    }
}