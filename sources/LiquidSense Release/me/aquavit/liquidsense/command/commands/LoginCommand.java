package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.utils.login.MinecraftAccount;
import me.aquavit.liquidsense.utils.misc.ServerUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.ui.client.gui.GuiAltManager;

public class LoginCommand extends Command
{
    public LoginCommand() {
        super("login");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            chatSyntax(".login <username/email> [password]");
            return;
        }
        String result;
        if (args.length > 2) {
            result = GuiAltManager.login(new MinecraftAccount(args[1], args[2]));
        } else {
            result = GuiAltManager.login(new MinecraftAccount(args[1]));
        }
        chat(result);
        if (result.startsWith("§cYour name is now")) {
            if (mc.isIntegratedServerRunning()) return;
            mc.theWorld.sendQuittingDisconnectingPacket();
            ServerUtils.connectToLastServer();
        }
    }
}
