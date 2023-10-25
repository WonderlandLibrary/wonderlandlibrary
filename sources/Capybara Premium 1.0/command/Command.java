package fun.rich.client.command;

@FunctionalInterface
public interface Command {
    void execute(String... strings);
}