package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.api.util.TextUtil;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.thread.LookUp;
import me.earth.earthhack.impl.services.thread.LookUpManager;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.thread.LookUpUtil;

@SuppressWarnings("unused")
public class HistoryCommand extends Command
{
    public HistoryCommand()
    {
        super(new String[][]{{"history"}, {"name"}});
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length == 1)
        {
            ChatUtil.sendMessage(TextColor.RED + "Please specify a name.");
        }
        else if (args.length > 1)
        {
            ChatUtil.sendMessage(TextColor.WHITE + "Looking up " + args[1] + ".");
            LookUpManager.getInstance().doLookUp(new LookUp(LookUp.Type.HISTORY, args[1])
            {
                @Override
                public void onSuccess()
                {
                    ChatUtil.sendMessage(TextColor.AQUA + args[1] + "'s name history:");
                    for (String name : names)
                    {
                        ChatUtil.sendMessage(TextColor.WHITE + name);
                    }
                }

                @Override
                public void onFailure()
                {
                    ChatUtil.sendMessage(TextColor.RED + "Failed to lookup " + name);
                }
            });
        }
    }

    @Override
    public String getPossibleInputs(String[] args)
    {
        if (args.length == 1)
        {
            return TextUtil.safeSubstring("history", args[0].length()) + " <name>";
        }
        else if (args.length == 2)
        {
            String player = LookUpUtil.findNextPlayerName(args[1]);
            return TextUtil.safeSubstring(player, args[1].length());
        }

        return null;
    }

    @Override
    public Completer onTabComplete(Completer completer)
    {
        if (completer.getArgs().length == 1)
        {
            if (completer.getArgs()[0].equalsIgnoreCase("history"))
            {
                completer.setMcComplete(true);
            }
            else
            {
                completer.setResult(Commands.getInstance().getPrefix() + "history");
            }
        }
        else if (completer.getArgs().length == 2)
        {
            String player = LookUpUtil.findNextPlayerName(completer.getArgs()[1]);
            if (player == null || player.equalsIgnoreCase(completer.getArgs()[1]))
            {
                completer.setMcComplete(true);
            }
            else
            {
                completer.setResult(Commands.getInstance().getPrefix() + "history " + player);
            }
        }

        return completer;
    }

}
