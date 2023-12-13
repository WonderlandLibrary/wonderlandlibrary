package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.api.util.TextUtil;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.services.client.FriendManager;
import me.earth.earthhack.impl.services.thread.LookUp;
import me.earth.earthhack.impl.services.thread.LookUpManager;
import me.earth.earthhack.impl.util.client.ChatIDs;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.thread.LookUpUtil;

@SuppressWarnings("unused")
public class FriendCommand extends Command implements Globals
{
    public FriendCommand()
    {
        super(new String[][]{{"friend"}, {"add", "del", "list"}, {"name"}});
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length == 1 || args.length == 2 && args[1].equalsIgnoreCase("list"))
        {
            StringBuilder builder = new StringBuilder("Friends: ");
            for (String name : FriendManager.getInstance().getFriends())
            {
                builder.append(name).append(", ");
            }
            ChatUtil.sendMessage(builder.substring(0, builder.length() - 2));
        }
        else if (args.length == 2)
        {
            boolean isFriended = FriendManager.getInstance().isFriend(args[1]);
            ChatUtil.sendMessage((isFriended ? TextColor.GREEN : TextColor.RED) + args[1] + " is " + (isFriended ? "" : "not ") + "friended.");
        }
        else
        {
            String name = args[2];
            if (args[1].equalsIgnoreCase("add"))
            {
                LookUpManager.getInstance().doLookUp(new LookUp(LookUp.Type.UUID, name)
                {
                    @Override
                    public void onSuccess()
                    {
                        FriendManager.getInstance().addFriend(name, uuid);
                        ChatManager.getInstance().sendDeleteMessage(TextColor.GREEN + name + " was added as a friend.", name, ChatIDs.FRIEND_COMMAND);
                    }

                    @Override
                    public void onFailure()
                    {
                        ChatUtil.sendMessage(TextColor.RED + "Failed to find " + name);
                    }
                });
            }
            else if (args[1].equalsIgnoreCase("del"))
            {
                FriendManager.getInstance().removeFriend(name);
                ChatManager.getInstance().sendDeleteMessage(TextColor.RED + name + " unfriended.", name, ChatIDs.FRIEND_COMMAND);
            }
            else
            {
                ChatUtil.sendMessage(TextColor.RED + "Please specify <add/del>.");
            }
        }
    }

    @Override
    public String getPossibleInputs(String[] args)
    {
        if (args.length == 1 && this.getName().startsWith(args[0].toLowerCase()))
        {
            return this.getName().substring(args[0].length()) + this.getFullUsage().substring(this.getName().length());
        }
        else if (args.length == 2)
        {
            String filler = fillArgs(args[1]);
            if (filler != null)
            {
                return TextUtil.safeSubstring(filler, args[1].length()) + (filler.equalsIgnoreCase("list") ? "" : " <name>");
            }
            else
            {
                String next = LookUpUtil.findNextPlayerName(args[1]);
                return TextUtil.safeSubstring(next, args[1].length());
            }
        }
        else if (args.length > 2)
        {
            String next = LookUpUtil.findNextPlayerName(args[2]);
            return TextUtil.safeSubstring(next, args[2].length());
        }

        return TextColor.RED + " invalid!";
    }

    @Override
    public Completer onTabComplete(Completer completer)
    {
        if (completer.getInitial().equals(completer.getLastCompleted()))
        {
            if (completer.getArgs().length == 2)
            {
                for (int i = 0; i < getUsage()[0].length; i++)
                {
                    String str = getUsage()[0][i];
                    if (str.equalsIgnoreCase(completer.getArgs()[1]))
                    {
                        String result = i == getUsage()[0].length - 1 ? getUsage()[0][0] : getUsage()[0][i + 1];
                        String newInitial = TextUtil.safeSubstring(completer.getInitial(), 0, completer.getInitial().length() - completer.getArgs()[completer.getArgs().length - 1].length());
                        completer.setResult(newInitial + result);
                    }
                }
            }
            else
            {
                completer.setMcComplete(true);
            }
        }
        else
        {
            if (completer.getArgs().length == 1)
            {
                completer.setResult(Commands.getInstance().getPrefix() + this.getName());
            }
            else if (completer.getArgs().length == 2)
            {
                String next = fillArgs(completer.getArgs()[1]);
                if (next != null)
                {
                    completer.setResult(completer.getInitial() + TextUtil.safeSubstring(next, completer.getArgs()[1].length()));
                }
                else
                {
                    completer.setMcComplete(true);
                }
            }
            else if (completer.getArgs().length > 2)
            {
                String next = LookUpUtil.findNextPlayerName(completer.getArgs()[2]);
                if (next != null)
                {
                    completer.setResult(completer.getInitial() + TextUtil.safeSubstring(next, completer.getArgs()[2].length()));
                }
                else
                {
                    completer.setMcComplete(true);
                }
            }
        }

        completer.setLastCompleted(completer.getResult());
        return completer;
    }

    private String fillArgs(String input)
    {
        for (String str : getUsage()[0])
        {
            if (str.startsWith(input.toLowerCase()))
            {
                return str;
            }
        }

        return null;
    }

}
