package ru.smertnix.celestial.command;

@FunctionalInterface
public interface Command {
    void execute(String... strings);
}