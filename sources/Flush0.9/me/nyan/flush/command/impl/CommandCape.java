package me.nyan.flush.command.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.misc.Cape;
import me.nyan.flush.utils.other.ChatUtils;

import javax.swing.*;

public class CommandCape extends Command {
    private final Cape cape;

    public CommandCape() {
        super("Cape", "Sets the custom cape", "cape set | help");
        cape = flush.getModuleManager().getModule(Cape.class);
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "set":
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle(Flush.NAME + " - Select cape");

                    if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    String path = chooser.getSelectedFile().getAbsolutePath();
                    cape.setCustomPath(path);
                    ChatUtils.println("Set cape to " + path);
                    return;

                case "help":
                    ChatUtils.println("You can download the cape templates at https://bit.ly/3aJIvyW (PNG and Figma formats)");
                    return;
            }
        }
        sendSyntaxHelpMessage();
    }
}
