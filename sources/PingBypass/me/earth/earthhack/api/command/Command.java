package me.earth.earthhack.api.command;

import me.earth.earthhack.api.util.TextUtil;

public abstract class Command
{
    private final String name;
    private final String fullUsage;
    private final String[][] usage;
    private final boolean hidden;

    public Command(String[][] usage)
    {
        this(usage, false);
    }

    public Command(String[][] usage, boolean hidden)
    {
        this.name = usage[0][0];
        this.usage = usage;
        this.hidden = hidden;
        this.fullUsage = concatenateUsage(0);
    }

    public String getName()
    {
        return name;
    }

    public boolean fits(String[] args)
    {
        return args[0].length() > 0 && TextUtil.startsWithIgnoreCase(name, args[0]);
    }

    /**
     * Called when a chatmessage starting with the prefix is sent
     * and fits returns true for the string array.
     *
     * @param args the input, length >= 1.
     */
    public abstract void execute(String[] args);

    /**
     * Used to render possible inputs in the command line.
     *
     * @param args the input, length >= 1.
     * @return a string with possible usage.
     */
    public String getPossibleInputs(String[] args)
    {
        if (args.length == 1)
        {
            return this.getFullUsage().substring(args[0].length());
        }

        if (args.length <= usage.length)
        {
            String last = getFullLast(args);
            return TextUtil.safeSubstring(last, args[args.length - 1].length()) + concatenateUsage(args.length);
        }

        return "";
    }

    /**
     * Used to tab complete commands. Override change the
     * implement own tab complete behaviour.
     *
     * @param completer the tab completer.
     * @return the tab completer (!= null).
     */
    public Completer onTabComplete(Completer completer)
    {
        if (this.usage != null)
        {
            if (completer.isSame())
            {
                if (completer.getArgs().length <= this.usage.length)
                {
                    String[] args = usage[completer.getArgs().length - 1];
                    int i;
                    for (i = 0; i < args.length; i++)
                    {
                        if (args[i].equalsIgnoreCase(completer.getArgs()[completer.getArgs().length - 1]))
                        {
                            break;
                        }
                    }

                    String arg = i >= args.length - 1 ? args[0] : args[i + 1];
                    String newInitial =
                            completer.getInitial().trim().substring(
                                            0,
                                            completer.getInitial().trim().length()
                                                    - completer.getArgs()[completer.getArgs().length - 1].length());
                    completer.setResult(newInitial + arg);
                    return completer;
                }
            }

            String possible = getFullLast(completer.getArgs());
            if (!possible.isEmpty())
            {
                String last = TextUtil.safeSubstring(
                        possible,
                        completer.getArgs()[completer.getArgs().length - 1].length());
                completer.setResult(completer.getInitial() + last);
                return completer;
            }
        }

        completer.setMcComplete(true);
        return completer;
    }

    public String[][] getUsage()
    {
        return usage;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public String getFullUsage()
    {
        return fullUsage;
    }

    private String getFullLast(String[] args)
    {
        if (args.length <= usage.length)
        {
            String last = args[args.length - 1];
            String[] array = usage[args.length - 1];
            for (String string : array)
            {
                if (TextUtil.startsWithIgnoreCase(string, last))
                {
                    return string;
                }
            }
        }

        return "";
    }

    private String getLast(String[] args)
    {
        if (args.length >= usage.length)
        {
            return "";
        }

        String[] array = usage[args.length - 1];
        for (String string : array)
        {
            if (TextUtil.startsWithIgnoreCase(string, args[args.length - 1]))
            {
                return string;
            }
        }

        return "";
    }

    private String concatenateUsage(int index)
    {
        if (usage.length == 1)
        {
            return this.name;
        }
        else if (index >= usage.length)
        {
            return "";
        }

        StringBuilder builder = new StringBuilder(index == 0 ? this.name : "");
        for (int j = index == 0 ? 1 : index; j < usage.length; j++)
        {
            StringBuilder partBuilder = new StringBuilder(" <");
            for (int i = 0; i < usage[j].length; i++)
            {
                partBuilder.append(usage[j][i]).append("/");
            }
            partBuilder.replace(partBuilder.length() - 1, partBuilder.length(), ">");
            builder.append(partBuilder);
        }

        return builder.toString();
    }

}
