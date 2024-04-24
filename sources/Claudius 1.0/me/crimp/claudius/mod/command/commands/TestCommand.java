package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.mod.command.Command;

public class TestCommand extends Command {
    public TestCommand() {
        super("TestName", new String[]{"<1/2>", "<3/4>","<5/6>","<7/8>","<9/10>"});
    }

    @Override
    public void execute(String[] commands) {
        Command.sendMessage("why did u put " + commands[0] + " in 1st");
        Command.sendMessage("why did u put " + commands[1] + " in 2nd");
        Command.sendMessage("why did u put " + commands[2] + " in 3rd");
        Command.sendMessage("why did u put " + commands[3] + " in 4th");
        Command.sendMessage("why did u put " + commands[4] + " in 5th");
    }
}