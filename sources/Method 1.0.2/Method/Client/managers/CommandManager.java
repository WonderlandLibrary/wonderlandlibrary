package Method.Client.managers;


import Method.Client.command.*;
import Method.Client.utils.visual.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CommandManager {
    static CommandManager instance;
    public final ArrayList<Command> commands;
    public static char cmdPrefix = '@';

    private CommandManager() {
        this.commands = new ArrayList<>();
        registerCommands();
    }

    public void registerCommands() {
        commands.add(new Help());
        commands.add(new VClip());
        commands.add(new Login());
        commands.add(new Edit());
        commands.add(new FakePlayer());
        commands.add(new UsernameHistory());
        commands.add(new Say());
        commands.add(new PrefixChange());
        commands.add(new OpenGui());
        commands.add(new Effect());
        commands.add(new PlayerFinder());
        commands.add(new WorldSeed());
        commands.add(new Friend());
        commands.add(new ClearChat());
        commands.add(new OpenDir());
        commands.add(new Invsee());
        commands.add(new Author());
        commands.add(new ResetGui());
        commands.add(new Yaw());
        commands.add(new Pitch());
        commands.add(new BedCoords());
        commands.add(new Drop());
        commands.add(new Peek());
        commands.add(new Vanish());
        commands.add(new StackSize());
        commands.add(new Hclip());
        commands.add(new Reset());
        commands.add(new Give());
        commands.add(new Hat());
        commands.add(new Head());
        commands.add(new Lore());
        commands.add(new Nbt());
        commands.add(new Rename());
        commands.add(new Repair());
        commands.add(new Tp());
        commands.add(new Profile());
    }

    public void runCommands(String input) {
        String sanitizedInput = input.trim().substring(String.valueOf(cmdPrefix).length()).trim();
        String[] parsedInput = sanitizedInput.split(" ");
        String commandName = parsedInput[0];
        String[] arguments = Arrays.copyOfRange(parsedInput, 1, parsedInput.length);

        Optional<Command> matchedCommand = commands.stream()
                .filter(cmd -> cmd.Syntax.split(" ")[0].equalsIgnoreCase(commandName.trim()))
                .findFirst();

        if (matchedCommand.isPresent()) {
            try {
                matchedCommand.get().runCommand(sanitizedInput, arguments);
            } catch (Exception e) {
                ChatUtils.error("Usage: " + matchedCommand.get().Syntax);
            }
        } else {
            ChatUtils.error("Cannot resolve internal command: \u00a7c" + commandName);
        }
    }

    public static synchronized CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }
}