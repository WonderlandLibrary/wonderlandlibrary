package Method.Client.command;


import Method.Client.managers.CommandManager;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Lists all commands.", Syntax = "Help")

public class Help extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        for (Command cmd : CommandManager.getInstance().commands) {
            if (cmd != this) {
                ChatUtils.message(cmd.Syntax.replace("<", "<\2479").replace(">", "\2477>") + " - " + cmd.getAnnotation().description());
            }
        }
    }


}