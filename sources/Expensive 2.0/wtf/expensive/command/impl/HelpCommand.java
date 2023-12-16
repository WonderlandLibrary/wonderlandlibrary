package wtf.expensive.command.impl;

import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.util.ClientUtil;

@CommandInfo(name = "help", description = "Телепортирует вас вперед.")
public class HelpCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        for (Command cmd : Managment.COMMAND_MANAGER.getCommands()) {
            if (cmd instanceof HelpCommand) continue;
            ClientUtil.sendMesage(cmd.command + " | " + cmd.description);
        }
    }

    @Override
    public void error() {

    }
}
