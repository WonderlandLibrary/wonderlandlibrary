/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command;

public class Command {
    private String[] commands;
    private String args;

    public Command(String[] commands) {
        this.commands = commands;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public void onCmd(String[] args) {
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getArgs() {
        return this.args;
    }
}

