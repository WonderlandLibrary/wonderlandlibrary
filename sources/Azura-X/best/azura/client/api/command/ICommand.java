package best.azura.client.api.command;

public interface ICommand {
    String getName();
    String getDescription();
    String[] getAliases();
    void handleCommand(String[] args);
}