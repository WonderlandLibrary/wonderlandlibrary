package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.util.client.ChatIDs;

public class PrefixCommand extends Command
{
    public PrefixCommand()
    {
        super(new String[][]{{"prefix", "prefix"}});
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length > 1)
        {
            String prefix = args[1];
            Commands.getInstance().setPrefix(prefix);
            ChatManager.getInstance().sendDeleteMessage("Prefix has been set to: " + prefix, "Prefix", ChatIDs.COMMAND);
        }
    }

    @Override
    public String getPossibleInputs(String[] args)
    {
        if (args.length > 1)
        {
            return "";
        }

        return super.getPossibleInputs(args);
    }

    @Override
    public Completer onTabComplete(Completer completer)
    {
        if (completer.getArgs().length > 1 || completer.getArgs()[0].equalsIgnoreCase("prefix"))
        {
            return completer;
        }

        return super.onTabComplete(completer);
    }

}
