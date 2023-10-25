package org.dreamcore.client.cmd;

@FunctionalInterface
public interface Command {
    void execute(String... strings);
}