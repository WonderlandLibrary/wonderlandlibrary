package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.impl.Client;
import org.lwjgl.input.Keyboard;

public class UsersCommand extends ACommand {

    @Override
    public String getName() {
        return "users";
    }

    @Override
    public String getDescription() {
        return "See how many Users are online right now.";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handleCommand(String[] args) {
        msg(Client.PREFIX + "§7There are currently §9" + Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().size() + " §7Azura Users in your cache!");
    }
}
