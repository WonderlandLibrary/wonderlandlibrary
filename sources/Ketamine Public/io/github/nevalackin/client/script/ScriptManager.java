package io.github.nevalackin.client.script;

public interface ScriptManager {

    String EXTENSION = ".js";

    boolean enable(final String script, final boolean enabled);
}
