package me.aquavit.liquidsense.command.shortcuts;

import me.aquavit.liquidsense.utils.data.Pair;
import me.aquavit.liquidsense.command.Command;

import java.util.List;

public class Shortcut extends Command {
    private final List<Pair<Command, String[]>> script;

    public Shortcut(String name, List<Pair<Command, String[]>> script) {
        super(name);
        this.script = script;
    }

    public List<Pair<Command, String[]>> getScript() {
        return script;
    }

    @Override
    public void execute(String[] args) {
        script.forEach(commandPair -> commandPair.getFirst().execute(commandPair.getSecond()));
    }
}
