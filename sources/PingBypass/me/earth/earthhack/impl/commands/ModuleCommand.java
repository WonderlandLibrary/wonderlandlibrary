package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BindSetting;
import me.earth.earthhack.api.util.Bind;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.api.util.TextUtil;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.services.client.ModuleManager;
import me.earth.earthhack.impl.util.client.ChatIDs;
import me.earth.earthhack.impl.util.text.ChatUtil;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ModuleCommand extends Command
{
    private final Set<Module> checked = new HashSet<>();

    public ModuleCommand()
    {
        super(new String[][]{{"module"}, {"set", "bind", "toggle", "reset"}, {"setting", "key", "name"}, {"value"}});
    }

    @Override
    public boolean fits(String[] args)
    {
        return args[0].length() > 0 && getModule(args[0]) != null;
    }

    @Override
    public void execute(String[] args)
    {
        Module module = ModuleManager.getInstance().getModule(args[0]);
        if (module == null)
        {
            module = getModule(args[0].toLowerCase());
            if (module == null)
            {
                ChatUtil.sendMessage(TextColor.RED + "Could not find " + args[0] + ". Try " + Commands.getInstance().getPrefix() + "modules.");
            }
            else
            {
                ChatUtil.sendMessage(TextColor.RED + "Did you mean " + module.getName() + "?");
            }

            return;
        }

        if (args.length == 1)
        {
            ChatUtil.sendMessage(module.getName());
            for (Setting<?> setting : module.getSettings())
            {
                ChatUtil.sendMessage(setting.getName() + " : " + setting.getValue().toString());
            }
        }
        else
        {
            processAllArgs(args, module);
        }
    }

    @Override
    public String getPossibleInputs(String[] args)
    {
        Module module = getModule(args[0]);
        String text = completeLast(args);
        if (text != null && module != null)
        {
            switch (args.length)
            {
                case 1:
                    return text + this.getFullUsage().substring(6);
                case 2:
                    String second = getSecondArg(args[1]);
                    assert second != null;
                    return text + getUsageSecondArg(second);
                case 3:
                    switch (args[1].toLowerCase())
                    {
                        case "set":
                            Setting<?> setting = getSetting(module, args[2]);
                            if (setting != null)
                            {
                                return text + " " + setting.getInputs(null);
                            }
                            break;
                        case "bind":
                            Setting<?> bind = module.getSetting("bind");
                            if (bind != null)
                            {
                                return TextUtil.safeSubstring(bind.getInputs(args[2]), args[2].length());
                            }
                            break;
                    }
                case 4:
                    return text;
            }
        }

        return TextColor.RED + " not found!";
    }

    @Override
    public Completer onTabComplete(Completer completer)
    {
        String initial = completer.getInitial();
        if (completer.isSame())
        {
            if (completer.getArgs().length == 2)
            {
                return super.onTabComplete(completer);
            }

            String last = getNextForLast(completer.getArgs());
            if (last != null)
            {
                String newInitial = TextUtil.safeSubstring(initial, 0, completer.getInitial().length() - completer.getArgs()[completer.getArgs().length - 1].length());
                completer.setResult(newInitial + last);
            }
        }
        else
        {
            String completed = completeLast(completer.getArgs());
            if (completed == null)
            {
                completer.setMcComplete(true);
            }
            else
            {
                completer.setResult(initial + completed);
                completer.setLastCompleted(completer.getResult());
            }
        }

        return completer;
    }

    private void processAllArgs(String[] args, Module module)
    {
        switch (args[1].toLowerCase())
        {
            case "set":
                processTwoArgs(args, module);
                break;
            case "bind":
                if (args.length == 2)
                {
                    ChatUtil.sendMessage(TextColor.RED + "Please specify a Key.");
                }
                else
                {
                    Bind bind = module.getBind();
                    int key = -1;
                    if (!args[2].equalsIgnoreCase("none"))
                    {
                        key = Keyboard.getKeyIndex(args[2].toUpperCase());
                    }

                    if (key == 0)
                    {
                        ChatUtil.sendMessage(TextColor.RED + "Unknown key.");
                        break;
                    }

                    bind.setKey(key);
                    ChatUtil.sendMessage(TextColor.GREEN + (key == -1 ? "Unbound " + module.getName() : ("Bound " + module.getName() + " to " + args[2]) + "."));
                }
                break;
            case "toggle":
                //ChatUtil.sendMessage(module.isEnabled() ? TextColor.RED : TextColor.GREEN + "Toggling " + module.getName());
                module.toggle();
                String message = (module.isEnabled() ? TextColor.GREEN : TextColor.RED) + module.getDisplayName() + (module.isEnabled() ? " enabled." : " disabled.");
                ChatManager.getInstance().sendDeleteMessage(message, module.getDisplayName(), ChatIDs.MODULE);
                break;
            case "name":
                if (args.length == 2)
                {
                    ChatUtil.sendMessage(TextColor.RED + "Please specify a Name.");
                }
                else
                {
                    if (args[2].equalsIgnoreCase("reset"))
                    {
                        ChatUtil.sendMessage(TextColor.GREEN + "Resetting " + module.getDisplayName() + " to " + module.getName() + ".");
                        module.setDisplayName(module.getName());
                    }
                    else
                    {
                        module.setDisplayName(args[2]);
                        ChatUtil.sendMessage(TextColor.GREEN + "Renamed " + module.getName() + " to " + module.getDisplayName() + ".");
                    }
                }
                break;
            case "reset":
                ChatUtil.sendMessage(TextColor.GREEN + "Resetting " + module.getName());
                module.resetSettings();
                break;
            default:
                ChatUtil.sendMessage(TextColor.RED + "Use " + this.getFullUsage());
                break;
        }
    }

    private void processTwoArgs(String[] args, Module module)
    {
        switch (args.length)
        {
            case 2:
                ChatUtil.sendMessage(TextColor.RED + "Please specify a Setting.");
                break;
            case 3:
                Setting<?> setting = module.getSetting(args[2]);
                if (setting == null)
                {
                    onSettingNoFound(module, args[2]);
                    break;
                }
                ChatUtil.sendMessage(TextColor.RED + "Please specify a value.");
                break;
            default:
                setting = module.getSetting(args[2]);
                if (setting == null)
                {
                    onSettingNoFound(module, args[2]);
                    break;
                }

                if (setting.getName().equalsIgnoreCase("Enabled"))
                {
                    if (args[3].equalsIgnoreCase("true"))
                    {
                        module.enable();
                        ChatManager.getInstance().sendDeleteMessage(TextColor.GREEN + module.getName() + " enabled.", module.getName(), ChatIDs.MODULE);
                    }
                    else if (args[3].equalsIgnoreCase("false"))
                    {
                        module.disable();
                        ChatManager.getInstance().sendDeleteMessage(TextColor.RED + module.getName() + " disabled.", module.getName(), ChatIDs.MODULE);
                    }
                    else
                    {
                        ChatUtil.sendMessage(TextColor.RED + "Try " + TextColor.WHITE + "true " + TextColor.RED + "or " + TextColor.WHITE + "false" + TextColor.RED + ".");
                    }
                }
                else
                {
                    if (!setting.fromString(args[3]))
                    {
                        ChatUtil.sendMessage(TextColor.RED + "Bad value. Required: " + setting.getInitial().getClass().getSimpleName() + ".");
                    }
                    else
                    {
                        String message = TextColor.GREEN + module.getName() + " " + setting.getName() + " set to " + setting.getValue().toString() + ".";
                        ChatManager.getInstance().sendDeleteMessage(message, setting.getName(), ChatIDs.COMMAND);
                    }
                }
        }
    }

    private String completeLast(String[] args)
    {
        Module module = getModule(args[0]);
        if (args.length == 1 || module == null)
        {
            return module == null ? null : TextUtil.safeSubstring(module.getName(), args[0].length());
        }

        if (args.length == 2)
        {
            String second = getSecondArg(args[1]);
            if (second != null)
            {
                return TextUtil.safeSubstring(second, args[1].length());
            }

            return null;
        }

        if (args[1].toLowerCase().equalsIgnoreCase("set"))
        {
            Setting<?> setting = getSetting(module, args[2]);
            if (setting != null)
            {
                if (args.length == 4 && args[2].equalsIgnoreCase(setting.getName()))
                {
                    String inputs = setting.getInputs(args[3]);
                    return inputs == null ? null : TextUtil.safeSubstring(inputs, args[3].length());
                }
                else if (args.length == 3)
                {
                    return TextUtil.safeSubstring(setting.getName(), args[2].length());
                }
            }
        }
        else if (args[1].toLowerCase().equalsIgnoreCase("bind"))
        {
            BindSetting bind = module.getSetting("Bind");
            return TextUtil.safeSubstring(bind.getInputs(args[2]), args[2].length());
        }
        else
        {
            return "";
        }

        return null;
    }

    private String getNextForLast(String[] args)
    {
        String last = args[0].toLowerCase();
        Module lastModule = ModuleManager.getInstance().getModule(last);
        if (lastModule != null)
        {
            switch (args.length)
            {
                case 1:
                    Module module = getNext(lastModule, args[args.length - 1]);
                    if (module != null && !module.equals(lastModule))
                    {
                        return module.getName();
                    }
                    break;
                case 2:
                    switch (args[1].toLowerCase())
                    {
                        case "set":
                            return "bind";
                        case "bind":
                            return "toggle";
                        case "toggle":
                            return "name";
                        case "name":
                            return "reset";
                        case "reset":
                        default:
                            return "set";
                    }
                case 3:
                    switch (args[1].toLowerCase())
                    {
                        case "set":
                            Setting<?> setting = lastModule.getSetting(args[2]);
                            if (setting != null)
                            {
                                Setting<?> next = getNext(setting, lastModule);
                                return next.getName();
                            }
                            break;
                        case "bind": //maybe keybind stuff?
                        default:
                            break;
                    }
                    break;
                default:
                    Setting<?> setting = lastModule.getSetting(args[2]);
                    if (setting != null && args[1].equalsIgnoreCase("set"))
                    {
                        return Completer.nextValueInSetting(setting, args[args.length - 1]);
                    }
            }
        }

        return null;
    }

    private void onSettingNoFound(Module moduleIn, String name)
    {
        Setting<?> setting = getSetting(moduleIn, name);
        if (setting == null)
        {
            ChatUtil.sendMessage(TextColor.RED + "Could not find " + name + ".");
        }
        else
        {
            ChatUtil.sendMessage(TextColor.RED + "Did you mean " + setting.getName());
        }
    }

    private Setting<?> getNext(Setting<?> settingIn, Module moduleIn)
    {
        boolean found = false;
        for (Setting<?> setting : moduleIn.getSettings())
        {
            if (found)
            {
                return setting;
            }

            if (setting.equals(settingIn))
            {
                found = true;
            }
        }

        if (found)
        {
            Optional<Setting<?>> firstElement = moduleIn.getSettings().stream().findFirst();
            if (firstElement.isPresent())
            {
                return firstElement.get();
            }
        }

        return settingIn;
    }

    private Module getNext(Module moduleIn, String uncompleted)
    {
        checked.add(moduleIn);
        return lookUp(moduleIn, uncompleted, true);
    }

    private Module lookUp(Module moduleIn, String uncompleted, boolean first)
    {
        for (Module module : ModuleManager.getInstance().getModules())
        {
            if (module.getName().toLowerCase().startsWith(uncompleted) && !checked.contains(module))
            {
                return module;
            }
        }

        if (first)
        {
            checked.clear();
            return lookUp(moduleIn, uncompleted, false);
        }

        return moduleIn;
    }

    private String getSecondArg(String input)
    {
        for (String string : getUsage()[1])
        {
            if (string.startsWith(input.toLowerCase()))
            {
                return string;
            }
        }

        return null;
    }

    private Module getModule(String input)
    {
        Module module = ModuleManager.getInstance().getModule(input);
        if (module == null)
        {
            for (Module mod : ModuleManager.getInstance().getModules())
            {
                if (mod.getName().toLowerCase().startsWith(input.toLowerCase()))
                {
                    return mod;
                }
            }

            return null;
        }

        return module;
    }

    private Setting<?> getSetting(Module module, String input)
    {
        for (Setting<?> setting : module.getSettings())
        {
            if (TextUtil.startsWithIgnoreCase(setting.getName(), input))
            {
                return setting;
            }
        }

        return null;
    }

    private String getUsageSecondArg(String input)
    {
        switch (input.toLowerCase())
        {
            case "set":
                return " <setting> <value>";
            case "bind":
                return " <key>";
            case "name":
                return " <name>";
            case "toggle":
            case "reset":
                return "";
        }

        return TextColor.RED + " <error>";
    }

}
