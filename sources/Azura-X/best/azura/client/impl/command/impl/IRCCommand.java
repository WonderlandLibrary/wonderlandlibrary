package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.impl.ui.chat.GuiCustomChat;
import best.azura.irc.impl.packets.client.C1ChatSendPacket;
import best.azura.client.impl.module.impl.other.IRCModule;
import com.google.gson.JsonObject;

public class IRCCommand extends ACommand
{
    @Override
    public String getName() {
        return "irc";
    }

    @Override
    public String getDescription() {
        return "Send messages through the client chat";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "chat","c" };
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length < 1) {
            msg(Client.PREFIX + "§cPlease use .irc <message>");
            return;
        }

        if (!Client.INSTANCE.getModuleManager().getModuleByClass(IRCModule.class).isEnabled()) {
            msg(Client.PREFIX + "§cPlease enable the IRC Module.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (String s : args) builder.append(s).append(" ");
        if (builder.toString().endsWith(" ")) builder = new StringBuilder(builder.substring(0, builder.length()-1));
        if (Client.INSTANCE.getIrcConnector().getClientSocket().isConnected()) {
            C1ChatSendPacket c1ChatSendPacket = new C1ChatSendPacket(null);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", builder.toString());
            jsonObject.addProperty("channel", GuiCustomChat.currentTabId);

            c1ChatSendPacket.setContent(jsonObject);

            Client.INSTANCE.getIrcConnector().sendPacket(c1ChatSendPacket);
        }
    }
}