package vestige.command;

import lombok.Getter;

@Getter
public abstract class Command {

    private final String name;
    private final String description;

    private String[] aliases;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public abstract void onCommand(String[] args);

}