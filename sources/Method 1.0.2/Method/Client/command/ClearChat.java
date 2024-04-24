package Method.Client.command;


@Command.CommandFeatures(description = "Clears Chat", Syntax = "Clear")

public class ClearChat extends Command {
    @Override
    public void runCommand(String s, String[] args) {
        mc.ingameGUI.getChatGUI().clearChatMessages(true);
    }

}