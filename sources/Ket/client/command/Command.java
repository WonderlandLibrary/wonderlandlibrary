package client.command;

import client.util.ChatUtil;
import client.util.MinecraftInstance;
import lombok.Getter;
import org.atteo.classindex.IndexSubclasses;

@Getter
@IndexSubclasses
public abstract class Command implements MinecraftInstance {

    private final String description;
    private final String[] expressions;

    public Command(final String description, final String... expressions) {
        this.description = description;
        this.expressions = expressions;
    }

    public abstract void execute(String[] args);

    protected final void error() {
        ChatUtil.display("§cInvalid command arguments.");
    }

    protected final void error(String usage) {
        error();
        ChatUtil.display("Correct Usage: " + usage);
    }
}