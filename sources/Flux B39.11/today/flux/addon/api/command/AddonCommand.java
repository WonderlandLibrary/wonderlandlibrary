package today.flux.addon.api.command;

import lombok.Getter;
import today.flux.module.Command;

public abstract class AddonCommand {
    @Getter
    protected final String name, description;
    @Getter
    protected final String[] syntax;

    @Getter
    private final Command nativeCommand;

    public AddonCommand(String name, String description, String... syntax) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;

        this.nativeCommand = new Command(name, description, syntax) {
            @Override
            public void execute(String[] args) {
                onExecute(args);
            }
        };
    }

    public abstract void onExecute(String[] args);
}
