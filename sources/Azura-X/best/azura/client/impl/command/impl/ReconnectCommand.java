package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.impl.Client;

public class ReconnectCommand extends ACommand {
    @Override
    public String getName() {
        return "reconnect";
    }

    @Override
    public String getDescription() {
        return "Reconnect to the Client Chat.";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handleCommand(String[] args) {
        Client.INSTANCE.getIrcConnector().stopConnection();
        Client.INSTANCE.getIrcConnector().startConnection();

        msg("Reconnected to Azura Chat!");
    }
}