package me.earth.earthhack.impl.commands.hidden;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.chat.CommandManager;
import me.earth.earthhack.impl.util.text.ChatUtil;

/**
 * Command returned by the CommandManager
 * when no applicable Command was found.
 */
public class FailCommand extends Command
{
    public FailCommand()
    {
        super(new String[][]{{"fail"}}, true);
    }

    @Override
    public void execute(String[] args)
    {
        ChatUtil.sendMessage(TextColor.RED + "Command not found. Type " + Commands.getInstance().getPrefix() + "commands to get a list of commands.");
    }

    @Override
    public String getPossibleInputs(String[] args)
    {
        return TextColor.RED + CommandManager.getInstance().getConcatenatedCommands();
    }

    @Override
    public Completer onTabComplete(Completer completer)
    {
        completer.setMcComplete(true);
        return completer;
    }

}
