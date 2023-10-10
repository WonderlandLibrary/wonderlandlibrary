package tech.atani.client.feature.command.impl;

import net.minecraft.util.EnumChatFormatting;
import tech.atani.client.feature.command.Command;
import tech.atani.client.feature.command.data.CommandInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

@CommandInfo(name = "ip", description = "get the server ip")
public class Ip extends Command {
    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            if(!mc.isSingleplayer()) {
                sendMessage(String.format("You are playing on: %s", EnumChatFormatting.GOLD + mc.getCurrentServerData().serverIP), true);
                copyToClipboard(mc.getCurrentServerData().serverIP);
            } else {
                sendMessage("You are playing on a singleplayer world!", true);
            }
            return true;
        }else {
            return false;
        }
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
}