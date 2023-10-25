/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
    public String syntax;
    public List<String> aliases = new ArrayList<String>();
    public String name;
    public String description;

    public String getSyntax() {
        return this.syntax;
    }

    public void setSyntax(String string) {
        this.syntax = string;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setAliases(List<String> list) {
        this.aliases = list;
    }

    public abstract void onCommand(String[] var1, String var2);

    public void setDescription(String string) {
        this.description = string;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public Command(String string, String string2, String string3, String ... stringArray) {
        this.name = string;
        this.description = string2;
        this.syntax = string3;
        this.aliases = Arrays.asList(stringArray);
    }
}

