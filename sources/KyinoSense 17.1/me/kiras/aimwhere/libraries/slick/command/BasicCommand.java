/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.command;

import me.kiras.aimwhere.libraries.slick.command.Command;

public class BasicCommand
implements Command {
    private String name;

    public BasicCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object other) {
        if (other instanceof BasicCommand) {
            return ((BasicCommand)other).name.equals(this.name);
        }
        return false;
    }

    public String toString() {
        return "[Command=" + this.name + "]";
    }
}

