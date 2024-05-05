package io.github.raze.commands.collection;

import io.github.raze.commands.system.Command;
import io.github.raze.utilities.collection.configs.ClipboardUtil;

public class IGNCommand extends Command {

    public IGNCommand() {
        super("IGN", "Copy the player username", "ign", "ig");
    }

    public String onCommand(String[] arguments, String command) {
        ClipboardUtil.copyToClipboard(mc.session.getUsername());
        
        return String.format("Your username is %s, copied to clipboard", mc.session.getUsername());
    }

}
