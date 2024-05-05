package client.command.impl;

import client.command.Command;
import client.util.ChatUtil;
import net.minecraft.client.gui.GuiScreen;

public final class Name extends Command {

    public Name() {
        super("Copies and displays your username", "name", "ign", "username", "nick", "nickname");
    }

    @Override
    public void execute(final String[] args) {
        final String name = mc.thePlayer.getName();
        GuiScreen.setClipboardString(name);
        ChatUtil.display("Copied your username to clipboard. (%s)", name);
    }
}
