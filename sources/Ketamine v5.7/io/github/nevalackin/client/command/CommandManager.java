package io.github.nevalackin.client.command;

import java.util.List;

public interface CommandManager {

    void register();

    void execute();

    List<String> getSuggestions();
}
