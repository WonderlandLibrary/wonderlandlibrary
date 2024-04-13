package com.canon.majik.impl.command.api;

import com.canon.majik.api.utils.Globals;

import java.util.Arrays;
import java.util.List;

public abstract class Command implements Globals {
    String name,description;
    List<String> syntax;

    public Command(String name, String description, String... syntax){
        this.name = name;
        this.description = description;
        this.syntax = Arrays.asList(syntax);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSyntax() {
        return syntax;
    }

    public void setSyntax(List<String> syntax) {
        this.syntax = syntax;
    }

    public abstract void runCommand(List<String> args);
}
