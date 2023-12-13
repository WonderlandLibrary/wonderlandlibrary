package vip.radium.command;

public interface Command {

    String[] getAliases();

    void execute(String[] arguments) throws CommandExecutionException;

    String getUsage();

}
