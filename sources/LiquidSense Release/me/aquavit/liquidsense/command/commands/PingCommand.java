package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.command.Command;

public class PingCommand extends Command
{
    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(final String[] args) {
        chat("§3Your ping is §a" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + " §3ms.");
    }
}
