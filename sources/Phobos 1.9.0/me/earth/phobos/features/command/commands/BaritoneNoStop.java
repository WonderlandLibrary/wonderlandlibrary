package me.earth.phobos.features.command.commands;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.command.Command;

public class BaritoneNoStop
        extends Command {
    public BaritoneNoStop() {
        super("noStop", new String[]{"<prefix>", "<x>", "<y>", "<z>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 5) {
            Phobos.baritoneManager.setPrefix(commands[0]);
            int x = 0;
            int y = 0;
            int z = 0;
            try {
                x = Integer.parseInt(commands[1]);
                y = Integer.parseInt(commands[2]);
                z = Integer.parseInt(commands[3]);
            } catch (NumberFormatException e) {
                BaritoneNoStop.sendMessage("Invalid Input for x, y or z!");
                Phobos.baritoneManager.stop();
                return;
            }
            Phobos.baritoneManager.start(x, y, z);
            return;
        }
        BaritoneNoStop.sendMessage("Stoping Baritone-Nostop.");
        Phobos.baritoneManager.stop();
    }
}

