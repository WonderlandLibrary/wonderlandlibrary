package me.nyan.flush.command.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.render.ESP;
import me.nyan.flush.utils.other.ChatUtils;

import javax.swing.*;

public class ImageESP extends Command {
    private final ESP esp = flush.getModuleManager().getModule(ESP.class);

    public ImageESP() {
        super("ImageESP", "Sets the custom image for the image ESP", "imageesp set", "iesp");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0 && args[0].equalsIgnoreCase("set")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(Flush.NAME + " - Select image");

            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            String path = chooser.getSelectedFile().getAbsolutePath();
            esp.path = path;
            if (esp.isEnabled()) {
                esp.setEnabled(false);
                esp.setEnabled(true);
            }
            ChatUtils.println("Set ImageESP image to " + path);
            return;
        }

        sendSyntaxHelpMessage();
    }
}
