package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.render.SigmaHUD;
import me.nyan.flush.utils.other.ChatUtils;

public class Sigmaname extends Command {
    public Sigmaname() {
        super("Sigmaname", "Changes the small text under Flush text in Sigma theme", "Sigmaname <text>");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            String sigmaname = buildStringFromArgs(args);

            if (sigmaname.equalsIgnoreCase("Jello")) {
                return;
            }
            flush.getModuleManager().getModule(SigmaHUD.class).setSigmaname(sigmaname);
            ChatUtils.println("Sigma name is now \"" + flush.getModuleManager().getModule(SigmaHUD.class).getSigmaname() + "§r\".");
            return;
        }

        sendSyntaxHelpMessage();
    }
}